package it.sephiroth.android.library.floatingmenu;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by alessandro on 24/05/14.
 */
final class FloatingActionItemImageView extends ImageView {
	final String TAG = "ActionItemImageView";

	int x1, x2;
	int y1, y2;
	FloatingActionMenu.Direction direction;
	int duration;
	Interpolator interpolator;

	public FloatingActionItemImageView(final Context context) {
		super(context);
	}

	void setDirection(final FloatingActionMenu.Direction direction) {
		this.direction = direction;
	}

	void setAnimationDuration(int ms) {
		this.duration = ms;
	}

	void setAnimationInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
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
		if (direction == FloatingActionMenu.Direction.Vertical) {
			if (! animate) {
				ViewHelper.setTranslationY(this, y1);
			}
			else {
				Animator animator = ObjectAnimator.ofFloat(this, "translationY", y1);
				animator.setStartDelay(delay);
				animator.setDuration(duration);
				animator.setInterpolator(interpolator);
				animator.start();
			}
		}
		else {
			if (! animate) {
				ViewHelper.setTranslationX(this, x1);
			}
			else {
				Animator animator = ObjectAnimator.ofFloat(this, "translationX", x1);
				animator.setStartDelay(delay);
				animator.setDuration(duration);
				animator.setInterpolator(interpolator);
				animator.start();
			}
		}
	}

	public void hide(boolean animate, int delay) {
		if (direction == FloatingActionMenu.Direction.Vertical) {
			if (! animate) {
				ViewHelper.setTranslationY(this, y2);
			}
			else {
				Animator animator = ObjectAnimator.ofFloat(this, "translationY", y2);
				animator.setStartDelay(delay);
				animator.setDuration(duration);
				animator.setInterpolator(interpolator);
				animator.start();
			}
		}
		else {
			if (! animate) {
				ViewHelper.setTranslationX(this, x2);
			}
			else {
				Animator animator = ObjectAnimator.ofFloat(this, "translationX", x2);
				animator.setStartDelay(delay);
				animator.setDuration(duration);
				animator.setInterpolator(interpolator);
				animator.start();
			}
		}
	}

	public FloatingActionItem getItem() {
		return (FloatingActionItem) getTag();
	}

	public void setItem(FloatingActionItem item) {
		setTag(item);
	}
}
