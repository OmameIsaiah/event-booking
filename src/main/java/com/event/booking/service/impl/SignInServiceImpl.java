package com.event.booking.service.impl;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {
    @Override
    public ResponseEntity<ApiResponse> signIn() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> initiatePasswordReset() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> completePasswordReset() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> signOut() {
        return null;
    }
}
