package com.booklion.service;

import com.booklion.Repository.UserRepository;
import com.booklion.model.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public Users register(String username, String password, String email) {
        String encodedPw = passwordEncoder.encode(password);
        Users user = Users.builder()
                .username(username)
                .password(encodedPw)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    public Users login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(null);
    }
}
