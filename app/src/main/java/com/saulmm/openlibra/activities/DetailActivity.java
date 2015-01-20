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

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivity extends Activity {

    // UI Stuff
    @InjectView(R.id.card_view)                         FrameLayout contentCard;
    @InjectView(R.id.activity_detail_fab)               View fabButton;
    @InjectView(R.id.activity_detail_main_container)    View mainContaienr;
    @InjectView(R.id.activity_detail_titles_container)  View titlesContainer;
    @InjectView(R.id.activity_detail_toolbar)           Toolbar toolbar;
    @InjectView(R.id.activity_detail_book_info)         LinearLayout bookInfoLayout;
    @InjectView(R.id.activity_detail_content)           TextView contentTextView;
    @InjectView(R.id.activity_detail_rating_title)      TextView ratingTextView;
    @InjectView(R.id.activity_detail_rating_value)      TextView ratingValueTextView;
    @InjectView(R.id.activity_detail_summary_title)     TextView summaryTitle;
    @InjectView(R.id.activity_detail_title)             TextView titleTextView;
    @InjectView(R.id.activity_detail_subtitle)          TextView subtitleTextView;

    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.inject(this);

        // Recover items from the intent
        final int position = getIntent().getIntExtra("position", 0);
        selectedBook = (Book) getIntent().getSerializableExtra("selected_book");

        // Recover book cover from BooksFragment cache
        Bitmap bookCoverBitmap = BooksFragment.photoCache.get(position);
        ImageView toolbarBookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        toolbarBookCover.setImageBitmap(bookCoverBitmap);

        // Fab button
        fabButton.setScaleX(0);
        fabButton.setScaleY(0);

        // Book summary card
        Utils.configuredHideYView(contentCard);

        // Book info layout
        Utils.configuredHideYView(bookInfoLayout);

        // Title container
        Utils.configuredHideYView(mainContaienr);

        // Define toolbar as the shared element
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
     * <p/>
     * Animations order
     * <p/>
     * 1. The image is animated automatically by the SharedElementTransition
     * 2. The layout that contains the titles
     * 3. An alpha transition to show the text of the titles
     * 3. A scale animation to show the book info
     */
    private CustomTransitionListener sharedTransitionListener = new CustomTransitionListener() {

        @Override
        public void onTransitionEnd(Transition transition) {

            super.onTransitionEnd(transition);

            ViewPropertyAnimator showTitleAnimator = Utils.showViewByScale(mainContaienr);
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

                ViewPropertyAnimator hideFabAnimator = Utils.hideViewByScaleY(mainContaienr);
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
                mainContaienr.setBackgroundColor(vibrantSwatch.getRgb());

                getWindow().setStatusBarColor(vibrantSwatch.getRgb());
                getWindow().setNavigationBarColor(vibrantSwatch.getRgb());

                titleTextView.setText(selectedBook.getTitle());
                contentTextView.setText(selectedBook.getContent());
                subtitleTextView.setTextColor(vibrantSwatch.getTitleTextColor());
                subtitleTextView.setText(selectedBook.getAuthor());
                ratingValueTextView.setText(selectedBook.getRating() + " / 10");

                summaryTitle.setTextColor(vibrantSwatch.getRgb());
                titleTextView.setTextColor(vibrantSwatch.getTitleTextColor());
                subtitleTextView.setTextColor(vibrantSwatch.getTitleTextColor());
                ratingTextView.setTextColor(vibrantSwatch.getTitleTextColor());
                ratingTextView.setTextColor(vibrantSwatch.getRgb());
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
