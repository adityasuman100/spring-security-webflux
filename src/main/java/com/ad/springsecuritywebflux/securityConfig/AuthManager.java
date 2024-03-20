package com.ad.springsecuritywebflux.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;

import com.ad.springsecuritywebflux.services.JWTService;

import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

    @Autowired
    JWTService jwtService;
    @Autowired
    ReactiveUserDetailsService users;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String userName = jwtService.getUserName(auth.getCredentials().toString().replace("Bearer ", ""));
                    return users.findByUsername(userName)
                            .filter(u -> jwtService.validate(u,
                                    auth.getCredentials().toString().replace("Bearer ", "")))
                            .map(u -> new UsernamePasswordAuthenticationToken(u.getUsername(),
                                    u.getPassword(), u.getAuthorities()));

                });
    }

}
