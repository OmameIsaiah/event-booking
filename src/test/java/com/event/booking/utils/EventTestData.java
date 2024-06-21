package com.event.booking.utils;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventUpdateRequestDTO;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.ApiResponse;
import com.event.booking.enums.*;
import com.event.booking.model.*;
import com.event.booking.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.event.booking.util.AppMessages.*;

public class EventTestData {
    public static String VALID_EVENT_NAME = "NBA Finals, 2024";
    public static String VALID_EVENT_DESCRIPTION = "Finals of the NBA Finals, 2024, between Boston Celtics and Dallas Mavericks.";
    public static String VALID_EVENT_DATE = "2024-06-30 04:30:00";
    public static Integer VALID_EVENT_AVAILABLE_ATTENDEES_COUNT = 1000;
    public static Category VALID_EVENT_CATEGORY = Category.GAME;
    public static Event VALID_EVENT = new Event();
    public static Event INVALID_VALID_EVENT = new Event();

    static {
        VALID_EVENT.setId(1L);
        VALID_EVENT.setUuid(UUID.randomUUID().toString());
        VALID_EVENT.setDateCreated(LocalDateTime.now());
        VALID_EVENT.setLastModified(LocalDateTime.now());
        VALID_EVENT.setName(VALID_EVENT_NAME);
        VALID_EVENT.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT.setEventDate(LocalDateTime.now().plusMonths(1));
        VALID_EVENT.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
        VALID_EVENT.setCategory(VALID_EVENT_CATEGORY);

        INVALID_VALID_EVENT.setId(1L);
        INVALID_VALID_EVENT.setUuid(UUID.randomUUID().toString());
        INVALID_VALID_EVENT.setDateCreated(LocalDateTime.now());
        INVALID_VALID_EVENT.setLastModified(LocalDateTime.now());
        INVALID_VALID_EVENT.setName(VALID_EVENT_NAME);
        INVALID_VALID_EVENT.setDescription(VALID_EVENT_DESCRIPTION);
        INVALID_VALID_EVENT.setEventDate(LocalDateTime.now().plusMonths(1));
        INVALID_VALID_EVENT.setAvailableAttendeesCount(0);
        INVALID_VALID_EVENT.setCategory(VALID_EVENT_CATEGORY);
    }

    public static EventRequestDTO VALID_EVENT_REQUEST = new EventRequestDTO();

    static {
        VALID_EVENT_REQUEST.setName(VALID_EVENT_NAME);
        VALID_EVENT_REQUEST.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT_REQUEST.setEventDate(VALID_EVENT_DATE);
        VALID_EVENT_REQUEST.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
        VALID_EVENT_REQUEST.setCategory(VALID_EVENT_CATEGORY);
    }

    public static EventUpdateRequestDTO VALID_EVENT_UPDATE_REQUEST = new EventUpdateRequestDTO();

    static {
        VALID_EVENT_UPDATE_REQUEST.setEventId(1L);
        VALID_EVENT_UPDATE_REQUEST.setName(VALID_EVENT_NAME);
        VALID_EVENT_UPDATE_REQUEST.setDescription(VALID_EVENT_DESCRIPTION);
        VALID_EVENT_UPDATE_REQUEST.setEventDate(VALID_EVENT_DATE);
        VALID_EVENT_UPDATE_REQUEST.setAvailableAttendeesCount(VALID_EVENT_AVAILABLE_ATTENDEES_COUNT);
    }

    public static ResponseEntity VALID_EVENT_SUCCESS_RESPONSE;

