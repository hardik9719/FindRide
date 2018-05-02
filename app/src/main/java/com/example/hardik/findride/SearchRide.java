package com.example.hardik.findride;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRide extends Fragment {


    public SearchRide() {
        // Required empty public constructor
    }

    TextView textView;
    JSONObject o;
    ListView l;

    List<SearchRide.Ride> list;
    JSONArray rides;
    SearchRide.base b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_ride, container, false);
        l=  (ListView)view.findViewById(R.id.listview);
        Log.d("before","before");
        l =  (ListView)view.findViewById(R.id.listview);
        list=new ArrayList<SearchRide.Ride>();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());  // this = context
        String url = "http://www.liftplease.codeadventure.in/www/html/rideFinder.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("SEARCHRIDEResponse", response);
                        try {
                            o=new JSONObject(response);
                            rides=o.getJSONArray("rides");
                            for (int i=0;i<rides.length();i++){
                                JSONObject tmp=rides.getJSONObject(i);
                                String starAddress=tmp.getString("startAddress");
                                String endAddress=tmp.getString("endAddress");
                                String RideType=tmp.getString("RideType");
                                Log.d("StartADDRESS",starAddress);
                                SearchRide.Ride r=new SearchRide.Ride(starAddress,endAddress,RideType);
                                list.add(r);
                            }
                            b.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
                Log.d("EMAIL",email);
                params.put("email", email);


                return params;
            }
        };
        queue.add(postRequest);
        Log.d("before","after");
        b=new base(getActivity(),list);
        l.setAdapter(b);
        return  view;
    }
    class Ride{
        String startAdd,endAdd,RidteType;

        public Ride(String startAdd, String endAdd, String rideType) {
            this.startAdd = startAdd;
            this.endAdd = endAdd;
            RidteType = rideType;
        }
    }
    class base extends BaseAdapter {
        Context context;
        List<SearchRide.Ride> rides;
        public base(Context c,List<SearchRide.Ride> list) {
            this.context = c;
            this.rides=list;

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= layoutInflater.inflate(R.layout.ride_row,viewGroup,false);

            TextView startAdd=(TextView)row.findViewById(R.id.startAdd);
            TextView endAdd=(TextView)row.findViewById(R.id.endAdd);
            TextView rideType=(TextView)row.findViewById(R.id.rideType);
            SearchRide.Ride temp=list.get(i);
            startAdd.setText(temp.startAdd);
            endAdd.setText(temp.endAdd);
            rideType.setText(temp.RidteType);

            return row;
        }


    }


}
