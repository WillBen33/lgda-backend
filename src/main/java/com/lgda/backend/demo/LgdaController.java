package com.lgda.backend.demo;

import com.lgda.backend.user.User;
import com.lgda.backend.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("users")
public class LgdaController {

    private final UserRepository userRepository;

    public LgdaController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{email}")
    public Optional<User> getByEmail(@PathVariable("email") String email) {
        return userRepository.findByEmail(email);
    }

    @GetMapping("/users-only")
    public ResponseEntity<String> getUsersOnly() {
        return ResponseEntity.ok("Sorry, users only !");
    }

    @GetMapping("/admins-only")
    public ResponseEntity<String> getAdminsOnly() {
        return ResponseEntity.ok("Sorry, admins only !");
    }
}

