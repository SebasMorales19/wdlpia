package com.example.wdlpia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SingUp extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;


    //Varaibles de los datos que vamos a registrar
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        usernameEditText = findViewById(R.id.Email_Singup);
        passwordEditText = findViewById(R.id.Password_Singup);
        Button buttonRegister = findViewById(R.id.Registrarse_Singup);


        buttonRegister.setOnClickListener(v -> {


            email = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {

                if (password.length() >= 6) {
                    registerUser();
                } else {
                    Toast.makeText(SingUp.this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(SingUp.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();

            }

        });


    }

    private void registerUser() {
// Esta parte sirve para poder crear el usaurio
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SingUp.this, task -> {
//aqui verifica si se pudo creare y si sí getCurrent user obtiene el usuarip y sendEmail verficacition, le manda una verificación email
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                user.sendEmailVerification();

//Crea un Hashmap para poderlo poner en la base de datos (RealTimeDatabase)
                Map<String, Object> map = new HashMap<>();
                map.put("email", email);
                map.put("password", password);
//Aqui obtienes el ID del usaurio para poder crear una rama especifica a cada usuario donde pondras el HashMap
                String id = mAuth.getCurrentUser().getUid();
//Aquí es donde creeas los nodos hijo del principal Donde tendrás Users , despues el ID de cada usuario despues el mapa (Correo y contraseña)
                mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        startActivity(new Intent(SingUp.this, Home.class));
                        finish();
                    } else {
                        Toast.makeText(SingUp.this, "No se pudieron crear los datos correctamente ", Toast.LENGTH_SHORT).show();


                    }
                });


            } else {
                Toast.makeText(SingUp.this, "No se pudo registar el usuario ", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void Return1(View view) {

        Intent intent = new Intent(SingUp.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}