package com.saulmm.openlibra.activities;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saulmm.openlibra.R;
import com.saulmm.openlibra.fragments.BooksFragment;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.other.CustomAnimatorListener;
import com.saulmm.openlibra.other.CustomTransitionListener;
import com.saulmm.openlibra.views.Utils;


public class DetailActivity extends Activity {

    // UI Stuff
    private FrameLayout contentCard;
    private View fabButton;
    private View titleContainer;
    private View titlesContainer;
    private Book selectedBook;
    private LinearLayout bookInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Recover items from the intent
        final int position = getIntent().getIntExtra("position",0);
        selectedBook = (Book) getIntent().getSerializableExtra("selected_book");

        titlesContainer = findViewById(R.id.activity_detail_titles);

        // Recover book cover from BooksFragment cache
        Bitmap bookCoverBitmap = BooksFragment.photoCache.get(position);
        ImageView toolbarBookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        toolbarBookCover.setImageBitmap(bookCoverBitmap);

        // Fab button
        fabButton = findViewById(R.id.activity_detail_fab);
        fabButton.setScaleX(0);
        fabButton.setScaleY(0);

        // Book summary card
        contentCard = (FrameLayout) findViewById(R.id.card_view);
        Utils.configuredHideYView(contentCard);

        // Book info layout
        bookInfoLayout = (LinearLayout) findViewById(R.id.activity_detail_book_info);
        Utils.configuredHideYView(bookInfoLayout);

        // Title container
        titleContainer =  findViewById(R.id.activity_detail_title_container);
        Utils.configuredHideYView(titleContainer);

        // Define toolbar as the shared element
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        toolbar.setBackground(new BitmapDrawable(getResources(), bookCoverBitmap));
        toolbar.setTransitionName("cover" + position);

        // Add a listener to get noticed when the transition ends to animate the fab button
        getWindow().getSharedElementEnterTransition().addListener(sharedTransitionListener);

        // Generate palette colors
        Palette.generateAsync(bookCoverBitmap, paletteListener);
    }


    /**
     * I use a listener to get notified when the enter transition ends, and with that notifications
     * build my own coreography built with the elements of the UI
     *
     * Animations order
     *
     * 1. The image is animated automatically by the SharedElementTransition
     * 2. The layout that contains the titles
     * 3. An alpha transition to show the text of the titles
     * 3. A scale animation to show the book info
     */
    private CustomTransitionListener sharedTransitionListener = new CustomTransitionListener() {

        @Override
        public void onTransitionEnd(Transition transition) {

        super.onTransitionEnd(transition);

        ViewPropertyAnimator showTitleAnimator = Utils.showViewByScale(titleContainer);
        showTitleAnimator.setListener(new CustomAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {

            super.onAnimationEnd(animation);
            titlesContainer.startAnimation(AnimationUtils.loadAnimation(DetailActivity.this, R.anim.alpha_on));
            titlesContainer.setVisibility(View.VISIBLE);

            Utils.showViewByScale(fabButton).start();
            Utils.showViewByScale(bookInfoLayout).start();
            }
        });

        showTitleAnimator.start();
        }
    };

    @Override
    public void onBackPressed() {

        ViewPropertyAnimator hideTitleAnimator = Utils.hideViewByScaleXY(fabButton);

        titlesContainer.startAnimation(AnimationUtils.loadAnimation(DetailActivity.this, R.anim.alpha_off));
        titlesContainer.setVisibility(View.INVISIBLE);

        Utils.hideViewByScaleY(bookInfoLayout);

        hideTitleAnimator.setListener(new CustomAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {

            ViewPropertyAnimator hideFabAnimator = Utils.hideViewByScaleY(titleContainer);
            hideFabAnimator.setListener(new CustomAnimatorListener() {

                @Override
                public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                coolBack();
                }
            });
            }
        });

        hideTitleAnimator.start();
    }

    private Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {

        @Override
        public void onGenerated(Palette palette) {

        if (palette.getVibrantSwatch() != null) {

            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            titleContainer.setBackgroundColor(vibrantSwatch.getRgb());

            getWindow().setStatusBarColor(vibrantSwatch.getRgb());
            getWindow().setNavigationBarColor(vibrantSwatch.getRgb());


            TextView contentTextView = (TextView) findViewById(R.id.activity_detail_content);
            String content = selectedBook.getContent();
            contentTextView.setText(content);

            TextView ratingTextView = (TextView) findViewById(R.id.activity_detail_rating_title);
            ratingTextView.setTextColor(vibrantSwatch.getTitleTextColor());
            ratingTextView.setTextColor(vibrantSwatch.getRgb());

            TextView ratingValueTextView = (TextView) findViewById(R.id.activity_detail_rating_value);
            ratingValueTextView.setText(selectedBook.getRating() + " / 10");

            TextView summaryTitle = (TextView) findViewById(R.id.activity_detail_summary_title);
            summaryTitle.setTextColor(vibrantSwatch.getRgb());

            TextView titleTV = (TextView) titleContainer.findViewById(R.id.activity_detail_title);
            titleTV.setTextColor(vibrantSwatch.getTitleTextColor());
            titleTV.setText(selectedBook.getTitle());

            TextView subtitleTV = (TextView) titleContainer.findViewById(R.id.activity_detail_subtitle);
            subtitleTV.setTextColor(vibrantSwatch.getTitleTextColor());
            subtitleTV.setText(selectedBook.getAuthor());

            ((TextView) titleContainer.findViewById(R.id.activity_detail_subtitle))
                .setTextColor(vibrantSwatch.getTitleTextColor());
        }
        }
    };

    private void coolBack() {

        try {
            super.onBackPressed();

        } catch (NullPointerException e) {

            // TODO: workaround
        }

    }
}
