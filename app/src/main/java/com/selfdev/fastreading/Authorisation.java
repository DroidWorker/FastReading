package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Authorisation extends AppCompatActivity {
    EditText login;
    EditText password;
    Context ctx;
    String value;

    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorisation);
        ctx=this;

        login = findViewById(R.id.editTextTextPersonName3);
        password = findViewById(R.id.editTextTextPassword5);
    }

    public void onSignInClick(View view){
        if (login.getText().length()>2&&password.getText().length()>2){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference ref = database.getReference("logins").child(login.getText().toString());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     value = snapshot.getValue(String.class);
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(value, password.getText().toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ctx, "пароль неверен", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(@NonNull AuthResult authResult) {
                            Toast.makeText(ctx, "AllOK", Toast.LENGTH_SHORT).show();
                            mSettings = getSharedPreferences("FRconfig", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString("login", login.getText().toString());
                            editor.commit();
                            Authorisation.this.finish();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ctx, "логин не найден", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onBackClick(View view)
    {
        this.finish();
    }

    public void onRegClick(View view){startActivity(new Intent(this, Registration.class));}
}