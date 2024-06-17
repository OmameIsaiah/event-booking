package com.event.booking.util;

public class AppMessages {
    public static final String TOKEN_PREFIX = "Bearer";
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    public static final String EMAIL_NOT_FOUND = "Oops! No user found with this email: %s";
    public static final String INVALID_EMAIL = "Oops! Invalid email, please check.";
    public static final String WRONG_ACCOUNT_EMAIL = "Oops! No account with this email, please check.";
    public static final String WRONG_ACCOUNT_PASSWORD = "Oops! Your password is incorrect, please check.";
    public static final String WRONG_CURRENT_PASSWORD = "Oops! Your current password is incorrect, please check.";
    public static final String EMPTY_EMAIL = "Oops! Email cannot be empty, please check.";
    public static final String EMPTY_EVENT_NAME = "Oops! Event name cannot be empty, please check.";
    public static final String NULL_EMAIL = "Oops! Email cannot be null, please check.";
    public static final String NULL_EVENT_NAME = "Oops! Event name cannot be null, please check.";
    public static final String MAX_NAME_LIMIT_EXCEEDED = "Oops! Your name cannot exceed 100 characters, please check.";
    public static final String NULL_NAME_PARAM = "Oops! Name cannot be null, please check.";
    public static final String EMPTY_NAME_PARAM = "Oops! Name cannot be empty, please check.";
    public static final String NULL_PASSWORD = "Oops! Password cannot be null, please check.";
    public static final String NULL_PASSWORD_PARAM = "Oops! Password/confirm password cannot be null, please check.";
    public static final String NULL_CURRENT_PASSWORD_PARAM = "Oops! Current password cannot be null, please check.";
    public static final String EMPTY_PASSWORD = "Oops! Password cannot be empty, please check.";
    public static final String EMPTY_PASSWORD_PARAM = "Oops! Password/confirm password cannot be empty, please check.";
    public static final String EMPTY_CURRENT_PASSWORD_PARAM = "Oops! Current password cannot be empty, please check.";
    public static final String NULL_OTP_PARAM = "Oops! OTP cannot be null, please check.";
    public static final String EMPTY_OTP_PARAM = "Oops! OTP cannot be empty, please check.";
    public static final String EXPIRED_OTP = "Oops! Your OTP has expired, kindly generate a new one.";
    public static final String INVALID_OTP = "Oops! The OTP provided is incorrect, please provide a valid OTP.";
    public static final String MAX_EVENT_NAME_LIMIT_EXCEEDED = "Oops! Event name cannot exceed 100 characters, please check.";
    public static final String MAX_EVENT_DESCRIPTION_LIMIT_EXCEEDED = "Oops! Event description cannot exceed 500 characters, please check.";
    public static final String MIN_PASSWORD_LENGTH_NOT_REACHED = "Oops! Your password cannot be less than 8 characters, please check.";
    public static final String INVALID_OTP_LENGTH = "Oops! Your OTP is a 4 dig number, please check.";
    public static final String AVAILABLE_ATTENDEES_COUNT_EXCEEDED = "Oops! Available attendees count cannot exceed 1000, please check.";
    public static final String MIN_AVAILABLE_ATTENDEES_COUNT_EXCEEDED = "Oops! Available attendees count cannot be less than 0, please check.";
    public static final String NEGATIVE_AVAILABLE_ATTENDEES_COUNT = "Oops! Available attendees count cannot be negative, please check.";
    public static final String INVALID_REQUEST_PARAMETERS = "Oops! Your request parameters is either incomplete or invalid, please check.";
    public static final String INVALID_ACCESS_TOKEN = "Oops! Invalid access token, please check.";
    public static final String ACCOUNT_NOT_VERIFIED = "Oops! Your account is not verified. Kindly check for the OTP sent to your registered email or resent a new one to verify your account";
    public static final String INVALID_USER_TYPE = "Oops! Invalid user type. Valid user types are: ADMIN and USER.";
    public static final String PASSWORD_MISMATCH = "Oops! Password mismatch, kindly retype your password.";
    public static final String EMAIL_ALREADY_TAKEN = "Oops! An account with this email already exist in our record, kindly use another email.";
    public static final String ROLE_NOT_FOUND = "Oops! Role %s not found, please check!";
    public static final String INVALID_AUTHORIZATION_TOKEN = "Oops! Invalid authorization, please provide your authorization token in the request header.";
    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    public static final String ACCOUNT_CREATION_SUCCESSFUL = "Awesome! Your account has been created successfully. An OTP has been sent to your registered email for verification.";
    public static final String OTP_SENT_SUCCESSFUL = "Awesome! An OTP has been sent to your registered email for verification.";
    public static final String OTP_VERIFIED_SUCCESSFULLY = "Awesome! Your email has been verified and your account is activate, sign in to access your dashboard.";
    public static final String ACCOUNT_ALREADY_ACTIVATED = "Awesome! Your account is already activated. Please sign in to access your dashboard.";
    public static final String SIGN_IN_SUCCESSFUL = "Awesome! You have signed in successfully.";
    public static final String SIGN_OUT_SUCCESSFUL = "Awesome! You have signed out successfully.";
    public static final String FETCH_PROFILE_SUCCESSFUL = "Awesome! User profile retrieved successfully.";
    public static final String PROFILE_UPDATED_SUCCESSFUL = "Awesome! User profile updated successfully.";
    public static final String PASSWORD_UPDATED_SUCCESSFUL = "Awesome! Password updated successfully.";
    public static final String AUTHORIZATION_PROCESSED_SUCCESSFULLY = "Awesome! Your authorization token is ready. Use the prefix 'Bearer ' followed by the token in the Authorization header.";
}
