package com.event.booking.route;

import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.event.booking.util.EndpointsURL.AUTH_BASE_URL;
import static com.event.booking.util.EndpointsURL.AUTH_TOKEN_URL;

@RestController
@RequestMapping(value = AUTH_BASE_URL, headers = "Accept=application/json")
@Api(tags = "user authentication route", description = "API for user authentication", consumes = "application/json", produces = "application/json", protocols = "https", value = "user authentication route")
@RequiredArgsConstructor
public class AuthRoute {
    private final AuthenticationService authenticationService;

    @PostMapping(value = AUTH_TOKEN_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for authenticating a user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> authenticate(@RequestParam("username") String username,
                                                    @RequestParam("password") String password) {
        return authenticationService.authenticate(username, password);
    }
}
