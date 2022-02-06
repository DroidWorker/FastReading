package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Date;

public class Registration extends AppCompatActivity {
    EditText etEmail;
    EditText etLogin;
    EditText etPassword;
    EditText etPasswordCorrect;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = this;

        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etLogin = findViewById(R.id.editTextLoginName);
        etPassword = findViewById(R.id.editTextTextPassword);
        etPasswordCorrect = findViewById(R.id.editTextTextPassword2);
    }

    public void onRegClick(View view){
        String email;
        String login;
        String password;

        if (etEmail.getText().length()>3&&android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            //success
            email = etEmail.getText().toString();
        }
        else {
            etEmail.setBackground(getDrawable(R.drawable.et_background_red));
            Toast.makeText(this, "некорректный email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etLogin.getText().length()>=3){
            if (etLogin.getText().toString().matches("\\w+")){
                //success
                login = etLogin.getText().toString();
            }
            else{
                etLogin.setBackground(getDrawable(R.drawable.et_background_red));
                Toast.makeText(this, "логин может содержатьь только буквы, цифры и символ подчеркивания", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            Toast.makeText(this, "логин не может быть меньше 3 символов", Toast.LENGTH_SHORT).show();
            etLogin.setBackground(getDrawable(R.drawable.et_background_red));
            return;
        }
        if (etPassword.getText().length()>5){
            if (etPassword.getText().toString().matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{6,}$")){
                //success
            }
            else{
                etPassword.setBackground(getDrawable(R.drawable.et_background_red));
                Toast.makeText(this, "Пароль должен содержать хотя бы одну заглавную букву и цифру", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            etPassword.setBackground(getDrawable(R.drawable.et_background_red));
            Toast.makeText(this, "Пароль не может быть короче 5 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etPasswordCorrect.getText().toString().equals(etPassword.getText().toString())){
            //success
            password = etPasswordCorrect.getText().toString();
        }
        else{
            etPasswordCorrect.setBackground(getDrawable(R.drawable.et_background_red));
            Toast.makeText(this, "Пароль не совпадает", Toast.LENGTH_SHORT).show();
            return;
        }

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(@NonNull AuthResult authResult) {
                FirebaseUser curUser = mAuth.getCurrentUser();
                String uID = (curUser).getUid();
                DatabaseReference myRef = database.getReference("users/"+uID);
                myRef.child("email").setValue(email);
                myRef.child("login").setValue(login);
                myRef.child("password").setValue(password);
                myRef.child("speed").setValue(0);
                myRef.child("understanding").setValue(0);//0-100
                myRef.child("LastEnter").setValue(new Date().getTime());
                //создаем открытую зависимость логин - email
                DatabaseReference ref = database.getReference("logins");
                ref.child(login).setValue(email);
                Registration.this.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Ошибка регистрации: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBackClick(View view)
    {
        this.finish();
    }
}