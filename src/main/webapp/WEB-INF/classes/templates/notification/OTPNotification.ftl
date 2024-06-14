<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Verify Email Address</title>
</head>

<body style="background-color: whitesmoke; font-family: Arial, sans-serif; color: #5E6670;  padding: 50px 0;">
<div style="background-color: white; width: 80%; margin: auto;  border-radius: 6px; border:thin solid #f1f1f1;">
    <div style="padding: 20px 0; background-color: #01de60; color:#f5f5f5; text-align: center; font-size: 15px; border-radius: 6px 6px 0 0;">
        <span>Verify your email address</span>
    </div>
    <div style="padding: 40px;">
        <div>
            <h1 style="color: black; font-size: 18px; font-weight: 600">Dear ${name},</h1>
            <p style="font-size: 15px; margin-bottom: 10px; margin-top: 10px; line-height: 30px; color: #666666;">
                Thank you for registering with Event Booking Ltd.! To complete your email verification, please use the OTP below:</p>
        </div>
        <div style="padding: 20px; color: #01de60; text-align: center; font-weight: 600; font-size: 20px;">One-time Password
        </div>
        <div style="text-align: center; background-color: #f9f9f9; border-radius: 8px; color:black; margin-top: 10px; margin-bottom: 20px; justify-content: center; font-size: 45px; font-weight: 600; padding-top: 40px; padding-bottom: 40px; ">
            < #list otp?split("") as digit>
            <span style="margin-right: 5px;">${digit}</span>
        </#list>
    </div>
    <div style="font-size: 15px; margin-bottom: 25px; margin-top: 25px; text-align: center; line-height: 30px; color: #666666;">
        Kindly note that this OTP is temporary and will expire in few minutes. If you did not create an account with Event Booking
        Ltd., please disregard this email.
    </div>
    <div style="background-color: #ffffff; color: #666666; margin-bottom: 30px; margin-top: 40px;">
        <div style="margin-bottom: 9px;">Best regards,</div>
        <div>Event Booking Ltd.</div>
    </div>
</div>
</div>
</body>

        </html>