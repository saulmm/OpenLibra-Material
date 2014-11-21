package com.saulmm.openlibra.activities;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.saulmm.openlibra.ObservableScrollView;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.ScrollViewListener;
import com.saulmm.openlibra.fragments.BooksFragment;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.other.CustomAnimatorListener;
import com.saulmm.openlibra.other.CustomTransitionListener;


@SuppressWarnings("UnusedDeclaration")
public class DetailActivity extends Activity {


    private int toolbarHeight;
    private View fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Recover items from the intent
        int position = getIntent().getIntExtra("position",0);
        Book selectedBook = (Book) getIntent().getSerializableExtra("selected_book");

        fabButton = findViewById(R.id.activity_detail_fab);
        fabButton.setScaleX(0);
        fabButton.setScaleY(0);

        // Recover book cover from BooksFragment cache
        Bitmap bookCoverBitmap = BooksFragment.photoCache.get(position);
        final ImageView toolbarBookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        toolbarBookCover.setImageBitmap(bookCoverBitmap);

        // Define toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        toolbar.setBackground(new BitmapDrawable(getResources(), bookCoverBitmap));
        toolbar.setTransitionName("cover" + position);

        Palette.generateAsync(bookCoverBitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
//            fabButton.setBackgroundColor(palette.getVibrantColor(Color.MAGENTA));
            }
        });

        // Add a listener when the transition ends to animate the fab button
        getWindow().getSharedElementEnterTransition().addListener(transitionListener);

        toolbarHeight = 0;

        final RelativeLayout parentRelative = (RelativeLayout) findViewById(R.id.parent_relative);

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });

        final int mActionBarSize = (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewListener(new ScrollViewListener() {
            public boolean isScrollingDown;

            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (toolbarHeight == 0) {
                    toolbarHeight = toolbar.getHeight();
                }

                if (y < 400) {

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            toolbar.getWidth(), (toolbarHeight - y));

                    toolbar.setLayoutParams(params);
                    isScrollingDown = (y > oldy);

                } else {

                    Log.d("[DEBUG]", "DetailActivity onScrollChanged - No more®");

                }
            }
        });
    }

    private CustomTransitionListener transitionListener = new CustomTransitionListener() {

        @Override
        public void onTransitionEnd(Transition transition) {
            super.onTransitionEnd(transition);

            ViewPropertyAnimator propertyAnimator = fabButton.animate().setStartDelay(30)
                    .scaleX(1).scaleY(1);

            propertyAnimator.start();
        }
    };

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

            // TODO: workaround
        }

    }
}
