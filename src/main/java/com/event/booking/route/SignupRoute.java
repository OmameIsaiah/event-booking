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

import static com.event.booking.util.EndpointsURL.*;

@RestController
@RequestMapping(value = ONBOARDING_BASE_URL, headers = "Accept=application/json")
@Api(tags = "user signup route", description = "API for user signup/onboarding", consumes = "application/json", produces = "application/json", protocols = "https", value = "user signup route")
@RequiredArgsConstructor
public class SignupRoute {
    private final SignUpService signUpService;

    @PostMapping(value = ONBOARDING_SIGNUP, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for creating new user account")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return signUpService.signUp(request);
    }

    @PostMapping(value = ONBOARDING_SEND_OTP, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for sending or resending signup otp for email verification and account activation")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody @Valid SendOTPRequest request) {
        return signUpService.sendOTP(request);
    }

    @PostMapping(value = ONBOARDING_VERIFY_OTP, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for verifying signup otp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody @Valid VerifyOTPRequest request) {
        return signUpService.verifyOTP(request);
    }
}
