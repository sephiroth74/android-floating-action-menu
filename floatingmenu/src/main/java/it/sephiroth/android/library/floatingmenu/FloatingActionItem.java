package it.sephiroth.android.library.floatingmenu;

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
	int backgroundResId;

	static public class Builder {
		int id;
		int resId = - 1;
		int delay = 0;
		int paddingTop = 0;
		int paddingBottom = 0;
		int paddingLeft = 0;
		int paddingRight = 0;
		int backgroundResId = 0;

		/**
		 * @param id unique id to be used with the
		 *           {@link it.sephiroth.android.library.floatingmenu.FloatingActionMenu.OnItemClickListener}
		 */
		public Builder(int id) {
			this.id = id;
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
		 * @param resId
		 * @return
		 */
		public Builder withResId(final int resId) {
			this.resId = resId;
			return this;
		}

		/**
		 * Show/Hide delay time
		 * @param delay
		 * @return
		 */
		public Builder withDelay(final int delay) {
			this.delay = delay;
			return this;
		}

		/**
		 * Image padding
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
		 * @param padding
		 * @return
		 */
		public Builder withPadding(int padding) {
			return withPadding(padding, padding, padding, padding);
		}

		public FloatingActionItem build() {
			if (resId == - 1) throw new IllegalArgumentException("resId missing");
			FloatingActionItem item = new FloatingActionItem();
			item.id = id;
			item.resId = resId;
			item.delay = delay;
			item.paddingBottom = paddingBottom;
			item.paddingLeft = paddingLeft;
			item.paddingRight = paddingRight;
			item.paddingTop = paddingTop;
			item.backgroundResId = backgroundResId;
			return item;
		}
	}
}
