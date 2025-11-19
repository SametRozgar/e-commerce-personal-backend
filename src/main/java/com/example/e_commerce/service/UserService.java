package com.example.e_commerce.service;

import com.example.e_commerce.dto.auth.RegisterRequest;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepositry;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Transactional
    public User registerUser(RegisterRequest registerRequest){
        if(userRepositry.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email is already using");
        }

        User user=User.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirsName())
                .lastName(registerRequest.getLastName())
                .role(User.Role.USER)
                .build();
        User savedUser=userRepositry.save(user);

        cartService.createCartForUser(savedUser);

        return savedUser;
    }

    public User findByEmail(String email){
        return userRepositry.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User couldnt found"))
    }
}
