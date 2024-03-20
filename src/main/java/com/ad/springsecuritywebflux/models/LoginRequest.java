package com.ad.springsecuritywebflux.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
