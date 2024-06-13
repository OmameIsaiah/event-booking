package com.event.booking.model;

import com.event.booking.enums.Permissions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Role extends BaseEntity {
    @Column(unique = true, name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @JsonIgnore
    @Column(name = "permissions")
    @Type(type = "json")
    private Set<Permissions> permissions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleid")
    private List<UserRole> userRoles;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + super.getId() +
                ", uuid='" + super.getUuid() + '\'' +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permissions=" + permissions +
                ", userRoles=" + userRoles +
                '}';
    }
}
