package com.raju.DevTrack.dto;

import com.raju.DevTrack.model.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data@AllArgsConstructor@NoArgsConstructor
public class LearningGoalResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private GoalStatus status;
    private int progress;
}
