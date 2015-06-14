package info.androidhive.slidingmenu.helper;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.library.Cache;
import info.androidhive.slidingmenu.library.JSONParser;

/**
 * Created by rer on 23.04.2015.
 */
public class Call_Termine extends AsyncTask <String, String,String> {


    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> terminList  = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ListeCat1 = new ArrayList<HashMap<String, String>>() ;
    ArrayList<HashMap<String, String>> ListeCat2 = new ArrayList<HashMap<String, String>>() ;
    ArrayList<HashMap<String, String>> ListeCat3 = new ArrayList<HashMap<String, String>>() ;
    ArrayList<HashMap<String, String>> ListeCat4 = new ArrayList<HashMap<String, String>>() ;


    JSONArray kalenderbox = null;                                       // products JSONArray
    JSONParser jsonParser = new JSONParser();                       // Creating JSON Parser object

    String Kalender_URL = "http://reitclub-hagen.info/connect/get_kalender.php";

    // ALL JSON node names
    private static final String TAG_MESSAGES = "events";
    private static final String TAG_SUCCESS = "success";
    public static final String TAG_ROWID = "id";
    public static final String TAG_EVENT_NAME = "post_name";
    public static final String TAG_EVENT_CONTENT = "post_content";
    public static final String TAG_START_DATE = "StartDate";
    public static final String TAG_START_TIME = "StartTime";
    public static final String TAG_FINISH_TIME = "FinishTime";
    public static final String TAG_EVENT_CATEGORY_ID = "event_category_id";


    private static final String TAG = "Call_Termine";
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;


    private static final String DATABASE_NAME = "BS";
    private static final String SQLITE_TABLE = "Termine";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    TAG_ROWID + "," +
                    TAG_EVENT_NAME + "," +
                    TAG_EVENT_CONTENT + "," +
                    TAG_START_DATE + "," +
                    TAG_START_TIME + "," +
                    TAG_FINISH_TIME + "," +
                    TAG_EVENT_CATEGORY_ID +");";


    @Override
    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("pid",GV.getPid()));

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Kalender_URL, "GET",
                params);

        // Check your log cat for JSON reponse
        Log.d("Termine: ", json.toString());

        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1){
            kalenderbox = json.getJSONArray(TAG_MESSAGES);
            // looping through All messages
                for (int i = 0; i < kalenderbox.length(); i++) {
                JSONObject c = kalenderbox.getJSONObject(i);

                // Storing each json item in variable
                String id = c.getString(TAG_ROWID);
                String post_name = c.getString(TAG_EVENT_NAME);
                String post_content = c.getString(TAG_EVENT_CONTENT);
                String StartDate = c.getString(TAG_START_DATE);
                String StartTime = c.getString(TAG_START_TIME);
                String FinishTime = c.getString(TAG_FINISH_TIME);
                String event_category_id = c.getString(TAG_EVENT_CATEGORY_ID);


                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_ROWID, id);
                map.put(TAG_EVENT_NAME, post_name);
                map.put(TAG_EVENT_CONTENT, post_content);
                map.put(TAG_START_DATE, StartDate);
                map.put(TAG_START_TIME, StartTime);
                map.put(TAG_FINISH_TIME, FinishTime);
                map.put(TAG_EVENT_CATEGORY_ID, event_category_id);

                // adding HashList to ArrayList

                    int cat_id = 0;
                    try {
                        cat_id = Integer.valueOf(event_category_id);
                    }catch (Exception e){
                        //nothing
                    }

                    // nun teile die Daten den Listen zu
                    switch (cat_id){
                        case 0: terminList.add(map);break;

                        case 1: ListeCat1.add(map);break;

                        case 2: ListeCat2.add(map);break;

                        case 3: ListeCat3.add(map);break;

                        case 4: ListeCat4.add(map);break;

                    }
                // Daten werden in Datenbank geschrieben
                //createTermine(id,post_name,post_content,StartDate,StartTime,FinishTime,event_category_id);
                }
            }
            // Listen werden globalisiert
            Cache.setTerminList(terminList);
            Cache.setListeCat1(ListeCat1);
            Cache.setListeCat2(ListeCat2);
            Cache.setListeCat3(ListeCat3);
            Cache.setListeCat4(ListeCat4);

        } catch (JSONException e) {	e.printStackTrace();}
        catch (Exception ex) {ex.toString();}

        return null;
    }

//=============== Datenbank =================================================================
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

    public Call_Termine(Context ctx) {
        this.mCtx = ctx;
    }

    public Call_Termine open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createTermine(String id,String post_name, String post_content, String StartDate,
                              String StartTime, String FinishTime, String event_category_id) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(TAG_ROWID,id);
        initialValues.put(TAG_EVENT_NAME, post_name);
        initialValues.put(TAG_EVENT_CONTENT, post_content);
        initialValues.put(TAG_START_DATE, StartDate);
        initialValues.put(TAG_START_TIME, StartTime);
        initialValues.put(TAG_FINISH_TIME,FinishTime);
        initialValues.put(TAG_EVENT_CATEGORY_ID,event_category_id);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllTermine() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }


    public Cursor fetchTermineBy(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {TAG_ROWID,
                            TAG_EVENT_NAME, TAG_EVENT_CONTENT, TAG_START_DATE, TAG_START_TIME,
                            TAG_FINISH_TIME, TAG_EVENT_CATEGORY_ID},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {TAG_ROWID,
                            TAG_EVENT_NAME, TAG_EVENT_CONTENT, TAG_START_DATE, TAG_START_TIME,
                            TAG_FINISH_TIME, TAG_EVENT_CATEGORY_ID},
                    TAG_EVENT_CATEGORY_ID + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllTermine() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {TAG_ROWID,
                        TAG_EVENT_NAME, TAG_EVENT_CONTENT, TAG_START_DATE, TAG_START_TIME,
                        TAG_FINISH_TIME, TAG_EVENT_CATEGORY_ID},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
//*******************************************************************************************
}
