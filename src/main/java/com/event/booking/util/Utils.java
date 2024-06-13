package com.event.booking.util;

import com.event.booking.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

public class Utils {

    @Value("${app.security.encryption.key}")
    private static String SECRET_KEY;

    public static String encode(Object plainText) {
        return new String(java.util.Base64.getEncoder().encode(SecurityUtils.encrypt(String.valueOf(plainText), SECRET_KEY).getBytes(StandardCharsets.UTF_8)));
    }


}
