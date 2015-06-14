package info.androidhive.slidingmenu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import info.androidhive.slidingmenu.helper.Create_Booking;
import info.androidhive.slidingmenu.helper.Insert_Historie;
import info.androidhive.slidingmenu.helper.Update_Booking;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.IntentIntegrator;
import info.androidhive.slidingmenu.library.IntentResult;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;
import info.androidhive.slidingmenu.helper.update_orders4booking;


public class WalletActivity extends ListActivity {

    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    ArrayList<HashMap<String, String>> buchungsList;
    JSONArray buchungsbox = null;                             // products JSONArray

    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyykkmm");

    ListView prd;
    UserFunctions UF;
    GlobalClass GV; AlertDialog alertbox = null;

    String karten_pid,Booking_URL,Befuelle_Liste_URL;



    String TAG_EVENT_ID = "event_id"; String TAG_SPACES = "spaces"; String TAG_PID = "pid";
    String TAG_BOOKING_COMMENT = "comment"; String TAG_BOOKING_STATUS = "0";
    String TAG_SUCCESS = "success";

    // ALL JSON node names
    String TAG_MESSAGES = "buchungen"; String TAG_EVENT_NAME = "name"; String TAG_ID = "id";
    String TAG_END_DATE = "end_date";  String TAG_COMMENT = "comment"; String TAG_BOOK_DATE = "book_date";
    String TAG_BOOK_STATUS = "book_status"; String TAG_PAID = "paid"; String TAG_USER = "user_name";







//-----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_wallet);

        GV = (GlobalClass) getApplicationContext();   UF = new UserFunctions(getApplicationContext());

        Booking_URL = "http://" + GV.getDomain()+"/" + GV.getVerz() + "/create_booking.php";
        Befuelle_Liste_URL = "hhtp://"+ GV.getDomain()+"/" + GV.getVerz() +"/get_all_buchungen-2.php";

    try {
/*
Bildschirm wird aufgebaut
 */

        String tvTicketTitel = GV.TicketTitel;
            TextView mtt = (TextView) findViewById(R.id.tvTicketTitel);
            mtt.setText(tvTicketTitel);

        final String tvTicketID = GV.getTicket_id();
            TextView mti = (TextView) findViewById(R.id.tvTicketID);
            mti.setText(tvTicketID);

        String  tvEventDate = GV.EventDate;
            TextView med = (TextView) findViewById(R.id.tvEventDate);
            med.setText(tvEventDate);

        String tvEventID = GV.getEvent_id();
            TextView mei = (TextView) findViewById(R.id.tvEventID);
            mei.setText(tvEventID);

        String tvEventStartTime = GV.EventStartTime;
            TextView mest = (TextView) findViewById(R.id.tvEventStartTime);
            mest.setText(tvEventStartTime);

        String tvEventTitel = GV.EventTitel;
            TextView met = (TextView) findViewById(R.id.tvEventTitel);
            met.setText(tvEventTitel);
/*
Button ausgewertet
 */
         /* Click auf den Button Bezahlen
         karte lesen und wenn die rolle > 3 dann buchung bestätigen
          */
        ImageView imgBezahlen = (ImageView)findViewById(R.id.imgBUY);
                imgBezahlen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentIntegrator integrator = new IntentIntegrator(WalletActivity.this);
                        integrator.initiateScan();
                    }
                });

        /* click in der Liste und vergleiche
        prd = getListView();
        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Buchung des Kartenowners wird auf 1 gesetzt
                new Update_Booking(karten_pid, "1", GV.getEvent_id());

            }
        });
        */

        /*
        Hier werden Event und Ticket zusammengeführt.
        Eine neue Buchung wird erzeugt (Tabelle wp_em_bookings), Status des Tickets wird auf 1 für
        Reserviert gesetzt.
         */
        ImageView imgbestaetige = (ImageView)findViewById(R.id.imgOK);
            imgbestaetige.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Erzeuge buchung
                    new Create_Booking(GV.getEvent_id(), GV.getPid(), "1",GV.EventDate+";"+GV.EventStartTime, "0", GV.getTicket_id()).execute();

                    // Status des Tickets wird von 0 auf 1 gesetzt, damit ist das Ticket reserviert
                    new update_orders4booking(GV.getTicket_id(), "1", GV.getEvent_id()).execute();

                    Toast("Reservierung erfolgreich");
                }
            });

        /*
        Hier kann eine Reservierung storniert werden.
        Wennn das Tagesdatum gleich dem EventDatum ist, kann keine Stornierung vorgenommen werden.
         */
        ImageView imgStorno = (ImageView)findViewById(R.id.imgCancel);
            imgStorno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ist Tagesdatum gleich Eventdatum ?
                    if (GV.EventDate.equals(dateFormat.format(new Date()))) {

                        {
                            Toast("Keine Stornierung möglich, gleicher Tag");
                        }
                    } else
                        //status der Buchung wird auf 2 gesetzt
                        new Update_Booking(GV.getPid(), "2", GV.getEvent_id()).execute();

                    // Ticket wird auf 0 gesetzt und damit wieder verfügbar
                    new update_orders4booking(GV.getTicket_id(), "0", GV.getEvent_id()).execute();

                    Toast("Stornierung vorgenommen");

                }
            });

        /*
        mit diesem Icon kann der REitlehrer die KArte des Kindes scannen und die Stunde bestätigen
         */

        ImageView imgQR = (ImageView) findViewById(R.id.imgQR);
            imgQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), KartenLeserActivity.class);
                    startActivity(i);
                }
            });


    } catch (Exception ex){ex.getMessage();}

    }

