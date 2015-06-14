/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package info.androidhive.slidingmenu.Login;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import info.androidhive.slidingmenu.Fragments.ShowWebFragment;
import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.library.GlobalClass;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;

	GlobalClass GV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		GV = (GlobalClass)getApplicationContext();

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		//session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		/*if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this, MainLoginActivity.class);
			startActivity(intent);
			finish();
		} */

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				//String email = inputEmail.getText().toString();
				//String password = inputPassword.getText().toString();

				// Check for empty data in the form
				/*if (email.trim().length() > 0 && password.trim().length() > 0) {
					// login user
					checkLogin(email, password);
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}*/

				GV.setDomain("reitclub-hagen.info");
				GV.setVerz("connect");
				GV.setUser_login("9999");
				GV.setRFUID("04:67:28:A2:DE:32:80");
				GV.setRolle("2");
				GV.setPid("10");
				GV.setTelefon("01728736878");
				GV.setUrl("http://" + GV.getDomain() + "/" + GV.getVerz());

				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				finish();

			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Login Response: " + response.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
							//http://stackoverflow.com/questions/29059704/error-org-json-jsonexception-value-1-at-error-of-type-java-lang-integer-cannot
							//boolean error = jObj.getBoolean("error");
							int intError = jObj.getInt("error");
							boolean error = (intError > 0) ? true : false;

							// Check for error node in json
							if (!error) {
								// user successfully logged in
								// Create login session
								session.setLogin(true);

								// starte Register für den Verein
								Intent intentV = new Intent(getApplicationContext(),
										ShowWebFragment.class);
								startActivity(intentV);

								// Launch main activity
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();
							} else {
								// Error in login. Get the error message
								String errorMsg = jObj.getString("error_msg");
								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "login");
				params.put("email", email);
				params.put("password", password);

				return params;
			}

		};

		// Adding request to request queue
		try {
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
			} catch (NullPointerException e){
			// nothing
		}

	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
