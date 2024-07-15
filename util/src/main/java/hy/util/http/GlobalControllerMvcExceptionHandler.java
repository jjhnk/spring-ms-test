package hy.util.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;

@ConditionalOnProperty(name = "app.reactive.enabled", havingValue = "false", matchIfMissing = false)
@RestControllerAdvice
public class GlobalControllerMvcExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerMvcExceptionHandler.class);

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public @ResponseBody HttpErrorInfo handleNotFoundExceptions(NotFoundException ex, WebRequest request) {

    return createHttpErrorInfo(NOT_FOUND, ex, request);
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(InvalidInputException.class)
  public @ResponseBody HttpErrorInfo handleInvalidInputException(InvalidInputException ex, WebRequest request) {

    return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex, request);
  }

  private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, Exception ex, WebRequest request) {
    final String path = request.getDescription(false);
    final String message = ex.getMessage();

    LOG.info("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
    return new HttpErrorInfo(httpStatus, path, message);
  }
}
