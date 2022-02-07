package com.selfdev.fastreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TrenajearActivity extends AppCompatActivity {
    int type = 0;
    AdView myAdview;
    float iteration = 1.0f;
    String str = "";
    int rotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trenajear);

        MobileAds.initialize(this);
        bannerAd();

        type = getIntent().getIntExtra("TrenType", 1);
            if(type==1) {
                ((ConstraintLayout)findViewById(R.id.ShulteSettingsLayout)).setVisibility(View.VISIBLE);
                RadioGroup radioSize = findViewById(R.id.radioSize);
                radioSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.x3:
                                startShulte(3);
                                break;
                            case R.id.x5:
                                startShulte(5);
                                break;
                            case R.id.x7:
                                startShulte(7);
                                break;
                            case R.id.x9:
                                startShulte(9);
                                break;
                            case R.id.x11:
                                startShulte(11);
                                break;
                            case R.id.x13:
                                startShulte(13);
                                break;
                        }
                        ConstraintLayout clShuttleSettings = findViewById(R.id.ShulteSettingsLayout);
                        clShuttleSettings.setVisibility(View.GONE);
                    }
                });
            }
            else if(type==2){
                ((ConstraintLayout)findViewById(R.id.lineOFsight)).setVisibility(View.VISIBLE);
            }
            else if (type==3){
                SharedPreferences sharedPreferences = getSharedPreferences("FRconfig", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int labiryntNo = sharedPreferences.getInt("labyryntNo", 1);
                editor.putInt("labyryntNo", labiryntNo+1);
                ImageView iv = findViewById(R.id.labyrynt);
                if (labiryntNo==1) iv.setImageResource(R.drawable.lab1);
                else if (labiryntNo==2) iv.setImageResource(R.drawable.lab2);
                else if (labiryntNo==3) iv.setImageResource(R.drawable.lab3);
                else if (labiryntNo==4) {
                    editor.putInt("labyryntNo", 1);
                    iv.setImageResource(R.drawable.lab4);
                }
                editor.commit();
                ((ConstraintLayout)findViewById(R.id.labiryntLayout)).setVisibility(View.VISIBLE);
            }
            else if (type==4){
                ((ConstraintLayout)findViewById(R.id.bariersLayout)).setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://fastreading-515a9-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference ref = database.getReference("textes");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         int count = (int)snapshot.getChildrenCount();
                        Random rand = new Random();
                        String text = snapshot.child(String.valueOf(rand.nextInt(count)+1)).getValue().toString();
                        TextView tv = findViewById(R.id.textView7);
                        switch (1+rand.nextInt(3)){
                            case 1://наложение решетки
                                tv.setText(text);
                                ((ImageView)findViewById(R.id.jail)).setVisibility(View.VISIBLE);
                                break;
                            case 2://нехватка букв
                                char[] textArr = text.toCharArray();
                                int rem = 5+rand.nextInt(5);
                                text= "";
                                for (int i=0; i<textArr.length; i++){
                                    if (i!=rem){
                                        text+=textArr[i];
                                    }
                                    else{
                                        rem = 5+i+rand.nextInt(5);
                                    }
                                }
                                tv.setText(text);
                                break;
                            case 3://поворот на 90
                                Timer timer = new Timer();
                                timer.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        rotate = rand.nextInt(180);
                                        rotate = 90-rotate;
                                        TrenajearActivity.this.runOnUiThread(new Runnable(){
                                            @Override
                                            public void run() {
                                                tv.setRotation(rotate);
                                            }});
                                    }
                                }, 0, 3000);
                                tv.setText(text);
                                tv.setMovementMethod(new ScrollingMovementMethod());
                                break;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {                    }
                });
            }
    }

    public void onClearClick(View view){
        TextView tv = findViewById(R.id.textView5);
        Button but = findViewById(R.id.ClearButton);
        tv.setVisibility(View.GONE);
        but.setVisibility(View.GONE);
        startPyramid();
    }

    void startPyramid()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                change();
            }
        }, 0, 1000);
    }
    void change(){
        TextView numsLine = findViewById(R.id.textViewNumsLine);
        if (numsLine.getLineCount()>1||0.4f+(iteration/20)>1.0f) {
            numsLine.setText("");
            return;
        }
        ConstraintSet set = new ConstraintSet();
        ConstraintLayout cl = findViewById(R.id.lineOFsight);
        set.clone(cl);
        set.setVerticalBias(R.id.textViewNumsLine, 0.4f+(iteration/20));

        for (int i=0; i<(int)iteration;i++){
            str+=" ";
        }
        Random rand = new Random();
        int a = rand.nextInt(99);
        int b = rand.nextInt(99);

        TrenajearActivity.this.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                set.applyTo(cl);
                numsLine.setText(a+"   "+str+"   "+b);
            }});

        iteration++;
    }

    void startShulte(int size){
        ConstraintLayout shulteLayout = findViewById(R.id.ShulteTableLayout);
        shulteLayout.setVisibility(View.VISIBLE);

        String html = "<html>\n" +
                "<head><title>111</title><style>button {background: #fff;color: #000000;border: none;margin-bottom: 1rem;}input{background: #fff;color:#000000}</style></head><body><script type=\"text/javascript\">window.onload = function(){ StartStop();};var isRun = 1;var base = 60;var clocktimer, dateObj, dh, dm, ds, ms;var readout = '';var h = 1,  m = 1,  tm = 1,  s = 0,  ts = 0,  ms = 0,  init = 0;function Stop(){isRun=0;};function ClearClock() {  clearTimeout(clocktimer);  h = 1;  m = 1;  tm = 1;  s = 0;  ts = 0;  ms = 0;  init = 0;  readout = '00:00:00';  document.MyForm.stopwatch.value = readout;}function StartTIME() {  var cdateObj = new Date();  var t = (cdateObj.getTime() - dateObj.getTime()) - (s * 1000);  if (t > 999) {    s++;  }  if (s >= (m * base)) {    ts = 0;    m++;  } else {    ts = parseInt((ms / 100) + s);    if (ts >= base) {      ts = ts - ((m - 1) * base);    }  }  if (m > (h * base)) {    tm = 1;    h++;  } else {    tm = parseInt((ms / 100) + m);    if (tm >= base) {      tm = tm - ((h - 1) * base);    }  }  ms = Math.round(t / 10);  if (ms > 99) {    ms = 0;  }  if (ms == 0) {    ms = '00';  }  if (ms > 0 && ms <= 9) {    ms = '0' + ms;  }  if (ts > 0) {    ds = ts;    if (ts < 10) {      ds = '0' + ts;    }  } else {    ds = '00';  }  dm = tm - 1;  if (dm > 0) {    if (dm < 10) {      dm = '0' + dm;    }  } else {    dm = '00';  }  dh = h - 1;  if (dh > 0) {    if (dh < 10) {      dh = '0' + dh;    }  } else {    dh = '00';  }  readout = dh + ':' + dm + ':' + ds;  document.MyForm.stopwatch.value = readout;  if(isRun==1){clocktimer = setTimeout('StartTIME()', 1);}}function StartStop() {if (init == 0) {    ClearClock();    dateObj = new Date();    StartTIME();    init = 1;  } else {    clearTimeout(clocktimer);    init = 0;  }}</script><center><form name=\"MyForm\" width=\"100%\">  <input name=\"stopwatch\"; size=\"10\" value=\"00:00:00.00\" disabled></form><button onClick=\"Stop()\">стоп</button></center><table border=\"3\" bordercolor="+"\"#000000\""+" cellspacing=\"0\" cellpadding=\"5\" width=\"100%\">";
        ArrayList<Integer> nums = new ArrayList<Integer>();
        Random rand = new Random();
        int iteration=0;
        int rint = 0;
        switch (size){
            case 3:
                while (nums.size()<9){
                    rint = rand.nextInt(9)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<3; i++){
                    for (int j=0; j<3; j++){
                        html += "<th height=\"100\" width=\"33%\" onClick=\"this.style.backgroundColor = '#DCDCDC';\"><h1 align=\"center\">"+nums.get(iteration)+"</h1></th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
            case 5:
                while (nums.size()<25){
                    rint = rand.nextInt(25)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<5; i++){
                    for (int j=0; j<5; j++){
                        html += "<th height=\"80\" width=\"20%\" onClick=\"this.style.backgroundColor = '#DCDCDC';\"><h1 align=\"center\">"+nums.get(iteration)+"</h1></th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
            case 7:
                while (nums.size()<49){
                    rint = rand.nextInt(49)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<7; i++){
                    for (int j=0; j<7; j++){
                        html += "<th height=\"60\" width=\"14%\" onClick=\"this.style.backgroundColor = '#DCDCDC';\"><h2 align=\"center\">"+nums.get(iteration)+"</h2></th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
            case 9:
                while (nums.size()<81){
                    rint = rand.nextInt(81)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<9; i++){
                    for (int j=0; j<9; j++){
                        html += "<th height=\"20\" width=\"11%\" align=\"center\" onClick=\"this.style.backgroundColor = '#DCDCDC';\">"+nums.get(iteration)+"</th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
            case 11:
                while (nums.size()<121){
                    rint = rand.nextInt(121)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<11; i++){
                    for (int j=0; j<11; j++){
                        html += "<th height=\"15\" width=\"9%\" align=\"center\" onClick=\"this.style.backgroundColor = '#DCDCDC';\"><font size=\"1\">"+nums.get(iteration)+"</font></th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
            case 13:
                while (nums.size()<169){
                    rint = rand.nextInt(169)+1;
                    if (!nums.contains(rint)) nums.add(rint);
                }
                html+="<tr>";
                for (int i =0; i<13; i++){
                    for (int j=0; j<13; j++){
                        html += "<th height=\"15\" width=\"7%\" align=\"center\" font-size=\"5pt\" onClick=\"this.style.backgroundColor = '#DCDCDC';\"><font size=\"1\">"+nums.get(iteration)+"</font></th>";
                        iteration++;
                    }
                    html+="</tr>";
                }
                break;
        }
        html+= "</table></body></html>";

        WebView wv = findViewById(R.id.WVshulte);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MyApplication", consoleMessage.message() + " -- From line " +
                        consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }
        });

        wv.loadData(html, "text/html; charset=utf-8", "utf-8");
    }

    public void onBackClick(View view)
    {
        this.finish();
    }

    void bannerAd()
    {
        myAdview = findViewById(R.id.adView2);
        myAdview.loadAd(new AdRequest.Builder().build());
    }
}