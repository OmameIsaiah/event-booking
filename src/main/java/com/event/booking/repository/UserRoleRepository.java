package com.event.booking.repository;

import com.event.booking.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(nativeQuery = false, value = "SELECT ur FROM UserRole ur WHERE ur.userrole.id=:id")
    Optional<UserRole> findUserRoleByUserId(@Param("id") Long id);

    @Query(nativeQuery = false, value = "SELECT ur FROM UserRole ur WHERE ur.userrole.userToken=:userToken")
    Optional<UserRole> findUserRoleByUserToken(@Param("userToken") String userToken);

    @Query(nativeQuery = false, value = "SELECT ur FROM UserRole ur WHERE ur.userrole.email=:email")
    Optional<UserRole> findUserRoleByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM UserRole ur WHERE ur.userrole.id=:id")
    void deleteUserRoleByUserId(@Param("id") Long id);
}
