package com.example.pixa.medikit.Presentation;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pixa.medikit.Business.PermissionUtils;
import com.example.pixa.medikit.Business.Place_JSON;
import com.example.pixa.medikit.Business.Position;
import com.example.pixa.medikit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

        /**
         * Request code for location permission request.
         *
         * @see #onRequestPermissionsResult(int, String[], int[])
         */

        /* Constants */
        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

        private int MAX_LENGTH;

        /*Lat, Long
        Thaïland : 18.796143, 98.979263;
        San Francisco : 37.773972, -122.431297;
        Sydney : -33.865143, 151.209900;
         */

        //LatLng cameraFocus = new LatLng(46.204391, 6.143158);
        private Position myPos;
        private LatLng myPosDet = new LatLng(46.174817, 6.139748);
        private ArrayList<MarkerOptions> arrayMarker = new ArrayList<>();

        private MenuItem itemSelected;


        /**
         * Flag indicating whether a requested permission has been denied after returning in
         * {@link #onRequestPermissionsResult(int, String[], int[])}.
         */
        private boolean mPermissionDenied = false;

        private GoogleMap mMap;

        public StringBuilder sbMethod() {

                //use your current location here

                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + myPos.getLat() + "," + myPos.getLng());
                sb.append("&radius=5000");
                sb.append("&types=" + "hospital");
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyBHGDyzUIn1MJfLg3vOEF88V8Q69ZMLoEQ");

                Log.d("Map", "api: " + sb.toString());

                return sb;
        }

        private class PlacesTask extends AsyncTask<String, Integer, String> {

                String data = null;

                // Invoked by execute() method of this object
                @Override
                protected String doInBackground(String... url) {
                        try {
                                data = downloadUrl(url[0]);
                        } catch (Exception e) {
                                Log.d("Background Task", e.toString());
                        }
                        return data;
                }

                // Executed after the complete execution of doInBackground() method
                @Override
                protected void onPostExecute(String result) {
                        ParserTask parserTask = new ParserTask();

                        // Start parsing the Google places in JSON format
                        // Invokes the "doInBackground()" method of the class ParserTask
                        parserTask.execute(result);
                }
        }

        private String downloadUrl(String strUrl) throws IOException {
                String data = "";
                InputStream iStream = null;
                HttpURLConnection urlConnection = null;
                try {
                        URL url = new URL(strUrl);

                        // Creating an http connection to communicate with url
                        urlConnection = (HttpURLConnection) url.openConnection();

                        // Connecting to url
                        urlConnection.connect();

                        // Reading data from url
                        iStream = urlConnection.getInputStream();

                        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                        StringBuffer sb = new StringBuffer();

                        String line = "";
                        while ((line = br.readLine()) != null) {
                                sb.append(line);
                        }

                        data = sb.toString();

                        br.close();

                } catch (Exception e) {
                        Log.d("Exception while downloading url", e.toString());
                } finally {
                        iStream.close();
                        urlConnection.disconnect();
                }
                return data;
        }

        private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

                JSONObject jObject;

                // Invoked by execute() method of this object
                @Override
                protected List<HashMap<String, String>> doInBackground(String... jsonData) {

                        List<HashMap<String, String>> places = null;
                        Place_JSON placeJson = new Place_JSON();

                        try {
                                jObject = new JSONObject(jsonData[0]);

                                places = placeJson.parse(jObject);

                        } catch (Exception e) {
                                Log.d("Exception", e.toString());
                        }
                        return places;
                }

                // Executed after the complete execution of doInBackground() method
                @Override
                protected void onPostExecute(List<HashMap<String, String>> list) {

                        Log.d("Map", "list size: " + list.size());
                        // Clears all the existing markers;
                        //mMap.clear(); PEUT ETRE A REMETTRE ////////////////////////////////////////////

                        for (int i = 0; i < list.size(); i++) {

                                // Creating a marker
                                MarkerOptions markerOptions = new MarkerOptions();

                                // Getting a place from the places list
                                HashMap<String, String> hmPlace = list.get(i);


                                // Getting latitude of the place
                                double lat = Double.parseDouble(hmPlace.get("lat"));

                                // Getting longitude of the place
                                double lng = Double.parseDouble(hmPlace.get("lng"));

                                // Getting name
                                String name = hmPlace.get("place_name");

                                Log.d("Map", "place: " + name);

                                // Getting vicinity
                                String vicinity = hmPlace.get("vicinity");

                                LatLng latLng = new LatLng(lat, lng);

                                // Setting the position for the marker
                                markerOptions.position(latLng);

                                markerOptions.title(name + " : " + vicinity);

                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                                // Placing a marker on the touched position
                                arrayMarker.add(markerOptions);
                                Marker m = mMap.addMarker(markerOptions);

                        }
                        //Marker me = mMap.addMarker(new MarkerOptions().position(myPosDet).title("ME"));
                        MAX_LENGTH = arrayMarker.size();

                }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_maps);
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location
                Location location = null;
                try {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException e) {
                        System.out.println("Error location");
                }

                ////System.out.println(location.getLatitude());
                myPosDet = new LatLng(46.174817, 6.139748);
                myPos = new Position("My position",myPosDet.latitude,myPosDet.longitude);
                //sI LOCATION = NULL ALORS REMETS CE CODE EN ACTIF ET CELUI QUI SUIT EN COMMENTAIRE, LANCE UNE FOIS, ET REMET LE BON CODE ET C'EST REGLE

                //myPosDet = new LatLng(location.getLatitude(), location.getLongitude());
                //myPos = new Position("My position",myPosDet.latitude,myPosDet.longitude);

        }

        private void fillRes(double[] tabRes){
                int i = 0;
                double resLat = 0;
                double resLng = 0;
                double resFinal = 0;
                for(MarkerOptions m : arrayMarker){
                        if((m.getPosition().latitude > 0 && myPos.getLat() > 0) || (myPos.getLng() < 0 && m.getPosition().longitude < 0)){
                                resLat = m.getPosition().latitude - myPos.getLat();
                                resLng = m.getPosition().longitude - myPos.getLng();
                        } else {
                                resLat = m.getPosition().latitude + myPos.getLat();
                                resLng = m.getPosition().longitude + myPos.getLng();
                        }
                        resFinal = resLat + resLng;
                        tabRes[i++] = resFinal;
                }
        }

        private int findTheNearest(double[] tabRes){
                int ind = 0;
                double resBest = Math.abs(tabRes[0]);
                for(int k = 0; k < tabRes.length; k++){
                        System.out.println(resBest + " " + tabRes[k]);
                        if(Math.abs(resBest) > Math.abs(tabRes[k])){
                                resBest = tabRes[k];
                                ind = k;
                        }
                }
                return ind;
        }

        private void lookForNearest(){
                double[] tabRes = new double[MAX_LENGTH];
                fillRes(tabRes);
                int ind = findTheNearest(tabRes);
                putMarker(arrayMarker.get(ind).getTitle(), arrayMarker.get(ind).getPosition());
        }

        private void setMarkers(){
                StringBuilder sbValue = new StringBuilder(sbMethod());
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sbValue.toString());
        }

        @Override
        public void onMapReady(GoogleMap map) {
                mMap = map;

                mMap.setOnMyLocationButtonClickListener(this);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosDet, 13));

                enableMyLocation();
                //putMarker("My position", myPosDet);

        }

        private void putMarker(String name, LatLng latlng){
                mMap.addMarker(new MarkerOptions().position(latlng).title(name));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        }


        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        private void enableMyLocation() {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                        // Permission to access the location is missing.
                        PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                                Manifest.permission.ACCESS_FINE_LOCATION, true);
                } else if (mMap != null) {
                        // Access to the location has been granted to the app.
                        mMap.setMyLocationEnabled(true);
                }
        }

        @Override
        public boolean onMyLocationButtonClick() {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosDet));
                //Toast.makeText(this, String.valueOf(mMap.getMyLocation()), Toast.LENGTH_SHORT).show(); //Normalement ca devrait donner ma position en temps réel
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
                return false;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
                if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                        return;
                }

                if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Enable the my location layer if the permission has been granted.
                        enableMyLocation();
                } else {
                        // Display the missing permission error dialog when the fragments resume.
                        mPermissionDenied = true;
                }
        }

        @Override
        protected void onResumeFragments() {
                super.onResumeFragments();
                if (mPermissionDenied) {
                        // Permission was not granted, display error dialog.
                        showMissingPermissionError();
                        mPermissionDenied = false;
                }
        }

        /**
         * Displays a dialog with error message explaining that the location permission is missing.
         */
        private void showMissingPermissionError() {
                PermissionUtils.PermissionDeniedDialog
                        .newInstance(true).show(getSupportFragmentManager(), "dialog");
        }

        public boolean onOptionsItemSelected(MenuItem item){
                if (itemSelected == null){
                        itemSelected = item;
                } else if(!itemSelected.toString().equals(item.toString())){
                        itemSelected.setEnabled(true);
                }
                itemSelected = item;
                item.setEnabled(false);
                mMap.clear();
                //putMarker("Ma position", myPosDet);
                if(itemSelected.toString().equals(getString(R.string.action_find_nearest))){
                        lookForNearest();

                } else if(itemSelected.toString().equals(getString(R.string.action_find_all))){
                        setMarkers();
                }
                return false;
        }

        public boolean onCreateOptionsMenu (Menu menu) {
                getMenuInflater().inflate(R.menu.map_menu, menu);
                return true;
        } // onCreateOptionsMenu

}