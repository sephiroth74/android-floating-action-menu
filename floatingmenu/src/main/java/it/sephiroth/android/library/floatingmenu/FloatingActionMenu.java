package it.sephiroth.android.library.floatingmenu;

import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FloatingActionMenu implements AbsListView.OnScrollListener, View.OnClickListener {
    private static final String TAG = "FloatingMenu";
    final Activity activity;
    final List<FloatingActionItemImageView> itemViews;
    private final ScrollDelegate scrollDelegate;
    private final int verticalPadding;
    private final int horizontalPadding;
    private final int animationDuration;
    private final Interpolator animationInterpolator;
    private final int gravity;
    private Direction direction;
    private OnItemClickListener clickListener;
    private int itemGap;
    private int threshold;
    private boolean visible;
    private ViewGroup root;
    static final boolean LOG_ENABLED = false;

    public FloatingActionMenu(final Builder builder) {
        this.activity = builder.activity;
        this.visible = builder.visible;
        this.threshold = builder.threshold;
        this.itemGap = builder.gap;
        this.verticalPadding = builder.verticalPadding;
        this.horizontalPadding = builder.horitontalPadding;
        this.gravity = builder.gravity;
        this.direction = builder.direction;
        this.animationDuration = builder.animationDuration;
        this.animationInterpolator = builder.interpolator;
        this.scrollDelegate = builder.scrollDelegate;

        if (null != scrollDelegate) {
            scrollDelegate.setOnScrollListener(this);
        }

        this.root = createRoot(this.activity);
        this.itemViews = create(builder.items);
        layout();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }

    /**
     * Hide all the menu items
     *
     * @param animate turn on/off the animation
     */
    public void hide(boolean animate) {
        hide(animate, false);
    }

    /**
     * Show all the menu items
     *
     * @param animate turn on/off the animation
     */
    public void show(boolean animate) {
        show(animate, false);
    }

    /**
     * @param animate
     * @param play_together play all the items together (override the item's delay value)
     * @see #hide(boolean)
     */
    public void hide(boolean animate, boolean play_together) {
        for (FloatingActionItemImageView view : itemViews) {
            view.hide(animate, play_together ? 0 : view.getItem().delay);
        }
        visible = false;
    }

    /**
     * @param animate
     * @param play_together play all the items together (override the item's delay value)
     * @see #show(boolean)
     */
    public void show(boolean animate, boolean play_together) {
        for (FloatingActionItemImageView view : itemViews) {
            view.show(animate, play_together ? 0 : view.getItem().delay);
        }
        visible = true;
    }

    /**
     * Get the current menu visibility
     *
     * @return
     */
    public boolean getVisible() {
        return visible;
    }

    private ViewGroup createRoot(final Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        final ViewGroup viewGroup = new RelativeLayout(activity);
        decorView
            .addView(
                viewGroup,
                1,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return viewGroup;
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    private List<FloatingActionItemImageView> create(final List<FloatingActionItem> items) {
        List<FloatingActionItemImageView> result = new ArrayList<>();

        FrameLayout.LayoutParams params;
        ArrayList<View> focusables = new ArrayList<>();

        for (FloatingActionItem actionItem : items) {
            params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = android.view.Gravity.TOP | android.view.Gravity.LEFT;
            FloatingActionItemImageView view =
                (FloatingActionItemImageView) LayoutInflater.from(activity).inflate(
                    R.layout.floating_action_item_view, root, false);
            view.setImageResource(actionItem.resId);
            view.setPadding(actionItem.paddingLeft, actionItem.paddingTop, actionItem.paddingRight, actionItem.paddingBottom);
            view.setItem(actionItem);

            if (actionItem.backgroundResId != 0) {
                view.setBackgroundResource(actionItem.backgroundResId);
            } else {
                view.setBackgroundResource(R.drawable.action_item_background_selector);
            }

            if (ApiHelper.AT_LEAST_21) {
                if (actionItem.stateListAnimId != 0) {
                    view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(activity, actionItem.stateListAnimId));
                }
            }

            view.setOnClickListener(this);
            root.addView(view, params);
            result.add(view);
            focusables.add(view);
        }
        root.addFocusables(focusables, View.FOCUS_FORWARD);
        //		root.addTouchables(focusables);
        return result;
    }

    private void layout() {
        Resources resources = activity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        boolean measured = false;

        int x = 0;
        int y = 0;

        boolean right = false;
        boolean bottom = false;

        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            x = metrics.widthPixels - horizontalPadding;
            right = true;
        } else if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = horizontalPadding;
        }

        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            y = metrics.heightPixels - verticalPadding;
            bottom = true;
        } else if ((gravity & Gravity.TOP) == Gravity.TOP) {
            y = verticalPadding;
        }

        if ((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL ||
            (gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            int totalWidth = 0;
            int totalHeight = 0;
            for (FloatingActionItemImageView view : itemViews) {
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                totalWidth += view.getMeasuredWidth() + itemGap;
                totalHeight += view.getMeasuredHeight() + itemGap;
            }
            totalWidth -= itemGap;
            totalHeight -= itemGap;

            if ((gravity & Gravity.CENTER_HORIZONTAL) != 0) {
                x = ((metrics.widthPixels - totalWidth) / 2) + horizontalPadding;
            }

            if ((gravity & Gravity.CENTER_VERTICAL) != 0) {
                y = ((metrics.heightPixels - totalHeight) / 2) + verticalPadding;
            }

            measured = true;
        }

        int x1;
        int x2;
        int y1;
        int y2;

        for (FloatingActionItemImageView view : itemViews) {
            if (!measured) {
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            }

            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();

            view.setDirection(direction);
            view.setAnimationDuration(animationDuration);
            view.setAnimationInterpolator(animationInterpolator);

            if (direction == Direction.Vertical) {
                if (bottom) {
                    y1 = metrics.heightPixels - height - verticalPadding;
                    y2 = metrics.heightPixels + height;
                } else {
                    y1 = verticalPadding * 2;
                    y2 = -height - verticalPadding;
                }

                view.setX1(x - (right ? width : 0), true);
                view.setY1(y1, false);
                view.setY2(y2);
            } else {
                if (right) {
                    x1 = metrics.widthPixels - width - horizontalPadding;
                    x2 = metrics.widthPixels + width;
                } else {
                    x1 = horizontalPadding * 2;
                    x2 = -width - horizontalPadding;
                }
                view.setY1(y - (bottom ? height : 0), true);
                view.setX1(x1, false);
                view.setX2(x2);
            }

            if (visible) {
                view.show(false, 0);
            } else {
                view.hide(false, 0);
            }

            if (direction == Direction.Vertical) {
                if (right) {
                    x -= (width + itemGap);
                } else {
                    x += (width + itemGap);
                }
            } else {
                if (bottom) {
                    y -= (height + itemGap);
                } else {
                    y += (height + itemGap);
                }
            }
        }
    }

    View mFirstView;
    boolean mScrollStarted;
    int mFirstVisibleItem = -1;
    int mFirstViewTop;
    int mFirstViewTopAmount;
    int mAnimationDirection;

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        if (scrollState == 1) {
            // scroll starting
            if (view.getChildCount() > 0) {
                mFirstView = view.getChildAt(0);
                if (null == mFirstView) {
                    return;
                }
                mFirstViewTop = mFirstView.getTop();
                mScrollStarted = true;
            }
            mFirstViewTopAmount = 0;
            mAnimationDirection = 0;
        } else if (scrollState == 0) {
            // scroll ended
            mFirstVisibleItem = -1;
            mScrollStarted = false;
        }
    }

    @Override
    public void onScroll(
        final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        if (totalItemCount == 0 || !mScrollStarted) {
            return;
        }

        if (mFirstVisibleItem != firstVisibleItem) {
            mFirstVisibleItem = firstVisibleItem;
            mFirstView = view.getChildAt(0);
            if (null == mFirstView) {
                return;
            }
            mFirstViewTop = mFirstView.getTop();
        }

        int newTop = mFirstView.getTop();
        int delta = (mFirstViewTop - newTop);

        mFirstViewTopAmount += delta;
        mFirstViewTop = newTop;

        if (Math.abs(mFirstViewTopAmount) > threshold) {
            if (mFirstViewTopAmount > 0) {
                if (mAnimationDirection != 1) {
                    hide(true, false);
                }
                mAnimationDirection = 1;
            } else {
                if (mAnimationDirection != -1) {
                    show(true, false);
                }
                mAnimationDirection = -1;
            }
            mFirstViewTopAmount = 0;
        }

    }

    @Override
    public void onClick(final View v) {
        FloatingActionItemImageView view = (FloatingActionItemImageView) v;

        if (null != clickListener) {
            clickListener.onItemClick(this, view.getItem().id);
        }
    }

    /**
     * Remove immediately all the items from their parent.
     */
    public void clear() {
//        Iterator<FloatingActionItemImageView> iterator = itemViews.iterator();
//        while (iterator.hasNext()) {
//            FloatingActionItemImageView item = iterator.next();
//            root.removeView(item);
//            iterator.remove();
//        }

        itemViews.clear();
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.removeView(root);
        root = null;

        if (null != scrollDelegate) {
            scrollDelegate.setOnScrollListener(null);
        }

    }

    public static interface OnItemClickListener {
        void onItemClick(FloatingActionMenu menu, final int id);
    }

    static public class Builder {
        private final List<FloatingActionItem> items = new ArrayList<>();
        private final Activity activity;
        private final Resources res;
        boolean visible;
        int threshold = 36;
        int gap = 12;
        int verticalPadding = 0;
        int horitontalPadding = 0;
        int gravity = Gravity.RIGHT;
        Direction direction = Direction.Vertical;
        ScrollDelegate scrollDelegate;
        int animationDuration = 300;
        Interpolator interpolator = new LinearInterpolator();

        public Builder(Activity context) {
            this(context, 0);
        }

        public Builder(Activity context, int styleId) {
            this.activity = context;
            this.res = activity.getResources();

            Resources.Theme theme = context.getTheme();
            TypedArray array = theme.obtainStyledAttributes(styleId, R.styleable.FloatingActionMenu);

            this.threshold = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_threshold, 36);
            this.gap = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_gap, 12);
            this.horitontalPadding = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_horizontalPadding, 0);
            this.verticalPadding = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_verticalPadding, 0);
            this.gravity = array.getInt(R.styleable.FloatingActionMenu_fam_gravity, Gravity.RIGHT);
            this.direction = Direction.values()[array.getInt(
                R.styleable.FloatingActionMenu_fam_direction,
                Direction.Horizontal.ordinal())];
            this.animationDuration = array.getInt(R.styleable.FloatingActionMenu_android_animationDuration, 200);

            int resId = array.getResourceId(
                R.styleable.FloatingActionMenu_android_interpolator,
                android.R.anim.anticipate_overshoot_interpolator);
            this.interpolator = AnimationUtils.loadInterpolator(activity, resId);
            array.recycle();

            this.visible = true;
        }

        /**
         * Interpolator used for the item's animation
         *
         * @param interpolator
         * @return
         */
        public Builder animationInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        /**
         * Duration for the item's animation
         *
         * @param ms
         * @return
         */
        public Builder animationDuration(int ms) {
            this.animationDuration = ms;
            return this;
        }

        /**
         * Direction of the menu
         *
         * @param direction
         * @return
         */
        public Builder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        /**
         * Menu gravity. Use one of the constants defined in the {@link it.sephiroth.android.library.floatingmenu
         * .FloatingActionMenu.Gravity } class.<br />
         * Both horizontal and vertical gravity should be passed using the '|' bitwise operator
         * Example:<br />
         * <code>
         * .withGravity(Gravity.RIGHT | Gravity.BOTTOM);
         * </code>
         *
         * @param gravity
         * @return
         * @see it.sephiroth.android.library.floatingmenu.FloatingActionMenu.Gravity
         */
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Distance of the menu from the top/bottom border. If gravity is Gravity.BOTTOM then the distance is measured from the
         * screen bottom.
         *
         * @param resId resource-id
         * @return
         */
        public Builder withVerticalPadding(int resId) {
            verticalPadding = res.getDimensionPixelSize(resId);
            return this;
        }

        /**
         * Distance of the menu from the left/right border. If gravity is Gravity.RIGHT then the distance is measured from the
         * screen right.
         *
         * @param resId resource-id
         * @return
         */
        public Builder withHorizontalPadding(int resId) {
            horitontalPadding = res.getDimensionPixelSize(resId);
            return this;
        }

        /**
         * Movement required to hide/show the floating menu
         *
         * @param resId resource-id
         * @return
         */
        public Builder withThreshold(int resId) {
            this.threshold = res.getDimensionPixelSize(resId);
            return this;
        }

        /**
         * Gap distance between items
         *
         * @param resId resource-id
         * @return
         */
        public Builder withGap(int resId) {
            gap = res.getDimensionPixelSize(resId);
            return this;
        }

        /**
         * Set a scroll delegate
         *
         * @param delegate
         * @return
         */
        public Builder withScrollDelegate(ScrollDelegate delegate) {
            this.scrollDelegate = delegate;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder addItem(FloatingActionItem item) {
            items.add(item);
            return this;
        }

        public FloatingActionMenu build() {
            return new FloatingActionMenu(this);
        }
    }

    /**
     * Default scroll delegate abstract class
     */
    public static interface ScrollDelegate {
        void setOnScrollListener(AbsListView.OnScrollListener listener);
    }

    /**
     * Scroll delegate used for all the AbsListView(s)
     */
    public static class AbsListViewScrollDelegate implements ScrollDelegate {
        final AbsListView listView;

        public AbsListViewScrollDelegate(AbsListView listView) {
            this.listView = listView;
        }

        @Override
        public void setOnScrollListener(final AbsListView.OnScrollListener listener) {
            listView.setOnScrollListener(listener);
        }
    }

    static public enum Direction {
        Horizontal, Vertical
    }

    @SuppressWarnings ("unused")
    static public class Gravity {
        public static final int LEFT = 1 << 0;
        public static final int RIGHT = 1 << 1;
        public static final int TOP = 1 << 2;
        public static final int BOTTOM = 1 << 3;
        public static final int CENTER_VERTICAL = 1 << 4;
        public static final int CENTER_HORIZONTAL = 1 << 5;
        public static final int CENTER = CENTER_HORIZONTAL | CENTER_VERTICAL;
    }

    private static class RootTag {
    }
}
