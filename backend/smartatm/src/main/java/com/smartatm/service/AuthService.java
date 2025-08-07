package com.smartatm.service;

import com.smartatm.dto.LoginRequest;
import com.smartatm.dto.RegisterRequest;
import com.smartatm.model.User;
import com.smartatm.repository.UserRepository;
import com.smartatm.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request) {
        User user = User.builder()
                .userName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);

        accountService.createAccountForUser(user);
        List<String> roles = List.of("ROLE_" + user.getRole());
        return jwtService.generateToken(user.getEmail(), roles);
    }

    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User can not find");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Password is incorrect");
        }

        List<String> roles = List.of("ROLE_" + user.getRole());

        return jwtService.generateToken(user.getEmail(), roles);
    }
}
