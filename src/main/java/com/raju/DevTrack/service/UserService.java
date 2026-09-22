package com.raju.DevTrack.service;


import com.raju.DevTrack.config.SecurityConfig;
import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  List<UserResponse> getAllUsers() {
        List<User> users=userRepository.findAll();
        return users.stream().map(this::mapToResponse).toList();
    }

    public UserResponse createUser(UserRequest userRequest){
        User user= new User(null,userRequest.getUsername(),userRequest.getEmail(),passwordEncoder.encode(userRequest.getPassword()),null, Role.USER);
        User savedUser=  userRepository.save(user);
        return mapToResponse(savedUser);
    }
    private UserResponse mapToResponse(User user){
        return new UserResponse(user.getId(),user.getUsername(),user.getEmail());
    }

    public UserResponse getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The user with "+id+" not Found."));
        return mapToResponse(user);
    }
}
