package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositiories.UserRepository;
import com.lcwd.electronic.store.security.JwtHelper;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthenticationController {
    //method to generate token
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager;
//    private Logger log = LoggerFactory.getLogger(AuthenticationController.class);
//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
        log.info("Username {},Password {}",request.getEmail(),request.getPassword());
        this.doAuthenticate(request.getEmail(),request.getPassword());
        User user= userRepository.findByEmail(request.getEmail()).orElse(null);

       String token= jwtHelper.generateToken(user);
        JwtResponse jwtResponse=JwtResponse.builder().token(token).user(modelMapper.map(user, UserDto.class)).build();
        return ResponseEntity.ok(jwtResponse);
    }
    private void doAuthenticate(String email,String password)
    {
        try {

            Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
            authenticationManager.authenticate(authentication);

        }catch (BadCredentialsException ex)
        {
            throw new BadCredentialsException("Invalid username and password !!");
        }
    }
}
