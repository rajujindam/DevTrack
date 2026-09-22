package com.raju.DevTrack.controller;

import com.raju.DevTrack.dto.LearningGoalRequest;
import com.raju.DevTrack.dto.LearningGoalResponse;
import com.raju.DevTrack.dto.PaginatedGoalResponse;
import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.service.LearningGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LearningGoalController {

    private final LearningGoalService learningGoalService;
    @Operation(
            summary = "Create a learning goal",
            description = "Creates a new learning goal for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goal created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid goal data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PostMapping("/goals")
    public ResponseEntity<LearningGoalResponse> createGoal(@Valid  @RequestBody LearningGoalRequest learningGoalRequest){
       return  ResponseEntity.status(HttpStatus.CREATED).body(learningGoalService.createGoal(learningGoalRequest));
    }
    @Operation(
            summary = "Get all learning goals",
            description = "Retrieves the authenticated user's learning goals with filtering, searching, sorting, pagination, and progress/date filters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goals retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @GetMapping("/goals")
    public ResponseEntity<PaginatedGoalResponse> getAllGoals(@RequestParam(required = false)GoalStatus status,
                                                             @RequestParam(required = false, defaultValue = "name") String sortBy,
                                                             @RequestParam(required = false, defaultValue = "asc") String direction,
                                                             @RequestParam(required = false,defaultValue = "0") int PageNumber,
                                                             @RequestParam(required = false, defaultValue = "5") int PageSize,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(required = false) Integer minProgress,
                                                             @RequestParam(required = false) Integer maxProgress,
                                                             @RequestParam(required = false)LocalDate startDateFrom,
                                                             @RequestParam(required = false) LocalDate startDateTo){
        return ResponseEntity.status(HttpStatus.OK).body(learningGoalService.getAllGoals(status,sortBy,direction,PageNumber,PageSize,search,minProgress,maxProgress,startDateFrom,startDateTo));
    }
    @Operation(
            summary = "Get learning goal by ID",
            description = "Retrieves a specific learning goal belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goal retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not own this goal"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Goal not found"
            )
    })
    @GetMapping("/goals/{id}")
    public ResponseEntity<LearningGoalResponse> getGoalById(@PathVariable Long id){
            return ResponseEntity.status(HttpStatus.OK).body(learningGoalService.getGoalById(id));
    }
    @Operation(
            summary = "Update a learning goal",
            description = "Updates an existing learning goal belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goal updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid goal data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not own this goal"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Goal not found"
            )
    })
    @PutMapping("/goals/{id}")
    public ResponseEntity<LearningGoalResponse> updateGoal(@PathVariable Long id, @Valid @RequestBody LearningGoalRequest learningGoalRequest){
        LearningGoalResponse goal=learningGoalService.updateGoal(id,learningGoalRequest);
            return ResponseEntity.status(HttpStatus.OK).body(goal);
    }
    @Operation(
            summary = "Delete a learning goal",
            description = "Deletes a learning goal belonging to the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goal deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not own this goal"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Goal not found"
            )
    })
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<LearningGoalResponse> deleteGoal(@PathVariable Long id){
        LearningGoalResponse goal=learningGoalService.deleteGoal(id);
            return ResponseEntity.ok(goal);
    }
}