    static {
        VALID_EVENT_SUCCESS_RESPONSE = ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        EVENT_CREATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(VALID_EVENT)
                ));

    }

    public static ResponseEntity VALID_EVENT_UPDATE_SUCCESS_RESPONSE;

    static {
        VALID_EVENT_UPDATE_SUCCESS_RESPONSE = ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        EVENT_UPDATED_SUCCESSFULLY,
                        Mapper.mapEventToEventResponseDTO(VALID_EVENT)
                ));

    }

    public static Long VALID_EVENT_ID = 1l;
    public static TicketRequest VALID_TICKET_REQUEST = new TicketRequest();
    public static TicketRequest INVALID_TICKET_REQUEST = new TicketRequest();
    public static MockHttpServletRequest VALID_MOCK_HTTP_SERVLET_REQUEST = new MockHttpServletRequest();
    public static HttpServletRequest VALID_HTTP_SERVLET_REQUEST = null;
    public static String VALID_AUTHORIZATION_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLXR5cGUiOiJBZG1pbiIsInN1YiI6Im9tYW1lYXp5QGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9DQU5fREVMRVRFX1JPTEUsUk9MRV9DQU5fVklFV19VU0VSUyxST0xFX0NBTl9BRERfUk9MRSxST0xFX0NBTl9ERUxFVEVfVVNFUlMsUk9MRV9DQU5fQ1JFQVRFX0VWRU5ULFJPTEVfQ0FOX1JFU0VSVkVfRVZFTlRfVElDS0VULFJPTEVfQ0FOX1VQREFURV9FVkVOVCxST0xFX0NBTl9ERUxFVEVfRVZFTlQsUk9MRV9DQU5fVklFV19BTExfUkVTRVJWQVRJT05TLFJPTEVfQ0FOX1VQREFURV9ST0xFLFJPTEVfQ0FOX1NFTkRfRVZFTlRfTk9USUZJQ0FUSU9OLFJPTEVfQ0FOX1ZJRVdfRVZFTlRTLFJPTEVfQ0FOX1NFQVJDSF9FVkVOVFMiLCJuYW1lIjoiRGF2aWQgVmljdG9yIiwiZXhwIjoxNzE4OTM1OTcyLCJpYXQiOjE3MTg5Mjk5NzIsImVtYWlsIjoib21hbWVhenlAZ21haWwuY29tIiwiaXNzdWVyIjoiRXZlbnQgQm9va2luZyBMdGQuIn0.1HzCM_CEzjmvH8muO-N8RLwigSUqjXfnWedMxls-emOwWTICPlGWDsloRaC0L6qV-kCQU8lFptYto0zWe3-Q4Q";

    static {
        VALID_TICKET_REQUEST.setAttendeesCount(10);
        VALID_MOCK_HTTP_SERVLET_REQUEST.addHeader(HttpHeaders.AUTHORIZATION, VALID_AUTHORIZATION_TOKEN);
        VALID_HTTP_SERVLET_REQUEST = VALID_MOCK_HTTP_SERVLET_REQUEST;

        INVALID_TICKET_REQUEST.setAttendeesCount(20000);
    }

    public static UsersTable VALID_USER = new UsersTable();
    public static String VALID_USER_NAME = "Isaiah Omame";
    public static String VALID_EMAIL = "omameazy@gmail.com";
    public static String VALID_PASSWORD = "$2a$10$lmpPHE6Plm8Kxi72x7HbaOFFaLS2aSYkO2wtesT3eYqdyoUFHH5ya";
    public static String VALID_USER_TOKEN = "TUxvREoyNWtySG9QM2hNL1RWaWU3L3pkUnd5Z2F2cHNyK3k2RXkxanF0RT0=";

    static {
        VALID_USER.setId(1L);
        VALID_USER.setName(VALID_USER_NAME);
        VALID_USER.setUuid(UUID.randomUUID().toString());
        VALID_USER.setDateCreated(LocalDateTime.now());
        VALID_USER.setLastModified(LocalDateTime.now());
        VALID_USER.setEmail(VALID_EMAIL);
        VALID_USER.setPassword(VALID_PASSWORD);
        VALID_USER.setUserType(UserType.ADMIN);
        VALID_USER.setUserToken(VALID_USER_TOKEN);
        VALID_USER.setOnboardingStage(OnboardingStage.VERIFIED);
        VALID_USER.setVerified(true);
        VALID_USER.setIsOnline(true);
        VALID_USER.setOtpCode(null);
        VALID_USER.setOtpExpireTime(null);
        VALID_USER.setLastLogin(LocalDateTime.now());
    }

    public static Set<Permissions> VALID_PERMISSIONS = Arrays.stream(Permissions.values()).collect(Collectors.toSet());
    public static Role VALID_ROLE = new Role();

    public static UserRole VALID_USER_ROLE = new UserRole();

    static {
        VALID_USER_ROLE.setId(1L);
        VALID_USER_ROLE.setUuid(UUID.randomUUID().toString());
        VALID_USER_ROLE.setLastModified(LocalDateTime.now());
        VALID_USER_ROLE.setLastModified(LocalDateTime.now());
        VALID_USER_ROLE.setRoleid(VALID_ROLE);
        VALID_USER_ROLE.setUserrole(VALID_USER);
    }

    static {
        VALID_ROLE.setId(1L);
        VALID_ROLE.setUuid(UUID.randomUUID().toString());
        VALID_ROLE.setLastModified(LocalDateTime.now());
        VALID_ROLE.setLastModified(LocalDateTime.now());
        VALID_ROLE.setName(DefaultRoles.ROLE_ADMIN.label);
        VALID_ROLE.setDescription(DefaultRoles.ROLE_ADMIN.label);
        VALID_ROLE.setPermissions(VALID_PERMISSIONS);
        VALID_ROLE.setUserRoles(List.of(VALID_USER_ROLE));
    }

    public static List<UserRole> VALID_USER_ROLE_LIST = new ArrayList<>();

    static {
        VALID_USER_ROLE_LIST.add(VALID_USER_ROLE);
    }


    public static UserEvent VALID_USER_EVENT = new UserEvent();
    public static Integer VALID_ATTENDEES_COUNT = 5;

    static {
        VALID_USER_EVENT.setId(1L);
        VALID_USER_EVENT.setUuid(UUID.randomUUID().toString());
        VALID_USER_EVENT.setDateCreated(LocalDateTime.now());
        VALID_USER_EVENT.setLastModified(LocalDateTime.now());
        VALID_USER_EVENT.setAttendeesCount(VALID_ATTENDEES_COUNT);
        VALID_USER_EVENT.setEventid(VALID_EVENT);
        VALID_USER_EVENT.setUserevent(VALID_USER);
    }

    public static ResponseEntity VALID_EVENT_RESERVATION_SUCCESS_RESPONSE;

    static {
        VALID_EVENT_RESERVATION_SUCCESS_RESPONSE = ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED,
                        EVENT_RESERVED_SUCCESSFULLY,
                        Mapper.mapUserEventToReservationResponseDTO(VALID_USER_EVENT)
                ));
    }
}
