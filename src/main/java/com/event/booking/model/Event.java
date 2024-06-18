package com.event.booking.model;

import com.event.booking.enums.Category;
import com.event.booking.model.listener.EntityListener;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.event.booking.util.AppMessages.*;

@Entity
@Table(name = "event")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(EntityListener.class)
public class Event extends BaseEntity implements Serializable {
    @Column(name = "name", nullable = false)
    @Size(max = 100, message = MAX_EVENT_NAME_LIMIT_EXCEEDED)
    @NotEmpty(message = EMPTY_EVENT_NAME)
    @NotNull(message = NULL_EVENT_NAME)
    private String name;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 500, message = MAX_EVENT_DESCRIPTION_LIMIT_EXCEEDED)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "available_attendees_count")
    @Max(value = 1000, message = AVAILABLE_ATTENDEES_COUNT_EXCEEDED)
    @Min(value = 0, message = MIN_AVAILABLE_ATTENDEES_COUNT_EXCEEDED)
    @Positive(message = NEGATIVE_AVAILABLE_ATTENDEES_COUNT)
    private Integer availableAttendeesCount;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventid")
    private List<UserEvent> userEvents;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + super.getId() +
                ", uuid='" + super.getUuid() + '\'' +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", availableAttendeesCount=" + availableAttendeesCount +
                ", category=" + category +
                ", userEvents=" + userEvents +
                '}';
    }
}
