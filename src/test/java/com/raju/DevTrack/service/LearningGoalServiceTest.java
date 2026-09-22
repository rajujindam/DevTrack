package com.raju.DevTrack.service;

import com.raju.DevTrack.dto.LearningGoalRequest;
import com.raju.DevTrack.dto.LearningGoalResponse;
import com.raju.DevTrack.dto.PaginatedGoalResponse;
import com.raju.DevTrack.dto.UserRequest;
import com.raju.DevTrack.exceptions.ResourceNotFoundException;
import com.raju.DevTrack.exceptions.UserNotMatched;
import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.LearningGoalRepository;
import com.raju.DevTrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LearningGoalServiceTest {
    @Mock
    private Authentication authentication;

    @Mock
    private LearningGoalRepository learningGoalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LearningGoalService learningGoalService;

    @BeforeEach
    void setUp(){
        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("raju");
    }
    @Test
    void getGoalById_shouldThrowUserNotMatchedWhenGoalBelongsToAnotherUser() {

        User user=new User(1L,"john","john@gmail.com",null,null, Role.USER);
        LearningGoal goal=new LearningGoal(1L,user,"DSA","Algorithms", LocalDate.now(),GoalStatus.IN_PROGRESS,66);
        when(learningGoalRepository.findById(1L)).thenReturn(Optional.of(goal));

        UserNotMatched exception=assertThrows(UserNotMatched.class,()->learningGoalService.getGoalById(1L));


        assertEquals(
                "Username raju and Goal with id 1 not matched",
                exception.getMessage()
        );
        verify(learningGoalRepository, times(1))
                .findById(1L);
    }
    @Test
    void updateGoal_shouldMarkGoalCompletedWhenProgressIs100() {

        User user=new User(1L,"raju","rajujindam@gmail.com",null, null,Role.USER);
        LearningGoal goal=new LearningGoal(1L,user,"Spring Boot","Backend Framework",LocalDate.now(),GoalStatus.IN_PROGRESS,50);
        when(learningGoalRepository.findById(1L)).thenReturn(Optional.of(goal));
        LearningGoalRequest request=new LearningGoalRequest("Spring Boot","Backend Framework",LocalDate.now(),100);
        when(learningGoalRepository.save(any(LearningGoal.class))).thenReturn(new LearningGoal(1L,user,"Spring Boot","Backend Framework",LocalDate.now(),GoalStatus.COMPLETED,100));
        LearningGoalResponse result=learningGoalService.updateGoal(1L,request);
        ArgumentCaptor<LearningGoal> captor=ArgumentCaptor.forClass(LearningGoal.class);
        verify(learningGoalRepository,times(1)).save(captor.capture());
        LearningGoal capturedGoal=captor.getValue();
        assertEquals("raju",capturedGoal.getUser().getUsername());
        assertEquals("Spring Boot",capturedGoal.getName());
        assertEquals("Backend Framework",capturedGoal.getDescription());
        assertEquals(GoalStatus.COMPLETED,capturedGoal.getStatus());
        assertEquals(100,capturedGoal.getProgress());
    }
    @Test
    public void updateGoal_shouldThrowGoalNotFound(){

        LearningGoalRequest request =
                new LearningGoalRequest(
                        "DSA",
                        "Algorithms",
                        LocalDate.now(),
                        50
                );

        when(learningGoalRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->learningGoalService.updateGoal(999L,request));
        assertEquals("Goal with id 999 not found",exception.getMessage());

        verify(learningGoalRepository,times(1)).findById(999L);
    }
    @Test
    public void createGoal_shouldThrowUserNotFound(){

        when(authentication.getName()).thenReturn("john");
        LearningGoalRequest request=new LearningGoalRequest("DSA","Problem solving",LocalDate.now(),90);
        when(userRepository.findUserByUsername("john")).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->learningGoalService.createGoal(request));
        assertEquals("User with username john not found",exception.getMessage());
    }

    @Test
    public void createGoal_ShouldCreateTheGoal(){

         User user=new User(1L,"raju","rajujindam@gmail.com",null,null,Role.USER);
         when(userRepository.findUserByUsername("raju")).thenReturn(Optional.of(user));
         LearningGoalRequest request=new LearningGoalRequest("DSA","Problem Solving",LocalDate.now(),90);
         LearningGoal savedGoal=new LearningGoal(1L,user,"DSA","Problem Solving",LocalDate.now(),GoalStatus.IN_PROGRESS,90);
         when(learningGoalRepository.save(any(LearningGoal.class))).thenReturn(savedGoal);
         LearningGoalResponse result =learningGoalService.createGoal(request);
         ArgumentCaptor<LearningGoal> goalCaptor=ArgumentCaptor.forClass(LearningGoal.class);
         verify(learningGoalRepository,times(1)).save(goalCaptor.capture());
         LearningGoal capturedGoal=goalCaptor.getValue();
         assertEquals(savedGoal.getUser(),capturedGoal.getUser());
         assertEquals(savedGoal.getName(),capturedGoal.getName());
         assertEquals(savedGoal.getDescription(),capturedGoal.getDescription());
         assertEquals(savedGoal.getProgress(),capturedGoal.getProgress());
         assertEquals(savedGoal.getStatus(),capturedGoal.getStatus());

        assertEquals(savedGoal.getId(), result.getId());
        assertEquals(savedGoal.getName(), result.getName());
        assertEquals(savedGoal.getDescription(), result.getDescription());
        assertEquals(savedGoal.getProgress(), result.getProgress());
        assertEquals(savedGoal.getStatus(), result.getStatus());

    }

    @Test
    public void deleteGoal_shouldDeleteTheGoal(){

        User user=new User(1L,"raju","rajujindam@gmail.com",null,null,Role.USER);
        LearningGoal deletingGoal=new LearningGoal(1L,user,"DSA","Problem Solving",LocalDate.now(),GoalStatus.IN_PROGRESS,90);
        when(learningGoalRepository.findById(1L)).thenReturn(Optional.of(deletingGoal));
        //when(learningGoalRepository.deleteById(anyLong())).thenReturn(deletingGoal);
        LearningGoalResponse deletedGoal=learningGoalService.deleteGoal(1L);
        verify(learningGoalRepository,times(1)).deleteById(1L);
        assertEquals(deletingGoal.getName(),deletedGoal.getName());
        assertEquals(deletingGoal.getDescription(),deletedGoal.getDescription());
        assertEquals(deletingGoal.getStatus(),deletedGoal.getStatus());
        assertEquals(deletingGoal.getProgress(),deletedGoal.getProgress());

    }
    @Test
    void deleteGoal_shouldThrowExceptionWhenGoalNotFound() {

        when(learningGoalRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> learningGoalService.deleteGoal(999L)
                );

        assertEquals(
                "Goal with id 999 not found",
                exception.getMessage()
        );

        verify(learningGoalRepository, times(1))
                .findById(999L);

        verify(learningGoalRepository, never())
                .deleteById(anyLong());
    }
    @Test
    void deleteGoal_shouldThrowExceptionWhenUserDoesNotOwnGoal() {

        User user = new User(
                1L,
                "john",
                "john@gmail.com",
                null,
                null,
                Role.USER
        );

        LearningGoal goal = new LearningGoal(
                1L,
                user,
                "DSA",
                "Problem Solving",
                LocalDate.now(),
                GoalStatus.IN_PROGRESS,
                90
        );

        when(learningGoalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        UserNotMatched exception =
                assertThrows(
                        UserNotMatched.class,
                        () -> learningGoalService.deleteGoal(1L)
                );

        assertEquals(
                "Username raju and Goal with id 1 not matched",
                exception.getMessage()
        );

        verify(learningGoalRepository, times(1))
                .findById(1L);

        verify(learningGoalRepository, never())
                .deleteById(anyLong());
    }
    @Test
    void getAllGoals_shouldReturnGoalsSuccessfully() {

        User user = new User(
                1L,
                "raju",
                "rajujindam@gmail.com",
                null,
                null,
                Role.USER
        );

        LearningGoal goal1 = new LearningGoal(
                1L,
                user,
                "DSA",
                "Problem Solving",
                LocalDate.of(2026, 1, 1),
                GoalStatus.IN_PROGRESS,
                50
        );

        LearningGoal goal2 = new LearningGoal(
                2L,
                user,
                "Spring Boot",
                "Backend Development",
                LocalDate.of(2026, 2, 1),
                GoalStatus.COMPLETED,
                100
        );

        List<LearningGoal> goals = List.of(goal1, goal2);

        Page<LearningGoal> page =
                new PageImpl<>(
                        goals,
                        PageRequest.of(0, 10),
                        2
                );

        when(learningGoalRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(page);

        PaginatedGoalResponse result =
                learningGoalService.getAllGoals(
                        null,
                        "id",
                        "asc",
                        0,
                        10,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(2, result.getContent().size());

        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("DSA", result.getContent().get(0).getName());

        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals("Spring Boot", result.getContent().get(1).getName());

        assertEquals(0, result.getCurrentPage());
        assertEquals(10, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());

        verify(learningGoalRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void getAllGoals_shouldReturnEmptyListWhenNoGoalsExist() {

        Page<LearningGoal> emptyPage =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(0, 10),
                        0
                );

        when(learningGoalRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(emptyPage);

        PaginatedGoalResponse result =
                learningGoalService.getAllGoals(
                        null,
                        "id",
                        "asc",
                        0,
                        10,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(0, result.getContent().size());

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());

        verify(learningGoalRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
    }
    @Test
    void getAllGoals_shouldApplyPaginationAndSorting() {

        Page<LearningGoal> page =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(
                                2,
                                5,
                                Sort.by("progress").descending()
                        ),
                        15
                );

        when(learningGoalRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(page);

        learningGoalService.getAllGoals(
                null,
                "progress",
                "desc",
                2,
                5,
                null,
                null,
                null,
                null,
                null
        );

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(learningGoalRepository).findAll(
                any(Specification.class),
                pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());

        assertEquals(
                Sort.Direction.DESC,
                pageable.getSort().getOrderFor("progress").getDirection()
        );
    }
}
