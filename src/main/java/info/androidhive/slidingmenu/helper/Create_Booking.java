package info.androidhive.slidingmenu.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

/**
 * Created by rer on 21.03.2015.
 */
public class Create_Booking extends AsyncTask<String, String, String> {

    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    

    String Booking_URL = "http://reitclub-hagen.info/connect/create_booking.php";
    String TAG_SUCCESS = "success";

    String event_id, person_id,spaces,comment,status,ticket_id;

    public Create_Booking (String event_id, String person_id, String spaces,
                           String comment, String status, String ticket_id)
    {
        this.event_id = event_id;
        this.person_id = person_id;
        this.spaces = spaces;
        this.comment = comment;
        this.status = status;
        this.ticket_id = ticket_id;

    }

    protected String doInBackground(String... args) {


        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("event_id", event_id));
        params.add(new BasicNameValuePair("person_id",person_id));
        params.add(new BasicNameValuePair("spaces",spaces));
        params.add(new BasicNameValuePair("comment",comment));
        params.add(new BasicNameValuePair("status",status));
        params.add(new BasicNameValuePair("ticket_id",ticket_id));




        JSONObject json = jsonParser.makeHttpRequest(Booking_URL, "POST", params);

        Log.d("Booking JSON: ", json.toString());  // Check your log cat for JSON reponse


        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                //UF.Toast("Reservierung erstellt");

            } else {
                //UF.Toast("Fehler");
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }

        return null;
    }




}
