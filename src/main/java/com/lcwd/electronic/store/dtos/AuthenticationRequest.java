package com.lcwd.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    private String email;
    private String password;

}
