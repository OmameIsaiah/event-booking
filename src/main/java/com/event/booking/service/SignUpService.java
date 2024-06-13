package com.event.booking.service;

import com.event.booking.dto.request.SignUpRequest;
import com.event.booking.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignUpService {
    ResponseEntity<ApiResponse> signUp(SignUpRequest request);

    ResponseEntity<ApiResponse> sendOTP();

    ResponseEntity<ApiResponse> verifyOTP();
}
