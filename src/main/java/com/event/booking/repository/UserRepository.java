package com.event.booking.repository;

import com.event.booking.enums.UserType;
import com.event.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.uid=:uuid")
    Optional<User> findUserByUUID(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.email=:email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query(nativeQuery = false, value = "SELECT us FROM User us WHERE us.userType=:userType")
    List<User> findUsersByType(@Param("userType") UserType userType);
}
