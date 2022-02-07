package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckSpeed extends AppCompatActivity {

    ConstraintLayout startLayout;
    ConstraintLayout textLayout;
    ConstraintLayout questionsLayout;
    ConstraintLayout resultLayout;

    TextView time;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    int wPm;
    int understanding;
    int prevRecordSpeed;

    FirebaseDatabase database;
    String uID;

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_speed);
        ctx = this;
        View view = findViewById(R.id.base);

        database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null)
            uID = user.getUid();
        else{
            Snackbar.make(view, "Для прохождения теста вам необхоимо зарегистрироваться!\nЭто займет меньше минуты!", Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, Authorisation.class));
        }

        startLayout = findViewById(R.id.startLayout);
        textLayout = findViewById(R.id.textLayout);
        questionsLayout = findViewById(R.id.questionssLayout);
        resultLayout = findViewById(R.id.resultLayout);

        startLayout.setVisibility(View.VISIBLE);

        TextView text = findViewById(R.id.textViewtext);
        text.setMovementMethod(new ScrollingMovementMethod());

        //получаем предыдущий рекорд
        DatabaseReference ref =  database.getReference("users/"+uID+"/recordSpeed");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val =  (snapshot.getValue()).toString();
                prevRecordSpeed = Integer.parseInt(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onStartClick(View view){
        startLayout.setVisibility(View.GONE);
        textLayout.setVisibility(View.VISIBLE);
        time = findViewById(R.id.textViewTime);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void onStopClick(View view){
        textLayout.setVisibility(View.GONE);
        questionsLayout.setVisibility(View.VISIBLE);
        //всего слов 250
        int secs = (int) (updatedTime / 1000);
        wPm = (60*250)/secs;
        startTime = 0L;
        customHandler.removeCallbacks(updateTimerThread);
    }

    public void onAcceptClick(View view){
        int countOFcorrect = 0;//0  из 5
        questionsLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
        RadioGroup rg1 = findViewById(R.id.rg1);
        RadioGroup rg2 = findViewById(R.id.rg2);
        RadioGroup rg3 = findViewById(R.id.rg3);
        RadioGroup rg4 = findViewById(R.id.rg4);
        RadioGroup rg5 = findViewById(R.id.rg5);
        if (rg1.getCheckedRadioButtonId()==R.id.correct1) countOFcorrect++;
        if (rg2.getCheckedRadioButtonId()==R.id.correct2) countOFcorrect++;
        if (rg3.getCheckedRadioButtonId()==R.id.correct3) countOFcorrect++;
        if (rg4.getCheckedRadioButtonId()==R.id.correct4) countOFcorrect++;
        if (rg5.getCheckedRadioButtonId()==R.id.correct5) countOFcorrect++;
        understanding = (100*countOFcorrect)/5;

        //вывод результата
        TextView resWpM = findViewById(R.id.resultWpM);
        TextView resUnderstanding = findViewById(R.id.resultUnderstanding);
        TextView WW = findViewById(R.id.textView9);
        resWpM.setText(wPm+" слов в минуту");
        resUnderstanding.setText("Понимание: "+understanding+"%");
        int WWtime = (188088/wPm)/60;
        if (understanding<33)
            WW.setText("Вы сможете прочитать  “Войну и мир” за "+WWtime+" часов, но ничего не поймете. ");
        else if(understanding>33&&understanding<66)
            WW.setText("Вы сможете прочитать  “Войну и мир” за "+WWtime+" часов, и сможете сделать краткий пересказ. ");
        else
            WW.setText("Вы сможете прочитать  “Войну и мир” за "+WWtime+" часов, и рассказять содержание, как это сделал бы автор. ");

        //если предыдущийс рекорд меньше то перезаписываем
        DatabaseReference ref;
        if (prevRecordSpeed<wPm){
            ref = database.getReference("users/"+uID+"/recordSpeed");
            ref.setValue(wPm);
            ref = database.getReference("users/"+uID+"/recordUnderstanding");
            ref.setValue(understanding);
        }
        ref = database.getReference("users/"+uID+"/records");
        ref.child(String.valueOf(wPm)).setValue(understanding);
    }

    public void onBackClick(View view)
    {
        this.finish();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            time.setText(mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };
}