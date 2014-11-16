package com.saulmm.openlibra.fragments;


import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.activities.DetailActivity;
import com.saulmm.openlibra.models.BookList;
import com.saulmm.openlibra.network.Api;
import com.saulmm.openlibra.views.adapters.BookAdapter;

import java.io.StringReader;

public class BooksFragment extends Fragment {

    public static SparseArray<Bitmap> photoCache = new SparseArray<Bitmap>(1);


    private ProgressDialog loadingDialog;
    private GridView bookGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookGrid = (GridView) rootView.findViewById(R.id.fragment_last_books_grid);

        // Init and show progress dialog
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage(getResources().getString(R.string.loading_books));
        loadingDialog.show();

        // Load books from API
        Ion.with(getActivity())
            .load(Api.getLastBooks())
            .asString()
            .setCallback(booksCallback);


        // Set gridListener
        bookGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("position", position);

                ImageView coverImage = (ImageView) view.findViewById(R.id.item_book_img);
                ((ViewGroup) coverImage.getParent()).setTransitionGroup(false);


                photoCache.put(position, coverImage.getDrawingCache());


                // Setup the transition to the detail activity
                ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "cover" + position);
                startActivity(detailIntent, options.toBundle());

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private FutureCallback<String> booksCallback = new FutureCallback<String>() {

        @Override
        public void onCompleted(Exception e, String result) {

        if (e == null) {

            // Set the malformed JSON as lenient JsonReader
            result = Api.cleanJSON(result);
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);

            // Serialize reader into objects
            Gson gson = new Gson();
            BookList books = gson.fromJson(reader, BookList.class);

            // Update adapter
            bookGrid.setAdapter(new BookAdapter(books.getBooks(), getActivity()));
            bookGrid.deferNotifyDataSetChanged();

            // Dismiss loading dialog
            loadingDialog.dismiss();


        } else {
            Log.d("[DEBUG]", "BooksFragment onCompleted - ERROR: "+e.getMessage());
        }
        }
    };
}
