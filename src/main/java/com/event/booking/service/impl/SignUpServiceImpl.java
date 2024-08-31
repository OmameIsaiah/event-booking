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
import com.event.booking.model.UsersTable;
import com.event.booking.model.UserRole;
import com.event.booking.notification.kafka.MessageProducer;
import com.event.booking.repository.RoleRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.repository.UserRoleRepository;
import com.event.booking.service.SignUpService;
import com.event.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;
import static com.event.booking.util.Utils.EMAIL_SIGNUP_OTP;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailNotificationService notificationService;
    private final MessageProducer messageProducer;

    private UsersTable validateUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(WRONG_ACCOUNT_EMAIL));
    }

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpRequest request) {
        validateSignupRequest(request);
        String otpAndTime[] = Utils.generate4DigitOTPAndExpireTime().split("_");
        UsersTable users = buildNewUserModel(request, otpAndTime);
        assignRolesToNewUser(users);
        sendSignupOTP(users, otpAndTime);
        log.info("USER CREATED SUCCESSFULLY: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        ACCOUNT_CREATED_SUCCESSFULLY));
    }

    private UsersTable buildNewUserModel(SignUpRequest request, String[] otpAndTime) {
        return UsersTable.builder()
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
    }

    private void assignRolesToNewUser(UsersTable users) {
        Set<String> strRoles = users.getUserType().label.equalsIgnoreCase(UserType.USER.label) ?
                Set.of(DefaultRoles.ROLE_USER.label) :
                Set.of(DefaultRoles.ROLE_USER.label, DefaultRoles.ROLE_ADMIN.label);

        Set<Role> roles = new HashSet<>();
        List<String> errorMessages = setUserRoles(strRoles, roles);
        if (!errorMessages.isEmpty()) {
            throw new BadRequestException(errorMessages.get(0));
        }
        users = userRepository.save(users);
        UsersTable roleAssignedUsers = users;
        List<UserRole> userRoleList = roles.stream()
                .map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setRoleid(role);
                    userRole.setUserrole(roleAssignedUsers);
                    userRole.setUuid(UUID.randomUUID().toString());
                    return userRole;
                })
                .peek(userRoleRepository::save)
                .collect(Collectors.toList());
        roleAssignedUsers.setUserRoles(userRoleList);
        userRepository.save(roleAssignedUsers);
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
        Optional<UsersTable> userOptional = userRepository.findUserByEmail(request.getEmail());
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
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        OTP_SENT_SUCCESSFULLY));
    }

    private void processAndSendOTP(SendOTPRequest request) {
        UsersTable users = validateUserByEmail(request.getEmail());
        String otpAndTime[] = Utils.generate4DigitOTPAndExpireTime().split("_");
        users.setOtpCode(otpAndTime[0]);
        users.setOtpExpireTime(otpAndTime[1]);
        userRepository.save(users);
        sendSignupOTP(users, otpAndTime);
    }

    private void sendSignupOTP(UsersTable users, String[] otpAndTime) {
        log.info("OTP CODE '{}' SENT TO : {}", otpAndTime[0], users.getEmail());
        messageProducer.sendMessage(EMAIL_SIGNUP_OTP,
                OTPNotificationRequest
                        .builder()
                        .name(users.getName())
                        .email(users.getEmail())
                        .otp(otpAndTime[0])
                        .build());
        // notificationService.sendOTPNotification(notificationRequest);
    }

    @Override
    public ResponseEntity<ApiResponse> verifyOTP(VerifyOTPRequest request) {
        UsersTable users = validateOTPVerificationRequest(request);
        if (users.getVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(true,
                            HttpStatus.OK.value(),
                            HttpStatus.OK,
                            ACCOUNT_ALREADY_ACTIVATED));
        }
        verifyAndActivateUserAccount(request, users);
        log.info("OTP CODE VERIFIED SUCCESSFULLY FOR: {}", users.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        OTP_VERIFIED_SUCCESSFULLY));
    }

    private void verifyAndActivateUserAccount(VerifyOTPRequest request, UsersTable users) {
        String otpCode = users.getOtpCode();
        long previousTime = Long.parseLong(Objects.isNull(users.getOtpExpireTime()) ? "0" : users.getOtpExpireTime());
        long current = System.currentTimeMillis() / 1000;
        if (current >= previousTime) {
            throw new BadRequestException(EXPIRED_OTP);
        }
        if (!otpCode.equals(request.getOtp())) {
            throw new BadRequestException(INVALID_OTP);
        }
        users.setVerified(true);
        users.setOnboardingStage(OnboardingStage.VERIFIED);
        users.setOtpCode(null);
        users.setOtpExpireTime(null);
        userRepository.save(users);
    }

    private UsersTable validateOTPVerificationRequest(VerifyOTPRequest request) {
        if (Objects.isNull(request)) {
            throw new BadRequestException(INVALID_REQUEST_PARAMETERS);
        }
        if (Objects.isNull(request.getEmail())) {
            throw new BadRequestException(NULL_EMAIL);
        }
        if (Objects.isNull(request.getOtp())) {
            throw new BadRequestException(NULL_OTP_PARAM);
        }
        return validateUserByEmail(request.getEmail());
    }
}
