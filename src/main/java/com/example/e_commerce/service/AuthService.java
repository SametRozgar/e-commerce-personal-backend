package com.example.e_commerce.service;

import com.example.e_commerce.dto.auth.JwtResponse;
import com.example.e_commerce.dto.auth.LoginRequest;
import com.example.e_commerce.dto.auth.RegisterRequest;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.security.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final CartService cartService;

    @Transactional
    public JwtResponse login(LoginRequest request) {
        // Kullanıcı durum kontrolü
        User user = userService.findByEmail(request.getEmail());
        if (user.getStatus() == User.UserStatus.FROZEN) {
            throw new RuntimeException("Hesabınız dondurulmuş. Lütfen müşteri hizmetleri ile iletişime geçin.");
        }
        if (user.getStatus() == User.UserStatus.BANNED) {
            throw new RuntimeException("Hesabınız engellenmiş. Giriş yapamazsınız.");
        }

        // Authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtTokenUtil.generateToken(userDetails);

        return new JwtResponse(jwt, user.getEmail(), user.getRole().name());
    }

    @Transactional
    public User register(RegisterRequest request) {
        // Kullanıcıyı kaydet
        User savedUser = userService.registerUser(request);

        // Kullanıcıya boş bir sepet oluştur - circular dependency olmayacak
        cartService.createCartForUser(savedUser);

        return savedUser;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Kullanıcı giriş yapmamış");
        }
        return (User) authentication.getPrincipal();
    }
}
