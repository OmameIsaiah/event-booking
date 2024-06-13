package com.event.booking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    private Boolean status;
    private HttpStatus code;
    private String message;
    private T data;
    private Map<String, Object> meta = new HashMap<>();

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ApiResponse addMeta(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public ApiResponse(Boolean status, T data) {
        this.status = status;
        this.data = data;
    }

    public ApiResponse(Boolean status, HttpStatus code, T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(Boolean status, HttpStatus code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Boolean status, HttpStatus code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}