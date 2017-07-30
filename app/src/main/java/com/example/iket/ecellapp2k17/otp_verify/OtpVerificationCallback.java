package com.example.iket.ecellapp2k17.otp_verify;

import com.example.iket.ecellapp2k17.otp_verify.model.OtpData;

/**
 * Created by samveg on 30/7/17.
 */

public interface OtpVerificationCallback {

    void onOtpVerified(OtpData otpData);
    void onFailure(String error);
}
