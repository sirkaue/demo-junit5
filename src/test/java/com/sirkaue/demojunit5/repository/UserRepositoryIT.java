package com.sirkaue.demojunit5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnTrueWhenIdExists() {
        // Arrange
        final long EXISTING_ID = 1L;

        // Act
        boolean exists = userRepository.existsById(EXISTING_ID);

        // Assert
        assertTrue(exists, "O método deve retornar true para um ID existente.");
    }

    @Test
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnFalseWhenIdDoesNotExist() {
        // Arrange
        final long NON_EXISTING_ID = 3L;

        // Act
        boolean exists = userRepository.existsById(NON_EXISTING_ID);

        // Assert
        assertFalse(exists, "O método deve retornar false para um ID inexistente.");
    }

    @Test
    void shouldReturnFindAll() {
        // Act
        var users = userRepository.findAll();

        // Assert
        assertFalse(users.isEmpty(), "O método deve retornar uma lista não vazia.");
    }
}
