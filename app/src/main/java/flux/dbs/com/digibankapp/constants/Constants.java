package flux.dbs.com.digibankapp.constants;

/**
 * Created by Prateek on 14-02-2018.
 */

public class Constants {
    public static final String DATABASE_NAME = "digihack2";
    public static final int DATABASE_VERSION = 1;
    public static final String USER_LOGIC_PREFERENCES_TABLE = "USER_LOGIN_PREF";
    public static final String USER_LOGIN_TABLE = "USER_LOGIN";
    public static final String USER_ACTIVITY_TRACKER_TABLE = "USER_ACTIVITY_TRACKER";
    public static final String USER_TRANSACTION_TABLE = "USER_TRANSACTION";
    public static final String DEALS_OFFERS_TABLE = "DEALS_OFFERS";
    public static final String BENEFICIARY_TABLE = "BENEFICIARIES";

    public static final String CREATE_USER_LOGIC_PREFERENCES_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_LOGIC_PREFERENCES_TABLE + "( " +
            "id INTEGER PRIMARY KEY, " +
            "user_name text," +
            "login_using_code int(1) default 0," +
            "login_using_finger_print int(1) default 0," +
            "login_using_password int(1) default 1)";

    public static final String CREATE_USER_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_LOGIN_TABLE + "( " +
            "id INTEGER PRIMARY KEY, " +
            "name text not null," +
            "user_name text not null," +
            "password text not null," +
            "email text not null," +
            "pin_login int(4) default 0)";

    public static final String CREATE_USER_ACTIVITY_TRACKER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_ACTIVITY_TRACKER_TABLE + "( " +
            "screen text not null," +
            "visits INTEGER default 0)";

    public static final String CREATE_USER_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TRANSACTION_TABLE + "( " +
            "txn_id text PRIMARY KEY, " +
            "txn_ref text not null," +
            "payee_name text not null," +
            "payee_acc text not null," +
            "amount text not null," +
            "txn_date text not null," +
            "category text not null)";

    public static final String CREATE_DEALS_OFFERS_TABLE = "CREATE TABLE IF NOT EXISTS " + DEALS_OFFERS_TABLE + "( " +
            "deals_offers text not null," +
            "category text not null)";

    public static final String CREATE_BENEFICIARY_TABLE = "CREATE TABLE IF NOT EXISTS " + BENEFICIARY_TABLE + "( " +
            "beneficiary_name text not null," +
            "acc_num text not null," +
            "vpa text not null)";

    public static final String GET_USER_ACTIVITY_FOR_SCREEN = "SELECT visits FROM " + USER_ACTIVITY_TRACKER_TABLE + " WHERE screen='%s'";

    public static final String GET_USER_ACTIVITY = "SELECT * FROM " + USER_ACTIVITY_TRACKER_TABLE + " ORDER BY visits DESC LIMIT 4";

    public static final String GET_USER_TRANSACTIONS = "SELECT * FROM " + USER_TRANSACTION_TABLE + " ORDER BY txn_date DESC";

    public static final String GET_DEALS_OFFERS = "select D.* from DEALS_OFFERS D " +
            "left join " +
            "(select count(*) cnt, T.category from USER_TRANSACTION T group by T.category order by cnt) T " +
            "on D.category = T.category " +
            "ORDER BY T.cnt DESC";

    public static final String GET_BENEFICIARY_VPA = "select vpa from " + BENEFICIARY_TABLE + " where beneficiary_name like '%s'";

    public static final String GET_BENEFICIARY_NAME = "select beneficiary_name from " + BENEFICIARY_TABLE + " where beneficiary_name like '%s'";

    public static final String INSERT_INTO_LOGIN_TABLE = "INSERT INTO" + USER_LOGIN_TABLE + "(name,user_name,password,email) VALUES('%s','%s','%s','%s')";

    public static final String UPI_LINK = "upi://pay?pa=%s&pn=%s&am=%s&tn=%s";
    public static final int CAMERA_INTENT_CODE = 1;
    public static final String DIGI_BANK_OTP_IDENTIFIER = "DIGIBANK DBS OTP: ";
    public static final String DIGI_BANK_OFFINE_BALANCE_IDENTIFIER = "DIGIBANK DBS : Current Balance In Account Ending With 7849 is INR 88,667.00";
    public static final String DIGI_BANK_OFFINE_OTP_IDENTIFIER = "DIGIBANK DBS : OTP Pin - ";
    public static final String DIGI_BANK_OFFINE_OTP_OK_IDENTIFIER = "DIGIBANK DBS : Offline OTP Verified Successfully";
    public static final String DIGI_BANK_OFFINE_OTP_NOT_OK_IDENTIFIER = "DIGIBANK DBS : Invalid OTP Offline PIN. One attempt left ";

    public static final String DIGI_BANK_OFFLINE_PAYMENT_RESPONSE = "DIGIBANK DBS : Payment successful for Amount %s send to Account Number %s";

    public static final int UPI_INTENT_CODE = 2;
}
