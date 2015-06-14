package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.StringTokenizer;

import info.androidhive.slidingmenu.Login.LoginActivity;
import info.androidhive.slidingmenu.Login.RegisterActivity;
import info.androidhive.slidingmenu.helper.Call_Termine;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.IntentIntegrator;
import info.androidhive.slidingmenu.library.IntentResult;
import info.androidhive.slidingmenu.library.UserFunctions;


public class StartActivity extends Activity {

    ImageButton btnKarte;
    ImageButton btnTAG;
    Button btnLogin;

    GlobalClass GV;
    UserFunctions UF;

    EditText editTextpassw;     EditText editTextpid;
    String email,pid;

//------------------------------------------- Los gehts ---------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        UF = new UserFunctions(getApplicationContext());
        GV = (GlobalClass)getApplicationContext();

        new Call_Termine(StartActivity.this).execute();     // Hole Daten ab


        //Login mit Karte
        btnKarte = (ImageButton) findViewById(R.id.btnKarte);
        btnKarte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // starte Scanner
                IntentIntegrator integrator = new IntentIntegrator(StartActivity.this);
                integrator.initiateScan();

            }
        });

        // Login mit TAG
        btnTAG = (ImageButton) findViewById(R.id.btnTAG);
        btnTAG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Belegungsplanung
                GV.setDomain("reitclub-hagen.info");
                GV.setVerz("connect");
                GV.setUser_login("573");
                GV.setRFUID("04:67:28:A2:DE:32:80");
                GV.setRolle("7");
                GV.setPid("9");
                GV.setTelefon("01728736878");
                GV.setUrl("http://" + GV.getDomain()+"/"+GV.getVerz());



                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();*/
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

    public void setBtnLogin (View view){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
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

                String karten_pid = stf.nextToken();
                String karten_ul = stf.nextToken();
                String karten_rl = stf.nextToken();
                String karten_rf = stf.nextToken();
                String karten_dom= stf.nextToken();
                String verz = stf.nextToken();          // wird nicht mehr benötigt, Verz ist hard codiert
                String karten_email = stf.nextToken();

                GV.setPid(karten_pid);
                GV.setUser_login(karten_ul);
                GV.setRolle(karten_rl);
                GV.setRFUID(karten_rf);
                GV.setDomain(karten_dom);
                GV.setVerz(verz);
                GV.setEmail(karten_email);

                GV.setUrl("http://" + karten_dom);

                if (GV.getRFUID().equals("9999")){
                    Toast("Keine Daten gefunden, Regestrierung wird gestartet");
                    Intent dashboard = new Intent(getApplicationContext(), RegisterActivity.class);
                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                    // Close Login Screen
                    finish();

                } else {

                    // Launch Dashboard Screen
                    Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);

                    // karte wurde gescannt, daher ist der login nicht mehr notwendig
                    findViewById(R.id.btnTAG).setVisibility(View.INVISIBLE);

                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);

                    // Close Login Screen
                    finish();
                    }

            }

            else {Toast("Keinen scannbaren Code erkannt bitte nochmal, ansonsten Scan abbrechen");

            }

        } catch (Exception ex) {
            //alert(ex.toString());
            Toast("Kein Code erkennbar, bitte manuell anmelden");
            Intent dashboard = new Intent(getApplicationContext(),LoginActivity.class);
            // Close all views before launching Dashboard
            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dashboard);

        }
    }
//================================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_wallet, menu);
        return true;
    }

    //@Override
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
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
