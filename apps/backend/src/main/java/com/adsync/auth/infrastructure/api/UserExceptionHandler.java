package com.adsync.auth.infrastructure.api;

import com.adsync.auth.domain.exceptions.EmailAlreadyTakenException;
import com.adsync.auth.domain.exceptions.IncorrectPasswordException;
import com.adsync.auth.domain.exceptions.UserDoesNotExistException;
import com.adsync.core.infrastructure.api.errors.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(1)
public class UserExceptionHandler {

  @ExceptionHandler(EmailAlreadyTakenException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyTakenException(
      EmailAlreadyTakenException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserDoesNotExistException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleUserDoesNotExistException(
      UserDoesNotExistException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid credentials");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IncorrectPasswordException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleInvalidPasswordException(
      IncorrectPasswordException ex) {
    var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid credentials");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
