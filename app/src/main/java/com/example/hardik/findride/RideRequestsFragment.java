package com.example.hardik.findride;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hardik on 6/9/17.
 */

public class RideRequestsFragment extends Fragment implements View.OnClickListener,ValueEventListener,AdapterView.OnItemSelectedListener{
    AutoCompleteTextView atvStartPlace,atvEndPlace;
    MainMapFragmentToMyRidesFragment mainMapFragmentToMyRidesFragment;
    int flag=0;

    TextView ridetype;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("rides");
    String startAddress,endAddress,TypeOfRide;
    FirebaseAuth mAuth;

    String[] tor = { "Rider", "Passenger" };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_riderequests,container,false);

        Button btn=(Button)view.findViewById(R.id.create);
        btn.setOnClickListener(this);


        Spinner spin = (Spinner) view.findViewById(R.id.TypeOfRide);
        spin.setOnItemSelectedListener(this);


        atvStartPlace = (AutoCompleteTextView) view.findViewById(R.id.atvStartPlace);
        atvEndPlace = (AutoCompleteTextView) view.findViewById(R.id.atvEndPlace);
        atvStartPlace.setThreshold(1);

        atvStartPlace.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                flag=1;
                RideRequestsFragment.PlaceCompleteResults placeCompleteResults = new RideRequestsFragment.PlaceCompleteResults();
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
             // startAdd= String.valueOf(s);
              Log.d("RideRequestsFragment", "afterTextChanged:"+atvStartPlace.getText());
            }
        });

        atvEndPlace.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RideRequestsFragment.PlaceCompleteResults placeCompleteResults = new RideRequestsFragment.PlaceCompleteResults();
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
                Log.d("ANS", String.valueOf(s));
                //endAdd= String.valueOf(s);

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,tor);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        return  view;
    }
    private String DownloadJSONData(String URL){
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            java.net.URL url = new URL(URL);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

        }finally{
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        Log.d("JSON DATA",data);
        return data;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.create:
                startAddress=atvStartPlace.getText().toString();
                endAddress=atvEndPlace.getText().toString();
                RideDetails rd=new RideDetails(startAddress,endAddress,TypeOfRide);
                Toast.makeText(getContext(), "Createeeeed", Toast.LENGTH_SHORT).show();
                Log.d("RideRequestsFragment", "onClick:"+database);

                mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                String creatorEmail=user.getUid();

                myRef.child(creatorEmail).child("myRides").child(String.valueOf(new Date())).setValue(rd);

        }
      /*  RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://192.168.2.113/rideRegister.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                       *//* try {
                            o=new JSONObject(response);
                            rides=o.getJSONArray("rides");
                            for (int i=0;i<rides.length();i++){
                                JSONObject tmp=rides.getJSONObject(i);
                                String starAddress=tmp.getString("startAddress");
                                String endAddress=tmp.getString("endAddress");
                                String RideType=tmp.getString("RideType");
                                Log.d("StartADDRESS",starAddress);
                                MyRidesFragment.Ride r=new MyRidesFragment.Ride(starAddress,endAddress,RideType);
                                list.add(r);
                            }
                            b.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*//*


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
                String email=sharedPreferences.getString("email",null);
                Log.d("EMAIL",startAdd);
                params.put("email", email);
                params.put("startAdd", startAdd);
                params.put("endAdd", endAdd);
                params.put("rideType", "Rider");


                return params;
            }
        };
        queue.add(postRequest);*/
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue(String.class)!=null){
            String key=dataSnapshot.getKey();
            if(key.equals("message"));
            String msg=dataSnapshot.getValue(String.class);
            Log.d("RideRequestsFragment", "onDataChange:"+msg);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TypeOfRide=tor[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
            RideRequestsFragment.ParserTask parserTask = new RideRequestsFragment.ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(s);
        }
    }
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            RideRequestsFragment.PlaceJSONParser placeJsonParser = new RideRequestsFragment.PlaceJSONParser();

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
            if(flag==1){
                atvStartPlace.setAdapter(arrayAdapter);
                flag=0;

            }
            else{
                atvEndPlace.setAdapter(arrayAdapter);
                flag=1;

            }


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

}
