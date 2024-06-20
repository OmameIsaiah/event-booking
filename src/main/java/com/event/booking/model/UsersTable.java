package com.event.booking.model;

import com.event.booking.enums.OnboardingStage;
import com.event.booking.enums.UserType;
import com.event.booking.model.listener.EntityListener;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.event.booking.util.AppMessages.*;

@Entity
@Table(name = "\"users_table\"", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(EntityListener.class)
public class UsersTable extends BaseEntity implements Serializable {
    @Column(name = "\"\"name\"")
    @Size(max = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
    @Column(name = "\"email\"", unique = true, nullable = false)
    @Email(message = INVALID_EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = EMPTY_EMAIL)
    @NotNull(message = NULL_EMAIL)
    private String email;
    @Column(name = "\"password\"")
    @Size(min = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String password;
    @Column(name = "\"user_type\"")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "\"user_token\"")
    private String userToken;
    @Column(name = "\"onboarding_stage\"")
    @Enumerated(EnumType.STRING)
    private OnboardingStage onboardingStage = OnboardingStage.STARTED;
    @Column(name = "\"verified\"")
    private Boolean verified = false;
    @Column(name = "\"is_online\"")
    private Boolean isOnline = false;
    @Column(name = "\"otp_code\"")
    private String otpCode;
    @Column(name = "\"otp_expire_time\"")
    private String otpExpireTime;
    @Column(name = "\"last_login\"")
    private LocalDateTime lastLogin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userrole")
    private List<UserRole> userRoles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userevent")
    private List<UserEvent> userEvents;

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", lastModified=" + super.getLastModified() +
                ", dateCreated=" + super.getDateCreated() +
                ", uuid='" + super.getUuid() + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", userToken='" + userToken + '\'' +
                ", onboardingStage=" + onboardingStage +
                ", verified=" + verified +
                ", active=" + isOnline +
                ", otpCode='" + otpCode + '\'' +
                ", otpExpireTime='" + otpExpireTime + '\'' +
                ", lastLogin=" + lastLogin +
                ", userRoles=" + userRoles +
                ", userEvents=" + userEvents +
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
