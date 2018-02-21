package flux.dbs.com.digibankapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import flux.dbs.com.digibankapp.login.LoginType;
import flux.dbs.com.digibankapp.utils.RandomString;

import static flux.dbs.com.digibankapp.constants.Constants.GET_BENEFICIARY_NAME;
import static flux.dbs.com.digibankapp.constants.Constants.GET_BENEFICIARY_VPA;
import static flux.dbs.com.digibankapp.constants.Constants.GET_DEALS_OFFERS;
import static flux.dbs.com.digibankapp.constants.Constants.GET_USER_ACTIVITY;
import static flux.dbs.com.digibankapp.constants.Constants.GET_USER_ACTIVITY_FOR_SCREEN;
import static flux.dbs.com.digibankapp.constants.Constants.GET_USER_TRANSACTIONS;
import static flux.dbs.com.digibankapp.constants.Constants.USER_ACTIVITY_TRACKER_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.USER_LOGIC_PREFERENCES_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.USER_LOGIN_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.USER_TRANSACTION_TABLE;

/**
 * Created by Prateek on 15-02-2018.
 */

public class DBCRUDHelper {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private RandomString randomString = new RandomString(10);
    private RandomString randomInteger = new RandomString(10,"0123456789");

    public DBCRUDHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }


    public LoginType getLoginType(String username) {
        username = username.trim().toLowerCase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from " + USER_LOGIC_PREFERENCES_TABLE + " where user_name = '" + username + "' LIMIT 1", null);
        if (c != null) {
            if (c.moveToFirst()) {
                if(c.getInt(3) == 1) {
                    return LoginType.PIN;
                } else if(c.getInt(4) == 1) {
                    return LoginType.FingerPrint;
                }
                //Login using password
            }
        }
        return LoginType.Password;
    }

    public Cursor getUserTableCusrsor(String userName) {
        String userQuery = "Select * from " + USER_LOGIN_TABLE + " where user_name = '" + userName.trim().toLowerCase()+"' LIMIT 1";
        return sqLiteDatabase.rawQuery(userQuery, null);
    }

    public boolean checkIfUserExistsInDatabase(String userName) {
        String userQuery = "Select * from " + USER_LOGIN_TABLE + " where trim(lower(user_name)) = '" + userName.trim().toLowerCase()+"' LIMIT 1";
        Cursor c = sqLiteDatabase.rawQuery(userQuery, null);
        if(c != null) {
            if (c.moveToFirst()) {
                c.close();
               return true;
            }
            c.close();
        }
        return false;
    }

    public boolean checkIfUserWithPasswordExistsInDatabase(String userName, String password) {
        String userQuery = "Select * from " + USER_LOGIN_TABLE + " where trim(lower(user_name)) = '" + userName.trim().toLowerCase()+"' and password='"+password+"' LIMIT 1";
        Cursor c = sqLiteDatabase.rawQuery(userQuery, null);
        if(c != null) {
            if (c.moveToFirst()) {
                c.close();
                return true;
            }
            c.close();
        }
        return false;
    }

    public void addUserToUserTable(String name,String username,String password, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name.trim());
        contentValues.put("user_name",username.trim().toLowerCase());
        contentValues.put("password",password.trim());
        contentValues.put("email",email.trim());

        sqLiteDatabase.insert(USER_LOGIN_TABLE, null, contentValues);
    }

    public void addUserActivity(String screen) {
        String selectStmt = String.format(GET_USER_ACTIVITY_FOR_SCREEN, screen);
        Cursor c = sqLiteDatabase.rawQuery(selectStmt, null);
        int count = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("visits", ++count);

        if (sqLiteDatabase.update(USER_ACTIVITY_TRACKER_TABLE, contentValues, "screen=?", new String[] {screen}) == 0) {
            contentValues.put("screen", screen);
            sqLiteDatabase.insert(USER_ACTIVITY_TRACKER_TABLE, null, contentValues);
        }
    }

    public String getUserActivity() {
        String selectStmt = String.format(GET_USER_ACTIVITY);
        Cursor c = sqLiteDatabase.rawQuery(selectStmt, null);
        List<Map<String, String>> rows = new ArrayList<>();
        if (c != null && c.isBeforeFirst()) {
            while (c.moveToNext()) {
                Map<String, String> column = new LinkedHashMap<>();
                column.put("screen", c.getString(0));
                column.put("visits", String.valueOf(c.getInt(1)));
                rows.add(column);
            }
            c.close();
        }
        JSONArray jsonArray = new JSONArray(rows);
        return jsonArray.toString();
    }

    public void addUserTXN(String payeeName, String payeeAcc, String amount, String category) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        ContentValues contentValues = new ContentValues();
        String txnId = randomInteger.nextString().replace("-", "");
        Log.i("DBCRUDHelper", "TXN_ID: " + txnId);
        contentValues.put("txn_id", txnId);
        contentValues.put("txn_ref", randomString.nextString());
        contentValues.put("payee_name", payeeName);
        contentValues.put("payee_acc", payeeAcc);
        contentValues.put("amount", amount);
        contentValues.put("txn_date", dateFormat.format(new Date()));
        contentValues.put("category", category);

        sqLiteDatabase.insert(USER_TRANSACTION_TABLE, null, contentValues);
    }

    public String getUserTransactions() {
        String selectStmt = String.format(GET_USER_TRANSACTIONS);
        Cursor c = sqLiteDatabase.rawQuery(selectStmt, null);
        List<Map<String, String>> rows = new ArrayList<>();
        if (c != null && c.isBeforeFirst()) {
            while (c.moveToNext()) {
                Map<String, String> column = new LinkedHashMap<>();
                column.put("txn_id", c.getString(0));
                column.put("txn_ref", c.getString(1));
                column.put("payee_name", c.getString(2));
                column.put("payee_acc", c.getString(3));
                column.put("amount", c.getString(4));
                column.put("txn_date", c.getString(5));
                column.put("category", c.getString(6));
                rows.add(column);
            }
            c.close();
        }
        JSONArray jsonArray = new JSONArray(rows);
        return jsonArray.toString();
    }

    public String getDealsOffers() {
        Cursor c = sqLiteDatabase.rawQuery(GET_DEALS_OFFERS, null);
        List<Map<String, String>> rows = new ArrayList<>();
        if (c != null && c.isBeforeFirst()) {
            while (c.moveToNext()) {
                Map<String, String> column = new LinkedHashMap<>();
                column.put("deals_offers", c.getString(0));
                column.put("category", c.getString(1));
                rows.add(column);
            }
            c.close();
        }
        JSONArray jsonArray = new JSONArray(rows);
        return jsonArray.toString();
    }

    public String getBeneficiaryVPA(String beneficiary) {
        String selectStmt = String.format(GET_BENEFICIARY_VPA, "%" +beneficiary + "%");
        Cursor c = sqLiteDatabase.rawQuery(selectStmt, null);
        if (c != null && c.moveToFirst()) {
            String vpa = c.getString(0);
            c.close();
            return vpa;
        }
        return "";
    }

    public String getBeneficiaryName(String beneficiary) {
        String selectStmt = String.format(GET_BENEFICIARY_NAME, "%" +beneficiary + "%");
        Cursor c = sqLiteDatabase.rawQuery(selectStmt, null);
        if (c != null && c.moveToFirst()) {
            String name = c.getString(0);
            c.close();
            return name;
        }
        return "";
    }

}
