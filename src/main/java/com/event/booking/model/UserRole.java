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
                ", uuid='" + super.getUuid() + '\'' +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", roleid=" + roleid +
                ", userrole=" + userrole +
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
