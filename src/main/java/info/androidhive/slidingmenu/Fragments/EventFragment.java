package info.androidhive.slidingmenu.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.TermineActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class EventFragment extends Fragment {

    private ProgressDialog pDialog;                     // Progress Dialog
    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    ArrayList<HashMap<String, String>> eventList;
    JSONArray eventbox = null;                             // products JSONArray

    ListView prd;

    UserFunctions UF;
    GlobalClass GV;


    //GV.getDomain();

    String EVENT_URL;
    String Event_zaehle_url;

    // ALL JSON node names
    private static final String TAG_LOCATION_ID = "location_id";
    private static final String TAG_MESSAGES = "events";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_END_DATE = "end_date";
    private static final String TAG_START_TIME = "start_time";
    private static final String TAG_END_TIME = "end_time";
    private static final String TAG_ANZB = "AnzB";
    private static final String TAG_EVENTSLUG = "eventslug";



    public void alert(String text) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(text);
        alertDialog.setMessage(text);
        alertDialog.show();
    }

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();    }

//----------------------------------------------------------------------------------------------
	public EventFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        UF = new UserFunctions(getActivity());
        GV = (GlobalClass)getActivity().getApplicationContext();

        EVENT_URL = "http://"+GV.getDomain()+"/"+GV.getVerz()+"/get_kalender_today.php";
        Event_zaehle_url = "http://"+GV.getDomain()+"/"+GV.getVerz()+ "/zaehle_booking.php";

        ImageView imgNewEvent = (ImageView) rootView.findViewById(R.id.newEvent);
        /*imgNewEvent.setVisibility(View.INVISIBLE);
        int IntRolle = Integer.valueOf(GV.getRolle());
        if (IntRolle >2) { imgNewEvent.setVisibility(View.VISIBLE);} */


        eventList = new ArrayList<HashMap<String, String>>();   // Hashmap for ListView
        prd = (ListView)rootView.findViewById(R.id.list);
        // Create new fragment and transaction
        final Fragment newFragment = new TicketFragment();
        final Fragment newBookFragment = new ShowWebFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Hole Daten für die Liste
        new LoadInbox().execute();

        // Mit Click in diese Liste wird das TicketFragment aufgerufen und dort das Ticket ausgewählt
        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                // merke dir die Ticketnummer
                GV.setEvent_id(eventList.get(position).get(TAG_ID));

                GV.EventDate = eventList.get(position).get(TAG_END_DATE);
                GV.EventStartTime = eventList.get(position).get(TAG_START_TIME);
                GV.EventTitel = eventList.get(position).get(TAG_EVENTSLUG);

                // springe zur Ticketliste und nehme die ID mit
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });


        imgNewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                //transaction.replace(R.id.frame_container, newBookFragment);
                //transaction.addToBackStack(null);
                Intent i = new Intent(getActivity(), TermineActivity.class);
                startActivity(i);

                // Commit the transaction
                //transaction.commit();
            }
        });


        return rootView;
    }
//================================================================================================

    /********************************************************************************
     * Background Async Task to Load all INBOX messages by making HTTP Request
     * */
    class LoadInbox extends AsyncTask<String, String, String> {

        /** Before starting background thread Show Progress Dialog  ****************/

        /**
         * getting Inbox JSON
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("location_id", GV.getLocal_id()));

            // getting JSON string from URL
            JSONObject json2 = jsonParser.makeHttpRequest(Event_zaehle_url, "GET", params);

            JSONObject json = jsonParser.makeHttpRequest(EVENT_URL, "GET", params);




            // Check your log cat for JSON reponse
            Log.d("Eventbox JSON: ", json.toString());


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1){
                    // event gefunden
                    eventbox = json.getJSONArray(TAG_MESSAGES);
                    // looping through All messages
                    for (int i = 0; i < eventbox.length(); i++) {
                        JSONObject c = eventbox.getJSONObject(i);

                        // Storing each json item in variable
                        String event_id = c.getString(TAG_ID);Log.d("event_id=",TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String location = c.getString(TAG_LOCATION);
                        String end_date = c.getString(TAG_END_DATE);
                        String start_time = c.getString(TAG_START_TIME);
                        String end_time = c.getString(TAG_END_TIME);
                        String anzb = c.getString(TAG_ANZB);
                        String eventslug = c.getString(TAG_EVENTSLUG);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, event_id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_LOCATION, location);
                        map.put(TAG_END_DATE, end_date);
                        map.put(TAG_START_TIME, start_time);
                        map.put(TAG_END_TIME, end_time);
                        map.put(TAG_ANZB, anzb);
                        map.put(TAG_EVENTSLUG, eventslug);



                        // adding HashList to ArrayList;
                        eventList.add(map);
                    }
                } // else { Toast("Keine Events gefunden");}



            } catch (JSONException ex) {ex.printStackTrace();}
            catch (Exception ex) {alert(ex.getMessage());}

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products

            //pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), eventList,
                            R.layout.fragment_home_list_item, new String[]
                            {TAG_ID, TAG_NAME, TAG_LOCATION, TAG_END_DATE, TAG_START_TIME,
                                    TAG_END_TIME, TAG_ANZB},
                            new int[]
                                    {R.id.post_id, R.id.titel, R.id.price, R.id.start_date,
                                            R.id.start_time, R.id.end_time, R.id.anzb});



                    // updating listview
                    //setListAdapter(adapter);
                    //http://stackoverflow.com/questions/21107287/
                    // setlistadapter-not-working-for-fragment-any-alternatives
                    prd.setAdapter(adapter);


                }
            });

        }

    }
//************************************************************************************************
}
