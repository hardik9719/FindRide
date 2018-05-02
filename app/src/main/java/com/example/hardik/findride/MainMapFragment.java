package com.example.hardik.findride;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hardik on 9/9/17.
 */

public class MainMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    GoogleMap mGoogleMap;
    MapView mapView;
    MainMapFragmentToMyRidesFragment mainMapFragmentToMyRidesFragment;
    AutoCompleteTextView atvStartPlace;
    View rootview;
    Button btnFindUserLocation,btnStartNavigation;
    MarkerOptions myLocation;
    Marker myMarker=null;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    String lat,lng;

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d("MainActivity", "onLocationResult:"+locationResult);
            for (Location location : locationResult.getLocations()) {
                lat= String.valueOf(location.getLatitude());
                lng= String.valueOf(location.getLongitude());
                Log.i("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                goToLocation(location.getLatitude() , location.getLongitude(),15, 1);
                atvStartPlace.setText(location.getLatitude()+","+location.getLongitude());
               // createVolleyRequest();

                //tvlatlng.setText("Time: "+new Date()+" | "+location.getLatitude()+"  /  "+location.getLongitude());
            }
        };

    };

    private void createVolleyRequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyDP918XH297Y5hHwbeRm0q9GmyTjgWXx1M";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_map, container, false);


        return rootview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rootview = view;
        mapView = (MapView) rootview.findViewById(R.id.mymap);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        btnFindUserLocation = (Button) view.findViewById(R.id.btnFindUserLocation);
        btnFindUserLocation.setOnClickListener(this);

        btnStartNavigation= (Button) view.findViewById(R.id.btnStartNavigation);


        atvStartPlace = (AutoCompleteTextView) rootview.findViewById(R.id.atvStartPlace);
        atvStartPlace.setThreshold(1);

        atvStartPlace.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PlaceCompleteResults placeCompleteResults = new PlaceCompleteResults();
                placeCompleteResults.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        getPermissions();



        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            getPermissions();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }




        btnStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + "Label which you want" + ")";
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });

    }

    public void requestLocationUpdates() {
        Log.d("MainActivity", "requestLocationUpdates:onnnnnnnn");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // two minute interval
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

     private void getPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x3);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x3);
            }
        } else {
            Toast.makeText(getActivity(), "" + Manifest.permission.ACCESS_FINE_LOCATION + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean CheckGpsStatus(Context context){
        LocationManager locationManager ;
        boolean GpsStatus ;

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GpsStatus){
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(myIntent);
        }
        Toast.makeText(getActivity(), "MainMapFragment"+GpsStatus, Toast.LENGTH_SHORT).show();
        if(GpsStatus)
            return true;
        else
            return false;


    }








    private String DownloadJSONData(String URL) {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(URL);

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

        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        Log.d("JSON DATA", data);
        return data;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFindUserLocation:
                if(CheckGpsStatus(getActivity()));{
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                Log.d("MainActivity", "onCreate:"+mFusedLocationClient);
                requestLocationUpdates();
            }

                break;




        }

    }






    class PlaceCompleteResults extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
         //   "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=bori&key=AIzaSyDP918XH297Y5hHwbeRm0q9GmyTjgWXx1M"
        String url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+strings[0]+"&key=AIzaSyDP918XH297Y5hHwbeRm0q9GmyTjgWXx1M";
        String jsonData=DownloadJSONData(url);
            return  jsonData;
        }


        @Override
        protected void onPostExecute(String s) {
           Log.d("response",s);
            // Creating ParserTask
             ParserTask parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(s);
        }
    }
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

         List<String> places =new ArrayList<String>();
            for (HashMap<String, String> entry : result) {
                String  s=entry.get("description").toString();
                places.add(s);
                Log.d("Places ",s);

//                for (String key : entry.keySet()) {
//                    String value = entry.get(key);
//                   Log.d("key = ", key);
//                    Log.d("value = ", value);
//
//                }
                Log.d("END OF A ENTRY","------------------------------------------------");
            }

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,places);
           atvStartPlace.setAdapter(arrayAdapter);
        }
    }
        class PlaceJSONParser {

            /** Receives a JSONObject and returns a list */
            public List<HashMap<String,String>> parse(JSONObject jObject){

                JSONArray jPlaces = null;
                try {
                    /** Retrieves all the elements in the 'places' array */
                    jPlaces = jObject.getJSONArray("predictions");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /** Invoking getPlaces with the array of json object
                 * where each json object represent a place
                 */
                return getPlaces(jPlaces);
            }

            private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
                int placesCount = jPlaces.length();
                List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
                HashMap<String, String> place = null;

                /** Taking each place, parses and adds to list object */
                for(int i=0; i<placesCount;i++){
                    try {
                        /** Call getPlace with place JSON object to parse the place */
                        place = getPlace((JSONObject)jPlaces.get(i));
                        placesList.add(place);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("PLACESCOUNT", String.valueOf(placesCount));
                return placesList;
            }

            /** Parsing the Place JSON object */
            private HashMap<String, String> getPlace(JSONObject jPlace){

                HashMap<String, String> place = new HashMap<String, String>();

                String id="";
                String reference="";
                String description="";

                try {

                    description = jPlace.getString("description");
                    id = jPlace.getString("id");
                    reference = jPlace.getString("reference");

                    place.put("description", description);
                    place.put("_id",id);
                    place.put("reference",reference);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return place;
            }
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        goToLocation(20.5937,78.9629,4,0);


    }
    private void goToLocation(double lat, double lng, float zoom, int i) {
        LatLng ll = new LatLng(lat, lng);
        if(i==0){


            CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
            mGoogleMap.animateCamera(cameraupdate);
        }else{

            if(myMarker!=null) {
                myMarker.remove();
                myMarker=null;
            }
            if(myMarker==null){
                myLocation=new MarkerOptions();
                myLocation.title("MyLocation");
                myLocation.position(ll);
                CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
                mGoogleMap.animateCamera(cameraupdate);
                myMarker=mGoogleMap.addMarker(myLocation);
            }

        }




    }
}
