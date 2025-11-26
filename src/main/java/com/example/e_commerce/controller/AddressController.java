package com.example.e_commerce.controller;

import com.example.e_commerce.dto.ApiResponse;
import com.example.e_commerce.dto.address.AddressCreateRequest;
import com.example.e_commerce.dto.address.AddressUpdateRequest;
import com.example.e_commerce.entity.Address;
import com.example.e_commerce.service.AddressService;
import com.example.e_commerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Address Management", description = "Adres yönetimi API'leri")
public class AddressController {

    private final AddressService addressService;
    private final AuthService authService; // AuthService eklendi

    @GetMapping
    @Operation(summary = "Kullanıcının adreslerini getir")
    public ResponseEntity<ApiResponse<List<Address>>> getUserAddresses() {
        Long userId = getCurrentUserId();
        List<Address> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses, "Adresler getirildi"));
    }

    @GetMapping("/default")
    @Operation(summary = "Varsayılan adresi getir")
    public ResponseEntity<ApiResponse<Address>> getDefaultAddress() {
        Long userId = getCurrentUserId();
        Address address = addressService.getDefaultAddress(userId);
        return ResponseEntity.ok(ApiResponse.success(address, "Varsayılan adres getirildi"));
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Adres detayını getir")
    public ResponseEntity<ApiResponse<Address>> getAddressById(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        Address address = addressService.getAddress(addressId);

        if (!addressService.isAddressBelongsToUser(addressId, userId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Bu adresi görüntüleme yetkiniz yok"));
        }

        return ResponseEntity.ok(ApiResponse.success(address, "Adres getirildi"));
    }

    @PostMapping
    @Operation(summary = "Yeni adres oluştur")
    public ResponseEntity<ApiResponse<Address>> createAddress(@Valid @RequestBody AddressCreateRequest request) {
        Long userId = getCurrentUserId();
        Address address = addressService.createAddress(request, userId);
        return ResponseEntity.ok(ApiResponse.success(address, "Adres başarıyla oluşturuldu"));
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Adres güncelle")
    public ResponseEntity<ApiResponse<Address>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressUpdateRequest request) {
        Long userId = getCurrentUserId();
        Address address = addressService.updateAddress(addressId, request, userId);
        return ResponseEntity.ok(ApiResponse.success(address, "Adres başarıyla güncellendi"));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Adres sil")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        addressService.deleteAddress(addressId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Adres başarıyla silindi"));
    }

    @PutMapping("/{addressId}/set-default")
    @Operation(summary = "Varsayılan adres olarak ayarla")
    public ResponseEntity<ApiResponse<Address>> setDefaultAddress(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        Address address = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.success(address, "Varsayılan adres ayarlandı"));
    }

    // Helper method - AuthService kullan
    private Long getCurrentUserId() {
        return authService.getCurrentUser().getId();
    }
}