package com.event.booking.model.listener;

import com.event.booking.model.Role;
import com.event.booking.model.UsersTable;
import com.event.booking.model.UserRole;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.UUID;

@Component
@Slf4j
public class EntityListener {
    private static UserRepository userRepository;

    @Autowired
    public void setAccountsRepo(UserRepository userRepository) {
        EntityListener.userRepository = userRepository;
    }

    @PrePersist
    private void beforeCreate(Object data) {
        if (data instanceof UsersTable) {
            UsersTable users = (UsersTable) data;
            users.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof Role) {
            Role role = (Role) data;
            role.setUuid(UUID.randomUUID().toString());
        } else if (data instanceof UserRole) {
            UserRole userRole = (UserRole) data;
            userRole.setUuid(UUID.randomUUID().toString());
        }
    }

    @PostPersist
    private void afterCreate(Object data) {
        if (data instanceof UsersTable) {
            UsersTable users = (UsersTable) data;
            users.setUserToken(SecurityUtils.encode(users.getEmail() + users.getId()));
            userRepository.save(users);
        }
    }
}
