/*
package com.example.hardik.findride;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;





class DataLongOperationAsynchTask extends AsyncTask<String, String, String[]> {
    String url="http://192.168.0.106:80/riderRegister.php";
    private Context context;
    String startAddress,endAddress;
    String res;
    public DataLongOperationAsynchTask(Context context){

        this.context=context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected String[] doInBackground(String... params) {
        // Log.d("url after adding",""+params[0]);

        // http%3A%2F%2Fmaps.google.com%2Fmaps%2Fapi%2Fgeocode%2Fjson%3Faddress%3DKandiwali+West%2C+Mumbai%2C+Maharashtra%2C+India%26sensor%3Dfalse
        //http://maps.google.com/maps/api/geocode/json?address="+params[0]+"&sensor=false"
        //     String url="http://maps.google.com/maps/api/geocode/json?address=malad&sensor=false";
        String url = "http://maps.google.com/maps/api/geocode/json?address=" + params[0] + "&sensor=false";
        String fbgraphapi = "https://graph.facebook.com/me?fields=first_name,last_name,gender&access_token=EAACEdEose0cBAF2murZC0yD2xS0vAb0gVeQ8ZB6gJxsqLZBQXydhfyfEAWg4MLk5s4n61nCXwrGbnPZATmVcqwkpTPcA6nOCLL2FSeHE7bDXu5j7ysNz9Wl2NpzgV1eKJo0w5QHLqLZCKiDDcnZC1RiZCa0vZAjNFfp7QkLUpxYGq3tF1DwBTCUfgCOX0eBqFItVZAHWa3tEIZCQZDZD";
    String address=params[0];
        String response, UserInforesponse;
        try {
            response = getLatLongByURL(url);
            Log.d("response", "" + response);
           UserInforesponse=createRide(address);
            return new String[]{response};
        } catch (Exception e) {
            return new String[]{"error"};
        }

    }

    private String createRide(String address) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

        res=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();

                String startAdd;
                params.put("id",startAddress);
                String endAdd;
                params.put("pass",endAddress);
                return params;
            }
        };
        MySingleTon.getInstance(context).addToRequestQue(stringRequest);
        return res;
    }

    @Override
    protected void onPostExecute(String... result) {
        try {
            JSONObject jsonObject = new JSONObject(result[0]);

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        //    Toast.makeText(getBaseContext(), "latitude" + lat, Toast.LENGTH_SHORT).show();
            Log.d("longitude", "" + lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void addRide(String startAdd, String endAdd) {
    this.startAddress=startAdd;
        this.endAddress=endAdd;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                  Toast.makeText(context,response,Toast.LENGTH_SHORT).show();

                res=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();

                params.put("email","hardik9719@gmail.com");
                params.put("startAdd",startAddress);
                params.put("endAdd",endAddress);
                params.put("rideType","RIDER");


                return params;
            }
        };
        MySingleTon.getInstance(context).addToRequestQue(stringRequest);
    }

}
*/
