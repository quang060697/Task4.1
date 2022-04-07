package com.example.task41;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String TIME = "time";
    private static final String RUN_STATE = "running state";
    private static final String TIME_TEXT = "record time";
    private static final String TASK = "task";
    int second;
    boolean isRunning;
    SharedPreferences sharedPreferences;
    String recordTime;
    String recordTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView timeTextView = findViewById(R.id.timeTextView);
        EditText task = findViewById(R.id.taskEditText);
        TextView record = findViewById(R.id.recordTextView);

        ImageButton start = findViewById(R.id.startButton);
        ImageButton reset = findViewById(R.id.resetButton);
        ImageButton stop = findViewById(R.id.stopButton);

        sharedPreferences = getSharedPreferences("com.example.task41",MODE_PRIVATE);
        second=0;

        recordTime= sharedPreferences.getString(TIME_TEXT,"00:00:00");
        recordTask= sharedPreferences.getString(TASK,"  ");
        record.setText("You spent "+recordTime+" on "+recordTask+" last time");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.remove(TIME_TEXT);
                editor.remove(TASK);

                editor.apply();
                isRunning=true;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning=false;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning=false;
                SharedPreferences.Editor editor= sharedPreferences.edit();
                recordTask=task.getText().toString();
                editor.putString(TIME_TEXT,recordTime);
                editor.putString(TASK,recordTask);
                editor.apply();
                record.setText("You spent "+recordTime+" on "+recordTask+" last time");
                second=0;
                timeTextView.setText("00:00:00");
            }
        });
        if (savedInstanceState!=null)
        {
            isRunning=savedInstanceState.getBoolean(RUN_STATE);
            second=savedInstanceState.getInt(TIME);
            recordTime=savedInstanceState.getString(TIME_TEXT);
            recordTask=savedInstanceState.getString(TASK);


        }
        Handler hd = new Handler();
        hd.post(new Runnable() {
            @Override
            public void run() {

                int hour = second / 3600;
                int minute = (second % 3600) / 60;
                int sec = second % 60;

                if (isRunning==true)
                {
                    String time= String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, sec);
                    timeTextView.setText(time);
                    recordTime = time;
                    second++;
                }
                hd.postDelayed(this, 1000);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(TIME,second);
        outState.putBoolean(RUN_STATE,isRunning);
        outState.putString(TIME_TEXT,recordTime);
        outState.putString(TASK,recordTask);

        super.onSaveInstanceState(outState);
    }

}