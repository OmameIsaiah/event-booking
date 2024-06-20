package com.event.booking.service;

import com.event.booking.dto.request.RoleRequest;
import com.event.booking.dto.request.RoleUpdateRequest;
import com.event.booking.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoleAndPermissionsService {
    ResponseEntity<ApiResponse> viewPermissions();

    ResponseEntity<ApiResponse> addRole(RoleRequest request);

    ResponseEntity<ApiResponse> updateRole(RoleUpdateRequest request);

    ResponseEntity<ApiResponse> viewAllRoles();

    ResponseEntity<ApiResponse> viewRoleByID(String uuid);
}