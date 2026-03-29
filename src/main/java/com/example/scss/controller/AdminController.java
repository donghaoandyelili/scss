package com.example.scss.controller;

import com.example.scss.dto.RegisterRequest;
import com.example.scss.dto.Result;
import com.example.scss.entity.User;
import com.example.scss.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/users/role/{role}")
    public Result<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(User.Role.valueOf(role.toUpperCase()));
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @PostMapping("/users")
    public Result<User> createUser(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        user.setPassword(null);
        return Result.success("用户创建成功", user);
    }

    @PutMapping("/users/{id}")
    public Result<User> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterRequest request) {
        User user = userService.updateUser(id, request);
        user.setPassword(null);
        return Result.success("用户更新成功", user);
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功", null);
    }
}
