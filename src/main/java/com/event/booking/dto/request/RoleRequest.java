package com.event.booking.dto.request;

import com.event.booking.enums.Permissions;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

import static com.event.booking.util.AppMessages.EMPTY_ROLE_NAME;
import static com.event.booking.util.AppMessages.NULL_ROLE_NAME;

@Data
public class RoleRequest implements Serializable {
    @NotNull(message = NULL_ROLE_NAME)
    @NotEmpty(message = EMPTY_ROLE_NAME)
    private String roleName;
    private String description;
    private Set<Permissions> permissions;
}
