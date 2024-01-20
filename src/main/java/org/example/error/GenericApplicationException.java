package org.example.error;

import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class GenericApplicationException extends ErrorResponseException {

  public GenericApplicationException() {
    super(HttpStatus.NOT_FOUND, asProblemDetail("Internal server error"), null);
  }

  private static ProblemDetail asProblemDetail(String message) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    problemDetail.setTitle("Internal server error");
    problemDetail.setType(URI.create("https://api.products.com/errors/internal-server-error"));
    problemDetail.setProperty("errorCode", "INTERNAL_SERVER_ERROR");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }
}
