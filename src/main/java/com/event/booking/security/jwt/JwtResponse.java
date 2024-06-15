package com.event.booking.security.jwt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JwtResponse implements Serializable {
    private String token;
    private String type = "Bearer";
    private String userType;
    private String username;
    private String password;
    private List<String> roles;
    private List<String> permissions;

    public JwtResponse(String accessToken, String userType, String username, List<String> roles, List<String> permission) {
        this.token = accessToken;
        this.userType = userType;
        this.username = username;
        this.roles = roles;
        this.permissions = permission;
    }
}
