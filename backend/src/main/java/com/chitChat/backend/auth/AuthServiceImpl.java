package com.chitChat.backend.auth;

import com.chitChat.backend.auth.dto.*;
import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.exceptions.*;
import com.chitChat.backend.security.CustomUserDetailsService;
import com.chitChat.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    public MessageResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistException("Email Already exists");
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistException("Username already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return new MessageResponse("Registration successfully !");

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .filter(u-> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .orElseThrow(()-> new BadCredentialsException("Invalid login credentials."));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        final String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token,user.getUsername());
    }

    @Override
    public MessageResponse changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with username " + username));
        if(!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new MessageResponse("Password changed successfully");
    }


}

