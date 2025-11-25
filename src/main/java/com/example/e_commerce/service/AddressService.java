package com.example.e_commerce.service;

import com.example.e_commerce.dto.address.AddressCreateRequest;
import com.example.e_commerce.dto.address.AddressUpdateRequest;
import com.example.e_commerce.entity.Address;
import com.example.e_commerce.entity.User;
import com.example.e_commerce.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // BU IMPORTU KULLANIN

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address getAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı"));
    }

    public Address getDefaultAddress(Long userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElse(null);
    }

    @Transactional
    public Address createAddress(AddressCreateRequest request, Long userId) {
        User user = userService.findById(userId);

        // Eğer bu adres default olarak işaretlenmişse, diğer adresleri default'dan çıkar
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultAddresses(userId);
        }

        Address address = Address.builder()
                .title(request.getTitle())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .neighborhood(request.getNeighborhood())
                .street(request.getStreet())
                .addressLine(request.getAddressLine())
                .zipCode(request.getZipCode())
                .user(user)
                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                .build();

        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(Long addressId, AddressUpdateRequest request, Long userId) {
        Address existingAddress = getAddress(addressId);

        // Adresin kullanıcıya ait olduğunu kontrol et
        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu adres kullanıcıya ait değil");
        }

        // Eğer bu adres default olarak işaretlenmişse, diğer adresleri default'dan çıkar
        if (Boolean.TRUE.equals(request.getIsDefault()) && !existingAddress.getIsDefault()) {
            addressRepository.clearDefaultAddresses(userId);
        }

        // Manuel setter kullan
        existingAddress.setTitle(request.getTitle());
        existingAddress.setFirstName(request.getFirstName());
        existingAddress.setLastName(request.getLastName());
        existingAddress.setPhone(request.getPhone());
        existingAddress.setCity(request.getCity());
        existingAddress.setDistrict(request.getDistrict());
        existingAddress.setNeighborhood(request.getNeighborhood());
        existingAddress.setStreet(request.getStreet());
        existingAddress.setAddressLine(request.getAddressLine());
        existingAddress.setZipCode(request.getZipCode());
        existingAddress.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()));

        return addressRepository.save(existingAddress);
    }

    @Transactional
    public void deleteAddress(Long addressId, Long userId) {
        Address address = getAddress(addressId);

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu adres kullanıcıya ait değil");
        }

        // Eğer silinecek adres default ise ve başka adres varsa, ilk adresi default yap
        if (address.getIsDefault()) {
            List<Address> userAddresses = addressRepository.findByUserId(userId);
            userAddresses.stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .findFirst()
                    .ifPresent(otherAddress -> {
                        otherAddress.setIsDefault(true);
                        addressRepository.save(otherAddress);
                    });
        }

        addressRepository.delete(address);
    }

    @Transactional
    public Address setDefaultAddress(Long userId, Long addressId) {
        Address address = getAddress(addressId);

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu adres kullanıcıya ait değil");
        }

        // Önce tüm adresleri default'dan çıkar
        addressRepository.clearDefaultAddresses(userId);

        // Sonra ilgili adresi default yap
        address.setIsDefault(true);
        return addressRepository.save(address);
    }

    public boolean isAddressBelongsToUser(Long addressId, Long userId) {
        return addressRepository.findById(addressId)
                .map(address -> address.getUser().getId().equals(userId))
                .orElse(false);
    }
}