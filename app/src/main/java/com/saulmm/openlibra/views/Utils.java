package com.saulmm.openlibra.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.PathInterpolator;


public class Utils {

    public final static int COLOR_ANIMATION_DURATION = 1000;

    public static void animateViewColor (View v, int startColor, int endColor) {

        ObjectAnimator animator = ObjectAnimator.ofObject(v, "backgroundColor",
            new ArgbEvaluator(), startColor, endColor);

        animator.setInterpolator(new PathInterpolator(0.4f,0f,1f,1f));
        animator.setDuration(COLOR_ANIMATION_DURATION);
        animator.start();
    }
}
