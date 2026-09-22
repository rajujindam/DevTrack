package com.raju.DevTrack.controller;

import com.raju.DevTrack.config.CustomUserDetailsService;
import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.exceptions.UserNotMatched;
import com.raju.DevTrack.filter.JwtAuthenticationFilter;
import com.raju.DevTrack.service.JwtService;
import com.raju.DevTrack.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.raju.DevTrack.dto.UserRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Test
    void getUserById_shouldReturnUser() throws Exception {

        UserResponse userResponse =
                new UserResponse(
                        1L,
                        "raju",
                        "rajujindam@gmail.com"
                );

        when(userService.getUserById(1L))
                .thenReturn(userResponse);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("raju"))
                .andExpect(jsonPath("$.email")
                        .value("rajujindam@gmail.com"));

        verify(userService, times(1))
                .getUserById(1L);
    }
    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception {

        List<UserResponse> users = List.of(
                new UserResponse(
                        1L,
                        "raju",
                        "rajujindam@gmail.com"
                ),
                new UserResponse(
                        2L,
                        "john",
                        "john@gmail.com"
                )
        );

        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("raju"))
                .andExpect(jsonPath("$[0].email")
                        .value("rajujindam@gmail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("john"))
                .andExpect(jsonPath("$[1].email")
                        .value("john@gmail.com"));

        verify(userService, times(1))
                .getAllUsers();
    }
    @Test
    void createUser_shouldCreateUserSuccessfully() throws Exception {

        UserResponse userResponse =
                new UserResponse(
                        1L,
                        "raju",
                        "rajujindam@gmail.com"
                );

        when(userService.createUser(any(UserRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "username": "raju",
                                        "email": "rajujindam@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("raju"))
                .andExpect(jsonPath("$.email")
                        .value("rajujindam@gmail.com"));

        verify(userService, times(1))
                .createUser(any(UserRequest.class));
    }
    @Test
    void createUser_shouldReturnBadRequestWhenUsernameIsBlank() throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "username": "",
                                        "email": "rajujindam@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }
    @Test
    void createUser_shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "username": "raju",
                                        "email": "invalid-email",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }
    @Test
    void createUser_shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "username": "raju",
                                        "email": "rajujindam@gmail.com",
                                        "password": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }
    @Test
    void getUserById_shouldReturnNotFoundWhenUserDoesNotExist()
            throws Exception {

        when(userService.getUserById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "The user with 999 not Found."
                ));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("The user with 999 not Found."))
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.errors")
                        .doesNotExist());

        verify(userService, times(1))
                .getUserById(999L);
    }
    @Test
    void createUser_shouldReturnValidationErrorResponseWhenUsernameIsBlank()
            throws Exception {

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "username": "",
                                        "email": "rajujindam@gmail.com",
                                        "password": "password123"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.errors.username")
                        .value("must not be blank"));

        verify(userService, never())
                .createUser(any(UserRequest.class));
    }
    @Test
    void getUserById_shouldReturnForbiddenWhenUserDoesNotMatch()
            throws Exception {

        when(userService.getUserById(1L))
                .thenThrow(new UserNotMatched(
                        "Username raju and Goal with id 1 not matched"
                ));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value("Username raju and Goal with id 1 not matched"))
                .andExpect(jsonPath("$.status")
                        .value(403))
                .andExpect(jsonPath("$.errors")
                        .doesNotExist());

        verify(userService, times(1))
                .getUserById(1L);
    }
}