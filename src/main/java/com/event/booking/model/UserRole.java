package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "roleid", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Role roleid;
    @JoinColumn(name = "userrole", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private User userrole;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
}
