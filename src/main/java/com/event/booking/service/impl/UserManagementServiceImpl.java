package com.event.booking.service.impl;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.UserType;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.User;
import com.event.booking.repository.UserEventRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.repository.UserRoleRepository;
import com.event.booking.service.UserManagementService;
import com.event.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserEventRepository userEventRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllUsers(Integer page, Integer size) {
        Page<User> list = userRepository.findAll(PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> filterUsers(Integer page, Integer size, UserType userType) {
        Page<User> list = userRepository.findUsersByType(
                userType,
                PageRequest.of((Objects.isNull(page) ? 0 : page), (Objects.isNull(size) ? 50 : size)));
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> searchUsers(String keyword) {
        if (Objects.isNull(keyword)) {
            throw new BadRequestException(NULL_KEYWORD_PARAM);
        }
        List<User> list = userRepository.searchUsers(keyword);
        if (list.isEmpty() || Objects.isNull(list)) {
            throw new RecordNotFoundException(NO_USER_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapUserProfileResponse)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUser(String uuid) {
        if (Objects.isNull(uuid)) {
            throw new BadRequestException(NULL_UUID_PARAM);
        }
        Optional<User> optional = userRepository.findUserByUUID(uuid);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(NO_USER_FOUND_WITH_UUID);
        }
        userRoleRepository.deleteUserRoleByUserId(optional.get().getId());
        userEventRepository.deleteUserEventByUserId(optional.get().getId());
        userRepository.deleteById(optional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        USERS_DELETED_SUCCESSFULLY
                ));
    }
}
