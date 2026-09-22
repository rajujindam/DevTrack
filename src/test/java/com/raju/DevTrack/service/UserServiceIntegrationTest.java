package com.raju.DevTrack.service;

import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.LearningGoalRepository;
import com.raju.DevTrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LearningGoalRepository learningGoalRepository;

    @Test
    void createUser_shouldSaveUserToDatabase() {

        UserRequest request = new UserRequest(
                "raju",
                "rajujindam@gmail.com",
                "password123"
        );

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        UserResponse response =
                userService.createUser(request);

        assertNotNull(response.getId());
        assertEquals("raju", response.getUsername());
        assertEquals(
                "rajujindam@gmail.com",
                response.getEmail()
        );

        User savedUser =
                userRepository.findById(response.getId())
                        .orElseThrow();

        assertEquals("raju", savedUser.getUsername());
        assertEquals(
                "rajujindam@gmail.com",
                savedUser.getEmail()
        );
        assertEquals(
                "encodedPassword",
                savedUser.getPassword()
        );
    }
    @Test
    void getUserById_shouldThrowExceptionWhenUserDoesNotExist() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(999L)
        );
    }
    @Test
    void getUserById_shouldReturnSavedUser() {

        UserRequest request = new UserRequest(
                "raju",
                "rajujindam@gmail.com",
                "password123"
        );

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        UserResponse createdUser =
                userService.createUser(request);

        UserResponse result =
                userService.getUserById(createdUser.getId());

        assertEquals(
                createdUser.getId(),
                result.getId()
        );

        assertEquals(
                "raju",
                result.getUsername()
        );

        assertEquals(
                "rajujindam@gmail.com",
                result.getEmail()
        );
    }
    @Test
    void getAllUsers_shouldReturnAllSavedUsers() {

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        UserResponse user1 = userService.createUser(
                new UserRequest(
                        "raju",
                        "rajujindam@gmail.com",
                        "password123"
                )
        );

        UserResponse user2 = userService.createUser(
                new UserRequest(
                        "amit",
                        "amit@gmail.com",
                        "password123"
                )
        );

        List<UserResponse> result =
                userService.getAllUsers();

        assertEquals(2, result.size());

        assertTrue(
                result.stream()
                        .anyMatch(user ->
                                user.getUsername().equals("raju"))
        );

        assertTrue(
                result.stream()
                        .anyMatch(user ->
                                user.getUsername().equals("amit"))
        );
    }
}
