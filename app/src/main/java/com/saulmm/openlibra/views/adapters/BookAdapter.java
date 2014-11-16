package com.saulmm.openlibra.views.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.saulmm.openlibra.R;
import com.saulmm.openlibra.models.Book;
import com.saulmm.openlibra.views.Utils;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    private final ArrayList<Book> books;
    private final Context context;
    private final int defaultTextColor;
    private final int defaultBackgroundcolor;

    public BookAdapter(ArrayList<Book> books, Context context) {

        this.books = books;
        this.context = context;

        defaultTextColor = context.getResources().getColor(R.color.text_without_palette);
        defaultBackgroundcolor = context.getResources().getColor(R.color.book_without_palette);
    }

    @Override
    public int getCount() {

        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder = null;

        if (convertView == null) {

            v = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
            holder = initViewHolder(v);
            v.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        if (holder != null) {

            Book currentBook = books.get(position);
            holder.bookTitle.setText(currentBook.getTitle());
            holder.bookAuthor.setText(currentBook.getAuthor());
            holder.bookCover.setDrawingCacheEnabled(true);

            final ViewHolder finalHolder = holder;

            Ion.with(context)
                .load(books.get(position).getCover())
                .intoImageView(holder.bookCover)
                .withBitmapInfo();



            try {
                // TODO unify requests
                Ion.with(context)
                    .load(books.get(position).getCover())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {

                        if (e == null && result != null) {

                            Palette.generateAsync(result, new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {

                                finalHolder.bookTitle.setTextColor(palette.getLightVibrantColor(defaultTextColor));
                                finalHolder.bookAuthor.setTextColor(palette.getVibrantColor(defaultTextColor));
                                finalHolder.bookCover.setTransitionName("cover"+position);

//                                // Se the image tint
//                                int darkVibrant = palette.getMutedColor(defaultBackgroundcolor);
//                                finalHolder.bookCover.setColorFilter(Color.argb(100,
//                                    Color.red(darkVibrant),
//                                    Color.green(darkVibrant),
//                                    Color.blue(darkVibrant))); // White Tint

                                // Animate the view color
                                Utils.animateViewColor(finalHolder.bookTextcontainer, defaultBackgroundcolor,
                                    palette.getDarkVibrantColor(defaultBackgroundcolor));
                                }
                            });
                        }
                        }
                    });

           // TODO workaround
            } catch (NullPointerException e) {

                Log.d("[DEBUG]", "BookAdapter getView - Null exception message produced");
            }
        }

        return v;
    }

    private ViewHolder initViewHolder(View v) {

        ViewHolder holder;
        holder = new ViewHolder();
        holder.bookTextcontainer = (FrameLayout) v.findViewById(R.id.item_book_text_container);
        holder.bookCover = (ImageView) v.findViewById(R.id.item_book_img);
        holder.bookTitle = (TextView) v.findViewById(R.id.item_book_title);
        holder.bookAuthor = (TextView) v.findViewById(R.id.item_book_author);
        return holder;
    }


    public static class ViewHolder {

        public FrameLayout bookTextcontainer;
        public TextView bookTitle;
        public TextView bookAuthor;
        public ImageView bookCover;
    }

}
