package com.pgw.payflow.service.impl;

import com.pgw.payflow.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserDetails implements org.springframework.security.core.userdetails.UserDetailsService {

    UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String passportNumber) throws UsernameNotFoundException {
        var user = userRepository.findByPassportNumber(passportNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + passportNumber));

        return User.withUsername(user.getPassportNumber())
                .password(user.getPassword())
                .build();
    }
}
