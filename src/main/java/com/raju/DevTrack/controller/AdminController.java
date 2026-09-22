package com.raju.DevTrack.controller;

import com.raju.DevTrack.dto.UserResponse;
import com.raju.DevTrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final UserService userService;
    @Operation(
            summary = "Get all users",
            description = "Retrieves all registered users. Requires administrator privileges."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Administrator privileges required"
            )
    })
    @GetMapping("/admin/users")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}
