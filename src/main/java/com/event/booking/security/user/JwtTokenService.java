package com.event.booking.security.user;

import com.event.booking.security.jwt.JwtResponse;

public interface JwtTokenService {
    JwtResponse getAccessToken(String username, String password);
}
