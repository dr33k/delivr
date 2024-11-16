package com.seven.delivr.user;

import com.seven.delivr.auth.JwtService;
import com.seven.delivr.auth.verification.email.OTP;
import com.seven.delivr.auth.verification.email.OTPRepository;
import com.seven.delivr.base.UpsertService;
import com.seven.delivr.requests.AppPageRequest;
import com.seven.delivr.notification.NotificationService;
import com.seven.delivr.user.location.Location;
import com.seven.delivr.user.location.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class UserService implements UpsertService<UserUpsertRequest, UUID>, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OTPRepository otpRepository;
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;
    private final User principal;

    public UserService(UserRepository userRepository, JwtService jwtService,
                       OTPRepository otpRepository,
                       NotificationService notificationService,
                       BCryptPasswordEncoder passwordEncoder,
                       LocationRepository locationRepository,
                       User principal){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.otpRepository = otpRepository;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
        this.locationRepository = locationRepository;
        this.principal = principal;
    }

    public List<UserMinifiedRecord> getAll(AppPageRequest request) {
        List<User> users = this.userRepository.findAll(PageRequest.of(
                request.getOffset(),
                request.getLimit(),
                Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().toList();

        return users.stream().map(UserMinifiedRecord::map).toList();
    }

    @Override
    public UserRecord get(UUID id) {
        User user = this.userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return UserRecord.map(user);
    }

    @Override
    @Transactional
    public UserRecord upsert(UserUpsertRequest request) {
            if(request.operation == UserUpsertRequest.Operation.CREATE){

                User user = User.creationMap(request);
                Location location = Location.creationMap(request);
                if(userRepository.existsByEmail(user.getEmail()))
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken");

                deleteOtp(request.otp, request.email);

                user.setPassword(passwordEncoder.encode(request.password));

                location.setUser(user);
                userRepository.save(user);
                locationRepository.saveAndFlush(location);

                return UserRecord.map(user);
            }
            else if(request.operation == UserUpsertRequest.Operation.UPDATE){
                if (request.id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not specified");

                User user = this.userRepository.findById(request.id).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

                User.updateMap(request, user);
                this.userRepository.saveAndFlush(user);
                return UserRecord.map(user);
            }

        return null;
    }

    @Override
    public void delete(UUID id) {
        if(!principal.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        this.userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

//    private EmailVerificationToken generateEVT(User user){
//        //Delete former token if necessary
//        evtRepository.deleteById(user.getUserNo());
//
//        String token = this.jwtService.generateToken(user.getEmail(),
//                Map.of(
//                        "role", user.getRole().getSpringFormat(),
//                        "authorities", user.getRole().getAuthorities().stream()
//                                .map(SimpleGrantedAuthority::getAuthority).toArray()));
//
//        EmailVerificationToken evt = new EmailVerificationToken();
//        evt.setUserNo(user.getUserNo());
//        evt.setToken(token);
//        evt.setUser(user);
//        this.evtRepository.save(evt);
//
//        return evt;
//    }
    private OTP generateOTP(String username){
        //Delete former token if necessary
        otpRepository.deleteByUsername(username);

        Integer pin = new Random().nextInt(100000,1000000);

        OTP otp = new OTP(pin, username);
        this.otpRepository.save(otp);

        return otp;
    }
//    @Transactional
//    public void sendEVT(User user){
//        //Generate new token
//        EmailVerificationToken evt = generateEVT(user);
//
//        //Send Email
//        this.notificationService.sendEmail(evt);
//    }
    @Transactional
    public void sendOtp(String username){
        //Generate new OTP
        OTP otp= generateOTP(username);
        if(this.userRepository.existsByEmail(username))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken");

        //Send Email
        this.notificationService.sendEmail(otp);
    }
    @Transactional
    private void deleteOtp(String otpString, String username){
        if(otpString == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Please confirm username: OTP Absent");

        Integer integerPin = Integer.parseInt(otpString);
        OTP otp = otpRepository.findById(integerPin)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Invalid OTP"));

        if(!otp.getUsername().equals(username)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Misplaced OTP");

        otpRepository.deleteById(integerPin);

    }

//    public boolean tokenIsValid(String token, UUID userNo){
//
//        EmailVerificationToken evt = this.evtRepository.findById(userNo).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.CONFLICT, "Invalid token : A"));
//
//        if(!token.equals(evt.getToken())) return false;
//
//        //If expired
//        if(evt.getTimestamp().plusHours(24L).isBefore(ZonedDateTime.now())) return false;
//
//        User user = evt.getUser();
//        if(user == null) return false;
//
//        this.evtRepository.deleteById(userNo);
//
//        user.setEnabled(Boolean.TRUE);
//        this.userRepository.save(user);
//        return true;
//    }

    public boolean otpIsValid(String pin, String username){
        Integer integerPin = Integer.parseInt(pin);
        OTP otp = this.otpRepository.findById(integerPin).orElse(null);

        if(otp == null) return false;
        if(!username.equals(otp.getUsername())) return false;
        if(otp.getTimestamp().plusHours(24L).isBefore(ZonedDateTime.now())) return false;

        return true;
    }

    public String login (User user){
        return jwtService.generateToken(user.getUsername(),
                Map.of(
                "role", user.getRole().getSpringFormat(),
                "authorities", user.getRole().getAuthorities().stream()
                                .map(SimpleGrantedAuthority::getAuthority).toArray())
        );
    }
    public String login (UserRecord ur){
        return jwtService.generateToken(ur.email(),
                Map.of(
                "role", ur.role().getSpringFormat(),
                "authorities", ur.role().getAuthorities().stream()
                                .map(SimpleGrantedAuthority::getAuthority).toArray())
        );
    }

}
