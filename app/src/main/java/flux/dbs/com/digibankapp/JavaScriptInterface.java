package flux.dbs.com.digibankapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import flux.dbs.com.digibankapp.db.DBCRUDHelper;
import flux.dbs.com.digibankapp.login.LoginType;
import flux.dbs.com.digibankapp.otp.OTPAuthService;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static flux.dbs.com.digibankapp.constants.Constants.CAMERA_INTENT_CODE;
import static flux.dbs.com.digibankapp.constants.Constants.UPI_INTENT_CODE;
import static flux.dbs.com.digibankapp.constants.Constants.UPI_LINK;

/**
 * @author Prateek
 */
public class JavaScriptInterface implements FingerprintHandler.FingerPrintAuthListener {

    private Context mContext;
    private WebView mWebView;
    private MainActivity activity;
    private static final String KEY_NAME = "example_key";
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private DBCRUDHelper dbcrudHelper;

    public static final int PERMISSION_REQUEST_READ_CONTACTS = 100;
    public  static String MOBILE_NUMBER = "8208908398";

    public JavaScriptInterface(Context context, WebView webView, MainActivity activity) {
        this.mContext = context;
        this.mWebView = webView;
        this.activity = activity;
        dbcrudHelper = new DBCRUDHelper(context);

    }

    @JavascriptInterface
    public void callFingerPrint() {

        showFingerPrintAlertDialog();

    }

    @JavascriptInterface
    public void signUp(String name, String userName, String password, String email) {
        if (null == userName || userName.length() == 0 || null == password || password.length() == 0 || null == email || email.length() == 0 ) {
            makeLongToast("Please enter all required fields");
            return;
        }

        Cursor c = dbcrudHelper.getUserTableCusrsor(userName);
        if (dbcrudHelper.checkIfUserExistsInDatabase(userName)) {
            makeLongToast("User with username " + userName + " already exists.");
            return;
        }

        dbcrudHelper.addUserToUserTable(name, userName, password, email);
        Toast.makeText(mContext, "Registration Successful. Please login", Toast.LENGTH_LONG);
        launchNextPage("login",1);
        c.close();
    }

    protected void init() {

    }

    private void makeLongToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void redirectToLogin() {
        launchNextPage("login", 1);
    }

    @JavascriptInterface
    public void loadSignUpPage() {
        launchNextPage("signup", 1);
    }


