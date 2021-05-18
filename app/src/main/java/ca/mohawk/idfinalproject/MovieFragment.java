package ca.mohawk.idfinalproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;


public class MovieFragment extends DialogFragment {

    private static final String TAG = "===Movie Fragment===";
    TextView title, actors, plot;
    ImageView poster;
    String mTitle, mActors, mPlot, mPoster, mImdbId;
    Button button;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Oncreate HIT!");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        title = v.findViewById(R.id.movieTitleFrag);
        actors = v.findViewById(R.id.movieActorFrag);
        plot = v.findViewById(R.id.movePlotFrag);
        poster = v.findViewById(R.id.dialogImageView);
        button = v.findViewById(R.id.addToFavouritesButton);

        //Creating bundle to send to the movie fragment to query api
        Bundle bundle = this.getArguments();
        mTitle = bundle.getString("title");
        mActors = bundle.getString("actor");
        mPlot = bundle.getString("plot");
        mPoster = bundle.getString("poster");
        mImdbId = bundle.getString("imdbid");

        button.setOnClickListener(this::AddToFav);

        title.setText(mTitle);
        actors.setText(mActors);
        plot.setText(mPlot);
        try {
            //Picasso library to allow for downloading and bitmapping of images into image views
            URL imageurl = new URL(mPoster);
            Picasso.with(v.getContext()).load(String.valueOf(imageurl)).fit().into(poster);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return v;
    }
    //Method to allow user to save movie to favourites and store in database
    public void AddToFav(View v) {
        SQLClass sqlClass = new SQLClass(getContext());
        SQLiteDatabase db = sqlClass.getWritableDatabase();
        //Storing data in database columns for later use with favourites activity
        ContentValues values = new ContentValues();
        values.put(sqlClass.IMDBID, mImdbId);
        values.put(sqlClass.TITLE, mTitle);
        values.put(sqlClass.IMAGE, mPoster);
        long row = db.insert(sqlClass.FAVOURITESTABLE, null, values);
        Log.d(TAG, "AddToFav: " + row);
        Toast.makeText(getView().getContext(), "Successfully added movie to favourites!", Toast.LENGTH_LONG).show();
    }
}