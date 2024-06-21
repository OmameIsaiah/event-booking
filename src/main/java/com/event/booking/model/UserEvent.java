package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

import static com.event.booking.util.AppMessages.*;

@Entity
@Table(name = "\"user_event\"")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEvent extends BaseEntity {
    @Column(name = "attendees_count")
    @Min(value = 1, message = MIN_ATTENDEES_COUNT_EXCEEDED)
    @Positive(message = NEGATIVE_ATTENDEES_COUNT)
    @NotNull(message = NULL_ATTENDEES_COUNT)
    private Integer attendeesCount;
    @JoinColumn(name = "eventid", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Event eventid;
    @JoinColumn(name = "userevent", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private UsersTable userevent;

    @Override
    public String toString() {
        return "UserEvent{" +
                "id=" + super.getId() +
                ", uuid='" + super.getUuid() + '\'' +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", attendeesCount=" + attendeesCount +
                ", eventid=" + eventid +
                ", userevent=" + userevent +
                '}';
    }

    @Override
    public void setUuid(String uuid) {
        super.setUuid(uuid);
    }

    @Override
    public String getUuid() {
        return super.getUuid();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setDateCreated(LocalDateTime dateCreated) {
        super.setDateCreated(dateCreated);
    }

    @Override
    public LocalDateTime getDateCreated() {
        return super.getDateCreated();
    }

    @Override
    public void setLastModified(LocalDateTime lastModified) {
        super.setLastModified(lastModified);
    }

    @Override
    public LocalDateTime getLastModified() {
        return super.getLastModified();
    }
}
