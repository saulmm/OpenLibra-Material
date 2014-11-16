package com.saulmm.openlibra.activities;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.saulmm.openlibra.R;
import com.saulmm.openlibra.fragments.BooksFragment;


public class DetailActivity extends Activity {

    private View fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getActionBar().hide();
        int position = getIntent().getIntExtra("position",0);

        // Get image from cache
        ImageView bookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        bookCover.setImageBitmap(BooksFragment.photoCache.get(position));
        bookCover.setTransitionName("cover" + position);

        // Configure fab
        fabButton = findViewById(R.id.activity_detail_fab);
        fabButton.setScaleX(0);
        fabButton.setScaleY(0);

        // Show FAB after transition
        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {

            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {

                ViewPropertyAnimator propertyAnimator = fabButton.animate().setStartDelay(30)
                        .scaleX(1).scaleY(1);

                propertyAnimator.start();
            }

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        });



    }

    @Override
    public void onBackPressed() {

        ViewPropertyAnimator propertyAnimator = fabButton.animate().setStartDelay(30)
            .scaleX(0).scaleY(0);

        propertyAnimator.start();

        propertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                coolBack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }

    private void coolBack() {

        try {
            super.onBackPressed();

        } catch (NullPointerException e) {

            // workaround
        }

    }
}
