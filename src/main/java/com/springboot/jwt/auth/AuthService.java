package com.springboot.jwt.auth;

import com.springboot.jwt.user.User;
import com.springboot.jwt.user.UserRepository;
import com.springboot.jwt.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) throws Exception {

        if (!userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            var user = User.builder()
                    .firstname(registerRequest.getFirstname())
                    .lastname(registerRequest.getLastname())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            return "User registered in Database";

        } else {
            System.out.println("Username already taken");
            throw new Exception("Username already taken");
        }
    }

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().toString());

        String jwtToken = jwtService.generateToken(new HashMap<>(extraClaims), user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
