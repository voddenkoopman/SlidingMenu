package info.androidhive.slidingmenu.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

/**
 * Created by rer on 12.05.2015.
 */
public class Insert_Payments extends AsyncTask<String, String, String> {

    GlobalClass GV;
    UserFunctions UF;
    JSONParser jsonParser = new JSONParser();

    String TAG_SUCCESS = "success";
    String INSERT_PAYMENT_URL = "http://reitclub-hagen.info/connect/create_zahlung.php";

    String prefix,empfaenger_id, sender_id, booking_id,verwendungszweck, betrag;


    public Insert_Payments(String prefix,String empfaenger_id,String sender_id,String booking_id,
                           String verwendungszweck,String betrag) {
        this.prefix = prefix;
        this.empfaenger_id = empfaenger_id;
        this.sender_id = sender_id;
        this.booking_id =booking_id;
        this.verwendungszweck = verwendungszweck;
        this.betrag = betrag;

    }


    protected String doInBackground(String... args) {


        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("prefix", prefix));
        params.add(new BasicNameValuePair("empfaenger_id", empfaenger_id));
        params.add(new BasicNameValuePair("sender_id", sender_id));
        params.add(new BasicNameValuePair("booking_id", booking_id));
        params.add(new BasicNameValuePair("verwendungszweck", verwendungszweck));
        params.add(new BasicNameValuePair("betrag", betrag));


        // getting JSON string from URL

        //JSONObject json2 = jsonParser.makeHttpRequest(INSERT_TICKET_URL, "POST", params);
        JSONObject json2 = jsonParser.makeHttpRequest(INSERT_PAYMENT_URL, "POST", params);


        Log.d("Insert_Pay: ", json2.toString());  // Check your log cat for JSON reponse
        return null;
    }

}





