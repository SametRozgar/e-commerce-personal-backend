package com.example.e_commerce.repository;

import com.example.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

Optional <User> findByEmail(String email);

 boolean existsByEmail(String email);

 List<User> findByRole(User.Role role);
 List<User> findByStatus(User.UserStatus userStatus);
 List<User> findByRoleAndStatus(User.Role role,User.UserStatus status);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    void updateUserStatus(@Param("userId") Long userId, @Param("status") User.UserStatus status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    Long countAllUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER' AND u.status = 'ACTIVE'")
    Long countActiveUsers();

    @Query("SELECT u FROM User u WHERE u.email LIKE %:email%")
    List<User> findByEmailContaining(@Param("email") String email);


}
