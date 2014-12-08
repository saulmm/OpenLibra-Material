package com.saulmm.openlibra.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;
import com.saulmm.openlibra.OnItemClickListener;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.views.Utils;

import java.util.ArrayList;

/**
 * Created by saulmm on 08/12/14.
 */
public class BookAdapter extends RecyclerView.Adapter<BooksViewHolder> {

    private final ArrayList<Book> books;
    private Context context;
    private int defaultBackgroundcolor;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BookAdapter(ArrayList<Book> books) {

        this.books = books;

    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {

        View rowView = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_book, viewGroup, false);

        this.context = viewGroup.getContext();
        defaultBackgroundcolor = context.getResources().getColor(R.color.book_without_palette);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v, position);
            }
        });

        return new BooksViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final BooksViewHolder booksViewHolder, final int position) {

        final Book currentBook = books.get(position);
        booksViewHolder.bookTitle.setText(currentBook.getTitle());
        booksViewHolder.bookAuthor.setText(currentBook.getAuthor());
        booksViewHolder.bookCover.setDrawingCacheEnabled(true);

        Ion.with(context)
            .load(books.get(position).getCover())
            .intoImageView(booksViewHolder.bookCover)
            .withBitmapInfo()
            .setCallback(new FutureCallback<ImageViewBitmapInfo>() {
                @Override
                public void onCompleted(Exception e, ImageViewBitmapInfo result) {

                    if (e == null && result != null && result.getBitmapInfo().bitmap != null) {

                        Palette.generateAsync(result.getBitmapInfo().bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

                                booksViewHolder.bookTitle.setTextColor(palette.getLightVibrantColor(vibrantSwatch.getTitleTextColor()));
                                booksViewHolder.bookAuthor.setTextColor(palette.getVibrantColor(vibrantSwatch.getTitleTextColor()));
                                booksViewHolder.bookCover.setTransitionName("cover" + position);

                                Utils.animateViewColor(booksViewHolder.bookTextcontainer, defaultBackgroundcolor,
                                        palette.getDarkVibrantColor(vibrantSwatch.getRgb()));
                            }
                        });
                    }
                }
            });
    }

    @Override
    public int getItemCount() {

        return books.size();
    }
}

class BooksViewHolder extends RecyclerView.ViewHolder {

    protected final FrameLayout bookTextcontainer;
    protected final ImageView bookCover;
    protected final TextView bookTitle;
    protected final TextView bookAuthor;

    public BooksViewHolder(View itemView) {

        super(itemView);
        bookTextcontainer = (FrameLayout) itemView.findViewById(R.id.item_book_text_container);
        bookCover = (ImageView) itemView.findViewById(R.id.item_book_img);
        bookTitle = (TextView) itemView.findViewById(R.id.item_book_title);
        bookAuthor = (TextView) itemView.findViewById(R.id.item_book_author);

    }
}

