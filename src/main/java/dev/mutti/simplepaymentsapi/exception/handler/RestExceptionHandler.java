package dev.mutti.simplepaymentsapi.exception.handler;

import dev.mutti.simplepaymentsapi.exception.TransactionNotAuthorizedException;
import dev.mutti.simplepaymentsapi.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.CredentialException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorMessageDto> handleUserNotFoundException(UserNotFoundException e) {
        log.info("UserNotFoundException caught: {}", e.getMessage());
        RestErrorMessageDto error = new RestErrorMessageDto(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler({CredentialException.class, TransactionNotAuthorizedException.class})
    public ResponseEntity<RestErrorMessageDto> handleRuntimeException(Exception e) {
        log.info("CredentialException caught: {}", e.getMessage());
        RestErrorMessageDto error = new RestErrorMessageDto(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessageDto> handleException(Exception e) {
        log.error("Unexpected exception caught: {}", e.getMessage(), e);
        RestErrorMessageDto error = new RestErrorMessageDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestErrorMessageDto> handleRuntimeException(IllegalArgumentException e) {
        log.error("Illegal argument exception caught: {}", e.getMessage(), e);
        RestErrorMessageDto error = new RestErrorMessageDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<RestErrorMessageDto> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("DuplicateKeyException caught: {}", e.getMessage(), e);
        RestErrorMessageDto error = new RestErrorMessageDto(HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(error.status()).body(error);
    }
}
