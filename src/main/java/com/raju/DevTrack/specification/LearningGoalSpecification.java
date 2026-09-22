package com.raju.DevTrack.specification;

import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LearningGoalSpecification {
    public static Specification<LearningGoal> hasStatus(GoalStatus status){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status));
    }

    public static Specification<LearningGoal> containsSearch(String search){
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+search.toLowerCase()+"%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),"%"+search.toLowerCase()+"%"));
    }
    public static Specification<LearningGoal> hasMinProgress(Integer progress){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("progress"),progress);
    }
    public static Specification<LearningGoal> hasMaxProgress(Integer progress){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("progress"),progress);
    }
    public static Specification<LearningGoal> hasStartDateFrom(LocalDate startDateFrom){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"),startDateFrom);
    }
    public static Specification<LearningGoal> hasStartDateTo(LocalDate startDateTo){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("startDate"),startDateTo);
    }
    public static Specification<LearningGoal> hasUsername(String username){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("username"),username);
    }
}
