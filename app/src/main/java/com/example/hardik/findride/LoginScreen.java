/*
package com.example.hardik.findride;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class LoginScreen extends AppCompatActivity {
    UserInfo user=new UserInfo();
    String startAdd,endAdd;
    String id,email,name,gender;
    LoginButton loginButton;

    String accTOK;

    SharedPreferences sharedPreferences;


    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
       // nextActivity(profile, id, email, name, gender, accTOK);
    }
    protected void onStop() {
        super.onStop();
        //Facebook login


    }
   @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }
    private void nextActivity(Profile profile){
        if(profile != null){
            Intent main = new Intent(LoginScreen.this, HomeScreen.class);




            startActivity(main);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        loginButton= (LoginButton) findViewById(R.id.fbloginbtn);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));



        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

            }
        };


        callbackManager=CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code


                     callgraph(loginResult);


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });





    }

    private void callgraph(final LoginResult loginResult) {
        // prepare the Request
        String id=loginResult.getAccessToken().getUserId();
        String token=loginResult.getAccessToken().getToken();


        Log.d("ID",id);
        Log.d("ACCESSTTOKEN",token);
       String url="https://graph.facebook.com/"+id+"/?fields=id,name,gender,birthday,email,picture&type=large&access_token="+token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {

                        String name,email,gender,dob,id,imgurl;
                           name=response.getString("name");
                            email=response.getString("email");
                            gender=response.getString("gender");
                            id=(String) response.get("id");
                            dob=response.getString("birthday");
                            imgurl= response.getJSONObject("picture").getJSONObject("data").getString("url");
                            Intent main=new Intent(LoginScreen.this,HomeScreen.class);
                            main.putExtra("id",id);
                            main.putExtra("name",name);
                            main.putExtra("email",email);
                            main.putExtra("gender",gender);
                            main.putExtra("dob",dob);
                            main.putExtra("imgurl",imgurl);
                            sharedPreferences=getSharedPreferences("Info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("email",email);
                            editor.putString("id",id);
                            editor.commit();
                            startActivity(main);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String response=null;
                        Log.d("Error.Response", response);
                    }
                }
        );
        MySingleTon.getInstance(LoginScreen.this).addToRequestQue(request);

    }




















}
*/
