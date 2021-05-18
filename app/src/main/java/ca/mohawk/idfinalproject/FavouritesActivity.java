package ca.mohawk.idfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    SQLClass sqlClass = new SQLClass(this);
    ListView listView;
    List imdbID = new ArrayList<>();
    ImageView imageView;
    TextView textView;
    String TAG = "===Favourites activity===";
    String Id;
    private DrawerLayout myDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        myDrawer = findViewById(R.id.drawer_layout);


        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle myactionbartoggle = new
                ActionBarDrawerToggle(this, myDrawer,
                (R.string.open), (R.string.close));
        myDrawer.addDrawerListener(myactionbartoggle);
        myactionbartoggle.syncState();

        NavigationView myNavView = (NavigationView)
                findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

    }
    // On resume to allow for seamless screen transitioning when travelling to this activity
    @Override
    protected void onResume() {
        super.onResume();
        //Request queue to handle http request asynchronously
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        textView = findViewById(R.id.favouritesIdTextView);
        imageView = findViewById(R.id.favouritesImageView);
        //instantiate database class to allow for querying
        SQLiteDatabase db = sqlClass.getReadableDatabase();
        listView = findViewById(R.id.favList);
        Cursor myCursor = db.rawQuery("SELECT * FROM favouritestable", null);
        Log.d(TAG, "On Resume: ." + myCursor.getCount());
        FavouritesListAdapter listAdapter = new FavouritesListAdapter(this, R.layout.favouriteslayout, myCursor, new String[]{sqlClass.IMDBID}, new int[]{R.id.favouritesIdTextView}, 0);

        listView.setAdapter(listAdapter);
        //Cursor to travers the database and grab the rows id's
        while (myCursor.moveToNext()) {
            Id = myCursor.getString(myCursor.getColumnIndexOrThrow(sqlClass.IMDBID));
            imdbID.add(Id);

        }
        //If to check if the user currently has any favourites
        if (imdbID.size() == 0) {
            Toast.makeText(getApplicationContext(), "NO FAVOURTIES TO LIST PLEASE ADD SOME", Toast.LENGTH_LONG).show();
        } else {
            String URL = "http://www.omdbapi.com/?i=" + imdbID.get(0) + "&apikey=81dd9b2b";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.toString();
                        }
                    });
            requestQueue.add(objectRequest);

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //onClick to provide the user with a dialog fragment that allows for deleting of a favourite
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteFragment deleteFragment = new DeleteFragment();
                deleteFragment.show(getSupportFragmentManager(), null);
                Bundle bundle = new Bundle();
                //Sending the id of the movie to the fragment to make sure the right movie gets deleted in the list
                bundle.putString("id", String.valueOf(imdbID.get(position)));
                deleteFragment.setArguments(bundle);

            }
        });
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        myDrawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.searchDrawer:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.favouritesDrawer:
                intent = new Intent(this, FavouritesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        item.setChecked(false);
        return false;

    }
    //Method to open drawer and show search and favourites menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
                if (isOpen == true) {
                    myDrawer.closeDrawer(GravityCompat.START);
                } else {
                    myDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}