package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Address;
import com.example.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    List<Address> findByUserId(Long userId);

    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void clearDefaultAddresses(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = true WHERE a.id = :addressId AND a.user.id = :userId")
    int setDefaultAddress(@Param("userId") Long userId, @Param("addressId") Long addressId);

    boolean existsByUserIdAndIsDefaultTrue(Long userId);

    @Query("SELECT COUNT(a) FROM Address a WHERE a.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Address a WHERE a.user.id = :userId AND a.id = :addressId")
    void deleteByUserIdAndAddressId(@Param("userId") Long userId, @Param("addressId") Long addressId);
}