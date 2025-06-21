package com.example.superligasparta.configuration;

import com.example.superligasparta.exception.ApiError;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> messages = ex.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    ApiError error = ApiError.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message("Ошибка валидации полей")
        .path(request.getRequestURI())
        .timestamp(LocalDateTime.now())
        .details(messages)
        .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ApiError> handleTypeEntityExists(EntityExistsException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiError> handleIllegalArgumentException(
      IllegalArgumentException ex,
      HttpServletRequest request
  ) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
  }

//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {
//    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
//  }

  private ResponseEntity<ApiError> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
    ApiError error = ApiError.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(status).body(error);
  }
}
