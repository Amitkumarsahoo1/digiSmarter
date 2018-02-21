package flux.dbs.com.digibankapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static flux.dbs.com.digibankapp.constants.Constants.BENEFICIARY_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_BENEFICIARY_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_DEALS_OFFERS_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_USER_ACTIVITY_TRACKER_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_USER_LOGIC_PREFERENCES_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_USER_LOGIN_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.CREATE_USER_TRANSACTION_TABLE;
import static flux.dbs.com.digibankapp.constants.Constants.DATABASE_NAME;
import static flux.dbs.com.digibankapp.constants.Constants.DATABASE_VERSION;
import static flux.dbs.com.digibankapp.constants.Constants.DEALS_OFFERS_TABLE;

/**
 * Created by Prateek on 14-02-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        sqLiteDatabase.execSQL(CREATE_USER_LOGIC_PREFERENCES_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_LOGIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_ACTIVITY_TRACKER_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TRANSACTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_DEALS_OFFERS_TABLE);
        addDealsOffers();
        sqLiteDatabase.execSQL(CREATE_BENEFICIARY_TABLE);
        addBeneficiaries();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void addDealsOffers() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("deals_offers", "5% off on flight booking");
        contentValues.put("category", "Travel");
        sqLiteDatabase.insert(DEALS_OFFERS_TABLE, null, contentValues);

        contentValues.put("deals_offers", "10% off on Dining");
        contentValues.put("category", "Food");
        sqLiteDatabase.insert(DEALS_OFFERS_TABLE, null, contentValues);
    }

    private void addBeneficiaries() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("beneficiary_name", "Ashhadul Islam");
        contentValues.put("acc_num", "1231809890");
        contentValues.put("vpa", "ashhadulislam@okaxis");
        sqLiteDatabase.insert(BENEFICIARY_TABLE, null, contentValues);

        contentValues.put("beneficiary_name", "Amar Singh");
        contentValues.put("acc_num", "1231876890");
        contentValues.put("vpa", "rathore.amrsingh@okhdfcbank");
        sqLiteDatabase.insert(BENEFICIARY_TABLE, null, contentValues);

        contentValues.put("beneficiary_name", "Prateek Mishra");
        contentValues.put("acc_num", "3424324556");
        contentValues.put("vpa", "prateek18@dbs");
        sqLiteDatabase.insert(BENEFICIARY_TABLE, null, contentValues);

        contentValues.put("beneficiary_name", "Ankur Marchattiwar");
        contentValues.put("acc_num", "3424324556");
        contentValues.put("vpa", "ankur.mcw@dbs");
        sqLiteDatabase.insert(BENEFICIARY_TABLE, null, contentValues);
    }

}
