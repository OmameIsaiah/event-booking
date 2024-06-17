package com.event.booking.service.impl;

import com.event.booking.dto.request.SignInRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.dto.response.JwtData;
import com.event.booking.dto.response.SignInResponse;
import com.event.booking.dto.response.UserData;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.User;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.jwt.JwtResponse;
import com.event.booking.security.user.JwtTokenService;
import com.event.booking.service.SignInService;
import com.event.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    private User validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signIn(SignInRequest request) {
        User user = validateSignInParamAndPassword(request);
        UserData userData = getUserData(user);
        JwtResponse jwtResponse = jwtTokenService.getAccessToken(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        SIGN_IN_SUCCESSFUL,
                        SignInResponse.builder()
                                .userAccountData(userData)
                                .jwtTokenData(JwtData.builder()
                                        .authorizationToken(jwtResponse.getAuthorizationToken())
                                        .type(jwtResponse.getType())
                                        .roles(jwtResponse.getRoles())
                                        .permissions(jwtResponse.getPermissions())
                                        .build())
                                .build()
                ));
    }

    private UserData getUserData(User user) {
        user.setIsOnline(true);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return UserData.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .userToken(user.getUserToken())
                .isOnline(user.getIsOnline())
                .lastLogin(Utils.convertLocalDateTimeToString(user.getLastLogin()))
                .build();
    }

    private User validateSignInParamAndPassword(SignInRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getEmail()) || "".equals(request.getEmail())) {
            throw new BadRequestException(NULL_EMAIL);
        }
        if (Objects.isNull(request.getPassword()) || "".equals(request.getPassword())) {
            throw new BadRequestException(NULL_PASSWORD);
        }
        User user = validateUserByEmail(request.getEmail());
        if (Objects.isNull(user.getVerified()) || !user.getVerified()) {
            throw new BadRequestException(ACCOUNT_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException(WRONG_ACCOUNT_PASSWORD);
        }
        return user;
    }
}
