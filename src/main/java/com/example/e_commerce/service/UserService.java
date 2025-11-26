package com.example.e_commerce.service;

import com.example.e_commerce.dto.auth.RegisterRequest;
import com.example.e_commerce.dto.user.UserProfileUpdateRequest;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // UserDetailsService metodunu implemente et - BU METODU EKLEYİN
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email zaten kullanılıyor");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.Role.USER)
                .status(User.UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);


        return savedUser;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        User user = findById(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    @Transactional
    public User updateUserStatus(Long userId, User.UserStatus status) {
        User user = findById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserRole(Long userId, User.Role role) {
        User user = findById(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mevcut şifre hatalı");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public Long getTotalUserCount() {
        return userRepository.countAllUsers();
    }

    public Long getActiveUserCount() {
        return userRepository.countActiveUsers();
    }

    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmailContaining(email);
    }
}