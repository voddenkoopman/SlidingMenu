package info.androidhive.slidingmenu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.StringTokenizer;

import info.androidhive.slidingmenu.helper.Insert_Historie;
import info.androidhive.slidingmenu.helper.Insert_Payments;
import info.androidhive.slidingmenu.helper.Update_Booking;
import info.androidhive.slidingmenu.helper.update_orders4booking;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.IntentIntegrator;
import info.androidhive.slidingmenu.library.IntentResult;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class KartenLeserActivity extends ListActivity {
//=============================================================================================
	private ProgressDialog pDialog;                                 // Progress Dialog
	JSONParser jsonParser = new JSONParser();                       // Creating JSON Parser object
	ArrayList<HashMap<String, String>> outboxList;
	JSONArray outbox = null;                                       // products JSONArray
    UserFunctions UF;
    GlobalClass GV;
    Vibrator RU;


    String buchungs_ul;             String booking_id, name, ticket_id, event_id;
    String buchungs_rfid;           String pid;
    String kartenownwer;

    private static final String OUTBOX_URL
            = "http://reitclub-hagen.info/connect/get_all_buchungen.php";   // Outbox JSON url
    private static final String UPDATE_URL
            = "http://reitclub-hagen.info/connect/update_tickets_order.php";

    // ALL JSON node names
	private static final String TAG_MESSAGES = "buchungen";
	private static final String TAG_ID = "id";
	private static final String TAG_EVENT_NAME = "name";
	private static final String TAG_END_DATE = "end_date";
	private static final String TAG_SPACES = "spaces";
    private static final String TAG_COMMENT = "comment";
    private static final String TAG_PID = "person_id";
    private static final String TAG_BOOK_STATUS = "book_status";
    private static final String TAG_USER_NAME = "user_name";
    private static final String TAG_USER = "user_login";
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_Ticket_ID = "ticket_id";


    public void alert(String text) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(text);
        alertDialog.setMessage(text);
        alertDialog.show();
    }

    public void Toast(String inhalt) {
        Toast.makeText(this, inhalt, Toast.LENGTH_LONG).show();    }


//------------------------------------------- Los gehts ------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox_list);

        UF = new UserFunctions(getApplicationContext());
        GV = (GlobalClass)getApplicationContext();
        RU = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        outboxList = new ArrayList<HashMap<String, String>>();          // Hashmap for ListView



        new LoadOutbox().execute();                           //Loading OUTBOX in Background Thread


        // click in der Liste und vergleiche
            ListView lv = getListView();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
        // getting values from selected ListItem
                //int uebergabe_booking_id = Integer.valueOf(outboxList.get(position).get(TAG_ID));
                buchungs_ul = outboxList.get(position).get(TAG_USER);
                buchungs_rfid = outboxList.get(position).get(TAG_USER_NAME);
                booking_id = outboxList.get(position).get(TAG_ID);
                event_id = outboxList.get(position).get(TAG_EVENT_ID);
                kartenownwer = GV.getKartenowner();
                name = outboxList.get(position).get(TAG_EVENT_NAME);
                ticket_id = outboxList.get(position).get(TAG_Ticket_ID);
                pid = outboxList.get(position).get(TAG_PID);



                // nun vergleiche buchung mit karte
                if (buchungs_ul.equals(kartenownwer)){

                    RU.vibrate(2000);

                    // Buchung bestätigen durch Mitarbeiter
                    new Update_Booking(pid,"90",event_id).execute();

                    // jetzt die Buchung im Kontoauszug eintragen
                    new Insert_Historie(kartenownwer,GV.Artnr,"-1","",booking_id,event_id,name).execute();

                    // nun die Event_id in das Ticket eintragen und Ticket als bezahlt eintragen
                    new update_orders4booking(ticket_id,"90",event_id).execute();

                    Toast("Buchung erfolgreich");


                }
                else {Toast("Karte passt nicht zur Buchung! Richtiges Kind? Richtige Buchung?");}
             }

        });



