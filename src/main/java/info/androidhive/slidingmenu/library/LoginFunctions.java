package info.androidhive.slidingmenu.library;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.adapter.LoginDbAdapter;

/**
 * Created by rer on 04.06.2015.
 */
public class LoginFunctions {

    private JSONParser jsonParser;
    GlobalClass GV;


    private String loginURL = GV.getUrl()+"/ah_login_api/";
    private String registerURL = GV.getUrl()+"/ah_login_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";

    // constructor
    public LoginFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "GET", params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(registerURL, "GET", params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        LoginDbAdapter db = new LoginDbAdapter(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        LoginDbAdapter db = new LoginDbAdapter(context);
        db.resetTables();
        return true;
    }


//###############################################################################################
}
