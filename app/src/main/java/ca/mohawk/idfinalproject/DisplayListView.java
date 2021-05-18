package ca.mohawk.idfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayListView extends AppCompatActivity {
    String jsonData;
    JSONObject jsonObject, secondObject;
    JSONArray jsonArray;
    ListViewAdapter listAdapter;
    ListView listView;
    String titleFrag, actorFrag, plotFrag, posterFrag;
    ArrayList<Movie> theMovies = new ArrayList<>();
    private DrawerLayout myDrawer;

    /**
     * On create
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
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

        listAdapter = new ListViewAdapter(this, R.layout.row_layout);

        listView = findViewById(R.id.listview);

        listView.setAdapter(listAdapter);

        //Receiving JSON data from main activity search
        jsonData = getIntent().getExtras().getString("data");
        try {
            jsonObject = new JSONObject(jsonData);
            int count = 0;
            String title, year, image, id;
            jsonArray = jsonObject.getJSONArray("Search");
            while (count < jsonArray.length()) {
                //Setting the movie objects attributes to match the JSON objects attributes
                JSONObject jo = jsonArray.getJSONObject(count);
                title = jo.getString("Title");
                year = jo.getString("Year");
                image = jo.getString("Poster");
                id = jo.getString("imdbID");
                Movie movie = new Movie(title, year, image, id);
                listAdapter.add(movie);
                theMovies.add(movie);
                count++;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Instantiating a new movie to grab the position of the clicked movie and send the data to the URL
                        Movie listMovie = theMovies.get(position);
                        String imdbID = listMovie.getId();
                        //String used to call api with replaced ID from the list of movies created
                        String URL = "http://www.omdbapi.com/?i=" + imdbID + "&apikey=81dd9b2b";
                        //Request queue which handles the http request asynchronously
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //Creating a new JSON Object to store the values grabbed from the API and send it
                                    //to the movie fragment
                                    secondObject = new JSONObject(response.toString());
                                    titleFrag = secondObject.getString("Title");
                                    actorFrag = secondObject.getString("Actors");
                                    plotFrag = secondObject.getString("Plot");
                                    posterFrag = secondObject.getString("Poster");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Bundle bundle = new Bundle();
                                bundle.putString("title", titleFrag);
                                bundle.putString("actor", actorFrag);
                                bundle.putString("plot", plotFrag);
                                bundle.putString("poster", posterFrag);
                                bundle.putString("imdbid", imdbID);
                                MovieFragment movieFragment = new MovieFragment();
                                movieFragment.show(getSupportFragmentManager(), null);
                                movieFragment.setArguments(bundle);
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.toString();
                                    }
                                });
                        //Add request to queue to allow for multiple http requests and async calls
                        requestQueue.add(objectRequest);

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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