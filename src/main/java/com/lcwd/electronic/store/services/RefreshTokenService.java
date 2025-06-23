package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.RefreshTokenDto;
import org.springframework.stereotype.Service;


public interface RefreshTokenService {
    // create
    RefreshTokenDto createRefreshToken(String username);

    //find by token]
    RefreshTokenDto findByToken(String token);

    //verify
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);


}
