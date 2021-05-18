package ca.mohawk.idfinalproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DeleteFragment extends DialogFragment {

    private static final String TAG = "";
    String id;

    public DeleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_delete, container, false);
        Button button = v.findViewById(R.id.removeButton);
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        button.setOnClickListener(this::DeleteFromFav);
        return v;
    }
    //Method for allowing users to delete a movie from their favourites list
    public void DeleteFromFav(View v) {
        SQLClass sqlClass = new SQLClass(getContext());
        SQLiteDatabase db = sqlClass.getWritableDatabase();
        //SQL Query
        long row = db.delete(sqlClass.FAVOURITESTABLE, sqlClass.IMDBID + " = ?", new String[]{String.valueOf(id)});

        Log.d(TAG, "row DELETED" + row);
    }
    //Method to refresh the view allowing the user to see the movie has been deleted instead of having to re enter favourites
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent(getContext(), FavouritesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}