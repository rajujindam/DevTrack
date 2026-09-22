package com.raju.DevTrack.security;

import com.raju.DevTrack.model.GoalStatus;
import com.raju.DevTrack.model.LearningGoal;
import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import com.raju.DevTrack.repository.LearningGoalRepository;
import com.raju.DevTrack.repository.UserRepository;
import com.raju.DevTrack.service.JwtService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LearningGoalRepository learningGoalRepository;

    @Test
    void protectedEndpoint_shouldReturn401WhenNoJwtIsProvided()
            throws Exception {

        mockMvc.perform(
                        get("/goals")
                )
                .andExpect(status().isUnauthorized());
    }
    @Test
    void protectedEndpoint_shouldReturn401WhenInvalidJwtIsProvided()
            throws Exception {

        mockMvc.perform(
                        get("/goals")
                                .header(
                                        "Authorization",
                                        "Bearer invalid.jwt.token"
                                )
                )
                .andExpect(status().isUnauthorized());
    }
    @Test
    void protectedEndpoint_shouldAllowRequestWhenValidUserJwtIsProvided()
            throws Exception {
        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );
        userRepository.save(user);
        String token = jwtService.generateToken("raju");
        mockMvc.perform(
                        get("/goals")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }
    @Test
    void adminEndpoint_shouldReturn403ForUserRole()
            throws Exception {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        userRepository.save(user);

        String token = jwtService.generateToken("raju");

        mockMvc.perform(
                        get("/admin/users")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isForbidden());
    }
    @Test
    void adminEndpoint_shouldAllowRequestForAdminRole()
            throws Exception {

        User admin = new User(
                null,
                "admin",
                "admin@gmail.com",
                "encodedPassword",
                null,
                Role.ADMIN
        );

        userRepository.save(admin);

        String token = jwtService.generateToken("admin");

        mockMvc.perform(
                        get("/admin/users")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }
    @Test
    void protectedEndpoint_shouldReturn401WhenJwtIsExpired()
            throws Exception {

        User user = new User(
                null,
                "expiredUser",
                "expired@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        userRepository.save(user);

        String token = jwtService.generateToken("expiredUser",-1000);


        mockMvc.perform(
                        get("/goals")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getGoalById_shouldReturn403WhenUserIsNotOwner()
            throws Exception {

        User raju = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        userRepository.save(raju);

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
                LocalDate.of(2026, 9, 14),
                GoalStatus.IN_PROGRESS,
                30
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        String rajuToken =
                jwtService.generateToken("raju");

        mockMvc.perform(
                        get("/goals/" + savedGoal.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + rajuToken
                                )
                )
                .andExpect(status().isForbidden());
    }
    @Test
    void getGoalById_shouldAllowUserToAccessOwnGoal()
            throws Exception {

        User raju = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedRaju = userRepository.save(raju);

        LearningGoal goal = new LearningGoal(
                null,
                savedRaju,
                "Learn Spring Boot",
                "Learn Spring Boot backend development",
                LocalDate.of(2026, 9, 14),
                GoalStatus.IN_PROGRESS,
                70
        );

        LearningGoal savedGoal =
                learningGoalRepository.save(goal);

        String rajuToken =
                jwtService.generateToken("raju");

        mockMvc.perform(
                        get("/goals/" + savedGoal.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + rajuToken
                                )
                )
                .andExpect(status().isOk());
    }
}