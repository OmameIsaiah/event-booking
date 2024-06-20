package com.event.booking.util;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.response.*;
import com.event.booking.model.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.EVENT_RETRIEVED_SUCCESSFULLY;

public class Mapper {
    public static RoleResponse mapRoleToRoleResponse(Role role) {
        return Optional.ofNullable(
                        Objects.isNull(role) ? null :
                                RoleResponse.builder()
                                        .uuid(role.getUuid())
                                        .name(role.getName())
                                        .description(role.getDescription())
                                        .permissions(role.getPermissions())
                                        .build())
                .orElse(null);
    }

    public static ReservationResponseDTO mapUserEventToReservationResponseDTO(UserEvent userEvent) {
        return Optional.ofNullable(
                        Objects.isNull(userEvent) ? null :
                                ReservationResponseDTO.builder()
                                        .reservationNo(String.valueOf(userEvent.getId()))
                                        .name(userEvent.getUserevent().getName())
                                        .email(userEvent.getUserevent().getEmail())
                                        .userType(userEvent.getUserevent().getUserType())
                                        .eventId(String.valueOf(userEvent.getEventid().getId()))
                                        .eventName(userEvent.getEventid().getName())
                                        .eventDescription(userEvent.getEventid().getDescription())
                                        .eventCategory(userEvent.getEventid().getCategory())
                                        .eventDate(Utils.convertLocalDateTimeToString(userEvent.getEventid().getEventDate()))
                                        .attendeesCount(userEvent.getAttendeesCount())
                                        .build())
                .orElse(null);
    }

    public static Event mapEventRequestDTOToEvent(EventRequestDTO event) {
        return Optional.ofNullable(
                        Objects.isNull(event) ? null :
                                Event.builder()
                                        .name(event.getName())
                                        .description(event.getDescription())
                                        .eventDate(Utils.convertDateStringToLocalDateTime(event.getEventDate()))
                                        .availableAttendeesCount(event.getAvailableAttendeesCount())
                                        .category(event.getCategory())
                                        .build())
                .orElse(null);
    }

    public static EventResponseDTO mapEventToEventResponseDTO(Event event) {
        return Optional.ofNullable(
                        Objects.isNull(event) ? null :
                                EventResponseDTO.builder()
                                        .id(event.getId())
                                        .name(event.getName())
                                        .description(event.getDescription())
                                        .eventDate(Utils.convertLocalDateTimeToString(event.getEventDate()))
                                        .availableAttendeesCount(event.getAvailableAttendeesCount())
                                        .category(event.getCategory())
                                        .build())
                .orElse(null);
    }

    public static UserProfileResponse mapUserProfileResponse(User user) {
        return Optional.ofNullable(
                        Objects.isNull(user) ? null :
                                UserProfileResponse.builder()
                                        .uuid(user.getUuid())
                                        .name(user.getName())
                                        .email(user.getEmail())
                                        .userType(user.getUserType())
                                        .userToken(user.getUserToken())
                                        .isOnline(user.getIsOnline())
                                        .lastLogin(Utils.convertLocalDateTimeToString(user.getLastLogin()))
                                        .roles(getRoles(user.getUserRoles()))
                                        .permissions(getPermissions(user.getUserRoles()))
                                        .build())
                .orElse(null);
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
        return List.copyOf(permissions.stream().collect(Collectors.toSet()));
    }

    public static ResponseEntity<ApiResponse> processEventPageResponse(Page<Event> list) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapEventToEventResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }

    public static ResponseEntity<ApiResponse> processEventListResponse(List<Event> list) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_RETRIEVED_SUCCESSFULLY,
                        list.stream()
                                .map(Mapper::mapEventToEventResponseDTO)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                ));
    }
}
