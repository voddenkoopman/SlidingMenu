package info.androidhive.slidingmenu.model;

/**
 * Created by rer on 03.06.2015.
 */
public class User {

    String id = null;
    String user_email = null;
    String display_name = null;
    String user_login = null;
    String oneid_key = null;
    String woo_key = null;

    public String getId(){return id;}
    public String getUser_email(){return user_email;}
    public String getDisplay_name(){return display_name;}
    public String getUser_login(){return user_login;}
    public String getOneid_key(){return oneid_key;}
    public String getWoo_key(){return woo_key;}

    public void setId(String id){this.id = id;}
    public void setUser_email(String user_email){this.user_email = user_email;}
    public void setDisplay_name(String display_name){this.display_name = display_name;}
    public void setUser_login(String user_login){this.user_login = user_login;}
    public void setOneid_key(String oneid_key){this.oneid_key = oneid_key;}
    public void setWoo_key(String woo_key){this.woo_key = woo_key;}

}
