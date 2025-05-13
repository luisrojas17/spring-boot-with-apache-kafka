package com.acme.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorMessage {
    private LocalDateTime timestamp;
    private String message;
    private String path;
    private String serviceName;
}
