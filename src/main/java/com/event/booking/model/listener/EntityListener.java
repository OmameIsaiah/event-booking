package com.event.booking.model.listener;

import com.event.booking.model.User;
import com.event.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        }
    }


}
