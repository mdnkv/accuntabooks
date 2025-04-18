package dev.mednikov.accunta.users.controllers;

import dev.mednikov.accunta.users.dto.CreateUserRequestDto;
import dev.mednikov.accunta.users.dto.UserDto;
import dev.mednikov.accunta.users.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody UserDto createUser (@RequestBody CreateUserRequestDto payload) {
        return this.userService.createUser(payload);
    }

    @PutMapping("/update")
    public @ResponseBody UserDto updateUser (@RequestBody UserDto payload) {
        return this.userService.updateUser(payload);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser (@PathVariable UUID id) {
        this.userService.deleteUser(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser (@PathVariable UUID id) {
        Optional<UserDto> result = this.userService.getUser(id);
        return ResponseEntity.of(result);
    }

}
