package com.event.booking.repository;

import com.event.booking.enums.Category;
import com.event.booking.model.UserEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<UserEvent> findUserEventByUserId(@Param("id") Long id, Pageable pageable);

    @Query(nativeQuery = false, value = "SELECT ue FROM UserEvent ue WHERE ue.eventid.category=:category")
    Page<UserEvent> findUserEventByCategory(@Param("category") Category category, Pageable pageable);

    @Query(nativeQuery = false, value = "SELECT ue FROM UserEvent ue WHERE ue.userevent.email=:email")
    Page<UserEvent> findUserEventByEmail(@Param("email") String email, Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM UserEvent ue WHERE ue.userevent.id=:id")
    void deleteUserEventByUserId(@Param("id") Long id);
}
