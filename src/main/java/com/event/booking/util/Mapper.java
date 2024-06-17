package com.event.booking.util;

import com.event.booking.dto.response.UserProfileResponse;
import com.event.booking.model.Role;
import com.event.booking.model.User;
import com.event.booking.model.UserRole;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {
    public static UserProfileResponse mapUserProfileResponse(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return UserProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .userToken(user.getUserToken())
                .isOnline(user.getIsOnline())
                .lastLogin(Utils.convertLocalDateTimeToString(user.getLastLogin()))
                .roles(getRoles(user.getUserRoles()))
                .permissions(getPermissions(user.getUserRoles()))
                .build();
    }

    private static List<String> getRoles(List<UserRole> userRoleList) {
        return userRoleList.stream()
                .filter(Objects::nonNull)
                .map(item -> item.getRoleid().getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<String> getPermissions(List<UserRole> userRoleList) {
        return removeDuplicates(userRoleList.stream()
                .filter(Objects::nonNull)
                .map(item -> getCommaSeparatedPermissionsFromRoles(item.getRoleid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    private static String getCommaSeparatedPermissionsFromRoles(Role role) {
        StringBuilder stringBuilder = new StringBuilder();
        role.getPermissions().forEach(permissionTypes -> {
            stringBuilder.append(permissionTypes.label).append(",");
        });
        return stringBuilder.toString();
    }

    private static List<String> removeDuplicates(List<String> permissions) {
        StringBuilder builder = new StringBuilder();
        for (String authority : permissions) {
            List<String> roleAuthorities = Arrays.stream(authority.split(",")).collect(Collectors.toList());
            if (Objects.nonNull(roleAuthorities) && !roleAuthorities.isEmpty()) {
                for (String permit : roleAuthorities) {
                    builder.append(permit).append(",");
                }
            }
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return removeDuplicatesPermissions(Arrays.stream(builder.toString().split(",")).collect(Collectors.toList()));
    }

    private static List<String> removeDuplicatesPermissions(List<String> permissions) {
        Set<String> uniquePermissions = permissions.stream().collect(Collectors.toSet());
        return List.copyOf(uniquePermissions);
    }
}
