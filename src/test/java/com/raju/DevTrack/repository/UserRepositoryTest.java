package com.raju.DevTrack.repository;

import com.raju.DevTrack.model.Role;
import com.raju.DevTrack.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByUsername_shouldReturnUserWhenUserExists() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        userRepository.save(user);

        Optional<User> result =
                userRepository.findUserByUsername("raju");

        assertTrue(result.isPresent());
        assertEquals("raju", result.get().getUsername());
        assertEquals(
                "rajujindam@gmail.com",
                result.get().getEmail()
        );
    }
    @Test
    void findById_shouldReturnUserWhenUserExists() {

        User user = new User(
                null,
                "raju",
                "rajujindam@gmail.com",
                "encodedPassword",
                null,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        Optional<User> result =
                userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getId());
        assertEquals("raju", result.get().getUsername());
        assertEquals("rajujindam@gmail.com", result.get().getEmail());
    }
    @Test
    void findById_shouldReturnEmptyWhenUserDoesNotExist() {

        Optional<User> result =
                userRepository.findById(999L);

        assertTrue(result.isEmpty());
    }
    @Test
    void findUserByUsername_shouldReturnEmptyWhenUserDoesNotExist() {

        Optional<User> result =
                userRepository.findUserByUsername("unknown");

        assertTrue(result.isEmpty());
    }
}