//------------------------------------------------------------------------------------------------
//================= Klassen =====================================================================
    public void alert(String text) {
    AlertDialog alertDialog;
    alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle(text);
    alertDialog.setMessage(text);
    alertDialog.show();
}

    public void Toast(String inhalt) {
        Toast.makeText(this, inhalt, Toast.LENGTH_LONG).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null){
                String barcode;

                barcode = scanResult.getContents();  // barcode gescannt
                barcode = UF.decode(barcode);   //Barcode entschlüsseln

                // Nun zerteilen
                StringTokenizer st = new StringTokenizer(barcode,"&");
                String fab = st.nextToken();
                String fab2= st.nextToken();

                // key entschlüsseln und aufteilen
                //fab2 = UF.decode(fab2); wird für BLOWfish benötigt

                StringTokenizer stf = new StringTokenizer(fab2,"!");

                karten_pid = stf.nextToken();
                String karten_ul = stf.nextToken();
                int karten_rl = Integer.valueOf(stf.nextToken());
                String karten_rf = stf.nextToken();
                String karten_dom= stf.nextToken();
                String karten_email = stf.nextToken();

            if (karten_rl > 3){
                // Status Booking wird auf bezahlt gesetzt = 1
                new Update_Booking(karten_pid,"1",GV.getEvent_id());

                // Eintrag in die Historie
                new Insert_Historie(GV.getUser_login(),GV.Artnr,"-1",TAG_COMMENT,GV.getEvent_id(),
                                        GV.getTicket_id(),GV.EventTitel);

                // nun die Event_id in das Ticket eintragen und Ticket als bezahlt eintragen
                new update_orders4booking(GV.getTicket_id(),"90",GV.getEvent_id()).execute();

                            }
            else {
                    // Stunde wird mit Karte bezahlt vom Unterrichtsnehmer
                    //befülle die Liste mit Buchungen des Kartenowner
                    new befuelle_liste().execute();

                        }
            }

            else {Toast("Keinen scannbaren Code erkannt");}

        } catch (Exception ex) {alert(ex.toString());Toast("Kein Code erkennbar");}
    }

    class befuelle_liste extends AsyncTask <String, String, String> {

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_login", GV.getUser_login()));

            JSONObject json = jsonParser.makeHttpRequest(Befuelle_Liste_URL, "GET", params);
            Log.d("Bezahlen", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1){
                    buchungsbox = json.getJSONArray(TAG_MESSAGES);
                    // looping through All messages
                    for (int i = 0; i < buchungsbox.length(); i++) {
                        JSONObject c = buchungsbox.getJSONObject(i);

                        // Storing each json item in variable
                        String booking_id = c.getString(TAG_ID);
                        String name = c.getString(TAG_EVENT_NAME);
                        String end_date = c.getString(TAG_END_DATE);
                        String spaces = c.getString(TAG_SPACES);
                        String comment = c.getString(TAG_COMMENT);
                        String book_date = c.getString(TAG_BOOK_DATE);
                        String book_status = c.getString(TAG_BOOK_STATUS);
                        String paid = c.getString(TAG_PAID);
                        String user_name = c.getString(TAG_USER);


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
                        map.put(TAG_USER, user_name);

                        // adding HashList to ArrayList
                        buchungsList.add(map);
                    }


                } else {
                    Toast("Keine Buchungen gefunden");
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.getMessage();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            WalletActivity.this, buchungsList,
                            R.layout.activity_wallet_list_item, new String[]
                            { TAG_EVENT_NAME, TAG_END_DATE, TAG_USER},
                            new int[] { R.id.name, R.id.start_date,R.id.user_name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }


//================================================================================================

//================================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wallet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//*************************************************************************************************
}
