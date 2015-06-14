/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package info.androidhive.slidingmenu.library;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import info.androidhive.slidingmenu.adapter.UserDbAdapter;

public class UserFunctions  {
    //**************  Variablen   ****************************************
    private final static String ALGORITM = "Blowfish";
    String str;

    private Context ctx;
    public UserFunctions(Context ctx)
    {
        this.ctx = ctx;
    }


    public void Toast(String inhalt) {
        //Toast.makeText(this, inhalt, Toast.LENGTH_LONG).show();
    }
	


    public void entschlüssele (String PLAIN_TEXT, String KEY) {
        // http://dexxtr.com/post/57145943236/blowfish-encrypt-and-decrypt-in-java-android
        try {

            byte[] encrypted = encrypt(KEY, PLAIN_TEXT);
            Log.i("FOO", "Encrypted: " + bytesToHex(encrypted));

            String decrypted = decrypt(KEY, encrypted);
            Log.i("FOO", "Decrypted: " + decrypted);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private byte[] encrypt(String key, String plainText) throws GeneralSecurityException {


        SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        return cipher.doFinal(plainText.getBytes());
    }

    private String decrypt(String key, byte[] encryptedText) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.DECRYPT_MODE, secret_key);

        byte[] decrypted = cipher.doFinal(encryptedText);

        return new String(decrypted);
    }

    public static String bytesToHex(byte[] data) {

        if (data == null)
            return null;

        String str = "";

        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + Integer.toHexString(data[i] & 0xFF);
            else
                str = str + Integer.toHexString(data[i] & 0xFF);
        }

        return str;

    }

    public String decode (String string){
        // string ist der gescannte QR
        try {
            byte[] tmp2 = Base64.decode(string,Base64.DEFAULT);
            str = new String(tmp2,"UTF-8");str = new String(tmp2,"UTF-8");
             } catch (Exception ex){ex.printStackTrace();}
        return str;
    }

    public String encode (String string){String str = Base64.encodeToString(string.getBytes()
            ,Base64.DEFAULT);
        return str;}


    public void SendSMS(String smstext) {
        Log.i("Send SMS", "");
        String phoneNo = "01728736878";
        String message = smstext;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(ctx, "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ctx, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
