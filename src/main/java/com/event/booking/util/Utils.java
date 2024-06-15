package com.event.booking.util;

import com.event.booking.enums.UserType;
import com.event.booking.security.SecurityUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
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
}
