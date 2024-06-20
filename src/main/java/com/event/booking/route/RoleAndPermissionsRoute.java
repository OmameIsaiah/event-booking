package com.event.booking.route;

import com.event.booking.dto.request.RoleRequest;
import com.event.booking.dto.request.RoleUpdateRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.service.RoleAndPermissionsService;
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
@RequestMapping(value = ROLES_BASE_URL, headers = "Accept=application/json")
@Api(tags = "roles and permissions route", description = "Endpoints for creating, updating and viewing roles and permissions [Accessible only to ADMIN users with valid authorization]", consumes = "application/json", produces = "application/json", protocols = "https", value = "roles and permissions route")
@RequiredArgsConstructor
public class RoleAndPermissionsRoute {
    private final RoleAndPermissionsService roleAndPermissionsService;

    @GetMapping(value = VIEW_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for viewing list of permissions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewPermissions() {
        return roleAndPermissionsService.viewPermissions();
    }

    @PostMapping(value = ADD_NEW_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for adding new custom role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> addRole(@RequestBody @Valid RoleRequest request) {
        return roleAndPermissionsService.addRole(request);
    }

    @PutMapping(value = UPDATE_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for updating existing role via the role uuid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> updateRole(@RequestBody @Valid RoleUpdateRequest request) {
        return roleAndPermissionsService.updateRole(request);
    }

    @GetMapping(value = VIEW_ALL_ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for viewing all roles")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewAllRoles() {
        return roleAndPermissionsService.viewAllRoles();
    }

    @GetMapping(value = VIEW_ROLE_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Endpoint for viewing role by uuid")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> viewRoleByID(@PathVariable("uuid") String uuid) {
        return roleAndPermissionsService.viewRoleByID(uuid);
    }
}
