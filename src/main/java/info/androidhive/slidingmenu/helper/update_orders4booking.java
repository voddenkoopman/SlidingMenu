package info.androidhive.slidingmenu.helper;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.library.JSONParser;

/**
 * Created by rer on 20.03.2015.
 */
public class update_orders4booking extends AsyncTask<String, String, String> {
    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    String Update_orders4booking_URL = "http://reitclub-hagen.info/connect/update_order4booking.php";

    String ticket_id, status,event_id;
    String TAG_SUCCESS = "success";

    public update_orders4booking (String ticket_id,String status,String event_id)
    {
        this.ticket_id = ticket_id;
        this.status = status;
        this.event_id = event_id;
    }


    protected String doInBackground(String... args){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tid",ticket_id));
        params.add(new BasicNameValuePair("status",status));
        params.add(new BasicNameValuePair("eid",event_id));


        JSONObject json2 = jsonParser.makeHttpRequest(Update_orders4booking_URL, "POST", params);

        Log.d("Update Order4Book: ", json2.toString());  // Check your log cat for JSON reponse

        try {
            int success = json2.getInt(TAG_SUCCESS);

            if (success == 1) {
                //Toast("Reservierung erstellt");

            } else {
                //Toast("Fehler");
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }

        return null;
    }


//**************************************************
}



