package com.event.booking.dto.request;

import com.event.booking.enums.UserType;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.*;

@Data
public class SignUpRequest implements Serializable {
    @NotNull(message = NULL_NAME_PARAM)
    @NotEmpty(message = EMPTY_NAME_PARAM)
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
    @Email(message = INVALID_EMAIL,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = EMPTY_EMAIL)
    @NotNull(message = NULL_EMAIL)
    private String email;
    @NotNull(message = NULL_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String password;
    private UserType userType = UserType.ADMIN;
}
