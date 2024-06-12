package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRole extends BaseEntity {
    @JoinColumn(name = "roleid", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Role roleid;
    @JoinColumn(name = "userrole", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User userrole;

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + super.getId() +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", roleid=" + roleid +
                ", userrole=" + userrole +
                '}';
    }
}
