package com.raju.DevTrack.service;

import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void createUser_shouldCreateUserSuccessfully(){

        UserRequest userRequest=new UserRequest("raju","rajujindam@gmail.com","password123");
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("EncodedPassword");
        User savedUser=new User(1L,"raju","rajujindam@gmail.com","EncodedPassword",null, Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result=userService.createUser(userRequest);

        assertEquals(1L,result.getId());
        assertEquals("raju",result.getUsername());
        assertEquals("rajujindam@gmail.com",result.getEmail());

        ArgumentCaptor<User> userCaptor=ArgumentCaptor.forClass(User.class);


        verify(passwordEncoder).encode(userRequest.getPassword());
        verify(userRepository,times(1)).save(userCaptor.capture());
        User capturedUser=userCaptor.getValue();
        assertEquals("raju", capturedUser.getUsername());
        assertEquals("rajujindam@gmail.com", capturedUser.getEmail());
        assertEquals("EncodedPassword", capturedUser.getPassword());
        assertEquals(Role.USER, capturedUser.getRole());
    }

    @Test
    public void getUserById_shouldThrowAnExceptionWhenUserNotFound(){
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->userService.getUserById(999L));

        assertEquals("The user with 999 not Found.",exception.getMessage());
        verify(userRepository,times(1)).findById(999L);
    }

    @Test
    public void getUserById_shouldReturnTheUser(){
        User user=new User(1L,"raju","rajujindam@gmail.com",null,null,Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponse result=userService.getUserById(1L);
        assertEquals(result.getId(),user.getId());
        assertEquals(result.getUsername(),user.getUsername());
        assertEquals(result.getEmail(),user.getEmail());
        verify(userRepository,times(1)).findById(1L);

    }
    @Test
    public void getUserById_shouldPropagateExceptionWhenDatabaseFails(){

        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Database Failure"));

        RuntimeException exception=assertThrows(RuntimeException.class,()->userService.getUserById(1L));

        assertEquals("Database Failure",exception.getMessage());
        verify(userRepository,times(1)).findById(1L);
    }
    @Test
    public void getAllUsers_shouldReturnAllUsersSuccessfully(){
        User user1 = new User(
                1L,
                "raju",
                "rajujindam@gmail.com",
                "EncodedPassword",
                null,
                Role.USER
        );

        User user2 = new User(
                2L,
                "john",
                "john@gmail.com",
                "EncodedPassword",
                null,
                Role.USER
        );

        when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        List<UserResponse> result=userService.getAllUsers();
        assertEquals(2,result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("raju", result.get(0).getUsername());
        assertEquals("rajujindam@gmail.com", result.get(0).getEmail());

        assertEquals(2L, result.get(1).getId());
        assertEquals("john", result.get(1).getUsername());
        assertEquals("john@gmail.com", result.get(1).getEmail());
        verify(userRepository, times(1)).findAll();


    }
    @Test
    void getAllUsers_shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<UserResponse> result=userService.getAllUsers();
        assertEquals(0,result.size());
        verify(userRepository,times(1)).findAll();
    }
}
