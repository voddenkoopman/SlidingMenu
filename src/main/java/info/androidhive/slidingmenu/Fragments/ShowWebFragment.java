package info.androidhive.slidingmenu.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.GlobalClass;

public class ShowWebFragment extends Fragment {

	public ShowWebFragment(){}

    private WebView webView;
    GlobalClass GV;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_show_web, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView1);

        //String data = GV.getEvent_id(); String url = GV.getDomain()+"?event="+data;
        //startWebView("http://reitclub-hagen.info///category/allgemein/");
        startWebView(GV.getUrl()+"/mitgliedschaft/");

        return rootView;
    }
//-------------------------------------------------------------------------------------------------
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


    /*@Override
    public void onBackPressed(){
        if (webView.canGoBack()){webView.goBack();}
        else {super.onBackPressed();}
    } */

//*************************************************************************************************
}
