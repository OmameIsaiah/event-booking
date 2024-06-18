package com.event.booking.service;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.UserType;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {
    ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size);

    ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType);

    ResponseEntity<ApiResponse> searchUsers(String keyword);

    ResponseEntity<ApiResponse> deleteUser(String uuid);
}
