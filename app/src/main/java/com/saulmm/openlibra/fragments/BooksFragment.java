package com.saulmm.openlibra.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.models.BookList;
import com.saulmm.openlibra.network.Api;
import com.saulmm.openlibra.views.adapters.BookAdapter;

import java.io.StringReader;

public class BooksFragment extends Fragment {

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

        Log.d("[DEBUG]", "BooksFragment onCreateView - "+Api.getLastBooks());

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
