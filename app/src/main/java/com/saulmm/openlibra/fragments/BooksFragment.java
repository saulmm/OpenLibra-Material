package com.saulmm.openlibra.fragments;


import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.saulmm.openlibra.OnItemClickListener;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.activities.DetailActivity;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.models.BookList;
import com.saulmm.openlibra.network.Api;
import com.saulmm.openlibra.views.adapters.BookAdapter;

import java.io.StringReader;
import java.util.ArrayList;

public class BooksFragment extends Fragment {

    public static SparseArray<Bitmap> photoCache = new SparseArray<Bitmap>(1);

    private ProgressDialog loadingDialog;
    private BookAdapter bookAdapter;
    private ArrayList<Book> books;
    private RecyclerView bookRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        bookRecycler = (RecyclerView) rootView.findViewById(R.id.fragment_last_books_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        bookRecycler.setLayoutManager(gridLayoutManager);

        // Init and show progress dialog
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage(getResources().getString(R.string.loading_books));
        loadingDialog.show();

        // Load books from API
        Ion.with(getActivity())
            .load(Api.getLastBooks())
            .asString()
            .setCallback(booksCallback);

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
            BookList bookList = gson.fromJson(reader, BookList.class);
            books = bookList.getBooks();

            BookAdapter recyclerAdapter = new BookAdapter(books);
            recyclerAdapter.setOnItemClickListener(recyclerRowClickListener);

            // Update adapter
            bookRecycler.setAdapter(recyclerAdapter);

            // Dismiss loading dialog
            loadingDialog.dismiss();


        } else {
            Log.d("[DEBUG]", "BooksFragment onCompleted - ERROR: "+e.getMessage());
        }
        }
    };

    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {

        @Override
        public void onClick(View v, int position) {
            Book selectedBook = books.get(position);

            Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra("position", position);
            detailIntent.putExtra("selected_book", selectedBook);

            ImageView coverImage = (ImageView) v.findViewById(R.id.item_book_img);
            ((ViewGroup) coverImage.getParent()).setTransitionGroup(false);
            photoCache.put(position, coverImage.getDrawingCache());

            // Setup the transition to the detail activity
            ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, "cover" + position);
            startActivity(detailIntent, options.toBundle());

        }
    };
}
