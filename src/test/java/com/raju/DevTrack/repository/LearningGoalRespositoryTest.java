package com.raju.DevTrack.repository;

import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.specification.LearningGoalSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LearningGoalRespositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LearningGoalRepository learningGoalRepository;
    @Test
    void save_shouldPersistLearningGoalSuccessfully() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal goal = new LearningGoal(
                null,
                savedUser,
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal savedGoal = learningGoalRepository.save(goal);

        Optional<LearningGoal> result =
                learningGoalRepository.findById(savedGoal.getId());

        assertTrue(result.isPresent());
        assertEquals(savedGoal.getId(), result.get().getId());
        assertEquals("Learn Spring Boot", result.get().getName());
        assertEquals(50, result.get().getProgress());
        assertEquals(GoalStatus.IN_PROGRESS, result.get().getStatus());
        assertEquals(
                "raju",
                result.get().getUser().getUsername()
        );
    }
    @Test
    void findAll_shouldReturnGoalsWithMatchingStatus() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal completedGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.COMPLETED,
                100
        );

        LearningGoal inProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Docker",
                "Learn Docker",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                50
        );

        learningGoalRepository.save(completedGoal);
        learningGoalRepository.save(inProgressGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasStatus(GoalStatus.COMPLETED);

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        assertEquals(GoalStatus.COMPLETED, result.get(0).getStatus());
    }
    @Test
    void findAll_shouldReturnGoalsMatchingSearch() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal springGoal = new LearningGoal(
                null,
                savedUser,
                "Learn Spring Boot",
                "Learn backend development",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal dockerGoal = new LearningGoal(
                null,
                savedUser,
                "Learn Docker",
                "Learn containerization",
                LocalDate.now(),
                GoalStatus.NOT_STARTED,
                0
        );

        learningGoalRepository.save(springGoal);
        learningGoalRepository.save(dockerGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.containsSearch("spring");

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Learn Spring Boot", result.get(0).getName());
    }
    @Test
    void findAll_shouldReturnGoalsWithMinimumProgress() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal lowProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal highProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                70
        );

        learningGoalRepository.save(lowProgressGoal);
        learningGoalRepository.save(highProgressGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasMinProgress(50);

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        assertEquals(70, result.get(0).getProgress());
    }
    @Test
    void findAll_shouldReturnGoalsWithMaximumProgress() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal lowProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal highProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                70
        );

        learningGoalRepository.save(lowProgressGoal);
        learningGoalRepository.save(highProgressGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasMaxProgress(50);

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals(30, result.get(0).getProgress());
    }
    @Test
    void findAll_shouldReturnGoalsStartingFromGivenDate() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal oldGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.of(2026, 6, 5),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal recentGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.of(2026, 6, 15),
                GoalStatus.IN_PROGRESS,
                70
        );

        learningGoalRepository.save(oldGoal);
        learningGoalRepository.save(recentGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasStartDateFrom(
                        LocalDate.of(2026, 6, 10)
                );

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        assertEquals(
                LocalDate.of(2026, 6, 15),
                result.get(0).getStartDate()
        );
    }
    @Test
    void findAll_shouldReturnGoalsStartingUntilGivenDate() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal oldGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.of(2026, 6, 5),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal recentGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.of(2026, 6, 15),
                GoalStatus.IN_PROGRESS,
                70
        );

        learningGoalRepository.save(oldGoal);
        learningGoalRepository.save(recentGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasStartDateTo(
                        LocalDate.of(2026, 6, 10)
                );

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals(
                LocalDate.of(2026, 6, 5),
                result.get(0).getStartDate()
        );
    }
    @Test
    void findAll_shouldReturnGoalsBelongingToGivenUsername() {

        User raju = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User amit = new User(
                null,
                "amit",
                "amit@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedRaju = userRepository.save(raju);
        User savedAmit = userRepository.save(amit);

        LearningGoal rajuGoal = new LearningGoal(
                null,
                savedRaju,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal amitGoal = new LearningGoal(
                null,
                savedAmit,
                "Docker",
                "Learn Docker",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                30
        );

        learningGoalRepository.save(rajuGoal);
        learningGoalRepository.save(amitGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasUsername("raju");

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        assertEquals(
                "raju",
                result.get(0).getUser().getUsername()
        );
    }
    @Test
    void findAll_shouldReturnGoalsMatchingMultipleSpecifications() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal matchingGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                70
        );

        LearningGoal lowProgressGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal completedGoal = new LearningGoal(
                null,
                savedUser,
                "Docker",
                "Learn Docker",
                LocalDate.now(),
                GoalStatus.COMPLETED,
                80
        );

        learningGoalRepository.save(matchingGoal);
        learningGoalRepository.save(lowProgressGoal);
        learningGoalRepository.save(completedGoal);

        Specification<LearningGoal> specification =
                LearningGoalSpecification.hasUsername("raju")
                        .and(LearningGoalSpecification.hasStatus(
                                GoalStatus.IN_PROGRESS))
                        .and(LearningGoalSpecification.hasMinProgress(50));

        List<LearningGoal> result =
                learningGoalRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getName());
        assertEquals(70, result.get(0).getProgress());
        assertEquals(
                GoalStatus.IN_PROGRESS,
                result.get(0).getStatus()
        );
    }
    @Test
    void findAll_shouldReturnPaginatedAndSortedGoals() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        LearningGoal javaGoal = new LearningGoal(
                null,
                savedUser,
                "Java",
                "Learn Java",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal springGoal = new LearningGoal(
                null,
                savedUser,
                "Spring Boot",
                "Learn Spring Boot",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                70
        );

        LearningGoal dockerGoal = new LearningGoal(
                null,
                savedUser,
                "Docker",
                "Learn Docker",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                50
        );

        learningGoalRepository.save(javaGoal);
        learningGoalRepository.save(springGoal);
        learningGoalRepository.save(dockerGoal);

        Pageable pageable = PageRequest.of(
                0,
                2,
                Sort.by("progress").descending()
        );

        Page<LearningGoal> result =
                learningGoalRepository.findAll(pageable);

        assertEquals(2, result.getContent().size());

        assertEquals(
                "Spring Boot",
                result.getContent().get(0).getName()
        );

        assertEquals(
                "Docker",
                result.getContent().get(1).getName()
        );

        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
    }

}
