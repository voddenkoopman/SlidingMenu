package info.androidhive.slidingmenu;

import info.androidhive.slidingmenu.Fragments.AuszugFragment;
import info.androidhive.slidingmenu.Fragments.EGBookingFragment;
import info.androidhive.slidingmenu.Fragments.EventFragment;
import info.androidhive.slidingmenu.Fragments.WalletFragment;
import info.androidhive.slidingmenu.Fragments.TicketFragment;
import info.androidhive.slidingmenu.Fragments.WhatsHotFragment;
import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.adapter.TabsPagerAdapter;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.UserFunctions;
import info.androidhive.slidingmenu.model.NavDrawerItem;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// Variable fuer Tabs uebernommen aus TErmineActivity
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Springen","Western", "Dressur", "VierKampf" };


	GlobalClass GV; UserFunctions UF;

		// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

//++++++++++++++++++++++ Los gehts +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	//setContentView(R.layout.activity_main);

		GV = (GlobalClass)getApplicationContext();
		UF = new UserFunctions(getApplicationContext());

		/*String s1=GV.getCheck_termine();
		try {
		http://stackoverflow.com/questions/23927500/get-back-to-a-fragment-from-an-activity
		if(s1.equals("1"))
		{
			s1 = "";
			Fragment fragment = new TicketFragment();
			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();
			}
		}
		} catch (Exception e){
		// Nothing
		}*/


		// +++++++++++++++++++ Tabs +++++
		// Initilization
		/*viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		/*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});*/

		//+++++++++++++++++++++++++++++++++Tabs Ende +++++++++
		mTitle = mDrawerTitle = getTitle();


		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Tickets
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Reservierungen
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Termine
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Kontoauszug
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Wallet füpr Kunden (Rolle 4-6)
		if (Integer.valueOf(GV.getRolle())> 3)
			{navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));}
		// Verwaltung, We  will add a counter here; Rolle 7-8
		if (Integer.valueOf(GV.getRolle())> 6)
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		{navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));}

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);


		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

//----------------------------------------- Klassen -------------------------------------------
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int IntRolle = Integer.valueOf(GV.getRolle());
		if (IntRolle >6) {
			getMenuInflater().inflate(R.menu.main, menu);}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			int IntRolle = Integer.valueOf(GV.getRolle());
			if (IntRolle >2) { Bezahlen();}
			Toast("Keine Berechtigung fuer diesen Menupkt");//Bezahlen();
			return true;

		case R.id.action_twitter:
			Twitter();
			return true;

		case R.id.action_profile:
			Suche_User();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Starte Ticket bezahlen
	//* **
	private void Bezahlen(){
		Intent i = new Intent(MainActivity.this, KartenLeserActivity.class);
		startActivity(i);
	}

	private void Twitter(){

	}

	private void Suche_User(){
		Intent i = new Intent(MainActivity.this, WhatsHotFragment.class);
		startActivity(i);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new TicketFragment();	//fragment = new EventFragment();
			break;
		case 1:
			fragment = new EGBookingFragment();
			break;
		case 2:
			fragment = new EventFragment();
			break;
		case 3:
			fragment = new AuszugFragment();
			break;
		case 4:
			fragment = new WalletFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();		//fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.

		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void Toast(String inhalt) {
		Toast.makeText(this, inhalt, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

//################################################################################################
}
