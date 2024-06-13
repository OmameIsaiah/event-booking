package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}
