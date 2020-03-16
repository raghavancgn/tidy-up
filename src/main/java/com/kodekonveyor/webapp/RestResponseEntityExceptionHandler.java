package com.kodekonveyor.webapp;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kodekonveyor.logging.LoggingMarkers;

@ControllerAdvice
@InterfaceClass
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

  @Autowired
  private Logger loggerService;

  @Value("${com.kodekonveyor.tidyup.loginUrl}")
  public String loginUrl;

  @ExceptionHandler({
      NotLoggedInException.class, ValidationException.class
  })
  public ResponseEntity<Object> handleNotLoggedInException(
      final NotLoggedInException exception,
      final WebRequest request
  ) {
    final String bodyOfResponse = exception.getMessage();

    loggerService.warn(LoggingMarkers.AUTHENTICATION, "not logged in");
    final HttpHeaders headers = new HttpHeaders();
    headers.add("Location", loginUrl);
    return handleExceptionInternal(
        exception, bodyOfResponse, headers, HttpStatus.FOUND, request
    );
  }

}
