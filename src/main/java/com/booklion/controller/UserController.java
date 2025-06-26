package com.booklion.controller;

import com.booklion.model.entity.Users;
import com.booklion.service.UserService;
import com.booklion.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email) {
        if (userService.isUsernameTaken(username)) {
            return "duplicate";
        }

        userService.register(username, password, email);
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username,
                                                      @RequestParam String password) {
        Users user = userService.login(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
