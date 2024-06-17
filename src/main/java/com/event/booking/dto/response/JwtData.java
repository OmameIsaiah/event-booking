package com.event.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static com.event.booking.util.ConfigParams.TOKEN_PREFIX;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtData implements Serializable {
    private String authorizationToken;
    private String type = TOKEN_PREFIX;
    private List<String> roles;
    private List<String> permissions;
}
