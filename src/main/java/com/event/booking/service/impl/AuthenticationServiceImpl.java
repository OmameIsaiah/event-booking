package com.event.booking.service.impl;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.User;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.user.JwtTokenService;
import com.event.booking.service.AuthenticationService;
import com.event.booking.util.AppMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    private User validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> authenticate(String username, String password) {
        validateUsernameAndPassword(username, password);
        User user = validateUserByEmail(username);
        checkAccountVerificationStatusAndPassword(password, user);
        String jwt = jwtTokenService.getAccessToken(username, password).getToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK, jwt), httpHeaders, HttpStatus.OK);
    }

    private static void validateUsernameAndPassword(String username, String password) {
        if (Objects.isNull(username) || Objects.isNull(password)) {
            throw new BadRequestException(AppMessages.INVALID_REQUEST_PARAMETERS);
        }
        if ("".equals(username) || "".equals(password)) {
            throw new BadRequestException(AppMessages.INVALID_REQUEST_PARAMETERS);
        }
    }

    private void checkAccountVerificationStatusAndPassword(String password, User user) {
        if (Objects.isNull(user.getVerified()) || !user.getVerified()) {
            throw new BadRequestException(ACCOUNT_NOT_VERIFIED);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(WRONG_ACCOUNT_PASSWORD);
        }
    }
}
