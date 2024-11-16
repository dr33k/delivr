package com.seven.delivr.auth;

import com.seven.delivr.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class Auth {
    @Bean("principal")
    @RequestScope
    public User principal(){
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
