package ca.mohawk.idfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String TAG = "===Main Activity===";
    String resultFromApi;
    Button button;
    EditText movie, year;
    Activity currentActivity = null;
    JSONObject jsonObject;
    private DrawerLayout myDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Hit!");
        super.onCreate(savedInstanceState);
        /**Check if user is connected to internet, if they are we present them the layout
            if not we show them a dialog to connect to the internet and retry
         */
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {

        setContentView(R.layout.activity_main);
        myDrawer = findViewById(R.id.drawer_layout);
        currentActivity = this;

        button = findViewById(R.id.searchButton);
        movie = findViewById(R.id.movieEditText);
        year = findViewById(R.id.yearEditText);
        button.setOnClickListener(this::parse);

        //Action bar to allow for interaction with the hamburger button
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
    }
    /**Method to allow the user to search for movies by querying the api with a
        title of the movie and the release year of the movie
    */
    public void parse(View v) {

        String movieText = movie.getText().toString();
        movieText = movieText.replace(' ', '+');
        //Request queue to handle https request asynchronously
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://www.omdbapi.com/?apikey=81dd9b2b&s=" + movieText + "&y=" + year.getText().toString() + "&type=movie";


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Response for the API call storing into json object which will later
                         //be used to store movie details into

                        String theResponse = "";
                        try {
                            jsonObject = new JSONObject(response.toString());
                            theResponse = jsonObject.getString("Response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, response.toString());

                        resultFromApi = response.toString();
                        //If to check if the search was valid as OMDB does not return a simple error
                        // Instead they return an error in JSON format

                        if (resultFromApi == null || theResponse.equals("False")) {
                            Toast.makeText(currentActivity, "No data found! Movie or Year is incorrect, please try again", Toast.LENGTH_LONG).show();
                        } else {
                            //start second activity and send data in an intent
                            Intent intent = new Intent(currentActivity, DisplayListView.class);
                            intent.putExtra("data", resultFromApi);
                            startActivity(intent);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    /**
     * Hamburger menu to start activities
     */
    @Override
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

    /**
     * Method to open drawer and show search and favourites menu items
     */
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

    /**Method to check if the user is connected to the internet(wifi or data)
     * Code taken from online at https://www.tutorialspoint.com/how-to-check-internet-connection-in-android
     */
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    /**
     * Dialogbox that pops up if the user is currently not connected to the internet
     */
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");

        builder.setMessage("You need to have Internet conenction to access this app please turn it on and relaunch. Press ok to Exit");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        return builder;
    }

}
