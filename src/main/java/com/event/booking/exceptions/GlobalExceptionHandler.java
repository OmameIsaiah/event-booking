package com.event.booking.exceptions;

import com.event.booking.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(new ApiResponse<>(false, status.value(), status, String.join(",", errors)));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity handleRecordNotFoundExceptions(RecordNotFoundException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn("{} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity handleDuplicateRecordExceptions(DuplicateRecordException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedExceptions(UnauthorizedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedExceptions(AccessDeniedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        String error = exception.getName() + " should be of type " + exception.getRequiredType().getName();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, error));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerGlobalErrors(Exception exception) {
        exception.printStackTrace();
        log.warn("An error occur  {}", exception.fillInStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
}
