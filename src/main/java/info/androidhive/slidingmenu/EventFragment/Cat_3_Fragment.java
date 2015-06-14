package info.androidhive.slidingmenu.EventFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import info.androidhive.slidingmenu.Fragments.TicketFragment;
import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.helper.Call_Termine;
import info.androidhive.slidingmenu.library.Cache;
import info.androidhive.slidingmenu.library.GlobalClass;


public class Cat_3_Fragment extends Fragment {
//http://techiedreams.com/android-simple-rss-reader/
/*http://stackoverflow.com/questions/23899694/open-rss-reader-activity-from-fragment-using-button
    private ListView feedList;
    private FeedAdapter adapter;
    private ProgressBar pbLoad;

    private final String url = "http://9gagrss.com/feed/";
*/
    private SimpleCursorAdapter dataAdapter;
    private Call_Termine dbHelper;
    ListView prd; GlobalClass GV;

    private static final String TAG_ROWID = "id";
    private static final String TAG_EVENT_NAME = "post_name";
    private static final String TAG_EVENT_CONTENT = "post_content";
    private static final String TAG_START_DATE = "StartDate";
    private static final String TAG_START_TIME = "StartTime";
    private static final String TAG_FINISH_TIME = "FinishTime";
    private static final String TAG_EVENT_CATEGORY_ID = "event_category_id";



    //------------------------------------- Los gehts -----------------------------------------------
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cat_1, container, false);
        prd = (ListView)rootView.findViewById(R.id.list);
        GV = (GlobalClass)getActivity().getApplicationContext();


        ListAdapter adapter = new SimpleAdapter(
                getActivity(), Cache.getListeCat3(),
                R.layout.fragment_cat_1_item, new String[]
                { TAG_EVENT_NAME, TAG_EVENT_CONTENT, TAG_START_DATE, TAG_START_TIME,
                        TAG_FINISH_TIME},
                new int[]
                        {R.id.name, R.id.location, R.id.start_date,
                                R.id.start_time, R.id.finish_time});

        prd.setAdapter(adapter);        //prd.setAdapter(dataAdapter); // Datenbankzugriff


        prd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // merke dir die Buchungsnummer
                try {

                    GV.setEvent_id(Cache.getListeCat3().get(position).get(TAG_ROWID));
                    GV.EventDate = Cache.getListeCat3().get(position).get(TAG_START_DATE);
                    GV.EventTitel = Cache.getListeCat3().get(position).get(TAG_EVENT_CONTENT);
                    GV.EventStartTime = Cache.getListeCat3().get(position).get(TAG_START_TIME);

                }catch (Exception e){alert(e.getMessage());}
                // springe zur Ticketliste
                Intent intent = new Intent(getActivity(),MainActivity.class);
                GV.setCheck_termine("1");
                startActivity(intent);
            }
        });

        return rootView ;
    }


//--------------------------------------------------- Ende Programm -----------------------------

    public void alert(String text) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(text);
        alertDialog.setMessage(text);
        alertDialog.show();
    }

    public void Toast(String inhalt) {
        Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();    }
/*-------------------- Klassen ------------------------------------------------------------------


    private void updateFeedList(ArrayList<RssItem> items){
        adapter = new FeedAdapter(items, getActivity());
        feedList.setAdapter(adapter);
        showList();
    }

    private void showList(){
        pbLoad.setVisibility(View.GONE);
        feedList.setVisibility(View.VISIBLE);
    }

    private void showProgress(){
        pbLoad.setVisibility(View.VISIBLE);
        feedList.setVisibility(View.GONE);
    }

    class GetRSSFeedTask extends AsyncTask<String, Void, RssFeed>{

        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();
        }
        @Override
        protected RssFeed doInBackground(String... params) {
            RssFeed feed = null;
            try {
                URL url = new URL(params[0]);
                feed = RssReader.read(url);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return feed;
        }

        @Override
        protected void onPostExecute(RssFeed feed) {
            super.onPostExecute(feed);
            if(feed == null)
                return;
            updateFeedList(feed.getRssItems());
        }
    }

    public interface OnRSSItemSelected {
        public void onRSSItemSelected(String url);
    }
*/
//************************************************************************************************
}
