package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.RefreshToken;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationResponse {
    private String token;
    UserDto user;
//    private String authenticationToken;
    private RefreshTokenDto refreshToken;
}
