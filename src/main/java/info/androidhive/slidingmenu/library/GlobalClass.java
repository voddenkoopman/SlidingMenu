package info.androidhive.slidingmenu.library;

import android.app.Application;

public class GlobalClass extends Application{

    public String TicketTitel = "";
    public String TicketDesc = "";
    public String EventDate = "";
    public String EventTitel = "";
    public String EventStartTime = "";
    public String Artnr = "";

	private String name;
	private String email;
    private String Local_id;
    private String RFUID;
    private String user_login;
    private String domain;
    private String verz;
    private String rolle;
    private String url;
    private String Kartenowner;
    private String TempInfo;
    private String pid;
    private String booking_id, ticket_id, event_id;
    private String Check_termine;
    private String Telefon;

	

	public String getName() {return name;}
	public void setName(String aName) {name = aName;}
   
	public String getEmail() {return email;}
	public void setEmail(String aEmail) { email = aEmail;}

    public String getLocal_id() {return Local_id;}
    public void setLocal_id(String string){Local_id = string;}

    public String getRFUID() {return RFUID;}
    public void setRFUID(String string) {RFUID = string;}

    public String getUser_login() {return user_login;}
    public void setUser_login(String string){user_login = string;}

    public String getDomain() {return domain;}
    public void setDomain(String string) {domain = string;}

    public String getVerz() {return verz;}
    public void setVerz(String string) {verz = string;}


    public String getRolle(){return rolle;}
    public void setRolle(String string){rolle = string;}

    public String getUrl(){return url;}
    public void setUrl(String string){url = string;}

    public String getKartenowner(){return Kartenowner;}
    public void setKartenowner(String kartenowner) {Kartenowner = kartenowner;}

    public String getTempInfo(){return TempInfo;}
    public void setTempInfo(String string){TempInfo = string;}

    public String getPid (){return pid;}
    public void setPid (String string) {pid = string;}

    public String getBooking_id(){return booking_id;}
    public void setBooking_id(String string) {booking_id = string;}

    public String getTicket_id(){return ticket_id;}
    public void setTicket_id(String string){ticket_id = string;}

    public String getEvent_id(){return event_id;};
    public void setEvent_id(String string){event_id = string;};

    public String getCheck_termine(){return Check_termine;}
    public void setCheck_termine(String string){Check_termine = string;}

    public String getTelefon(){return Telefon;}
    public void setTelefon(String string){Telefon = string;}
}
