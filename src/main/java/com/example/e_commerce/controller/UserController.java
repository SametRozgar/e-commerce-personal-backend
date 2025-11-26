package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.user.*;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.service.AuthService;
import com.example.e_commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Kullanıcı yönetimi API'leri")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/profile")
    @Operation(summary = "Kullanıcı profilini getir")
    public ResponseEntity<ApiResponse<User>> getCurrentUserProfile() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(user, "Profil bilgileri getirildi"));
    }

    @PutMapping("/profile")
    @Operation(summary = "Kullanıcı profilini güncelle")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        User currentUser = authService.getCurrentUser();
        User updatedUser = userService.updateUserProfile(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Profil başarıyla güncellendi"));
    }

    @PutMapping("/change-password")
    @Operation(summary = "Şifre değiştir")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = authService.getCurrentUser();
        userService.changePassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null, "Şifre başarıyla değiştirildi"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tüm kullanıcıları getir (ADMIN)")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success(users, "Kullanıcılar getirildi"));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı detayını getir (ADMIN)")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "Kullanıcı bilgileri getirildi"));
    }

    @PutMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı durumunu güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        User updatedUser = userService.updateUserStatus(userId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Kullanıcı durumu güncellendi"));
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı rolünü güncelle (ADMIN)")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleUpdateRequest request) {
        User updatedUser = userService.updateUserRole(userId, request.getRole());
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Kullanıcı rolü güncellendi"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı ara (ADMIN)")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(
            @RequestParam String email) {
        List<User> users = userService.searchUsersByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(users, "Arama sonuçları getirildi"));
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı istatistikleri (ADMIN)")
    public ResponseEntity<ApiResponse<UserStatsResponse>> getUserStats() {
        Long totalUsers = userService.getTotalUserCount();
        Long activeUsers = userService.getActiveUserCount();

        UserStatsResponse stats = new UserStatsResponse();
        stats.setTotalUsers(totalUsers);
        stats.setActiveUsers(activeUsers);

        return ResponseEntity.ok(ApiResponse.success(stats, "İstatistikler getirildi"));
    }
}