package com.example.erikj.jobbtider;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DatabaseViewer extends AppCompatActivity {

    private ListView lvTimesList;
    private Button btnDays;
    private Button btnMonths;
    private Button btnYears;
    private Button btnClear;

    private Database db;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        db = new Database(this);

        lvTimesList = (ListView)findViewById(R.id.lvTimesList);
        btnDays = (Button)findViewById(R.id.btnDays);
        btnMonths = (Button)findViewById(R.id.btnMonths);
        btnYears = (Button)findViewById(R.id.btnYears);
        btnClear = (Button)findViewById(R.id.btnClearList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, db.getAllTimes());
        lvTimesList.setAdapter(adapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.clearTable("Time");
                adapter.clear();
            }
        });
    }
}
