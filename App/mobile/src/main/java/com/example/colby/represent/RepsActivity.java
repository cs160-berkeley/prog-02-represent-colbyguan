package com.example.colby.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class RepsActivity extends AppCompatActivity {

    ListView repsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repsList = (ListView) findViewById(R.id.repsList);

        final RepAdapter adapter = new RepAdapter(this);
        repsList.setAdapter(adapter);

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        startService(sendIntent);
    }

}
