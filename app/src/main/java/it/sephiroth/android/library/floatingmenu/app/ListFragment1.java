package it.sephiroth.android.library.floatingmenu.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu.Gravity;

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
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, data));
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
            view.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = ((MainActivity2) getActivity()).mViewPager.getCurrentItem();
                        if (currentItem == sectionNumber) {
                            mFloatingMenu.show(true, true);
                        }
                    }
                }, 400);
        }
    }

    private void initFloatingMenu(final int sectionNumber, final Bundle savedInstanceState) {
        int currentItem = ((MainActivity2) getActivity()).mViewPager.getCurrentItem();

        FloatingActionMenu.Builder builder = createMenu(sectionNumber);

        FloatingActionItem item1 = new FloatingActionItem.Builder(getActivity(), 0, R.style.FloatingActionMenuItemStyle)
            .withResId(R.drawable.ic_facebook)
            .build();

        FloatingActionItem item2 = new FloatingActionItem.Builder(getActivity(), 1, R.style.FloatingActionMenuItemStyle)
            .withResId(R.drawable.ic_twitter)
            .withDelay(sectionNumber == 3 ? 50 : 0)
            .build();

        FloatingActionItem item3 = new FloatingActionItem.Builder(getActivity(), 2, R.style.FloatingActionMenuItemStyle)
            .withResId(R.drawable.ic_facebook)
            .withDelay(sectionNumber == 3 ? 100 : 0)
            .build();

        FloatingActionItem item4 = new FloatingActionItem.Builder(getActivity(), 3, R.style.FloatingActionMenuItemStyle)
            .withResId(R.drawable.ic_instagram)
            .withDelay(sectionNumber == 3 ? 150 : 0)
            .build();

        if (sectionNumber == 0) {
            builder.addItem(item1);
        } else if (sectionNumber == 1) {
            builder.addItem(item1).addItem(item2);
        } else if (sectionNumber == 2) {
            builder.addItem(item1).addItem(item2).addItem(item3);
        } else if (sectionNumber == 3) {
            builder.addItem(item1).addItem(item2).addItem(item3).addItem(item4);
        }

        mFloatingMenu = builder.build();
        mFloatingMenu.setOnItemClickListener(this);
    }

    private FloatingActionMenu.Builder createMenu(int sectionNumber) {
        FloatingActionMenu.Builder builder = new FloatingActionMenu
            .Builder(
            getActivity(), sectionNumber < 2 ? R.style.FloatingActionMenuStyle_Horizontal : R.style.FloatingActionMenuStyle)
            .withScrollDelegate(new FloatingActionMenu.AbsListViewScrollDelegate(getListView()))
            .animationInterpolator(new OvershootInterpolator())
            .visible(false);

        if (sectionNumber == 0) {
            builder.withGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        } else if (sectionNumber == 1) {
            builder.withGravity(Gravity.RIGHT | Gravity.BOTTOM);
        } else if (sectionNumber == 2) {
            builder.withGravity(Gravity.RIGHT | Gravity.BOTTOM);
        } else {
            builder.withGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }

        return builder;
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
                } else {
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
        Toast.makeText(getActivity(), "clicked item " + id, Toast.LENGTH_SHORT).show();
    }
}
