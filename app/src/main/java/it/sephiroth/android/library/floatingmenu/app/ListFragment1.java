package it.sephiroth.android.library.floatingmenu.app;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;

public class ListFragment1 extends ListFragment implements FloatingActionMenu.OnItemClickListener {
	private static final String TAG = "ListFragment";

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String ARG_SECTION_VISIBLE = "section_visible";

	private FloatingActionMenu mFloatingMenu;
	private int sectionNumber;

	public static ListFragment1 newInstance(int sectionNumber, boolean visible) {
		Log.i(TAG, "newInstance");
		ListFragment1 fragment = new ListFragment1();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		args.putBoolean(ARG_SECTION_VISIBLE, visible);
		fragment.setArguments(args);
		return fragment;
	}

	public ListFragment1() {}

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
		Log.i(TAG, "onCreateView: " + sectionNumber);

		View rootView = inflater.inflate(R.layout.fragment_main_activity2, container, false);
		List<String> data = createData();
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, data));
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (null != mFloatingMenu) {
			mFloatingMenu.clear();
		}
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i(TAG, "onViewCreated: " + sectionNumber);
		initFloatingMenu(sectionNumber, savedInstanceState);

		if (null != mFloatingMenu) {
			view.postDelayed(new Runnable() {
				@Override
				public void run() {
					int currentItem = ((MainActivity2) getActivity()).mViewPager.getCurrentItem();
					if (currentItem == sectionNumber) {
						mFloatingMenu.show(false);
					}
				}
			}, 20);
		}
	}


	private void initFloatingMenu(final int sectionNumber, final Bundle savedInstanceState) {
		int currentItem = ((MainActivity2) getActivity()).mViewPager.getCurrentItem();

		if (sectionNumber == 0 || sectionNumber == 1) {

			Resources res = getResources();
			int action_item_padding = res.getDimensionPixelSize(R.dimen.float_action_item_padding);

			//@formatter:off
			FloatingActionItem item1 = new FloatingActionItem.Builder(0)
				.withResId(R.drawable.ic_facebook)
				.withDelay(50)
				.withPadding(action_item_padding)
				.build();

			FloatingActionItem item2 = new FloatingActionItem.Builder(1)
				.withResId(R.drawable.ic_twitter)
				.withDelay(100)
				.withPadding(action_item_padding)
				.build();

			FloatingActionItem item3 = new FloatingActionItem.Builder(1)
				.withResId(R.drawable.ic_facebook)
				.withDelay(0)
				.withPadding(action_item_padding)
				.build();

			FloatingActionItem item4 = new FloatingActionItem.Builder(1)
				.withResId(R.drawable.ic_instagram)
				.withDelay(100)
				.withPadding(action_item_padding)
				.build();

			FloatingActionMenu.Builder builder = new FloatingActionMenu
				.Builder(getActivity())
				.withScrollDelegate(new FloatingActionMenu.AbsListViewScrollDelegate(getListView()))
				.withThreshold(R.dimen.float_action_threshold)
				.withGap(R.dimen.float_action_item_gap)
				.withHorizontalPadding(R.dimen.float_action_h_padding)
				.withVerticalPadding(R.dimen.float_action_v_padding)

				.withGravity(
					sectionNumber == 1 ?
					FloatingActionMenu.Gravity.RIGHT | FloatingActionMenu.Gravity.BOTTOM :
				    FloatingActionMenu.Gravity.RIGHT | FloatingActionMenu.Gravity.BOTTOM
				)

				.withDirection(
					sectionNumber == 1 ?
						FloatingActionMenu.Direction.Vertical :
				        FloatingActionMenu.Direction.Horizontal
				)
				.animationDuration(200)
				.animationInterpolator(new AccelerateDecelerateInterpolator())
				//.withState(savedInstanceState)
				.visible(false);

			if(sectionNumber==0){
				builder.addItem(item1);
//				builder.addItem(item2);
			} else {
				builder.addItem(item3);
				builder.addItem(item4);
			}

			mFloatingMenu = builder.build();

			//@formatter:on
			mFloatingMenu.setOnItemClickListener(this);
		}
	}

	private Boolean visible;

	boolean getVisible() {
		if (null != visible) {
			return visible.booleanValue();
		}
		return false;
	}

	public void onVisibilityChanged(boolean visible) {
		if (null == this.visible || this.visible != visible) {
			this.visible = visible;
			if (null != mFloatingMenu) {
				Log.i(TAG, "onVisibilityChanged: " + sectionNumber + ", visible: " + visible);

				if (visible) {
					mFloatingMenu.show(true, true);
				}
				else {
					mFloatingMenu.hide(true, true);
				}
			}
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		Log.i(TAG, "onSaveInstanceState: " + sectionNumber);
		super.onSaveInstanceState(outState);
	}

	private List<String> createData() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			data.add(String.format("fragment %d - item %d", sectionNumber, i));
		}
		return data;
	}

	@Override
	public void onItemClick(final FloatingActionMenu menu, final int id) {
		Log.i(TAG, "onItemClick: " + id);

		//@formatter:off
		new AlertDialog.Builder(getActivity())
			.setTitle("Item Click")
			.setMessage("Clicked item with id: " + id)
			.show();
		//@formatter:on

	}
}
