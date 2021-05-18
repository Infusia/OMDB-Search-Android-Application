package ca.mohawk.idfinalproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class FavouritesListAdapter extends SimpleCursorAdapter {
    private Context myContext;
    private int layout;

    ImageView imageView;

    public FavouritesListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to);
        this.myContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView textView = view.findViewById(R.id.favouritesIdTextView);
        ImageView imageView = view.findViewById(R.id.favouritesImageView);
        //Grabbing column title and image to set them in the
        int title = cursor.getColumnIndexOrThrow(SQLClass.TITLE);
        int image = cursor.getColumnIndexOrThrow(SQLClass.IMAGE);
        textView.setText(cursor.getString(title));
        String urlString = cursor.getString(image);
        //Picasso library allows for easy bitmapping and downloading of images into image views
        try {
            URL imageurl = new URL(urlString);
            Picasso.with(view.getContext()).load(String.valueOf(imageurl)).fit().into(imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.favouriteslayout, null);

    }


}







