package ca.mohawk.idfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(Movie object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.row_layout, parent, false);
        MovieHolder movieHolder = new MovieHolder();
        Movie movie = (Movie) this.getItem(position);
        // HARD CODE FOR TESTING  String theMoviePoster = "https://m.media-amazon.com/images/M/MV5BMTg1MTY2MjYzNV5BMl5BanBnXkFtZTgwMTc4NTMwNDI@._V1_SX300.jpg";


        movieHolder.title = v.findViewById(R.id.movieTitle);
        movieHolder.year = v.findViewById(R.id.movieRelease);
        movieHolder.image = v.findViewById(R.id.movieImage);
        //Picasso library allowing for bitmapping and image downloading into image views
        try {
            URL imageurl = new URL(movie.getImage());
            Picasso.with(v.getContext()).load(String.valueOf(imageurl)).fit().into(movieHolder.image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //setting text and title with methods from the Movie class using
        //static holder class
        movieHolder.year.setText(movie.getYear());
        movieHolder.title.setText(movie.getTitle());

        v.setTag(movieHolder);
        return v;
    }
    //Movie holder class for allowing method calls to set the text and images
    //of movies in the view
    static class MovieHolder {
        TextView title, year;
        ImageView image;
    }

}
