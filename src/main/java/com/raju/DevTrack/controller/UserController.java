package com.raju.DevTrack.controller;

import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )
    })
    @PostMapping("/users")
    public UserResponse createUser(@Valid  @RequestBody UserRequest userRequest){
        return  userService.createUser(userRequest);
    }
    @Operation(
            summary = "Get all users",
            description = "Retrieves all registered users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @GetMapping("/users")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user using their unique ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
