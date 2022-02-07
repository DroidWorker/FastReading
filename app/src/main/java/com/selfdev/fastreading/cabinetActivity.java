package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class cabinetActivity extends AppCompatActivity {

    Boolean editName = false;

    String uID;
    HashMap<String, String> wPm_understanding = new HashMap<>();
    EditText etNickName;

    FirebaseDatabase database;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        etNickName  = findViewById(R.id.NickName);
        mSettings = getSharedPreferences("FRconfig", Context.MODE_PRIVATE);
        etNickName.setText(mSettings.getString("login", "NickName"));
        
        database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
            uID = user.getUid();
        if (uID!=null) {
            DatabaseReference ref = database.getReference("users/"+uID+"/records");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()
                         ) {
                        wPm_understanding.put(snap.getKey().toString(), snap.getValue().toString());
                        printRecords();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    
    void printRecords(){
        String text = "статистика рекордов:\n";
        for (Map.Entry<String, String> entry: wPm_understanding.entrySet()) {
            text+=entry.getKey()+" слов/минуту; "+entry.getValue()+"%\n";
        }
        TextView recordsView = findViewById(R.id.recordsView);
        recordsView.setText(text);
    }

    public void onChangeClick(View view){
        if (!editName) {
            etNickName.setFocusableInTouchMode(true);
            editName=true;
        }
        else
        {
            etNickName.setFocusable(false);
            editName=false;
            mSettings = getSharedPreferences("FRconfig", Context.MODE_PRIVATE);
            String Oldnickname = mSettings.getString("login", "NickName");
            DatabaseReference ref = database.getReference("users/"+uID+"/login");
            ref.setValue(etNickName.getText().toString());
            ref = database.getReference("logins/"+Oldnickname);
            ref.removeValue();
            ref = database.getReference("users/"+uID+"/email");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DatabaseReference ref = database.getReference("logins");
                    ref.child(etNickName.getText().toString()).setValue(snapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("login", etNickName.getText().toString());
        }
    }

    public void onBackClick(View view){
        this.finish();
    }
}