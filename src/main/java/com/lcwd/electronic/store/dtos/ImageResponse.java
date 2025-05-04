package com.lcwd.electronic.store.dtos;

import org.springframework.http.HttpStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
}
