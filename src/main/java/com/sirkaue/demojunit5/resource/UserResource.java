package com.sirkaue.demojunit5.resource;

import com.sirkaue.demojunit5.dto.request.UserRequestDto;
import com.sirkaue.demojunit5.dto.response.UserResponseDto;
import com.sirkaue.demojunit5.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResource {

    public static final String ID = "/{id}";
    
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(ID)
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto dto = userService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserResponseDto user = userService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(ID)
                .buildAndExpand(user.id())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(ID)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        userService.update(id, dto);
        return ResponseEntity.noContent().build();
    }
}
