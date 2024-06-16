package com.event.booking.dto.response;

import com.event.booking.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {
    private String name;
    private String email;
    private UserType userType;
    private String userToken;
    private Boolean isOnline;
    private LocalDateTime lastLogin;
}
