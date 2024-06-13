package com.event.booking.service.impl;

import com.event.booking.dto.request.SignUpRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.DefaultRoles;
import com.event.booking.enums.OnboardingStage;
import com.event.booking.enums.UserType;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.DuplicateRecordException;
import com.event.booking.model.Role;
import com.event.booking.model.User;
import com.event.booking.model.UserRole;
import com.event.booking.repository.RoleRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.repository.UserRoleRepository;
import com.event.booking.service.SignUpService;
import com.event.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (!Utils.isEmailValid(request.getEmail())) {
            throw new BadRequestException(INVALID_EMAIL);
        }
        if (Objects.nonNull(request.getUserType()) && !Utils.isValidUserType(request.getUserType())) {
            throw new BadRequestException(INVALID_USER_TYPE);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException(PASSWORD_MISMATCH);
        }

        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new DuplicateRecordException(EMAIL_ALREADY_TAKEN);
        }

        String otpAndTime[] = Utils.generate4DigitOTPAndExpireTime().split("_");
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(Objects.nonNull(request.getUserType()) ? request.getUserType() : UserType.USER)
                .onboardingStage(OnboardingStage.STARTED)
                .verified(false)
                .active(false)
                .otpCode(otpAndTime[0])
                .otpExpireTime(otpAndTime[1])
                .userRoles(null)
                .userEvents(null)
                .build();
        user = userRepository.save(user);

        Set<String> strRoles = user.getUserType().label.equalsIgnoreCase(UserType.USER.label) ?
                Set.of(DefaultRoles.ROLE_USER.label) :
                Set.of(DefaultRoles.ROLE_USER.label, DefaultRoles.ROLE_ADMIN.label);

        Set<Role> roles = new HashSet<>();
        List<String> errorMessages = setUserRoles(strRoles, roles);
        if (!errorMessages.isEmpty()) {
            throw new BadRequestException(errorMessages.get(0));
        }

        User finalUser = user;
        List<UserRole> userRoleList = roles.stream()
                .map(role -> UserRole.builder()
                        .roleid(role)
                        .userrole(finalUser)
                        .build())
                .peek(userRoleRepository::save)
                .collect(Collectors.toList());
        user.setUserRoles(userRoleList);

        userRepository.save(user);
        //TODO SEND OTP THE EMAIL FOR VERFICATION

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED,
                        ACCOUNT_CREATION_SUCCESSFUL));
    }

    public List<String> setUserRoles(Set<String> strRoles, Set<Role> roles) {
        List<String> errorMessages = new ArrayList<>();
        strRoles.forEach(role -> {
            Optional<Role> optional = roleRepository.findRoleByName(role);
            if (Objects.isNull(optional) || optional.isEmpty()) {
                String errorMessage = String.format(ROLE_NOT_FOUND, role);
                errorMessages.add(errorMessage);
            } else {
                roles.add(optional.get());
            }
        });
        return errorMessages;
    }

    @Override
    public ResponseEntity<ApiResponse> sendOTP() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> verifyOTP() {
        return null;
    }
}
