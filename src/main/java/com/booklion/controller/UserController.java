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

    //회원 가입
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
    
    //로그인
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
    
    //회원정보 조회 - 마이페이지 이름 같은 거
    @GetMapping("/me")
    public Users getMyInfo(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        return userService.getUserInfoFromToken(pureToken);
    }
    
    //회원정보 수정
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

    //회원 탈퇴 (로그아웃은 프론트에서 구현)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        String pureToken = token.replace("Bearer ", "");
        userService.deleteUserByToken(pureToken);
        return ResponseEntity.ok("deleted");
    }

    

}
