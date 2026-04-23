package com.pgw.payflow.service.impl;

import com.pgw.payflow.constant.error.ErrorType;
import com.pgw.payflow.dto.request.LoginRequest;
import com.pgw.payflow.dto.request.UserCreateRequest;
import com.pgw.payflow.dto.response.AuthResponse;
import com.pgw.payflow.entity.UserEntity;
import com.pgw.payflow.exception.ApplicationException;
import com.pgw.payflow.jwt.JwtService;
import com.pgw.payflow.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.pgw.payflow.constant.error.Error.INTERNAL_SERVICE_ERROR_CODE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPassportNumber(), request.getPassword())
        );
        var userDetails = userDetailsService.loadUserByUsername(request.getPassportNumber());
        var token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token);
    }

    public AuthResponse register(UserCreateRequest request) {
        userExistCheck(request.getPassportNumber());
        UserEntity user = buildUser(request);
        userRepository.save(user);
        var userDetails = userDetailsService.loadUserByUsername(user.getPassportNumber());
        var token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token);
    }

    private AuthResponse buildAuthResponse(String token) {
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpiration())
                .build();
    }


    public UserEntity buildUser(UserCreateRequest request){
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .passportNumber(request.getPassportNumber())
                .age(request.getAge())
                .build();
    }

    public void userExistCheck(String passportNumber){
        if (userRepository.existsByPassportNumber(passportNumber)) {
            throw new ApplicationException(
                    INTERNAL_SERVICE_ERROR_CODE.getCode(),
                    "User with passport number already exists",
                    ErrorType.INTERNAL,
                    HttpStatus.CONFLICT
            );
        }
    }
}
