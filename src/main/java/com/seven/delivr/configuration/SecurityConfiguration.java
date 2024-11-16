package com.seven.delivr.configuration;

import com.seven.delivr.user.UserService;
import com.seven.delivr.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment environment;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests((registry ->
                        registry
                                .requestMatchers(HttpMethod.POST, String.format("/%s/user",Constants.VERSION)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui.html/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, String.format("/%s/auth/**",Constants.VERSION)).permitAll()
                                .requestMatchers(HttpMethod.GET, String.format("/%s/payment/callback/**",Constants.VERSION)).permitAll()
                                .requestMatchers(HttpMethod.POST, String.format("/%s/auth/**",Constants.VERSION)).permitAll()
                                .requestMatchers("/hello/**","/actuator/**",String.format("/%s/rider/**",Constants.VERSION)).permitAll()
                                .anyRequest().authenticated()
                        ))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao =
                new DaoAuthenticationProvider();
        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(bCryptPasswordEncoder);
        return dao;
    }
}