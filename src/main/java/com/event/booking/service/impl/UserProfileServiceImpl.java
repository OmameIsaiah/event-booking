package com.event.booking.service.impl;

import com.event.booking.dto.request.UpdatePasswordRequest;
import com.event.booking.dto.request.UpdateProfileRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.exceptions.UnauthorizedException;
import com.event.booking.model.User;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.jwt.JwtUtils;
import com.event.booking.service.UserProfileService;
import com.event.booking.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private User validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    private String validateAuthorizedUser(Authentication authentication) {
        if (Objects.isNull(authentication)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        String username = jwtUtils.getUsernameFromAuthentication(authentication);
        if (Objects.isNull(username)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        return username;
    }

    private String validateAuthorizedUser(HttpServletRequest httpServletRequest) {
        if (Objects.isNull(httpServletRequest)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        String username = jwtUtils.getUsernameFromHttpServletRequest(httpServletRequest);
        if (Objects.isNull(username)) {
            throw new UnauthorizedException(INVALID_AUTHORIZATION_TOKEN);
        }
        return username;
    }

    @Override
    public ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest) {
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        FETCH_PROFILE_SUCCESSFUL,
                        Mapper.mapUserProfileResponse(user)
                ));
    }

    @Override
    public ResponseEntity<ApiResponse> updateProfileInfo(HttpServletRequest httpServletRequest, UpdateProfileRequest request) {
        validateProfileUpdateRequest(request);
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        user.setName(request.getName());
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PROFILE_UPDATED_SUCCESSFUL,
                        Mapper.mapUserProfileResponse(user)
                ));
    }

    private static void validateProfileUpdateRequest(UpdateProfileRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getName()) || "".equals(request.getName())) {
            throw new BadRequestException(NULL_NAME_PARAM);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatePassword(HttpServletRequest httpServletRequest, UpdatePasswordRequest request) {
        validatePasswordUpdateRequest(request);
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(WRONG_CURRENT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        PASSWORD_UPDATED_SUCCESSFUL,
                        Mapper.mapUserProfileResponse(user)
                ));
    }

    private static void validatePasswordUpdateRequest(UpdatePasswordRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getCurrentPassword()) || "".equals(request.getCurrentPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (Objects.isNull(request.getNewPassword()) || "".equals(request.getNewPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (Objects.isNull(request.getConfirmNewPassword()) || "".equals(request.getConfirmNewPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException(PASSWORD_MISMATCH);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest) {
        User user = validateUserByEmail(validateAuthorizedUser(httpServletRequest));
        user.setIsOnline(false);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        SIGN_OUT_SUCCESSFUL
                ));
    }
}
