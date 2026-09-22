package com.raju.DevTrack.dto;

import com.raju.DevTrack.model.GoalStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningGoalRequest {
    @NotBlank
    @Size(min = 3 , max = 100)
    private String name;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    private LocalDate startDate;
    @Min(0)
    @Max(100)
    private int progress;

}
