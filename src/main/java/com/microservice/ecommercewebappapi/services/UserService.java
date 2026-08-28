package com.microservice.ecommercewebappapi.services;

import com.microservice.ecommercewebappapi.Enums.Roles;
import com.microservice.ecommercewebappapi.dto.UpdatePasswordRequest;
import com.microservice.ecommercewebappapi.dto.UpdateUserProfileRequest;
import com.microservice.ecommercewebappapi.dto.UserResponseDto;
import com.microservice.ecommercewebappapi.models.Users;
import com.microservice.ecommercewebappapi.repository.RefreshTokenRepository;
import com.microservice.ecommercewebappapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private  final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponseDto getUserProfile(){

        String email=SecurityContextHolder.getContext().getAuthentication().getName();

        Users user= userRepository.findUsersByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));
        return  UserResponseDto
        .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .Phone(user.getPhone())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public String updatePassword(UpdatePasswordRequest updatePasswordRequest){

        if(!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword()))
        {
            throw new RuntimeException("Password Doesnt Matche's");
        }
        String email= SecurityContextHolder.getContext().getAuthentication().getName();

        Users user=userRepository.findUsersByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));

        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),user.getPassword())){
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));

        userRepository.save(user);

        return "Password Updated Successfully";

    }

    @Transactional
    public String deleteMyProfile(){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        Users user=userRepository.findUsersByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));
        refreshTokenRepository.deleteByUser(user);
        userRepository.deleteUsersByEmail(email);

        return String.format(
                "%s, your profile has been deleted successfully.",
                user.getFirstName()
        );
    }

    public String updateProfile(UpdateUserProfileRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository
                .findUsersByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);

        return "Profile Updated Successfully";
    }

    public String updateUserRole(String username, Roles role) {

        Users user = userRepository.findUsersByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        List<Roles> roles = user.getRoles();

        if (!roles.contains(role)) {
            roles.add(role);
        }

        user.setRoles(roles);

        userRepository.save(user);

        return "Role Added Successfully";
    }

    public String removeUserRole(String email, Roles role) {

        Users user = userRepository.findUsersByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        List<Roles> roles = user.getRoles();

        if (role == Roles.ROLE_USER) {
            throw new RuntimeException(
                    "Cannot remove default USER role");
        }

        roles.remove(role);

        user.setRoles(roles);

        userRepository.save(user);

        return "Role Removed Successfully";
    }

    public List<Users> getAllUsers(){
        return  userRepository.findAll();
    }


}

