package com.event.booking.service.impl;

import com.event.booking.dto.request.OTPNotificationRequest;
import com.event.booking.dto.request.SendOTPRequest;
import com.event.booking.dto.request.SignUpRequest;
import com.event.booking.dto.request.VerifyOTPRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.DefaultRoles;
import com.event.booking.enums.OnboardingStage;
import com.event.booking.enums.UserType;
import com.event.booking.exceptions.BadRequestException;
import com.event.booking.exceptions.DuplicateRecordException;
import com.event.booking.exceptions.RecordNotFoundException;
import com.event.booking.model.Role;
import com.event.booking.model.User;
import com.event.booking.model.UserRole;
import com.event.booking.notification.EmailNotificationService;
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
    private final EmailNotificationService notificationService;

    private User validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpRequest request) {
        validateSignupRequest(request);
        String otpAndTime[] = Utils.generate4DigitOTPAndExpireTime().split("_");
        User user = buildNewUserModel(request, otpAndTime);
        assignRolesToNewUser(user);
        sendSignupOTP(user, otpAndTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED,
                        ACCOUNT_CREATION_SUCCESSFUL));
    }

    private User buildNewUserModel(SignUpRequest request, String[] otpAndTime) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(Objects.nonNull(request.getUserType()) ? request.getUserType() : UserType.USER)
                .onboardingStage(OnboardingStage.STARTED)
                .verified(false)
                .isOnline(false)
                .otpCode(otpAndTime[0])
                .otpExpireTime(otpAndTime[1])
                .userRoles(null)
                .userEvents(null)
                .build();
        //user = userRepository.save(user);
        return user;
    }

    private void assignRolesToNewUser(User user) {
        Set<String> strRoles = user.getUserType().label.equalsIgnoreCase(UserType.USER.label) ?
                Set.of(DefaultRoles.ROLE_USER.label) :
                Set.of(DefaultRoles.ROLE_USER.label, DefaultRoles.ROLE_ADMIN.label);

        Set<Role> roles = new HashSet<>();
        List<String> errorMessages = setUserRoles(strRoles, roles);
        if (!errorMessages.isEmpty()) {
            throw new BadRequestException(errorMessages.get(0));
        }
        user = userRepository.save(user);
        User roleAssignedUser = user;
        List<UserRole> userRoleList = roles.stream()
                .map(role -> UserRole.builder()
                        .roleid(role)
                        .userrole(roleAssignedUser)
                        .build())
                .peek(userRoleRepository::save)
                .collect(Collectors.toList());
        roleAssignedUser.setUserRoles(userRoleList);
        userRepository.save(roleAssignedUser);
    }

    private void validateSignupRequest(SignUpRequest request) {
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
    public ResponseEntity<ApiResponse> sendOTP(SendOTPRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        processAndSendOTP(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK,
                        OTP_SENT_SUCCESSFUL));
    }

    private void processAndSendOTP(SendOTPRequest request) {
        User user = validateUserByEmail(request.getEmail());
        String otpAndTime[] = Utils.generate4DigitOTPAndExpireTime().split("_");
        user.setOtpCode(otpAndTime[0]);
        user.setOtpExpireTime(otpAndTime[1]);
        userRepository.save(user);
        sendSignupOTP(user, otpAndTime);
    }

    private void sendSignupOTP(User user, String[] otpAndTime) {
        //TODO SEND IT THROUGH KAFKA OR REDIS
        OTPNotificationRequest notificationRequest = OTPNotificationRequest
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .otp(otpAndTime[0])
                .build();
        notificationService.sendOTPNotification(notificationRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> verifyOTP(VerifyOTPRequest request) {
        User user = validateOTPVerificationRequest(request);
        if (user.getVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(true,
                            HttpStatus.OK,
                            ACCOUNT_ALREADY_ACTIVATED));
        }
        verifyAndActivateUserAccount(request, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK,
                        OTP_VERIFIED_SUCCESSFULLY));
    }

    private void verifyAndActivateUserAccount(VerifyOTPRequest request, User user) {
        String otpCode = user.getOtpCode();
        long previousTime = Long.parseLong(Objects.isNull(user.getOtpExpireTime()) ? "0" : user.getOtpExpireTime());
        long current = System.currentTimeMillis() / 1000;
        if (current >= previousTime) {
            throw new BadRequestException(EXPIRED_OTP);
        }
        if (!otpCode.equals(request.getOtp())) {
            throw new BadRequestException(INVALID_OTP);
        }
        user.setVerified(true);
        user.setOnboardingStage(OnboardingStage.VERIFIED);
        user.setOtpCode(null);
        user.setOtpExpireTime(null);
        userRepository.save(user);
    }

    private User validateOTPVerificationRequest(VerifyOTPRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getEmail())) {
            throw new BadRequestException(NULL_EMAIL);
        }
        if (Objects.isNull(request.getOtp())) {
            throw new BadRequestException(NULL_OTP_PARAM);
        }
        User user = validateUserByEmail(request.getEmail());
        return user;
    }
}
