package com.acme.common.aspect;

import com.acme.common.domain.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GeneralHandlerException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handlerRuntimeException(
            RuntimeException e, HandlerMethod handlerMethod, HttpServletRequest request) {

        log.error("Processing exception...");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .serviceName(handlerMethod.getMethod().getName()).build());

    }
}
