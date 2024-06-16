package com.event.booking.route;

import com.event.booking.dto.request.SignInRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.SignInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.event.booking.util.EndpointsURL.ENTRANCE_BASE_URL;
import static com.event.booking.util.EndpointsURL.ENTRANCE_SIGNIN;

@RestController
@RequestMapping(value = ENTRANCE_BASE_URL, headers = "Accept=application/json")
@Api(tags = "signin/entrance route", description = "Endpoints for user sign in.", consumes = "application/json", produces = "application/json", protocols = "https", value = "signin/entrance route")
@RequiredArgsConstructor
public class SigninRoute {
    private final SignInService signInService;

    @PostMapping(value = ENTRANCE_SIGNIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoints for user sign in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return signInService.signIn(request);
    }
}
