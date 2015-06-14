package info.androidhive.slidingmenu.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by rer on 03.06.2015.
 */
public class UserDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_LOGIN = "user_login";
    public static final String KEY_DISPLAY_NAME = "display_name";
    public static final String KEY_ONEID = "oneid_key";
    public static final String KEY_WOO = "woo_key";

    private static final String TAG = "CountriesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "BuchungsSystem";
    private static final String SQLITE_TABLE = "User";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_USER_EMAIL + "," +
                    KEY_USER_LOGIN + "," +
                    KEY_DISPLAY_NAME + "," +
                    KEY_ONEID + "," +
                    KEY_WOO +");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public UserDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public UserDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createUser(String id,String user_email, String user_login,
                              String display_name, String oneid_key, String woo_key) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_USER_EMAIL, user_email);
        initialValues.put(KEY_USER_LOGIN, user_login);
        initialValues.put(KEY_DISPLAY_NAME, display_name);
        initialValues.put(KEY_ONEID, oneid_key);
        initialValues.put(KEY_WOO, woo_key);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllUser() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchUserByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_USER_EMAIL, KEY_USER_LOGIN, KEY_DISPLAY_NAME, KEY_ONEID, KEY_WOO},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_USER_EMAIL, KEY_USER_LOGIN, KEY_DISPLAY_NAME, KEY_ONEID, KEY_WOO},
                    KEY_DISPLAY_NAME + " like '%" + inputText + "%'", null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllUser() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_USER_EMAIL, KEY_USER_LOGIN, KEY_DISPLAY_NAME, KEY_ONEID, KEY_WOO},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

//##############################################################################################
}
