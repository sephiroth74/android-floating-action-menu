package it.sephiroth.android.library.floatingmenu.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

        if (sectionNumber < 2) {

            FloatingActionItem item1 = new FloatingActionItem.Builder(getActivity(), 0, R.style.FloatingActionMenuItemStyle)
                .withResId(R.drawable.ic_facebook)
                .withDelay(50)
                .build();

            FloatingActionItem item2 = new FloatingActionItem.Builder(getActivity(), 1, R.style.FloatingActionMenuItemStyle)
                .withResId(R.drawable.ic_twitter)
                .withDelay(100)
                .build();

            FloatingActionItem item3 = new FloatingActionItem.Builder(getActivity(), 2, R.style.FloatingActionMenuItemStyle)
                .withResId(R.drawable.ic_facebook)
                .withDelay(0)
                .build();

            FloatingActionItem item4 = new FloatingActionItem.Builder(getActivity(), 3, R.style.FloatingActionMenuItemStyle)
                .withResId(R.drawable.ic_instagram)
                .withDelay(100)
                .build();

            FloatingActionMenu.Builder builder = new FloatingActionMenu
                .Builder(
                getActivity(), sectionNumber == 1 ? R.style.FloatingActionMenuStyle : R.style
                .FloatingActionMenuStyle_Horizontal)
                .withScrollDelegate(new FloatingActionMenu.AbsListViewScrollDelegate(getListView()))
                .visible(false);

            if (sectionNumber == 0) {
                builder.addItem(item1);
            } else {
                builder.addItem(item3);
                builder.addItem(item4);
            }

            mFloatingMenu = builder.build();

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
