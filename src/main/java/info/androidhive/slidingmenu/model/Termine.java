package info.androidhive.slidingmenu.model;

/**
 * Created by rer on 24.05.2015.
 */
public class Termine {
    String post_name = null;
    String post_content = null;
    String StartDate = null;
    String StartTime = null;
    String FinishTime = null;
    String event_category_id = null;


    public String getPost_name(){return post_name;};
    public String getPost_content(){return post_content;};
    public String getStartDate(){return StartDate;};
    public String getStartTime(){return StartTime;};
    public String getFinishTime(){return FinishTime;};
    public String getEvent_category_id(){return event_category_id;};

    public void setPost_name(String post_name){this.post_name = post_name;}
    public void setPost_content(String post_content){this.post_content = post_content;}
    public void setStartDate(String StartDate){this.StartDate = StartDate;}
    public void setStartTime(String StartTime){this.StartTime = StartTime;}
    public void setFinishTime(String FinishTime){this.FinishTime = FinishTime;}
    public void setEvent_category_id(String event_category_id){this.event_category_id = event_category_id;}

}
