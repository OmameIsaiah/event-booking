package com.event.booking.service;

import com.event.booking.dto.request.UpdatePasswordRequest;
import com.event.booking.dto.request.UpdateProfileRequest;
import com.event.booking.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserProfileService {
    ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest);

    ResponseEntity<ApiResponse> updateProfileInfo(HttpServletRequest httpServletRequest, UpdateProfileRequest request);

    ResponseEntity<ApiResponse> updatePassword(HttpServletRequest httpServletRequest, UpdatePasswordRequest request);

    ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest);
}
