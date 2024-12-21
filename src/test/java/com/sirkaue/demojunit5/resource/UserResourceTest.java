package com.sirkaue.demojunit5.resource;

import com.sirkaue.demojunit5.domain.User;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.factory.UserFactory;
import com.sirkaue.demojunit5.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserResource.class)
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private User user;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        user = UserFactory.createDefaultUser();
        nonExistingId = UserFactory.getNonExistingId();
    }

    @Test
    void shouldFindByIdWhenIdExists() throws Exception {
        // Arrange
        when(userService.findById(user.getId())).thenReturn(new UserResponseDto(user.getId(), user.getName(), user.getEmail()));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andDo(print());
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        // Arrange
        when(userService.findById(nonExistingId)).thenThrow(new ObjectNotFoundException("Resource not found"));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource not found"))
                .andDo(print());
    }

    @Test
    void shouldFindAllWhenExistsUsers() throws Exception {
        // Arrange
        when(userService.findAll()).thenReturn(List.of(new UserResponseDto(user.getId(), user.getName(), user.getEmail())));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andDo(print());
    }
}