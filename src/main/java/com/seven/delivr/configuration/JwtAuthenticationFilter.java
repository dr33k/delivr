package com.seven.delivr.configuration;

import com.seven.delivr.auth.JwtService;
import com.seven.delivr.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            String token = request.getHeader("Authorization");

            if(token != null && token.startsWith("Bearer ")){
                token = token.substring(7);

                    Claims claims = jwtService.extractClaims(token);
                    String username = claims.getSubject();

                    if(username != null){
                        var user = userService.loadUserByUsername(username);

                        if(jwtService.isTokenValid(claims)){
                            request.setAttribute("subject", username);
                            request.setAttribute("role", claims.get("role"));
                            request.setAttribute("authorities", claims.get("authorities"));

                            UsernamePasswordAuthenticationToken authenticationToken
                                    = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        }
                    }
            }
        }
        filterChain.doFilter(request, response);
    }
}
