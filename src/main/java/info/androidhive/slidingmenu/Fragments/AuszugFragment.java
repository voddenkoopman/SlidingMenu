package info.androidhive.slidingmenu.Fragments;
/*
*Dieses Fragment zeigt die Historie an
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class AuszugFragment extends Fragment {
	public AuszugFragment(){}

    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    ArrayList<HashMap<String, String>> auszugList;
    JSONArray auszugbox = null;                             // products JSONArray

    ListView prd;
    UserFunctions UF;
    GlobalClass GV; AlertDialog alertbox = null;

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();
    }

    //GV.getDomain();
    String domain = "reitclub-hagen.info";
    String seite = "/connect";
    Integer value_spaces;

    String AUSZUG_URL = "http://" + domain + seite + "/get_all_historie-2-V1.php";
    String INSERT_URL = "http://" + domain + seite +"/create_historie.php";

    String TAG_MESSAGES = "historie"; String TAG_SUCCESS = "success";
    String TAG_booking_date = "booking_date";
    String TAG_user_login = "user_login";
    String TAG_artnr = "artnr";
    String TAG_booking_id = "booking_id";
    String TAG_ticket_id = "ticket_id";
    String TAG_spaces = "spaces";
    String TAG_preis = "preis";
    String TAG_beschreibung = "beschreibung";




    //-------------------------------------------------------------------------------------------------
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_pay, container, false);

        prd = (ListView)rootView.findViewById(R.id.list);
        auszugList = new ArrayList<HashMap<String, String>>();   // Hashmap for ListView

        GV = (GlobalClass)getActivity().getApplicationContext();
        UF = new UserFunctions(getActivity());

        new Load_Kontoauszuege().execute();
        // Hier werden die Daten abgeholt und angezeigt

         
        return rootView;
    }
//#################################################################################################
    class Load_Kontoauszuege extends AsyncTask<String, String, String> {
    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_login", GV.getUser_login()));

        //Starte JSON
        JSONObject json = jsonParser.makeHttpRequest(AUSZUG_URL, "GET", params);

        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // event gefunden
                auszugbox = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < auszugbox.length(); i++) {
                    JSONObject c = auszugbox.getJSONObject(i);

                    // Storing each json item in variable
                    String booking_date = c.getString(TAG_booking_date);
                    String artnr = c.getString(TAG_artnr);
                    String booking_id = c.getString(TAG_booking_id);
                    String ticket_id = c.getString(TAG_ticket_id);
                    String spaces = c.getString(TAG_spaces);
                    String preis = c.getString(TAG_preis);
                    String beschreibung = c.getString(TAG_beschreibung);



                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_booking_date, booking_date);
                    map.put(TAG_artnr, artnr);
                    map.put(TAG_booking_id, booking_id);
                    map.put(TAG_ticket_id, ticket_id);
                    map.put(TAG_spaces, spaces);
                    map.put(TAG_preis, preis);
                    map.put(TAG_beschreibung, beschreibung);

                    // adding HashList to ArrayList;
                    auszugList.add(map);
                }
            } else {
                Toast("Keine Auszüge gefunden");
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.getMessage();
        }



    return null;
    }

    protected void onPostExecute(String file_url){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), auszugList,
                        R.layout.fragment_pay_list_item, new String[]
                        {TAG_booking_date, TAG_beschreibung,TAG_spaces, TAG_preis},
                        new int[]
                                {R.id.booking_date, R.id.beschreibung,R.id.spaces,R.id.preis});

                //value_spaces = Integer.parseInt(TAG_spaces);
                //if (value_spaces < 0) {} else {}

                prd.setAdapter(adapter);
            }
        });


    }


    }



//*************************************************************************************************
}
