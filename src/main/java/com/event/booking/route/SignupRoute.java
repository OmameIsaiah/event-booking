package com.event.booking.route;

import com.event.booking.dto.request.SendOTPRequest;
import com.event.booking.dto.request.SignUpRequest;
import com.event.booking.dto.request.VerifyOTPRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.SignUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/users/onboarding", headers = "Accept=application/json")
@Api(tags = "user signup route", description = "API for user signup/onboarding", consumes = "application/json", produces = "application/json", protocols = "https", value = "user signup route")
@RequiredArgsConstructor
public class SignupRoute {
    private final SignUpService signUpService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for creating new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return signUpService.signUp(request);
    }

    @PostMapping(value = "/send/otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for sending or resending signup otp for email verification and account activation")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody @Valid SendOTPRequest request) {
        return signUpService.sendOTP(request);
    }

    @PostMapping(value = "/verify/otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for verifying signup otp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody @Valid VerifyOTPRequest request) {
        return signUpService.verifyOTP(request);
    }
}
