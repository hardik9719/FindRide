package com.example.hardik.findride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseLogin extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText etEmail;
    EditText etPassword;
    String Email,Password;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            Intent homescreen=new Intent(FirebaseLogin.this,HomeScreen.class);
            startActivity(homescreen);


        }



    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(FirebaseLogin.this, "Succesfully registered", Toast.LENGTH_SHORT).show();
                            Log.d("FirebaseLogin", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent homescreen=new Intent(FirebaseLogin.this,HomeScreen.class);
                            startActivity(homescreen);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FirebaseLogin", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(FirebaseLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void showHomeScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        Button btnRegister= (Button) findViewById(R.id.btnRegisterUser);
        btnRegister.setOnClickListener(this);

        Button btnLogin= (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnLogout= (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegisterUser:
                etEmail= (EditText) findViewById(R.id.Email);
                etPassword= (EditText) findViewById(R.id.Password);
               Email=(etEmail.getText()).toString();
                 Password= (etPassword.getText()).toString();
                registerUser();
            break;
            case R.id.btnLogin:
                etEmail= (EditText) findViewById(R.id.Email);
                etPassword= (EditText) findViewById(R.id.Password);
                Email=(etEmail.getText()).toString();
                Password= (etPassword.getText()).toString();
                Log.d("FirebaseLogin", "onClick:"+Email+" "+Password);
                loginUser();
                break;
            case R.id.btnLogout:

                break;
              //  Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Log", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(FirebaseLogin.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent homescreen=new Intent(FirebaseLogin.this,HomeScreen.class);
                            startActivity(homescreen);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Log", "signInWithEmail:failure", task.getException());
                            Toast.makeText(FirebaseLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
