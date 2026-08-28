package com.microservice.ecommercewebappapi.configuration;

import com.microservice.ecommercewebappapi.models.Users;
import com.microservice.ecommercewebappapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user=userRepository.findUsersByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));

        return  user;
    }
}
