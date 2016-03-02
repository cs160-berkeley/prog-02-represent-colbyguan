package com.example.colby.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.colby.common.Rep;

import java.util.ArrayList;

public class RepsActivity extends AppCompatActivity {

    ListView repsList;
    public static ArrayList<Rep> reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repsList = (ListView) findViewById(R.id.repsList);

        Intent i = getIntent();
        int zipcode = i.getIntExtra("zipcode", 95070);
        reps = new ArrayList();
        if (zipcode < 50000) {
            reps.add(new Rep("Sen. Charles Rangel", true, "https://rangel.house.gov/contact/email-me", "https://pelosi.house.gov/", "https://twitter.com/NancyPelosi"));
            reps.add(new Rep("Rep. Steve Israel", true, "https://rangel.house.gov/contact/email-me", "https://pelosi.house.gov/", "https://twitter.com/NancyPelosi"));
            reps.add(new Rep("Rep. Elise Stefanik", false, "https://rangel.house.gov/contact/email-me", "https://pelosi.house.gov/", "https://twitter.com/NancyPelosi"));
        } else {
            reps.add(new Rep("Sen. Nancy Pelosi", true, "https://pelosi.house.gov/contact-me/email-me", "https://rangel.house.gov/", "https://twitter.com/cbrangel"));
            reps.add(new Rep("Rep. Kevin McCarthy", false, "https://pelosi.house.gov/contact-me/email-me", "https://rangel.house.gov/", "https://twitter.com/cbrangel"));
            reps.add(new Rep("Rep. Judy Chu", true, "https://pelosi.house.gov/contact-me/email-me", "https://rangel.house.gov/", "https://twitter.com/cbrangel"));
        }

        final RepAdapter adapter = new RepAdapter(this, reps);
        repsList.setAdapter(adapter);

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        startService(sendIntent);
    }

}
