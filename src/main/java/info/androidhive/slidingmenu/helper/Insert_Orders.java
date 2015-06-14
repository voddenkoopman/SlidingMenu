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

/**
 * Created by rer on 20.03.2015.
 */
public class Insert_Orders extends AsyncTask<String, String, String> {
    JSONParser jsonParser = new JSONParser();
    String INSERT_TICKET_URL = "http://reitclub-hagen.info/connect/create_orders.php";

    String TAG_SUCCESS = "success";
    String user_login, artnr, sku, price,titel,status;

    public Insert_Orders (String user_login, String artnr,String sku, String price, String titel,String status)
    {
        this.user_login = user_login;
        this.artnr = artnr;
        this.sku = sku;
        this.price = price;
        this.titel = titel;
        this.status = status;
    }


    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_login", user_login));
        params.add(new BasicNameValuePair("artnr", artnr));
        params.add(new BasicNameValuePair("sku", sku));
        params.add(new BasicNameValuePair("price", price));
        params.add(new BasicNameValuePair("titel", titel));
        params.add(new BasicNameValuePair("status", status));


        // getting JSON string from URL
        //JSONObject json2 = jsonParser.makeHttpRequest(Event_zaehle_url, "GET", params);

        JSONObject json = jsonParser.makeHttpRequest(INSERT_TICKET_URL, "POST", params);

        Log.d("Insert_Orders: ", json.toString());  // Check your log cat for JSON reponse


        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // event gefunden
                //Toast("Buchung erfolgreich");
            } else {
                //Toast("Fehler #2:214");
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return null;
    }
}
