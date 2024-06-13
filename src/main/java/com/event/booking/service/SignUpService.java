package com.event.booking.service;

import com.event.booking.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignUpService {
    ResponseEntity<ApiResponse> signUp();

    ResponseEntity<ApiResponse> sendOTP();

    ResponseEntity<ApiResponse> verifyOTP();
}
