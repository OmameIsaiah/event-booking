package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_event")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEvent extends BaseEntity {
    @JoinColumn(name = "eventid", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Event eventid;
    @JoinColumn(name = "userevent", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User userevent;

    @Override
    public String toString() {
        return "UserEvent{" +
                "id=" + super.getId() +
                ", uuid='" + super.getUuid() + '\'' +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
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
