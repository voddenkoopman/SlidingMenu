package info.androidhive.slidingmenu.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.library.JSONParser;

/**
 * Created by rer on 19.03.2015.
 */


public class Insert_Historie extends AsyncTask<String, String, String> {

    String INSERT_HISTORY_URL = "http://reitclub-hagen.info/connect/create_historie.php";

    String user_login, booking_id, ticket_id;
    String artikelnummer, anz_ticket,beschreibung;
    String price;
    String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();

    //final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    public Insert_Historie(String user_login, String artikelnummer,String anz_ticket, String price,
                           String booking_id, String ticket_id, String beschreibung)
    {
        this.user_login = user_login;
        this.artikelnummer = artikelnummer;
        this.anz_ticket = anz_ticket;
        this.price = price;
        this.booking_id = booking_id;
        this.ticket_id = ticket_id;
        this.beschreibung = beschreibung;
    }

    protected String doInBackground(String... args) {



        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add (new BasicNameValuePair("user_login",user_login));
        params.add (new BasicNameValuePair("artnr", artikelnummer));
        params.add (new BasicNameValuePair("spaces",anz_ticket));
        params.add (new BasicNameValuePair("preis",price));
        params.add (new BasicNameValuePair("booking_id",booking_id));
        params.add (new BasicNameValuePair("ticket_id", ticket_id));
        params.add (new BasicNameValuePair("beschreibung", beschreibung));


        // getting JSON string from URL

        //JSONObject json2 = jsonParser.makeHttpRequest(INSERT_TICKET_URL, "POST", params);
        JSONObject json2 = jsonParser.makeHttpRequest(INSERT_HISTORY_URL, "POST", params);

        Log.d("Insert_Hist: ", json2.toString());  // Check your log cat for JSON reponse


        try {
            int success = json2.getInt(TAG_SUCCESS);

            if (success == 1) {
                // event gefunden
                //v.vibrate(200);
            } else {
                //v.vibrate(2000);
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }

        return null;
    }




}
