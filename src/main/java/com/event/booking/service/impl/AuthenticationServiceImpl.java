package com.event.booking.service.impl;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public ResponseEntity<ApiResponse> authenticate(String username, String password) {


        String jwt = "";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK, jwt), httpHeaders, HttpStatus.OK);
    }
}
