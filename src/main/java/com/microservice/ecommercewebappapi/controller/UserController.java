package com.microservice.ecommercewebappapi.controller;

import com.microservice.ecommercewebappapi.dto.UpdatePasswordRequest;
import com.microservice.ecommercewebappapi.dto.UpdateUserProfileRequest;
import com.microservice.ecommercewebappapi.dto.UserResponseDto;
import com.microservice.ecommercewebappapi.repository.UserRepository;
import com.microservice.ecommercewebappapi.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health")
    public  String greet(){
        return "Health is Up";
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserProfileInfo(){
        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }



    @PatchMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest){
        return new ResponseEntity<>(userService.updateProfile(updateUserProfileRequest),HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest){
        return new ResponseEntity<>(userService.updatePassword(updatePasswordRequest),HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserProfile(){
        return new ResponseEntity<>(userService.deleteMyProfile(),HttpStatus.OK);
    }



}
