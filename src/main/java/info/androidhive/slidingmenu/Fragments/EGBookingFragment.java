package info.androidhive.slidingmenu.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.WalletActivity;
import info.androidhive.slidingmenu.helper.update_orders4booking;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class EGBookingFragment extends Fragment {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();                       // Creating JSON Parser object
    ArrayList<HashMap<String, String>> buchungsList;
    JSONArray buchungsbox = null;                                       // products JSONArray
    ListView prd;

    UserFunctions UF;
    GlobalClass GV;
    String booking_id, event_id,name, end_date,comment, ticket_id, OUTBOX_URL;




    // ALL JSON node names
    private static final String TAG_MESSAGES = "buchungen";
    private static final String TAG_ID = "id";
    private static final String TAG_EVENT_NAME = "name";
    private static final String TAG_END_DATE = "end_date";
    private static final String TAG_SPACES = "spaces";
    private static final String TAG_COMMENT = "comment";
    private static final String TAG_BOOK_DATE = "book_date";
    private static final String TAG_BOOK_STATUS = "book_status";
    private static final String TAG_PAID = "paid";
    private static final String TAG_USER = "user_login";
    private static final String TAG_DISPLAY = "user_name";
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_BOOKING_META = "booking_meta";

    public void alert(String text) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(text);
        alertDialog.setMessage(text);
        alertDialog.show();
    }

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();    }



//------------------------------------------------------------------------------------------------
	public EGBookingFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_egbooking, container, false);

        UF = new UserFunctions(getActivity());
        GV = (GlobalClass) getActivity().getApplicationContext();

        OUTBOX_URL = "http://"+ GV.getDomain() + "/"+ GV.getVerz() + "/get_all_buchungen-2.php";   // Outbox JSON url

        buchungsList = new ArrayList<HashMap<String, String>>();          // Hashmap for ListView
        final Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        prd = (ListView)rootView.findViewById(R.id.list);

        new SyncData().execute();                                   // lade JSON Daten

        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // merke dir die Buchungsnummer
                booking_id = buchungsList.get(position).get(TAG_ID);
                event_id = buchungsList.get(position).get(TAG_EVENT_ID);
                GV.EventTitel = buchungsList.get(position).get(TAG_EVENT_NAME);
                GV.EventDate = buchungsList.get(position).get(TAG_END_DATE);
                ticket_id = buchungsList.get(position).get(TAG_BOOKING_META);

                GV.setBooking_id(booking_id);   GV.setEvent_id(event_id);
                GV.setTicket_id(ticket_id);

                // springe zur Wallet
                Intent i = new Intent(getActivity().getApplicationContext(), WalletActivity.class);
                startActivity(i);
            }
        });



        /*prd.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        event_id = buchungsList.get(position).get(TAG_EVENT_ID);
                        booking_id = buchungsList.get(position).get(TAG_ID);

                        //in der wp_em_bookings die event_id mit -1*event_id ersetzen
                        // booking_status = 2

                        // in der ticket liste status auf 0


                        Toast("Reservierung gelöscht");
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();


                return true;
            }
        }); */






            return rootView;
        }
//================================================================================================
        private class SyncData extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Lade eigene Buchungen ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pid",GV.getPid()));

        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(OUTBOX_URL, "GET",
                params);

        // Check your log cat for JSON reponse
        Log.d("Outbox JSON: ", json.toString());

        try {
            buchungsbox = json.getJSONArray(TAG_MESSAGES);
            // looping through All messages
            for (int i = 0; i < buchungsbox.length(); i++) {
                JSONObject c = buchungsbox.getJSONObject(i);

                // Storing each json item in variable
                booking_id = c.getString(TAG_ID);
                name = c.getString(TAG_EVENT_NAME);
                end_date = c.getString(TAG_END_DATE);
                String spaces = c.getString(TAG_SPACES);
                comment = c.getString(TAG_COMMENT);
                String book_date = c.getString(TAG_BOOK_DATE);
                String book_status = c.getString(TAG_BOOK_STATUS);
                String paid = c.getString(TAG_PAID);
                String user_login = c.getString(TAG_USER);
                String display_name = c.getString(TAG_DISPLAY);
                event_id = c.getString(TAG_EVENT_ID);
                ticket_id = c.getString(TAG_BOOKING_META);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_ID, booking_id);
                map.put(TAG_EVENT_NAME, name);
                map.put(TAG_END_DATE, end_date);
                map.put(TAG_SPACES, spaces);
                map.put(TAG_COMMENT, comment);
                map.put(TAG_BOOK_DATE, book_date);
                map.put(TAG_BOOK_STATUS, book_status);
                map.put(TAG_PAID, paid);
                map.put(TAG_USER, user_login);
                map.put(TAG_DISPLAY, display_name);
                map.put(TAG_EVENT_ID,event_id);
                map.put(TAG_BOOKING_META, ticket_id);

                // adding HashList to ArrayList
                buchungsList.add(map);
            }
        } catch (JSONException e) {	e.printStackTrace();}
        catch (Exception ex) {alert(ex.toString());}

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //refreshMenuItem.collapseActionView();
        // remove the progress bar view
        //refreshMenuItem.setActionView(null);

        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                   getActivity(), buchungsList,
                   R.layout.fragment_egbooking_list_item, new String[]
                   {TAG_EVENT_NAME, TAG_END_DATE, TAG_SPACES, TAG_BOOK_DATE, TAG_COMMENT},
                   new int[]{R.id.name, R.id.start_date, R.id.sku, R.id.book_date, R.id.comment});
                // updating listview
                prd.setAdapter(adapter);
            }
        });
    }
}
//************************************************************************************************
}
