package it.sephiroth.android.library.floatingmenu;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by alessandro on 24/05/14.
 */
public class FloatingActionItem {
    int id;
    int resId;
    int delay = 0;
    int paddingTop = 0;
    int paddingBottom = 0;
    int paddingLeft = 0;
    int paddingRight = 0;
    int stateListAnimId = 0;
    int backgroundResId;

    static public class Builder {
        int id;
        int resId = -1;
        int delay = 0;
        int paddingTop = 0;
        int paddingBottom = 0;
        int paddingLeft = 0;
        int paddingRight = 0;
        int backgroundResId = 0;
        int stateListAnimResId = 0;

        /**
         * @param id unique id to be used with the
         *           {@link it.sephiroth.android.library.floatingmenu.FloatingActionMenu.OnItemClickListener}
         */
        public Builder(final Context context, int id) {
            this(context, id, 0);
        }

        public Builder(final Context context, int id, int styleId) {
            this.id = id;

            TypedArray array =
                context.getTheme().obtainStyledAttributes(styleId, R.styleable.FloatingActionItem);

            this.backgroundResId = array.getResourceId(
                R.styleable.FloatingActionItem_fam_item_background,
                R.drawable.action_item_background_selector);

            this.resId = array.getResourceId(R.styleable.FloatingActionItem_android_src, 0);
            this.delay = array.getInteger(R.styleable.FloatingActionItem_fam_item_delay, 0);
            this.stateListAnimResId =
                array.getResourceId(R.styleable.FloatingActionItem_fam_item_stateListAnimator, R.anim.action_item_raise);

            int defaultPadding = context.getResources().getDimensionPixelSize(R.dimen.floating_action_item_default_padding);

            int padding = array.getDimensionPixelSize(R.styleable.FloatingActionItem_android_padding, defaultPadding);
            array.recycle();

            withPadding(padding);
        }

        /**
         * Available only for API level 21
         *
         * @param resId
         * @return
         */
        public Builder withStateListAnimator(int resId) {
            this.stateListAnimResId = resId;
            return this;
        }

        /**
         * Assign a custom background resource
         */
        public Builder withBackgroundResId(int resId) {
            this.backgroundResId = resId;
            return this;
        }

        /**
         * Image drawable resource-id
         *
         * @param resId
         * @return
         */
        public Builder withResId(final int resId) {
            this.resId = resId;
            return this;
        }

        /**
         * Show/Hide delay time
         *
         * @param delay
         * @return
         */
        public Builder withDelay(final int delay) {
            this.delay = delay;
            return this;
        }

        /**
         * Image padding
         *
         * @param left
         * @param top
         * @param right
         * @param bottom
         * @return
         */
        public Builder withPadding(int left, int top, int right, int bottom) {
            this.paddingBottom = bottom;
            this.paddingLeft = left;
            this.paddingRight = right;
            this.paddingTop = top;
            return this;
        }

        /**
         * Image padding, applied to all the padding directions
         *
         * @param padding
         * @return
         */
        public Builder withPadding(int padding) {
            return withPadding(padding, padding, padding, padding);
        }

        public FloatingActionItem build() {
            if (resId == -1) {
                throw new IllegalArgumentException("resId missing");
            }
            FloatingActionItem item = new FloatingActionItem();
            item.id = id;
            item.resId = resId;
            item.delay = delay;
            item.paddingBottom = paddingBottom;
            item.paddingLeft = paddingLeft;
            item.paddingRight = paddingRight;
            item.paddingTop = paddingTop;
            item.backgroundResId = backgroundResId;
            item.stateListAnimId = stateListAnimResId;
            return item;
        }
    }
}
