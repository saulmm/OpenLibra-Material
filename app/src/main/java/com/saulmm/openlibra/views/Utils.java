package com.saulmm.openlibra.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.PathInterpolator;

@SuppressWarnings("UnnecessaryLocalVariable")
public class Utils {

    public final static int COLOR_ANIMATION_DURATION = 1000;
    public final static int DEFAULT_DELAY = 0;

    /**
     * Change the color of a view with an animation
     *
     * @param v the view to change the color
     * @param startColor the color to start animation
     * @param endColor the color to end the animation
     */
    public static void animateViewColor (View v, int startColor, int endColor) {

        ObjectAnimator animator = ObjectAnimator.ofObject(v, "backgroundColor",
            new ArgbEvaluator(), startColor, endColor);

        animator.setInterpolator(new PathInterpolator(0.4f,0f,1f,1f));
        animator.setDuration(COLOR_ANIMATION_DURATION);
        animator.start();
    }

    /**
     * Scale and set the pivot when the animation will start from
     *
     * @param v the view to set the pivot
     */
    public static void configureHideYView(View v) {

        v.setScaleY(0);
        v.setPivotY(0);
    }

    /**
     * Reduces the X & Y from a view
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator hideViewByScaleXY(View v) {

        return hideViewByScale(v, DEFAULT_DELAY, 0, 0);
    }

    /**
     * Reduces the Y from a view
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator hideViewByScaleY(View v) {

        return hideViewByScale(v, DEFAULT_DELAY, 1, 0);
    }

    /**
     * Reduces the X from a view
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator hideViewByScalyInX(View v) {

        return hideViewByScale(v, DEFAULT_DELAY, 0, 1);
    }

    /**
     * Reduces the X & Y
     *
     * @param v the view to be scaled
     * @param delay to start the animation
     * @param x integer to scale
     * @param y integer to scale
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    private static ViewPropertyAnimator hideViewByScale (View v, int delay, int x, int y) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(delay)
          .scaleX(x).scaleY(y);

        return propertyAnimator;
    }

    /**
     * Shows a view by scaling
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator showViewByScale (View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY)
            .scaleX(1).scaleY(1);

        return propertyAnimator;
    }
}
