package com.booklion.service;

import com.booklion.model.entity.Users;
import com.booklion.repository.UserRepository;
import com.booklion.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //아이디 중복 조회
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    //회원가입
    public Users register(String username, String password, String email) {
        String encodedPw = passwordEncoder.encode(password);
        Users users = Users.builder()
                .username(username)
                .password(encodedPw)
                .email(email)
                .build();
        return userRepository.save(users);
    }

    //로그인
    public Users login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(users -> passwordEncoder.matches(password, users.getPassword()))
                .orElse(null);
    }
    
    //회원정보 조회 - 토큰 기반
    public Users getUserInfoFromToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    //회원정보 수정
    public Users updateUserInfo(String token, String newEmail, String newPassword) {
        String username = jwtUtil.getUsernameFromToken(token);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }

    //회원 탈퇴 (로그아웃은 프론트에서 구현)
    public void deleteUserByToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }


}
