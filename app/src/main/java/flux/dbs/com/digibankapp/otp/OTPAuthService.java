package flux.dbs.com.digibankapp.otp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OFFINE_BALANCE_IDENTIFIER;
import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OFFINE_OTP_IDENTIFIER;
import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OFFINE_OTP_NOT_OK_IDENTIFIER;
import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OFFINE_OTP_OK_IDENTIFIER;
import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OFFLINE_PAYMENT_RESPONSE;
import static flux.dbs.com.digibankapp.constants.Constants.DIGI_BANK_OTP_IDENTIFIER;

/**
 * Created by Prateek on 16-02-2018.
 */

public class OTPAuthService {

    public static String generateOTP(String mobileNumber){
        return getOTPSequence(mobileNumber.trim(),getAlgoToUse());
    }

    public static boolean verifyOTP(String mobileNumber, String otp){
        int algo = getAlgoToUse();
        Log.w("Verify_ALGO", algo+"");
        Log.w("INPUT_OTP", otp);
        String generatedOTP = getOTPSequence(mobileNumber.trim(), algo);
        Log.w("generated_otp", generatedOTP);
        return generatedOTP.equals(otp);
    }

    private static int whichAlgo = 1;
    public static void sendOTPSMS(Activity activity, Context context,String phoneNumber) {
        requestSENDSMS(activity, context);
        setAlgoToUse();
        SmsManager sms = SmsManager.getDefault();
        String otp = OTPAuthService.generateOTP(phoneNumber);
        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OTP_IDENTIFIER + otp , null, null);
    }

    public static void sendOTPOkayMsg(Activity activity, Context context,String phoneNumber) {
        requestSENDSMS(activity, context);
        setAlgoToUse();
        SmsManager sms = SmsManager.getDefault();
        String otp = OTPAuthService.generateOTP(phoneNumber);
        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OFFINE_OTP_OK_IDENTIFIER + otp , null, null);
    }

    public static void sendOTPNotOkayMsg(Activity activity, Context context,String phoneNumber) {
        requestSENDSMS(activity, context);
        setAlgoToUse();
        SmsManager sms = SmsManager.getDefault();
        String otp = OTPAuthService.generateOTP(phoneNumber);
        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OFFINE_OTP_NOT_OK_IDENTIFIER + otp , null, null);
    }

    public static void sendOfflinePin(Activity activity, Context context,String phoneNumber, String otp) {


        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OFFINE_OTP_IDENTIFIER + otp , null, null);
    }

    public static void sendOtpOkayResponse(Activity activity, Context context,String phoneNumber, String otp) {


        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OFFINE_OTP_IDENTIFIER + otp , null, null);
    }

    public static void sendOfflineOTPBalance(Activity activity, Context context,String phoneNumber) {
        setAlgoToUse();
        SmsManager sms = SmsManager.getDefault();
        String otp = OTPAuthService.generateOTP(phoneNumber);
        sms.sendTextMessage(phoneNumber, null, DIGI_BANK_OFFINE_BALANCE_IDENTIFIER , null, null);
    }

    public static void sendOfflinePayment(Activity activity, Context context,String phoneNumber, String amount, String accountNumber) {


        setAlgoToUse();
        SmsManager sms = SmsManager.getDefault();
        String otp = OTPAuthService.generateOTP(phoneNumber);
        sms.sendTextMessage(phoneNumber, null, String.format(DIGI_BANK_OFFLINE_PAYMENT_RESPONSE,amount, accountNumber) , null, null);

    }

    public static void requestSENDSMS(Activity activity, Context context) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.READ_PHONE_STATE
        }, 190);

        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.SEND_SMS
        }, 140);

        int sendSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if( sendSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.SEND_SMS
            }, 140);
        }
    }

    public static void setAlgoToUse() {
        Random r = new Random();
        int Low = 1;
        int High = 6;
        int algo = r.nextInt(High-Low) + Low;
        OTPAuthService.whichAlgo = algo;
    }

    public static int getAlgoToUse (){
        return whichAlgo;
    }

    private static String getOTPSequence(String mobileNumber, int whichAlgo) {
        String otp;
        switch (whichAlgo) {
            case 1:
                otp = ""+mobileNumber.charAt(1) + mobileNumber.charAt(5) + mobileNumber.charAt(8) + mobileNumber.charAt(0);
                break;

            case 2:
                otp = ""+mobileNumber.charAt(0) + mobileNumber.charAt(5) + mobileNumber.charAt(0) + mobileNumber.charAt(7);
                break;

            case 3:
                otp = ""+mobileNumber.charAt(2) + mobileNumber.charAt(6) + mobileNumber.charAt(4) + mobileNumber.charAt(8);
                break;
            case 4:
                otp = ""+mobileNumber.charAt(2) + mobileNumber.charAt(6) + mobileNumber.charAt(4) + mobileNumber.charAt(4);
                break;
            case 5:
                otp = ""+mobileNumber.charAt(5) + mobileNumber.charAt(2) + mobileNumber.charAt(3) + mobileNumber.charAt(9);
                break;
            default:
                otp = ""+mobileNumber.charAt(9) + mobileNumber.charAt(3) + mobileNumber.charAt(8) + mobileNumber.charAt(6);
                break;
        }
        return otp;
    }

}
