package info.androidhive.slidingmenu.Fragments;
// Inhalt Tickets aus tabwithgest...

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.WalletActivity;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class TicketFragment extends Fragment {
    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    ArrayList<HashMap<String, String>> ticketList;
    JSONArray ticketbox = null;                             // products JSONArray

    ListView prd;
    UserFunctions UF;
    GlobalClass GV; AlertDialog alertbox = null;

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();
    }

    //GV.getDomain();
    String domain = "reitclub-hagen.info";
    String seite = "/connect";

    String Ticket_URL = "http://" + domain + seite + "/get_all_tickets-2.php";


    String ticket_id, titel, desc, artnr, sku, booking_date;
    String TAG_MESSAGES = "tickets"; String TAG_SUCCESS = "success";

    String TAG_ticket_id = "id";
    String TAG_titel = "name";
    String TAG_artnr = "artnr";
    String TAG_sku = "AnzT";
    String TAG_booking_date = "datum_erst";


    //-----------------------------------------------------------------------------------------------
    public TicketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ticket, container, false);

        prd = (ListView)rootView.findViewById(R.id.list);
        ticketList = new ArrayList<HashMap<String, String>>();   // Hashmap for ListView

        GV = (GlobalClass)getActivity().getApplicationContext();
        UF = new UserFunctions(getActivity());
        // Create new fragment and transaction
        final Fragment newProdFragment = new ProductFragment();
        final Fragment newBookFragment = new ShowWebFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();



        new Load_Tickets().execute();                           // hole Daten ab

        // Mit Click auf dem Bild findpeople können neue Tickets gekauft werden
        ImageView img = (ImageView) rootView.findViewById(R.id.newTicket);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
                transaction.replace(R.id.frame_container, newProdFragment);
                transaction.addToBackStack(null);

        // Commit the transaction
                transaction.commit();
            }
        });


        // Mit Click in diese Liste wird die Wallet aufgerufen und dort das Ticket bezahlt
        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent i = new Intent(getActivity().getApplicationContext(), WalletActivity.class);

                // merke dir die Ticketnummer
                ticket_id = ticketList.get(position).get(TAG_ticket_id);
                GV.setTicket_id(ticket_id);

                //http://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android
                GV.TicketTitel = ticketList.get(position).get(TAG_titel);
                GV.Artnr = ticketList.get(position).get(TAG_artnr);

                startActivity(i);
            }
        });



        return rootView;
    }
//----------------------------------------------------------------------------------------------

    /**
     * *****************************************************************************
     * Background Async Task to Load all data by making HTTP Request
     */
    class Load_Tickets extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_login", GV.getUser_login()));

            JSONObject json = jsonParser.makeHttpRequest(Ticket_URL, "GET", params);

            Log.d("Ticketbox JSON: ", json.toString());  // Check your log cat for JSON reponse


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // event gefunden
                    ticketbox = json.getJSONArray(TAG_MESSAGES);
                    // looping through All messages
                    for (int i = 0; i < ticketbox.length(); i++) {
                        JSONObject c = ticketbox.getJSONObject(i);

                        // Storing each json item in variable
                        ticket_id = c.getString(TAG_ticket_id);
                        titel = c.getString(TAG_titel);
                        artnr = c.getString(TAG_artnr);
                        sku = c.getString(TAG_sku);
                        booking_date = c.getString(TAG_booking_date);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ticket_id, ticket_id);
                        map.put(TAG_titel, titel);
                        map.put(TAG_artnr, artnr);
                        map.put(TAG_sku, sku);
                        map.put(TAG_booking_date, booking_date);


                        // adding HashList to ArrayList;
                        ticketList.add(map);
                    }
                } else {
                    Toast("Keine Tickets gefunden");
                }


            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.getMessage();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), ticketList,
                            R.layout.fragment_ticket_list_item, new String[]
                            {TAG_ticket_id, TAG_titel, TAG_sku, TAG_booking_date},
                            new int[]
                                    {R.id.ticket_id, R.id.titel, R.id.booking_date,R.id.sku});


                    prd.setAdapter(adapter);
                }
            });

        }
    }

//################################################################################################
        public void alert(String text) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(text);
            alertDialog.setMessage(text);
            alertDialog.show();
        }

    String ADSetMessage=("Ich ermächtige Reitclub Hagen, Apfelallee 1, 21337 Lüneburg" +
            ", Gläubiger-Identifikationsnummer DExxxxx " +
            ", Zahlungen von meinem Konto mittels Lastschrift einzuziehen." +
            " Zugleich weise ich mein Kreditinstitut an, die oben genannte Firma auf" +
            " mein Konto gezogene Lastschriften einzulösen. Die Mandatsreferenz wird mir" +
            " separat mitgeteilt.\n" + "\n" +
            "Hinweis: Ich kann innerhalb von acht Wochen, beginnend mit dem" +
            " Belastungsdatum, die Erstattung des belasteten Betrages verlangen." +
            " Es gelten die mit meinem Kreditinstitut vereinbarten Bedingungen.");


//***********************************************************************************************
}
