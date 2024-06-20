package com.event.booking.dto.response;

import com.event.booking.enums.Permissions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse implements Serializable {
    private String uuid;
    private String name;
    private String description;
    private Set<Permissions> permissions;
}
