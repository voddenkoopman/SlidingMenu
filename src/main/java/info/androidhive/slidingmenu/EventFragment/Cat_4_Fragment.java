package info.androidhive.slidingmenu.EventFragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import info.androidhive.slidingmenu.Fragments.TicketFragment;
import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.WalletActivity;
import info.androidhive.slidingmenu.helper.Call_Termine;
import info.androidhive.slidingmenu.library.Cache;
import info.androidhive.slidingmenu.library.GlobalClass;


public class Cat_4_Fragment extends Fragment {

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

//------------------------------------------------------------------------------------------------
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cat_1, container, false);
		prd = (ListView)rootView.findViewById(R.id.list);
		GV = (GlobalClass)getActivity().getApplicationContext();

		ListAdapter adapter = new SimpleAdapter(
				getActivity(), Cache.getListeCat4(),
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

					GV.setEvent_id(Cache.getListeCat4().get(position).get(TAG_ROWID));
					GV.EventDate = Cache.getListeCat4().get(position).get(TAG_START_DATE);
					GV.EventTitel = Cache.getListeCat4().get(position).get(TAG_EVENT_CONTENT);
					GV.EventStartTime = Cache.getListeCat4().get(position).get(TAG_START_TIME);

				}catch (Exception e){alert(e.getMessage());}

				// springe zur Ticketliste
				Intent intent = new Intent(getActivity(),MainActivity.class);
				GV.setCheck_termine("1");
				startActivity(intent);
			}
		});

		return rootView ;
	}
//------------------------------------------------------------------------------------------
	public void alert(String text) {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setTitle(text);
		alertDialog.setMessage(text);
		alertDialog.show();
	}

	public void Toast(String inhalt) {
		Toast.makeText(getActivity(), inhalt, Toast.LENGTH_LONG).show();    }
//*************************************************************************************************
}
