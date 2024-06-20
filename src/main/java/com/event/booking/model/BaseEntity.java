package com.event.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.event.booking.util.Utils.DATE_FORMAT;

@MappedSuperclass
@Setter
@Getter
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Basic(optional = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "last_modified")
    @UpdateTimestamp
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime lastModified;
    @Column(name = "date_created")
    @CreationTimestamp
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime dateCreated;
}
