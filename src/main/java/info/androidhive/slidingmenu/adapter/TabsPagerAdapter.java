package info.androidhive.slidingmenu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.androidhive.slidingmenu.EventFragment.Cat_1_Fragment;
import info.androidhive.slidingmenu.EventFragment.Cat_2_Fragment;
import info.androidhive.slidingmenu.EventFragment.Cat_3_Fragment;
import info.androidhive.slidingmenu.EventFragment.Cat_4_Fragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Termine fragment activity
			return new Cat_1_Fragment();
        case 1:
            // Termine fragment activity
            return new Cat_2_Fragment();
		case 2:
			// News fragment activity
			return new Cat_3_Fragment();
		case 3:
			// Login fragment activity
			return new Cat_4_Fragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
