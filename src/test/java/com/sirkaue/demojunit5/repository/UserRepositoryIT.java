package com.sirkaue.demojunit5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
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
}
