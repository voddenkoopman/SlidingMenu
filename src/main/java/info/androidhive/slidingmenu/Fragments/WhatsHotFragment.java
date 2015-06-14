package info.androidhive.slidingmenu.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.slidingmenu.KartenLeserActivity;
import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.adapter.UserDbAdapter;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

public class WhatsHotFragment extends Fragment {

    UserFunctions UF;
    GlobalClass GV;


    public WhatsHotFragment(){}

    private WebView webView;

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_web, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView1);

        //String data = GV.getEvent_id(); String url = GV.getDomain()+"?event="+data;

        startWebView("http://reitclub-hagen.info///category/allgemein/"); // Intranet
        //startWebView(GV.getUrl()+"/mitgliedschaft/");


        return rootView;

    }
 //----------------------------------------------------------------------------------------------

    private void startWebView (String url) {

        webView.setWebViewClient(new WebViewClient() {

            ProgressDialog progressDialog;


            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }
    public void alert(String text) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(text);
        alertDialog.setMessage(text);
        alertDialog.show();
    }

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();    }
 //===============================================================================================


// ***************************************************
}
