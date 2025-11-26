package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.auth.JwtResponse;
import com.example.e_commerce.dto.auth.LoginRequest;
import com.example.e_commerce.dto.auth.RegisterRequest;

import com.example.e_commerce.entity.User;
import com.example.e_commerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Kullanıcı kayıt ve giriş işlemleri")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Kullanıcı kaydı", description = "Yeni kullanıcı oluşturur")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(user, "Kullanıcı başarıyla kaydedildi"));
    }

    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı girişi yapar ve JWT token döner")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(jwtResponse, "Giriş başarılı"));
    }

    @GetMapping("/me")
    @Operation(summary = "Mevcut kullanıcı bilgisi", description = "Giriş yapmış kullanıcının bilgilerini getirir")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(currentUser, "Kullanıcı bilgileri getirildi"));
    }
}