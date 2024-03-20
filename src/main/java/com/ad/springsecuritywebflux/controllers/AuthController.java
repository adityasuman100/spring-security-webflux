package com.ad.springsecuritywebflux.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.springsecuritywebflux.models.LoginRequest;
import com.ad.springsecuritywebflux.services.JWTService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    ReactiveUserDetailsService users;
    @Autowired
    JWTService jwtService;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public Mono<String> logi(@RequestBody LoginRequest request) {
        Mono<UserDetails> foundUser = users.findByUsername(request.getUsername()).defaultIfEmpty(new UserDetails() {

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
            }

            @Override
            public String getPassword() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
            }

            @Override
            public String getUsername() {
                // TODO Auto-generated method stub
                return null;
                // throw new UnsupportedOperationException("Unimplemented method
                // 'getUsername'");
            }

            @Override
            public boolean isAccountNonExpired() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
            }

            @Override
            public boolean isAccountNonLocked() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
            }

            @Override
            public boolean isCredentialsNonExpired() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
            }

            @Override
            public boolean isEnabled() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
            }

        });
        return foundUser.map(u -> {
            if (u.getUsername() == null) {
                return "user null";
            }
            if (encoder.matches(request.getPassword(), u.getPassword())) {
                return jwtService.generate(u.getUsername());
            }
            return "invalid credentials";
        });
    }

    @GetMapping("/protected")
    public Mono<String> publicUrl(UsernamePasswordAuthenticationToken authentication) {
        return Mono.just("Hello this is protected.");
    }

}
