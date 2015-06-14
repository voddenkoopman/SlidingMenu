package info.androidhive.slidingmenu.helper;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.JSONParser;
import info.androidhive.slidingmenu.library.UserFunctions;

/**
 * Created by rer on 08.06.2015.
 */
public class Insert_User extends AsyncTask<String, String, String> {
    JSONParser jsonParser = new JSONParser();
    GlobalClass GV; UserFunctions UF;

    String INSERT_USER_1_URL = GV.getUrl()+"/connect/insert_user_1.php";


    private String Fullname, eTEmail, eTTelefon, user_login;



    public Insert_User (String Fullname, String eTEmail, String eTTelefon,
                        String user_login ){
        this.Fullname = Fullname;
        this.eTEmail = eTEmail;
        this.eTTelefon = eTTelefon;
        this.user_login = user_login;

    }


    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params_teil_1 = new ArrayList<NameValuePair>();
        params_teil_1.add(new BasicNameValuePair("display_name", Fullname));
        params_teil_1.add(new BasicNameValuePair("user_email",eTEmail));
        params_teil_1.add(new BasicNameValuePair("user_nicename",eTTelefon));
        params_teil_1.add(new BasicNameValuePair("user_login",user_login));


        JSONObject json = jsonParser.makeHttpRequest(INSERT_USER_1_URL, "POST", params_teil_1);
        Log.d("Insert_User: ", json.toString());  // Check your log cat for JSON reponse




        return null;
    }

}
