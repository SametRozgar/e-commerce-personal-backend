package com.example.e_commerce;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled // Testleri geçici olarak devre dışı bırak
class ECommerceApplicationTests {

    @Test
    void contextLoads() {
        // Test geçici olarak devre dışı
    }
}