package com.event.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.event.booking.util.AppMessages.*;

@Data
public class UpdateProfileRequest implements Serializable {
    @NotNull(message = NULL_NAME_PARAM)
    @NotEmpty(message = EMPTY_NAME_PARAM)
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
}
