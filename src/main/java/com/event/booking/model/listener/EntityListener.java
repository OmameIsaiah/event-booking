package com.event.booking.model.listener;

import com.event.booking.model.Role;
import com.event.booking.model.User;
import com.event.booking.model.UserRole;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.UUID;

@Component
public class EntityListener {
    private static UserRepository userRepository;

    @Autowired
    public void setAccountsRepo(UserRepository userRepository) {
        EntityListener.userRepository = userRepository;
    }

    @PrePersist
    private void beforeCreate(Object data) {
        if (data instanceof User) {
            User user = (User) data;
            user.setUuid(UUID.randomUUID().toString());
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
        if (data instanceof User) {
            User user = (User) data;
            user.setUserToken(SecurityUtils.encode(user.getEmail() + user.getId()));
            userRepository.save(user);
        }
    }
}
