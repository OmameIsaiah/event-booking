package com.event.booking.repository;

import com.event.booking.model.Event;
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
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(nativeQuery = false, value = "SELECT e FROM Event e WHERE e.uuid=:uuid")
    Optional<Event> findEventByUuid(@Param("uuid") String uuid);

    @Query(nativeQuery = false, value = "SELECT e FROM Event e WHERE e.name=:name")
    List<Event> findEventByName(@Param("name") String name);

    @Query(nativeQuery = false, value = "SELECT e FROM Event e")
    Page<Event> findAllEvents(Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = false, value = "DELETE FROM Event e WHERE e.uuid=:uuid")
    void deleteEventByUuid(@Param("uuid") String uuid);
}
