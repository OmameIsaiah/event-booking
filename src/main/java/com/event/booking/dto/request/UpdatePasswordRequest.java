package com.event.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.*;

@Data
public class UpdatePasswordRequest implements Serializable {
    @NotNull(message = NULL_CURRENT_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_CURRENT_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String currentPassword;
    @NotNull(message = NULL_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String newPassword;
    @NotNull(message = NULL_PASSWORD_PARAM)
    @NotEmpty(message = EMPTY_PASSWORD_PARAM)
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String confirmNewPassword;
}
