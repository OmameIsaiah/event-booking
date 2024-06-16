package com.event.booking.service;

import com.event.booking.dto.request.SignInRequest;
import com.event.booking.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface SignInService {

    ResponseEntity<ApiResponse> signIn(SignInRequest request);
}
