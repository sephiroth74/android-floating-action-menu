package it.sephiroth.android.library.floatingmenu;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

final class FloatingActionItemImageView extends ImageView {
	final String TAG = "ActionItemImageView";
	static final boolean IS_AT_LEAST_HONEY_COMB = Build.VERSION.SDK_INT >= 11;

	FloatingActionMenu.Direction direction;
	Interpolator interpolator;
	Animator.AnimatorListener mShowListener;
	Animator.AnimatorListener mHideListener;
	ObjectAnimator mShowAnimator;
	ObjectAnimator mHideAnimator;

	/** final x for for horizontal animation */
	int x1;
	/** final y for for vertical animation */
	int y1;

	int x2;
	int y2;

	int duration;

	public FloatingActionItemImageView(final Context context) {
		super(context);

		if (! IS_AT_LEAST_HONEY_COMB) {
			mShowListener = new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(final Animator animation) {
					FloatingActionItemImageView.this.onAnimationStart(0);
				}

				@Override
				public void onAnimationEnd(final Animator animation) {
					FloatingActionItemImageView.this.onAnimationEnd(0);
				}

				@Override
				public void onAnimationCancel(final Animator animation) {}

				@Override
				public void onAnimationRepeat(final Animator animation) {}
			};

			mHideListener = new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(final Animator animation) {
					FloatingActionItemImageView.this.onAnimationStart(1);
				}

				@Override
				public void onAnimationEnd(final Animator animation) {
					FloatingActionItemImageView.this.onAnimationEnd(1);
				}

				@Override
				public void onAnimationCancel(final Animator animation) {}

				@Override
				public void onAnimationRepeat(final Animator animation) {}
			};
		}

		mShowAnimator = new ObjectAnimator();
		mShowAnimator.setTarget(this);
		if (null != mShowListener) {
			mShowAnimator.addListener(mShowListener);
		}

		mHideAnimator = new ObjectAnimator();
		mHideAnimator.setTarget(this);
		if (null != mHideListener) {
			mHideAnimator.addListener(mHideListener);
		}
	}

	void setDirection(final FloatingActionMenu.Direction direction) {
		this.direction = direction;
		this.mShowAnimator.setPropertyName(direction == FloatingActionMenu.Direction.Vertical ? "translationY" : "translationX");
		this.mHideAnimator.setPropertyName(direction == FloatingActionMenu.Direction.Vertical ? "translationY" : "translationX");
	}

	void setAnimationDuration(int ms) {
		this.duration = ms;
		this.mHideAnimator.setDuration(duration);
		this.mShowAnimator.setDuration(duration);
	}

	void setAnimationInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		this.mShowAnimator.setInterpolator(interpolator);
		this.mHideAnimator.setInterpolator(interpolator);
	}

	void setX1(final int x1, final boolean immediate) {
		this.x1 = x1;
		if (immediate) {
			ViewHelper.setTranslationX(this, x1);
		}
	}

	void setX2(final int x2) {
		this.x2 = x2;
	}

	void setY1(final int y1, boolean immediate) {
		this.y1 = y1;
		if (immediate) {
			ViewHelper.setTranslationY(this, y1);
		}
	}

	void setY2(final int y2) {
		this.y2 = y2;
	}

	public void show(boolean animate, int delay) {
		if (mHideAnimator.isStarted()) {
			mHideAnimator.cancel();
		}

		if (direction == FloatingActionMenu.Direction.Vertical) {
			if (! animate) {
				ViewHelper.setTranslationY(this, y1);
				onAnimationEnd(0);
			}
			else {
				if (IS_AT_LEAST_HONEY_COMB) {
					mShowAnimator.setFloatValues(y1);
				}
				else {
					mShowAnimator.setFloatValues(y2, y1);
				}
				mShowAnimator.setStartDelay(delay);
				mShowAnimator.start();
			}
		}
		else {
			if (! animate) {
				ViewHelper.setTranslationX(this, x1);
				onAnimationEnd(0);
			}
			else {
				if (IS_AT_LEAST_HONEY_COMB) {
					mShowAnimator.setFloatValues(x1);
				}
				else {
					mShowAnimator.setFloatValues(x2, x1);
				}
				mShowAnimator.setStartDelay(delay);
				mShowAnimator.start();
			}
		}
	}

	public void hide(boolean animate, int delay) {
		if (mShowAnimator.isStarted()) {
			mShowAnimator.cancel();
		}

		if (direction == FloatingActionMenu.Direction.Vertical) {
			if (! animate) {
				ViewHelper.setTranslationY(this, y2);
				onAnimationEnd(1);
			}
			else {
				if (IS_AT_LEAST_HONEY_COMB) {
					mHideAnimator.setFloatValues(y2);
				}
				else {
					mHideAnimator.setFloatValues(y1, y2);
				}
				mHideAnimator.setStartDelay(delay);
				mHideAnimator.start();
			}
		}
		else {
			if (! animate) {
				ViewHelper.setTranslationX(this, x2);
				onAnimationEnd(1);
			}
			else {
				if (IS_AT_LEAST_HONEY_COMB) {
					mHideAnimator.setFloatValues(x2);
				}
				else {
					mHideAnimator.setFloatValues(x1, x2);
				}
				mHideAnimator.setStartDelay(delay);
				mHideAnimator.start();
			}
		}
	}

	public FloatingActionItem getItem() {
		return (FloatingActionItem) getTag();
	}

	public void setItem(FloatingActionItem item) {
		setTag(item);
	}

	protected void onAnimationStart(int type) {
		if (! IS_AT_LEAST_HONEY_COMB) {
			Log.i(TAG, "onAnimationStart: " + type);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			setLayoutParams(params);

			if(direction == FloatingActionMenu.Direction.Vertical) {
				ViewHelper.setTranslationX(this, x1);
				ViewHelper.setTranslationY(this, type == 0 ? y2 : y1);
			} else {
				ViewHelper.setTranslationX(this, type == 0 ? x2 : x1);
				ViewHelper.setTranslationY(this, y1);
			}
		}
	}

	protected void onAnimationEnd(int type) {
		if (! IS_AT_LEAST_HONEY_COMB) {
			Log.i(TAG, "onAnimationEnd: " + type);

			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();

			ViewHelper.setTranslationX(this, 0);
			ViewHelper.setTranslationY(this, 0);

			if(direction == FloatingActionMenu.Direction.Vertical ) {
				params.setMargins(x1, type == 0 ? y1 : y2, 0, 0);
			} else {
				params.setMargins(type == 0 ? x1 : x2, y1, 0, 0);
			}

			setLayoutParams(params);
		}
	}
}
