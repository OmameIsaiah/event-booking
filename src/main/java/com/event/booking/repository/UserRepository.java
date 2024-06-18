package com.event.booking.repository;

import com.event.booking.enums.UserType;
import com.event.booking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.uuid=:uuid")
    Optional<User> findUserByUUID(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.email=:email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.email LIKE %:keyword% OR us.name  LIKE %:keyword%  OR us.userType LIKE %:keyword%")
    List<User> searchUsers(@Param("keyword") String keyword);

    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.userType=:userType")
    Page<User> findUsersByType(@Param("userType") UserType userType, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM User us WHERE us.uuid=:uuid")
    void deleteUserByUUID(@Param("uuid") String uuid);
}
