package com.teixeira.diogo.stcp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.teixeira.diogo.stcp.dto.RouteDto;
import com.teixeira.diogo.stcp.dto.StopDto;
import com.teixeira.diogo.stcp.dto.StopTimeDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopsList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static List<String> IdStops = new ArrayList<String>();

    TextView stops;
    
    public List<StopDto> StopsList = new ArrayList<StopDto>();

    static Spinner spinner = null;

    RequestQueue queue = null;

    long stopID = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        spinner = (Spinner)this.findViewById(R.id.spinner);

        // Tell the spinner what to do when an item is changed
        spinner.setOnItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stops_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addStopView(View view) {
        Intent intent = new Intent(this, AddStopActivity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("StopsList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    public void getStops(View view) {


        //stops.append(getJSON("http://api.ost.pt/stops/?name=metro&agency=22&withroutes=true&key=KlFZynGEwtziLMJGXTeiPnRtIbhRqsfwFblZpRwm", 10000));



        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://api.ost.pt/stops/?name=metro&agency=22&withroutes=true&key=nVOPoyvMJdRDcenkrtswLLIuxdqXiuXJqvafeMNn", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray stopsList = response.getJSONArray("Objects");
                            for (int i = 0; i < stopsList.length(); i++) {
                                JSONObject stopJSON = stopsList.getJSONObject(i);
                                StopDto stop = new StopDto();
                                stop.id = stopJSON.getInt("id");
                                stop.stopName = stopJSON.getString("stop_name");
                                stop.routes = new ArrayList<RouteDto>();

                                JSONArray routesList = stopJSON.getJSONArray("_routes");
                                for (int h = 0; h < routesList.length(); h++) {
                                    JSONObject routeJSON = routesList.getJSONObject(h);
                                    RouteDto route = new RouteDto();
                                    route.id = routeJSON.getInt("id");
                                    route.shortName = routeJSON.getString("route_short_name");
                                    stop.routes.add(route);
                                }

                                StopsList.add(stop);
                            }


                        } catch (Exception e) {

                        }
                        UpdateSpinner();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stops.setText("That didn't work!");

                    }
                });


        // Add the req
        queue.add(jsObjRequest);


    }

    public void UpdateSpinner() {

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, StopsList);

        spinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Get the currently selected State object from the spinner
        StopDto st = (StopDto) spinner.getSelectedItem();

        // Show it via a toast
        toastState( "onItemSelected", st );

        GetStopTimes(st);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent )
    {
    }

    public void toastState( String prefix, StopDto st )
    {
        if ( st != null )
        {
            String desc = "Event: " + prefix + "\nstop: " + st.id;
            desc += "\nname: " + st.stopName;
            Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_SHORT).show();
        }
    }

    public void GetStopTimes(StopDto st) {
        if(st.routes != null) {
            stopID = st.id;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, "https://api.ost.pt/stoptimes/?time=14%3A00&route=" + st.routes.get(0).id + "&range=1&key=nVOPoyvMJdRDcenkrtswLLIuxdqXiuXJqvafeMNn", null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                TextView stopsListText = (TextView) findViewById(R.id.stopsListTextView);

                                JSONArray stopTimesList = response.getJSONArray("Objects");
                                for (int i = 0; i < stopTimesList.length(); i++) {
                                    JSONObject stopTimeJSON = stopTimesList.getJSONObject(i);
                                    if(stopTimeJSON.getLong("stop_id") == stopID) {
                                        StopTimeDto stopTime = new StopTimeDto();
                                        stopTime.stopID = stopTimeJSON.getInt("stop_id");
                                        if (stopTimeJSON.getJSONArray("arrival_times") != null) {
                                            stopTime.arrivalTime = stopTimeJSON.getJSONArray("arrival_times").get(0).toString();
                                        }
                                        if (stopTimeJSON.getJSONArray("departure_times") != null) {
                                            stopTime.arrivalTime = stopTimeJSON.getJSONArray("departure_times").get(0).toString();
                                        }

                                        stopsListText.append(stopTime.toString());
                                        stopsListText.append("\n");
                                    }
                                }


                            } catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            stops.setText("That didn't work!");

                        }
                    });


            // Add the req
            queue.add(jsObjRequest);
        }
    }
}
