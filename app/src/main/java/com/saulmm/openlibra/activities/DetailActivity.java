package com.saulmm.openlibra.activities;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.saulmm.openlibra.R;
import com.saulmm.openlibra.fragments.BooksFragment;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.other.CustomAnimatorListener;
import com.saulmm.openlibra.other.CustomTransitionListener;


public class DetailActivity extends Activity {

    private View fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().hide();

        // Recover items from the intent
        int position = getIntent().getIntExtra("position",0);
        Book selectedBook = (Book) getIntent().getSerializableExtra("selected_book");

        // Recover book cover from BooksFragment cache
        Bitmap bookCoverBitmap = BooksFragment.photoCache.get(position);

        setContentView(R.layout.activity_detail);

        // Get image from cache
        ImageView bookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        bookCover.setImageBitmap(bookCoverBitmap);
        bookCover.setTransitionName("cover" + position);

        // Configure fab
        fabButton = findViewById(R.id.activity_detail_fab);
        fabButton.setScaleX(0);
        fabButton.setScaleY(0);

        // Show FAB after transition
        getWindow().getSharedElementEnterTransition().addListener(new CustomTransitionListener() {

            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);

                ViewPropertyAnimator propertyAnimator = fabButton.animate().setStartDelay(30)
                        .scaleX(1).scaleY(1);

                propertyAnimator.start();
            }
        });
    }

    @Override
    public void onBackPressed() {

        ViewPropertyAnimator propertyAnimator = fabButton.animate().setStartDelay(30)
            .scaleX(0).scaleY(0);

        propertyAnimator.start();
        propertyAnimator.setListener(new CustomAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {

                coolBack();
            }
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
