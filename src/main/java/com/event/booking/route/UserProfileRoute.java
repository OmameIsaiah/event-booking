package com.event.booking.route;

import com.event.booking.dto.request.UpdatePasswordRequest;
import com.event.booking.dto.request.UpdateProfileRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.UserProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.event.booking.util.EndpointsURL.*;

@RestController
@RequestMapping(value = PROFILE_BASE_URL, headers = "Accept=application/json")
@Api(tags = "profile route", description = "Endpoints for fetching and updating user profile info, updating password and signing out [Accessible to ALL users with valid authorization]", consumes = "application/json", produces = "application/json", protocols = "https", value = "profile route")
@RequiredArgsConstructor
public class UserProfileRoute {
    private final UserProfileService userProfileService;

    @GetMapping(value = PROFILE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint fetching user profile info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> getProfileInfo(HttpServletRequest httpServletRequest) {
        return userProfileService.getProfileInfo(httpServletRequest);
    }

    @PostMapping(value = PROFILE_UPDATE_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint updating user profile info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateProfileInfo(@RequestBody @Valid UpdateProfileRequest request, HttpServletRequest httpServletRequest) {
        return userProfileService.updateProfileInfo(httpServletRequest, request);
    }

    @PostMapping(value = PROFILE_UPDATE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint updating password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody @Valid UpdatePasswordRequest request, HttpServletRequest httpServletRequest) {
        return userProfileService.updatePassword(httpServletRequest, request);
    }

    @PostMapping(value = PROFILE_SIGNOUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint signing out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signOut(HttpServletRequest httpServletRequest) {
        return userProfileService.signOut(httpServletRequest);
    }
}