    private void init(final Dialog dialog) {

        keyguardManager =
                (KeyguardManager) mContext.getSystemService(KEYGUARD_SERVICE);
        fingerprintManager =
                (FingerprintManager) mContext.getSystemService(FINGERPRINT_SERVICE);

        if (!keyguardManager.isKeyguardSecure()) {

            Toast.makeText(mContext,
                    "Lock screen security not enabled in Settings",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext,
                    "Fingerprint authentication permission not enabled",
                    Toast.LENGTH_LONG).show();

            return;
        }
        if (!fingerprintManager.hasEnrolledFingerprints()) {

            // This happens when no fingerprints are registered.
            Toast.makeText(mContext,
                    "Register at least one fingerprint in Settings",
                    Toast.LENGTH_LONG).show();
            return;
        }
        generateKey();

        if (cipherInit()) {
            cryptoObject =
                    new FingerprintManager.CryptoObject(cipher);
            FingerprintHandler helper = new FingerprintHandler(mContext, this);
            helper.startAuth(fingerprintManager, cryptoObject);
        }
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void loadAllOptions() {
        launchNextPage("alloptions",1);
    }
    @JavascriptInterface
    public int loginUserWithPassword(String userName, String password) throws  InterruptedException {
        if(! isNetworkAvailable()) {
           // showOfflineMode();
            showOfflineMode();
        } else {



            if (dbcrudHelper.checkIfUserWithPasswordExistsInDatabase(userName, password)) {
                launchNextPage("landingpage", 1);
                return 1;
            } else {
                makeLongToast("Invalid Credentials !");
                return 0;

            }
        }
        return 0;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
        if(null == networkInfo) {
            return false;
        }
        return networkInfo.isConnectedOrConnecting();
    }

    @JavascriptInterface
    public void showAlertDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showFingerPrintAlertDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fingerprint_alert);

        Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancel);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        init(dialog);


    }

    @JavascriptInterface
    public void showProgressBarLogin() {
    }

    public void showOfflineMode() throws InterruptedException {
        final Dialog offlineDialog = new Dialog(activity);
        offlineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offlineDialog.setCancelable(false);
        offlineDialog.setContentView(R.layout.offline_la);

        Button cancelDialog = (Button) offlineDialog.findViewById(R.id.cancelOfflineLogin);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {offlineDialog.dismiss();
            }
        });

        Button offlineLoginButton = (Button) offlineDialog.findViewById(R.id.okayOfflineLogin);
        offlineLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                /*OTPAuthService.sendOTPSMS(activity, mContext, MOBILE_NUMBER);
                sleepThread(3000);
                makeLongToast("Otp Verified");
                showFingerPrintAlertDialog();
                launchNextPage("offlinepayment",1);
                if(OTP_OFFLINE.equals(ACTUAL_OTP_OFFLINE)) {
                    offlineDialog.dismiss();
                }*/
                offlineDialog.dismiss();
                showOfflinePin();

            }
        });

        offlineDialog.show();
    }

    private boolean showOfflinePin() {
        final Dialog offlineDialog = new Dialog(activity);
        offlineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offlineDialog.setCancelable(false);
        offlineDialog.setContentView(R.layout.offline_pin);

        Button cancelDialog = (Button) offlineDialog.findViewById(R.id.cancelOfflineOTP);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {offlineDialog.dismiss();
            }
        });


        Button offlineLoginButton = (Button) offlineDialog.findViewById(R.id.OkayOfflineOtp);



        offlineLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                TextView textView = offlineDialog.findViewById(R.id.otpNumberOffline);
                final String pin = textView.getText().toString();

                OTPAuthService.sendOfflinePin(activity, mContext, MOBILE_NUMBER, pin);
                sleepThread(3000);

                if(null == pin || pin.length() !=4) {
                    makeLongToast("Please Enter valid 4 digit Pin");

                } else {
                    OTP_OFFLINE = pin;
                    if(ACTUAL_OTP_OFFLINE.equals(OTP_OFFLINE)) {
                        OTPAuthService.sendOTPOkayMsg(activity, mContext, MOBILE_NUMBER);
                        sleepThread(2000);
                        offlineDialog.dismiss();
                        launchNextPage("offlinepayment",1);

                    } else {
                        OTPAuthService.sendOTPNotOkayMsg(activity, mContext, MOBILE_NUMBER);
                    }
                }




            }
        });

        offlineDialog.show();
        return false;
    }
    public static String OTP_OFFLINE = "";
    public static String ACTUAL_OTP_OFFLINE = "2537";
    private void sleepThread(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void launchNextPage(final String screen, final int type) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {

                mWebView.loadUrl("file:///android_asset/" + screen + ".html");
                /*switch (type) {
                    case 1:

                    case 2:
                        mWebView.loadUrl(screen);
                        break;
                }*/
            }
        });
    }


    @Override
    public void success() {

    }

    @Override
    public void failure() {

    }

    //@JavascriptInterface
    public void loadChatBot() {
        launchNextPage("http://pure-beach-92745.herokuapp.com", 2);
    }

    @JavascriptInterface
    public void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE);
        }
    }


    @JavascriptInterface
    public String readSms() {
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Cursor cur = mContext.getContentResolver().query(uriSMSURI, null, null, null, null);
            String sms = "";
            while (cur.moveToNext()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                sms += "From :" + cur.getString(index_Address) + " : " + cur.getString(index_Date) + "\n";
            }
            //Log.e("renuka",sms);
            return sms;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_READ_CONTACTS);
            return "";
        }
    }


    protected boolean checkIfFingerPrintAvailableAndConfigured(String username) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                Log.w("FingerPrintStatus", "Finger Print not supported");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Log.w("FingerPrintStatus", "Finger Print not configured for mobile");
            } else {
                LoginType loginType = dbcrudHelper.getLoginType(username);
                return loginType == LoginType.FingerPrint;
            }
        }
        return false;
    }

    @JavascriptInterface
    public int fetchBalance() {
        OTPAuthService.sendOfflineOTPBalance(activity,mContext, MOBILE_NUMBER);
        return 88667;
    }

    @JavascriptInterface
    public void transferOfflineMoney(String accountNumber,String amount) {

        OTPAuthService.sendOfflinePayment(activity,mContext, MOBILE_NUMBER, amount, accountNumber);
        makeLongToast("Payment Successful !");
        launchNextPage("offlinepayment",1);
    }
    @JavascriptInterface
    public void upiPay(String payeeName, String amount) {
        String vpa = getBeneficiaryVPA(payeeName);
        String name = getBeneficiaryName(payeeName);
        String note = "";
        String UPI = String.format(UPI_LINK, vpa, name, amount, note).replace(" ", "%20");
        Intent intent = new Intent();
        intent.setData(Uri.parse(UPI));
        intent.setAction(Intent.ACTION_VIEW);
        Intent chooser = Intent.createChooser(intent, "Pay with...");
        activity.startActivityForResult(chooser, UPI_INTENT_CODE);
    }

    @JavascriptInterface
    public void callJavaScriptFunction(final String functionName, final String message) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:" + functionName + "('" + message + "');");
            }
        });
    }

    @JavascriptInterface
    public void
    addUserActivity(String screen) {
        dbcrudHelper.addUserActivity(screen);
    }

    @JavascriptInterface
    public String getUserActivity() {
        return dbcrudHelper.getUserActivity();
    }

    @JavascriptInterface
    public void addUserTxn(String payeeName, String payeeAcc, String amount, String category) {
        dbcrudHelper.addUserTXN(payeeName, payeeAcc, amount, category);
    }

    @JavascriptInterface
    public String getUserTxns() {
        return dbcrudHelper.getUserTransactions();
    }

    @JavascriptInterface
    public String getDealsOffers() {
        String dealsOffers = dbcrudHelper.getDealsOffers();
        Log.i("DEALS_OFFERS", dealsOffers);
        return dealsOffers;
    }

    @JavascriptInterface
    public String getBeneficiaryVPA(String beneficiary) {
        String vpa = dbcrudHelper.getBeneficiaryVPA(beneficiary);
        Log.i("BENEFICIARY_VPA", vpa);
        return vpa;
    }

    @JavascriptInterface
    public String getBeneficiaryName(String beneficiary) {
        String name = dbcrudHelper.getBeneficiaryName(beneficiary);
        Log.i("BENEFICIARY_VPA", name);
        return name;
    }

    @JavascriptInterface
    public void sendSMS(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);
    }
}
