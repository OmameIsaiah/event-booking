package com.event.booking.service.impl;

import com.event.booking.dto.request.RoleRequest;
import com.event.booking.dto.request.RoleUpdateRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.Permissions;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.DuplicateRecordException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.Role;
import com.event.booking.repository.RoleRepository;
import com.event.booking.service.RoleAndPermissionsService;
import com.event.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class RoleAndPermissionsServiceImpl implements RoleAndPermissionsService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<ApiResponse> viewPermissions() {
        List<Permissions> list = Arrays.stream(Permissions.values()).toList();
        if (list.isEmpty()) {
            throw new RecordNotFoundException(NO_PERMISSIONS_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PERMISSIONS_RETRIEVED_SUCCESSFULLY,
                        list
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> addRole(RoleRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getPermissions()) || request.getPermissions().isEmpty()) {
            throw new BadRequestException(NULL_PERMISSIONS_PARAM);
        }
        Optional<Role> roleOptional = roleRepository.findRoleByName(request.getRoleName());
        if (roleOptional.isPresent()) {
            throw new DuplicateRecordException(DUPLICATE_ROLE_NAME);
        }
        Role role = new Role();
        role.setUuid(UUID.randomUUID().toString());
        role.setName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setPermissions(request.getPermissions());
        role = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        ROLE_CREATED_SUCCESSFULLY,
                        Mapper.mapRoleToRoleResponse(role)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> updateRole(RoleUpdateRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getPermissions()) || request.getPermissions().isEmpty()) {
            throw new BadRequestException(NULL_PERMISSIONS_PARAM);
        }
        Optional<Role> roleOptional = roleRepository.findRoleByUuid(request.getUuid());
        if (roleOptional.isEmpty()) {
            throw new RecordNotFoundException(NO_ROLE_FOUND_WITH_UUID);
        }

        Role role = roleOptional.get();
        role.setName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setPermissions(request.getPermissions());
        role = roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ROLE_UPDATED_SUCCESSFULLY,
                        Mapper.mapRoleToRoleResponse(role)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> viewAllRoles() {
        List<Role> list = roleRepository.findAll();
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new RecordNotFoundException(NO_ROLES_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ROLES_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapRoleToRoleResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> viewRoleByID(String uuid) {
        Optional<Role> optional = roleRepository.findRoleByUuid(uuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_ROLE_FOUND_WITH_UUID);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        ROLE_RETRIEVED_SUCCESSFULLY,
                        Mapper.mapRoleToRoleResponse(optional.get())
                ));
    }
}