//-------------------------- Ende -----------------------------------------------------------------
	}


	/**
	 * Background Async Task to Load all OUTBOX messages by making HTTP Request
	 * */
	class LoadOutbox extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(KartenLeserActivity.this);
			pDialog.setMessage("Lade Buchungen ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Outbox JSON
		 * */
		protected String doInBackground(String... args) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_login",globalVariable.getUser_login()));

			
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(OUTBOX_URL, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("Outbox JSON: ", json.toString());

			try {
				outbox = json.getJSONArray(TAG_MESSAGES);
				// looping through All messages
				for (int i = 0; i < outbox.length(); i++) {
                    JSONObject c = outbox.getJSONObject(i);

					// Storing each json item in variable
					String booking_id = c.getString(TAG_ID);
                    name = c.getString(TAG_EVENT_NAME);
					String end_date = c.getString(TAG_END_DATE);
					String spaces = c.getString(TAG_SPACES);
                    String comment = c.getString(TAG_COMMENT);
                    pid = c.getString(TAG_PID);
                    String book_status = c.getString(TAG_BOOK_STATUS);
                    String user_name = c.getString(TAG_USER_NAME);
                    String user_login = c.getString(TAG_USER);
                    ticket_id = c.getString(TAG_Ticket_ID);
                    event_id = c.getString(TAG_EVENT_ID);

					
					// subject taking only first 23 chars to fit into screen
					/*
					if(subject.length() > 23){
						subject = subject.substring(0, 22) + "..";
					} */

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_ID, booking_id);
					map.put(TAG_EVENT_NAME, name);
					map.put(TAG_END_DATE, end_date);
					map.put(TAG_SPACES, spaces);
                    map.put(TAG_COMMENT, comment);
                    map.put(TAG_PID, pid);
                    map.put(TAG_BOOK_STATUS, book_status);
                    map.put(TAG_USER_NAME, user_name);
                    map.put(TAG_USER, user_login);
                    map.put(TAG_Ticket_ID, ticket_id);
                    map.put(TAG_EVENT_ID, event_id);



					// adding HashList to ArrayList
					outboxList.add(map);
				}

			} catch (JSONException e) {	e.printStackTrace();}
              catch (Exception ex) {alert(ex.toString());}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							KartenLeserActivity.this, outboxList,
							R.layout.outbox_list_item, new String[]
                            { TAG_ID, TAG_EVENT_NAME, TAG_END_DATE, TAG_USER_NAME, TAG_USER,
                              TAG_Ticket_ID, TAG_EVENT_ID},
							new int[] {R.id.book_id, R.id.name, R.id.start_date,R.id.user_name,R.id.user_login,
                                       R.id.ticket_id,R.id.event_id });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}

    public void scanne_code (View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                // handle scan result
                String barcode;  barcode = scanResult.getContents();
                barcode = UF.decode(barcode);

                // barcode gescannt, nun aufteilen
                StringTokenizer st = new StringTokenizer(barcode,"&");
                String fab = st.nextToken();
                String fab2= st.nextToken();

                // key entschlüsseln und aufteilen
                //fab2 = UF.decode(fab2);

                StringTokenizer stf = new StringTokenizer(fab2,"!");


                String karten_pid = stf.nextToken();
                String karten_ul = stf.nextToken();
                String karten_rl = stf.nextToken();
                String karten_rf = stf.nextToken();
                String karten_dom= stf.nextToken();
                String karten_email = stf.nextToken();

                // Wichtigen Infos sichern
                GV.setKartenowner(karten_ul);

                // Karteninfos anzeigen
                TextView txtName = (TextView)findViewById(R.id.txtName);
                txtName.setText(karten_ul);

                TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
                txtEmail.setText(karten_email);



            }
            else { Toast("Kein Code erkennbar");}


        }catch (Exception ex) {alert(ex.toString());Toast("Kein Code erkennbar");}
    }


}