package com.lcwd.electronic.store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entities.Providers;
import com.lcwd.electronic.store.entities.RefreshToken;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositiories.UserRepository;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.RefreshTokenService;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

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
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenService  refreshTokenService;
    @Value("${app.google.client_id}")
    private String googleClientId;
    @Value("${app.google.default.password}")
    private String googleProviderDefaultPassword;

    @PostMapping("/login")   //Todo: /login
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
    {
        log.info("Username {},Password {}",request.getEmail(),request.getPassword());
        this.doAuthenticate(request.getEmail(),request.getPassword());
        User user= userRepository.findByEmail(request.getEmail()).orElse(null);

       String token= jwtHelper.generateToken(user);
       //Refresh Token
        RefreshTokenDto refreshToken =refreshTokenService.createRefreshToken(user.getEmail());
        AuthenticationResponse authenticationResponse = AuthenticationResponse
                .builder()
                .token(token)
                .user(modelMapper.map(user, UserDto.class))
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(authenticationResponse);
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
    //handle login with
    //{IdToken}
    @PostMapping("/login-with-google")
    public ResponseEntity<AuthenticationResponse> handleGoogleLogin(@RequestBody GoogleLoginRequest loginRequest) throws GeneralSecurityException, IOException {
        log.info("id Token :{}",loginRequest.getIdToken());
        GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(),new GsonFactory()).setAudience(Collections.singleton(googleClientId)).build();
        GoogleIdToken googleIdToken=verifier.verify(loginRequest.getIdToken());
        if(googleIdToken!=null)
        {
            // verified
            GoogleIdToken.Payload payload=googleIdToken.getPayload();
            String email=payload.getEmail();

            String userName= payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            log.info("Name {}",name);
            log.info("Email {}",email);
            log.info("Picture {}",pictureUrl);
            log.info("userName {}",userName);
            UserDto userDto=new UserDto();
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setImageName(pictureUrl);
            userDto.setPassword(googleProviderDefaultPassword);
            userDto.setAbout("user is created using google ");
            userDto.setProviders(Providers.GOOGLE);
//            UserDto userByEmail=userService.getUserByEmail(userDto.getEmail());
//            UserDto user= userService.createUser(userDto);
            UserDto user=null;
            try{
                log.info("user is loaded from database");
                 user=userService.getUserByEmail(userDto.getEmail());
                 //logic implement
                //provider
                if(user.getProviders().equals(userDto.getProviders()))
                {
                    //continue
                }
                else{
                    throw new BadCredentialsException("Your email is already registered !! Try to login with username and password");
                }

            }
          catch(ResourceNotFoundException ex)
            {
                log.info("This time user created: because this is new user");
                user=userService.createUser(userDto);
            }
            this.doAuthenticate(user.getEmail(),userDto.getPassword());
            User user1=modelMapper.map(user, User.class);
            String token= jwtHelper.generateToken(user1);
            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().token(token).user(modelMapper.map(user, UserDto.class)).build();
            return ResponseEntity.ok(authenticationResponse);
        }
        else{
            log.info("token is invalid ! !");
            throw new BadApiRequest("Invalid Google User");
        }


    }
}
