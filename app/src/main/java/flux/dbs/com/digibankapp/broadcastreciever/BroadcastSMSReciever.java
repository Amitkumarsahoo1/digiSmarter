package flux.dbs.com.digibankapp.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Prateek on 14-02-2018.
 */
import flux.dbs.com.digibankapp.otp.OTPAuthService;
import flux.dbs.com.digibankapp.otp.SMSService;

import static flux.dbs.com.digibankapp.constants.Constants.*;

public class BroadcastSMSReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null) {
            // Retrieve the SMS.
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);

            String phoneNumber = msgs[0].getOriginatingAddress();
            String message = msgs[0].getMessageBody().toString();

            if(message.trim().startsWith(DIGI_BANK_OTP_IDENTIFIER)) {
                String phoneNumberWithoutISDCode = phoneNumber.substring(3).trim();
                String otp = message.substring(DIGI_BANK_OTP_IDENTIFIER.length()).trim();
                if(OTPAuthService.verifyOTP(phoneNumber,otp)) {

                } else {

                }
            }


        }
    }
}
