package info.androidhive.slidingmenu.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.helper.Insert_Payments;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.helper.Insert_Historie;
import info.androidhive.slidingmenu.helper.Insert_Orders;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class ProductFragment extends Fragment {
    JSONParser jsonParser = new JSONParser();           // Creating JSON Parser object
    ArrayList<HashMap<String, String>> productList;
    JSONArray productbox = null;                             // products JSONArray

    ListView prd;
    UserFunctions UF;
    GlobalClass GV; AlertDialog.Builder alertbox;

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();
    }

    //GV.getDomain();
    String domain = "reitclub-hagen.info";
    String seite = "/connect";

    String Ticket_URL = "http://" + domain + seite + "/get_all_product.php";

    String post_id, titel, desc, artnr, sku, price;
    String TAG_MESSAGES = "product"; String TAG_SUCCESS = "success";

    String TAG_post_id = "post_id";
    String TAG_titel = "titel";
    String TAG_desc = "desc";
    String TAG_artnr = "artnr";
    String TAG_sku = "sku";
    String TAG_price = "price";


    //-----------------------------------------------------------------------------------------------
    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        prd = (ListView)rootView.findViewById(R.id.list);
        productList = new ArrayList<HashMap<String, String>>();   // Hashmap for ListView

        GV = (GlobalClass)getActivity().getApplicationContext();
        UF = new UserFunctions(getActivity());

        alertbox = new AlertDialog.Builder(getActivity());


        new Load_Product().execute();                           // hole Daten ab

        // Mit Click auf dem Button wird das ausgewählte Ticket gekauft
        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sku = productList.get(position).get(TAG_sku);
                artnr = productList.get(position).get(TAG_artnr);
                titel = productList.get(position).get(TAG_titel);
                price = productList.get(position).get(TAG_price);

                // set the message to display
                alertbox.setMessage("Hiermit ermächtige ich Fa xx" +
                        ", Gläubiger-Identifikationsnummer DExxxxx " +
                        ", Zahlungen von meinem Konto mittels Lastschrift einzuziehen im Auftrage der Fa yy." +
                        " Zugleich weise ich mein Kreditinstitut an, die oben genannte Firma auf" +
                        " mein Konto gezogene Lastschriften einzulösen. Die Mandatsreferenz wird mir" +
                        " separat mitgeteilt.\n" + "\n" +
                        "Hinweis: Ich kann innerhalb von acht Wochen, beginnend mit dem" +
                        " Belastungsdatum, die Erstattung des belasteten Betrages verlangen." +
                        " Es gelten die mit meinem Kreditinstitut vereinbarten Bedingungen.");

                // set a positive/yes button and create a listener
                alertbox.setPositiveButton("Karte", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        new Insert_Historie(GV.getUser_login(), artnr, sku, "Karte", "0", "0", titel).execute();

                        // Tickets werden in die DB geschrieben
                        new Insert_Orders(GV.getUser_login(), artnr, sku, price, titel,"0").execute();

                        // nun zum Schluss in Tabelle payments eintragen, damit das Geld eingezogen wird
                        new Insert_Payments("RCH","100",GV.getUser_login(),artnr,"Kauf Ticket",price).execute();

                        // nun noch SMS schicken
                        UF.SendSMS("Ticket gekauft mit Karte");


                        Toast("Buchung erfolgreich");
                    }
                });

                // set a negative/no button and create a listener
                alertbox.setNegativeButton("Bargeld", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        new Insert_Historie(GV.getUser_login(), artnr, sku, "Bargeld", "0", "0", titel).execute();
                        // Tickets werden in die DB geschrieben
                        new Insert_Orders(GV.getUser_login(), artnr, sku, price, titel,"0").execute();

                        // Info das Bargeld geflossen ist
                        new Insert_Payments("RCH","300",GV.getUser_login(),artnr,"Kauf Ticket",price).execute();

                        // nun noch SMS schicken
                        UF.SendSMS("Ticket gekauft mit Bargeld");


                        Toast("Buchung erfolgreich ");
                    }
                });
                // set neutral button:
                alertbox.setNeutralButton("PayPal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast("Wird mit dem neuen Release fertig sein !");
                    }
                });

                //AlertDialog alertDialog = alertbox.create();

                // display box
                //alertDialog.show();
                alertbox.show();

            }
        });

        return rootView;
    }

    /**
     * *****************************************************************************
     * Background Async Task to Load all data by making HTTP Request
     */
    class Load_Product extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("user_login", GV.getUser_login()));

            // getting JSON string from URL
            //JSONObject json2 = jsonParser.makeHttpRequest(Event_zaehle_url, "GET", params);

            JSONObject json = jsonParser.makeHttpRequest(Ticket_URL, "GET", params);

            Log.d("Productbox JSON: ", json.toString());  // Check your log cat for JSON reponse


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // event gefunden
                    productbox = json.getJSONArray(TAG_MESSAGES);
                    // looping through All messages
                    for (int i = 0; i < productbox.length(); i++) {
                        JSONObject c = productbox.getJSONObject(i);

                        // Storing each json item in variable
                        post_id = c.getString(TAG_post_id);
                        titel = c.getString(TAG_titel);
                        desc = c.getString(TAG_desc);
                        artnr = c.getString(TAG_artnr);
                        sku = c.getString(TAG_sku);
                        price = c.getString(TAG_price);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_post_id, post_id);
                        map.put(TAG_titel, titel);
                        map.put(TAG_desc, desc);
                        map.put(TAG_artnr, artnr);
                        map.put(TAG_sku, sku);
                        map.put(TAG_price, price);


                        // adding HashList to ArrayList;
                        productList.add(map);
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
                            getActivity(), productList,
                            R.layout.fragment_product_list_item, new String[]
                            {TAG_post_id, TAG_titel, TAG_desc, TAG_price},
                            new int[]
                                    {R.id.post_id, R.id.titel, R.id.desc,R.id.price});


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
