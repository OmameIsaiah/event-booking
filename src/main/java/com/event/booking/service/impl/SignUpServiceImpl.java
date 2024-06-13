package com.event.booking.service.impl;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    @Override
    public ResponseEntity<ApiResponse> signUp() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> sendOTP() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> verifyOTP() {
        return null;
    }
}
