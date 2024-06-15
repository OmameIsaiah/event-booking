package com.event.booking.security.user;

import com.event.booking.model.Role;
import com.event.booking.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {
    private String name;
    private String userType;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String name, String userType, String username, String password,
                           Collection<? extends GrantedAuthority> roles,
                           Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }

    public static String getListOfPermissionsFromRoles(Role userRole) {
        StringBuilder stringBuilder = new StringBuilder();
        userRole.getPermissions().forEach(permissionTypes -> {
            stringBuilder.append(permissionTypes.label).append(",");
        });
        if (stringBuilder.length() > 0) {
            // Remove the trailing comma
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static List<String> removeDuplicatesPermissions(List<String> permissions) {
        return List.copyOf(permissions.stream().collect(Collectors.toSet()));
    }

    public static List<GrantedAuthority> removeDuplicatesFromAuthorities(List<GrantedAuthority> permissions) {
        StringBuilder builder = new StringBuilder();
        for (GrantedAuthority authority : permissions) {
            List<String> roleAuthorities = Arrays.stream(authority.getAuthority().split(",")).toList();
            if (Objects.nonNull(roleAuthorities) && !roleAuthorities.isEmpty()) {
                for (String permit : roleAuthorities) {
                    builder.append("ROLE_" + permit).append(",");
                }
            }
        }
        if (builder.length() > 0) {
            // Remove the trailing comma
            builder.setLength(builder.length() - 1);
        }
        List<String> cleanedList = removeDuplicatesPermissions(
                Arrays.stream(builder.toString().split(",")).toList()
        );
        return cleanedList.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getName(),
                user.getUserType().label,
                user.getEmail(),
                user.getPassword(),
                processUserRoles(user),
                processUserAuthorities(user));
    }

    private static List<GrantedAuthority> processUserRoles(User user) {
        return user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleid().getName()))
                .collect(Collectors.toList());
    }

    private static List<GrantedAuthority> processUserAuthorities(User user) {
        return removeDuplicatesFromAuthorities(
                user.getUserRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(getListOfPermissionsFromRoles(role.getRoleid())))
                        .collect(Collectors.toList())
        );
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
