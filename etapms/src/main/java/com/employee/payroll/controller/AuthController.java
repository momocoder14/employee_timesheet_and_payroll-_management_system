package com.employee.payroll.controller;

import com.employee.payroll.DTOs.AuthResponse;
import com.employee.payroll.DTOs.CurrentUserResponse;
import com.employee.payroll.DTOs.LoginRequest;
import com.employee.payroll.DTOs.RegisterRequest;
import com.employee.payroll.entity.Employee;
import com.employee.payroll.security.JwtTokenProvider;
import com.employee.payroll.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        @Autowired
        private UserService userService;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
                List<String> roles = List.of("ROLE_EMPLOYEE"); // Override roles to EMPLOYEE only
                userService.registerUser(
                                request.getUsername(),
                                request.getPassword(),
                                roles,
                                request.getFullName(),
                                request.getEmail(),
                                request.getDepartment(),
                                request.getHourlyRate(),
                                request.getTaxRate());

                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                String token = jwtTokenProvider.createToken(auth.getName(), auth.getAuthorities());

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new AuthResponse("User registered successfully", "Bearer " + token));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                String token = jwtTokenProvider.createToken(auth.getName(), auth.getAuthorities());

                return ResponseEntity.ok(new AuthResponse("Login successful", "Bearer " + token));
        }

        @PostMapping("/setup-admin")
        public ResponseEntity<AuthResponse> setupAdmin() {
                userService.registerUser("admin4", "admin1234", List.of("ROLE_ADMIN"), "Admin User",
                                "admin@example.com", "Admin Department", 50.0, 10.0);

                Authentication auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken("admin4", "admin1234"));

                String token = jwtTokenProvider.createToken(auth.getName(), auth.getAuthorities());

                return ResponseEntity.ok(new AuthResponse("Admin created", "Bearer " + token));
        }

        @GetMapping("/current-user")
        public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
                if (user == null) {
                        return ResponseEntity.status(401).body("Unauthorized");
                }

                List<String> roles = user.getAuthorities()
                                .stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority())
                                .toList();

                Employee employee = userService.findEmployeeByUsername(user.getUsername())
                                .orElseThrow(() -> new RuntimeException("Employee not found"));

                CurrentUserResponse response = new CurrentUserResponse(user.getUsername(), roles, employee);
                return ResponseEntity.ok(response);
        }

}
