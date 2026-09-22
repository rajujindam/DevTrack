package com.raju.DevTrack.service;

import com.raju.DevTrack.dto.LearningGoalRequest;
import com.raju.DevTrack.dto.LearningGoalResponse;
import com.raju.DevTrack.dto.PaginatedGoalResponse;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.exceptions.UserNotMatched;
import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.LearningGoalRepository;
import com.raju.DevTrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LearningGoalIntegrationTest {
    @Autowired
    private LearningGoalService learningGoalService;

    @Autowired
    private LearningGoalRepository learningGoalRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {


        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "raju",
                        null
                )
        );
    }

    @Test
    void createGoal_shouldSaveGoalWithAuthenticatedUser() {

        LearningGoalRequest request = new LearningGoalRequest(
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.of(2026, 9, 14),
                70
        );

        LearningGoalResponse response =
                learningGoalService.createGoal(request);

        assertNotNull(response.getId());
        assertEquals("Learn Spring Boot", response.getName());

        LearningGoal savedGoal =
                learningGoalRepository.findById(response.getId())
                        .orElseThrow();

        assertEquals(
                "raju",
                savedGoal.getUser().getUsername()
        );

        assertEquals(
                "Learn Spring Boot",
                savedGoal.getName()
        );

        assertEquals(
                GoalStatus.IN_PROGRESS,
                savedGoal.getStatus()
        );
    }
    @Test
    void getGoalById_shouldReturnSavedGoal() {

        User user = userRepository.findUserByUsername("raju")
                .orElseThrow();

        LearningGoal goal = new LearningGoal(
                null,
                user,
                "Learn Java",
                "Learn Java backend development",
                LocalDate.of(2026, 9, 14),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        LearningGoalResponse result =
                learningGoalService.getGoalById(savedGoal.getId());

        assertNotNull(result);

        assertEquals(
                savedGoal.getId(),
                result.getId()
        );

        assertEquals(
                "Learn Java",
                result.getName()
        );

        assertEquals(
                "Learn Java backend development",
                result.getDescription()
        );

        assertEquals(
                GoalStatus.IN_PROGRESS,
                result.getStatus()
        );
    }
    @Test
    void getGoalById_shouldThrowExceptionWhenGoalDoesNotExist() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> learningGoalService.getGoalById(999L)
        );
    }
    @Test
    void getGoalById_shouldThrowExceptionWhenUserIsNotOwner() {

        User amit = new User(
                null,
                "amit",
                "amit@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedAmit = userRepository.save(amit);

        LearningGoal goal = new LearningGoal(
                null,
                savedAmit,
                "Learn Docker",
                "Learn Docker",
                LocalDate.of(2026, 9, 14),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        assertThrows(
                UserNotMatched.class,
                () -> learningGoalService.getGoalById(savedGoal.getId())
        );
    }
    @Test
    void updateGoal_shouldUpdateExistingGoal() {

        User user = userRepository.findUserByUsername("raju")
                .orElseThrow();

        LearningGoal goal = new LearningGoal(
                null,
                user,
                "Learn Java",
                "Learn Java basics",
                LocalDate.of(2026, 9, 10),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        LearningGoalRequest request = new LearningGoalRequest(
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.of(2026, 9, 14),
                100
        );

        LearningGoalResponse response =
                learningGoalService.updateGoal(
                        savedGoal.getId(),
                        request
                );

        assertEquals(
                savedGoal.getId(),
                response.getId()
        );

        assertEquals(
                "Learn Spring Boot",
                response.getName()
        );

        LearningGoal updatedGoal =
                learningGoalRepository.findById(savedGoal.getId())
                        .orElseThrow();

        assertEquals(
                "Learn Spring Boot",
                updatedGoal.getName()
        );

        assertEquals(
                "Learn Spring Boot backend development",
                updatedGoal.getDescription()
        );

        assertEquals(
                100,
                updatedGoal.getProgress()
        );

        assertEquals(
                GoalStatus.COMPLETED,
                updatedGoal.getStatus()
        );
    }
    @Test
    void updateGoal_shouldThrowExceptionWhenGoalDoesNotExist() {

        LearningGoalRequest request = new LearningGoalRequest(
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.of(2026, 9, 14),
                50
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> learningGoalService.updateGoal(999L, request)
        );
    }
    @Test
    void updateGoal_shouldThrowExceptionWhenUserIsNotOwner() {

        User amit = new User(
                null,
                "amit",
                "amit@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedAmit = userRepository.save(amit);

        LearningGoal goal = new LearningGoal(
                null,
                savedAmit,
                "Learn Docker",
                "Learn Docker basics",
                LocalDate.of(2026, 9, 10),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        LearningGoalRequest request = new LearningGoalRequest(
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.of(2026, 9, 14),
                70
        );

        assertThrows(
                UserNotMatched.class,
                () -> learningGoalService.updateGoal(
                        savedGoal.getId(),
                        request
                )
        );
    }
    @Test
    void deleteGoal_shouldDeleteExistingGoal() {

        User user = userRepository.findUserByUsername("raju")
                .orElseThrow();

        LearningGoal goal = new LearningGoal(
                null,
                user,
                "Learn Java",
                "Learn Java basics",
                LocalDate.of(2026, 9, 10),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        learningGoalService.deleteGoal(savedGoal.getId());

        assertTrue(
                learningGoalRepository
                        .findById(savedGoal.getId())
                        .isEmpty()
        );
    }
    @Test
    void deleteGoal_shouldThrowExceptionWhenGoalDoesNotExist() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> learningGoalService.deleteGoal(999L)
        );
    }
    @Test
    void deleteGoal_shouldThrowExceptionWhenUserIsNotOwner() {

        User amit = new User(
                null,
                "amit",
                "amit@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedAmit = userRepository.save(amit);

        LearningGoal goal = new LearningGoal(
                null,
                savedAmit,
                "Learn Docker",
                "Learn Docker basics",
                LocalDate.of(2026, 9, 10),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        assertThrows(
                UserNotMatched.class,
                () -> learningGoalService.deleteGoal(savedGoal.getId())
        );
        assertTrue(
                learningGoalRepository
                        .findById(savedGoal.getId())
                        .isPresent()
        );
    }
    @Test
    void getAllGoals_shouldReturnFilteredPaginatedAndSortedGoals() {

        User raju = userRepository.findUserByUsername("raju")
                .orElseThrow();

        User amit = new User(
                null,
                "amit",
                "amit@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedAmit = userRepository.save(amit);

        LearningGoal javaGoal = new LearningGoal(
                null,
                raju,
                "Java",
                "Learn Java",
                LocalDate.of(2026, 9, 1),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal springGoal = new LearningGoal(
                null,
                raju,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.of(2026, 9, 2),
                GoalStatus.IN_PROGRESS,
                70
        );

        LearningGoal dockerGoal = new LearningGoal(
                null,
                raju,
                "Docker",
                "Learn Docker",
                LocalDate.of(2026, 9, 3),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal amitGoal = new LearningGoal(
                null,
                savedAmit,
                "Kubernetes",
                "Learn Kubernetes",
                LocalDate.of(2026, 9, 4),
                GoalStatus.IN_PROGRESS,
                90
        );

        learningGoalRepository.save(javaGoal);
        learningGoalRepository.save(springGoal);
        learningGoalRepository.save(dockerGoal);
        learningGoalRepository.save(amitGoal);

        PaginatedGoalResponse result =
                learningGoalService.getAllGoals(
                        GoalStatus.IN_PROGRESS,
                        "progress",
                        "desc",
                        0,
                        2,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(2, result.getContent().size());

        assertEquals(
                "Spring Boot",
                result.getContent().getFirst().getName()
        );

        assertEquals(
                "Docker",
                result.getContent().get(1).getName()
        );

        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getPageSize());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());

        assertTrue(result.isFirst());
        assertFalse(result.isLast());
    }
}
