package com.event.booking.repository;

import com.event.booking.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    @Query(nativeQuery = false, value = "SELECT ue FROM UserEvent ue WHERE ue.userevent.id=:id")
    List<UserEvent> findUserEventByUserId(@Param("id") Long id);

    @Query(nativeQuery = false, value = "SELECT ue FROM UserEvent ue WHERE ue.userevent.userToken=:userToken")
    List<UserEvent> findUserEventByUserToken(@Param("userToken") String userToken);

    @Query(nativeQuery = false, value = "SELECT ue FROM UserEvent ue WHERE ue.userevent.email=:email")
    List<UserEvent> findUserEventByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM UserEvent ue WHERE ue.userevent.id=:id")
    void deleteUserEventByUserId(@Param("id") Long id);
}
