package dev.mutti.simplepaymentsapi.exception.handler;

import org.springframework.http.HttpStatus;

public record RestErrorMessageDto(HttpStatus status, String message) {
}
