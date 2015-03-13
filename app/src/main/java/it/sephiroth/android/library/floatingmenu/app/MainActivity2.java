package it.sephiroth.android.library.floatingmenu.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Locale;


public class MainActivity2 extends ActionBarActivity implements ActionBar.TabListener {
	private static final String TAG = "MainActivity";
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate: " + savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity2);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		int viewPagerItem = 0;
		if (null != savedInstanceState) {
			viewPagerItem = savedInstanceState.getInt("viewpage-current-item", 0);
		}

		Log.i(TAG, "viewPagerItem: " + viewPagerItem);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.example1) {
			startActivity(new Intent(this, MainActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Log.i(TAG, "onTabSelected: " + tab.getPosition());
		mViewPager.setCurrentItem(tab.getPosition());

		ListFragment1 fragment = mSectionsPagerAdapter.getFragmentAt(tab.getPosition());
		if (null != fragment) {
			fragment.onVisibilityChanged(true);
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Log.i(TAG, "onTabUnselected: " + tab.getPosition());

		ListFragment1 fragment = mSectionsPagerAdapter.getFragmentAt(tab.getPosition());
		if (null != fragment) {
			fragment.onVisibilityChanged(false);
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("viewpage-current-item", mViewPager.getCurrentItem());
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState: " + savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
		final HashMap<Integer, ListFragment1> mFragmentTable;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			mFragmentTable = new HashMap<Integer, ListFragment1>();
		}

		@Override
		public Fragment getItem(int position) {
			ListFragment1 item = ListFragment1.newInstance(position, false);
			return item;
		}

		public ListFragment1 getFragmentAt(int position) {
			return mFragmentTable.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.i(TAG, "destroyItem: " + position);
			super.destroyItem(container, position, object);
			mFragmentTable.remove(position);
		}

		@Override
		public Object instantiateItem(final ViewGroup container, final int position) {
			Log.i(TAG, "instantiateItem: " + position);
			Object object = super.instantiateItem(container, position);
			mFragmentTable.put(position, (ListFragment1) object);
			return object;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
				case 3:
					return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

}
