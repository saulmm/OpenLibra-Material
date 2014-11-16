package com.saulmm.openlibra.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.saulmm.openlibra.R;
import com.saulmm.openlibra.fragments.BooksFragment;

/**
 * Created by saulmm on 16/11/14.
 */
public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getActionBar().hide();

        int position = getIntent().getIntExtra("position",0);
        Bitmap bitmap = BooksFragment.photoCache.get(position);
        ImageView bookCover = (ImageView) findViewById(R.id.activity_detail_cover);
        bookCover.setImageBitmap(bitmap);

        bookCover.setTransitionName("cover"+position);





    }
}
