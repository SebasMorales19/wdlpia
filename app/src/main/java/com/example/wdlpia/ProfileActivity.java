
package com.example.wdlpia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextViewEmail;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //de aqui sacamos las referencias
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTextViewEmail = findViewById(R.id.textViewEmail);


        Button mResetPasswordButton = findViewById(R.id.Password_Button_ProfileActivity);
        ImageButton mReturn = findViewById(R.id.return2);

     
        ///////////////////// Return Password Button/////////////////////////
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this , ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        getUserInfo();
    }

    private void getUserInfo(){
// Esto lo puedo usar para poder hacer el agregar cuentas que puedan usar la puerta
        String  id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                String Email = dataSnapshot.child("email").getValue().toString();


                mTextViewEmail.setText(Email);


            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}