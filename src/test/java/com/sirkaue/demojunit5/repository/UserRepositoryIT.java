package com.sirkaue.demojunit5.repository;

import com.sirkaue.demojunit5.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnEmptyListWhenFindAllDoesNotFindAnyUser() {
        // Act
        var users = userRepository.findAll();

        // Assert
        assertTrue(users.isEmpty(), "O método deve retornar uma lista vazia.");
    }

    @Test
    void shouldCreateUserIfEmailDoesNotExist() {
        // Arrange
        User user = new User();
        user.setName("Jane");
        user.setEmail("jane@email.com");
        user.setPassword("123456");

        // Act
        userRepository.save(user);

        // Assert
        var savedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(savedUser, "O usuário deve ser salvo.");
        assertEquals(user.getId(), savedUser.getId(), "O ID do usuário deve ser salvo.");
        assertEquals("Jane", savedUser.getName(), "O nome do usuário deve ser salvo.");
        assertEquals("jane@email.com", savedUser.getEmail(), "O e-mail do usuário deve ser salvo.");
        assertEquals("123456", savedUser.getPassword(), "A senha do usuário deve ser salva.");
    }

    @Test
    void shouldNotCreateUserIfEmailExists() {
        // Arrange
        User userWithSameEmail = new User();
        userWithSameEmail.setName("Bob");
        userWithSameEmail.setEmail("john@email.com");
        userWithSameEmail.setPassword("654321");

        // Act
        Executable executable = () -> userRepository.save(userWithSameEmail);

        // Assert
        assertThrows(DataIntegrityViolationException.class, executable,
                "Deve lançar uma exceção quando tentar salvar um usuário com e-mail já existente.");
    }

    @Test
    void shouldNotUpdateWhenUserDoesNotExist() {
        // Arrange
        final long NON_EXISTING_ID = 3L;

        // Act & Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            User user = userRepository.findById(NON_EXISTING_ID).orElseThrow(
                    () -> new EmptyResultDataAccessException(1));

            // Se o usuário não for encontrado, o código nunca chega a esta linha
            user.setName("Jane Doe");
            user.setEmail("jane.doe@example.com");
            user.setPassword("654321");
            userRepository.save(user);
        }, "Deve lançar uma exceção ao tentar atualizar um usuário inexistente.");
    }

    @Test
    void shouldUpdateUserWhenExists() {
        // Arrange
        final long EXISTING_ID = 1L;
        User user = userRepository.findById(EXISTING_ID).orElseThrow();

        // Act
        user.setName("Jane Doe");
        user.setEmail("jane@email.com");
        user.setPassword("654321");
        userRepository.save(user);

        // Assert
        var updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertNotNull(updatedUser, "O usuário deve ser atualizado.");
        assertEquals(user.getId(), updatedUser.getId(), "O ID do usuário deve ser atualizado.");
        assertEquals("Jane Doe", updatedUser.getName(), "O nome do usuário deve ser atualizado.");
    }
}
