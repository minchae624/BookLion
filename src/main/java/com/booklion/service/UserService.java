package com.booklion.service;

import com.booklion.model.entity.Users;
import com.booklion.repository.UserRepository;
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
        Users users = Users.builder()
                .username(username)
                .password(encodedPw)
                .email(email)
                .build();
        return userRepository.save(users);
    }

    public Users login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(users -> passwordEncoder.matches(password, users.getPassword()))
                .orElse(null);
    }
}
