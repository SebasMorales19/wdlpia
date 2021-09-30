package com.example.wdlpia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.Email_Login);
        passwordEditText = findViewById(R.id.Password_login);


        firebaseAuth =  FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {

            FirebaseUser user =  firebaseAuth.getCurrentUser();


            //si un usuario ya inicio sesión
            if (user != null){

                if (!user.isEmailVerified()){

                    Toast.makeText(MainActivity.this, "Correo Electronico no verificado" + user.getEmail(), Toast.LENGTH_SHORT).show();

                }else{

                        gotoHomeActivity();

                    }

            }
        };
    }

    private void gotoHomeActivity() {
        Intent intent =  new Intent(MainActivity.this, Home.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);


    }


    @Override
    protected void onStop() {
        super.onStop();
//si seguimos escuchando eventos por iniciar sesión cunado ya esta detenida

        if(authStateListener != null ){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }


    }

    public void Login(View view) {

        String username = usernameEditText.getText().toString();
        String password  =  passwordEditText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {

            if(!task.isSuccessful()){
                Toast.makeText(MainActivity.this, "Hubo un error", Toast.LENGTH_SHORT).show();

            }
        });



    }


    public void SingUp(View view) {

        //para mandarnos a la patalla de SingUp

        Intent intent =  new Intent(this, SingUp.class);
        startActivity(intent);
        finish();//para que no use la flecha de ir hacia atras
    }


    public void RestfullerPassword1(View view) {
        Intent intent  = new Intent(MainActivity.this , ResetPasswordActivity.class);
        startActivity(intent);

    }


}