package com.example.erikj.jobbtider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private Button btnTid;
    private Button btnViewDB;
    private TextView tvTitle;
    private Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTid = (Button)findViewById(R.id.btnTime);
        btnViewDB = (Button)findViewById(R.id.btnViewDB);
        tvTitle = (TextView)findViewById(R.id.tvTitle);

        long timeMillis = Calendar.getInstance().getTimeInMillis();
        Date thisTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy ww");
        String dateString = format.format(thisTime);

        tvTitle.setText(dateString.substring(0, 10) + "\n" + "Vecka: " + dateString.substring(11));

        if(database.getStartTime() == 0){
            btnTid.setText("Ny tid");
        }else{
            btnTid.setText("Spara tid");
        }

        btnTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long timeMillis = Calendar.getInstance().getTimeInMillis();
                Date thisTime = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy ww");
                String dateString = format.format(thisTime);

                if(database.getStartTime() == 0){
                    database.startTime(timeMillis, dateString);
                    tvTitle.setText(dateString.substring(0, 10) + "\n" + "Vecka: " + dateString.substring(11));
                    btnTid.setText("Spara tid");
                }else{
                    database.addTime(database.getStartDate().substring(0, 10),Integer.parseInt(database.getStartDate().substring(11, 13)), String.valueOf(((timeMillis - database.getStartTime())/1000)/60));
                    btnTid.setText("Ny tid");
                }
            }
        });
        btnViewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DatabaseViewer.class);

                startActivity(intent);
            }
        });
    }
}
