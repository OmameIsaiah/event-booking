package com.event.booking.model;

import com.event.booking.enums.OnboardingStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.event.booking.util.AppMessages.*;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "name")
    @Max(value = 100, message = MAX_NAME_LIMIT_EXCEEDED)
    private String name;
    @Column(name = "email", nullable = false)
    @Email(message = "Email is not valid",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = EMPTY_EMAIL)
    @NotNull(message = NULL_EMAIL)
    private String email;
    @Column(name = "password")
    @Min(value = 8, message = MIN_PASSWORD_LENGTH_NOT_REACHED)
    private String password;
    @Column(name = "user_token")
    private String userToken;
    @Column(name = "onboarding_stage")
    @Enumerated(EnumType.STRING)
    private OnboardingStage onboardingStage = OnboardingStage.STARTED;
    @Column(name = "verified")
    private Boolean verified = false;
    @Column(name = "active")
    private Boolean active = false;
    @Column(name = "otp_code")
    private String otpCode;
    @Column(name = "otp_expire_time")
    private String otpExpireTime;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
}
