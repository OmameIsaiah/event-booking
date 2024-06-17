package com.event.booking.util;

import com.event.booking.enums.UserType;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {
    private static String OTP_EXPIRE_TIME = "600";
    static EmailValidator validator = EmailValidator.getInstance();

    public static Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (!pat.matcher(email).matches()) {
            return false;
        } else {
            return validator.isValid(email);
        }
    }

    public static Boolean isValidUserType(UserType userType) {
        if (Objects.isNull(userType)) {
            return false;
        }
        for (UserType myEnum : UserType.values()) {
            if (myEnum.label.equals(userType.label)) {
                return true;
            }
        }
        return false;
    }

    public static String generate4DigitOTPAndExpireTime() {
        int min = 9999;
        int max = 1000;
        int randomValue = (int) (Math.random() * (max - min + 1) + min);
        String otp = String.valueOf(randomValue);
        long baseTime = (System.currentTimeMillis() / 1000) + Integer.parseInt(OTP_EXPIRE_TIME);
        String otpLimitTime = "" + baseTime + "";
        return otp + "_" + otpLimitTime;
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .format(java.util.Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime convertDateStringToLocalDateTime(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        try {
            if (date.length() <= 10) {
                date += " 00:00:00";
            }
            return LocalDateTime
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC+1"))
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
