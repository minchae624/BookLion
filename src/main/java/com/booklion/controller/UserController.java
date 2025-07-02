package com.booklion.controller;

import com.booklion.model.entity.Users;
import com.booklion.service.UserService;
import com.booklion.util.JwtUtil;

import jakarta.servlet.http.HttpSession;
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

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        String email = payload.get("email");

        if (userService.isUsernameTaken(username)) {
            return ResponseEntity.ok("duplicate");
        }

        userService.register(username, password, email);
        return ResponseEntity.ok("success");
    }

    // ID 중복 확인
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = !userService.isUsernameTaken(username);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", available);
        return ResponseEntity.ok(result);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload, HttpSession session) {
        String username = payload.get("username");
        String password = payload.get("password");

        Users user = userService.login(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user.getUsername());
        
        session.setAttribute("loginUser", user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@SessionAttribute(name = "loginUser", required = false) Users loginUser) {
        Map<String, Object> result = new HashMap<>();
        if (loginUser != null) {
            result.put("username", loginUser.getUsername());
        }
        return result;
    }


    // 회원정보 조회
    @GetMapping("/me")
    public Users getMyInfo(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        return userService.getUserInfoFromToken(pureToken);
    }

    // 회원정보 수정 (이미 JSON 구조 사용 중이므로 변경 없음)
    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(@RequestHeader("Authorization") String token,
                                          @RequestBody Map<String, String> updates) {
        String pureToken = token.replace("Bearer ", "");
        String email = updates.get("email");
        String newPassword = updates.get("password");

        Users updatedUser = userService.updateUserInfo(pureToken, email, newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", updatedUser.getUserId());
        response.put("username", updatedUser.getUsername());
        response.put("email", updatedUser.getEmail());

        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        userService.deleteUserByToken(pureToken);
        return ResponseEntity.ok("deleted");
    }
}
