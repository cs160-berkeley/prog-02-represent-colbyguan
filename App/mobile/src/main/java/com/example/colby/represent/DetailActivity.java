package com.example.colby.represent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colby.common.Rep;

public class DetailActivity extends AppCompatActivity {
    Rep rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        rep = (Rep) i.getParcelableExtra("rep");

        Button linkEmail = (Button) findViewById(R.id.linkEmail);
        Button linkWebsite = (Button) findViewById(R.id.linkWebsite);
        Button linkTwitter = (Button) findViewById(R.id.linkTwitter);
        TextView name = (TextView) findViewById(R.id.name);
        ImageView profile = (ImageView) findViewById(R.id.profile);

        if (!rep.d) {
            View tray = findViewById(R.id.tray);
            View timeline = findViewById(R.id.timeline);
            ImageView party = (ImageView) findViewById(R.id.party);
            ImageView email = (ImageView) findViewById(R.id.email);
            ImageView website = (ImageView) findViewById(R.id.website);
            ImageView twitter = (ImageView) findViewById(R.id.twitter);

            ImageView endMarker = (ImageView) findViewById(R.id.endMarker);
            ImageView check1 = (ImageView) findViewById(R.id.check1);
            ImageView check2 = (ImageView) findViewById(R.id.check2);
            ImageView check3 = (ImageView) findViewById(R.id.check3);

            tray.setBackgroundColor(Color.parseColor("#F0002F"));
            timeline.setBackgroundColor(Color.parseColor("#F0002F"));
            party.setImageResource(R.drawable.ic_republican);
            email.setImageResource(R.drawable.ic_email_r);
            website.setImageResource(R.drawable.ic_website_r);
            twitter.setImageResource(R.drawable.ic_twitter_r);
            endMarker.setImageResource(R.drawable.ic_blank_r);
            check1.setImageResource(R.drawable.ic_bill_r);
            check2.setImageResource(R.drawable.ic_bill_r);
            check3.setImageResource(R.drawable.ic_bill_r);
        }

        name.setText(rep.name);
        switch(Rep.nameHash(rep.name)) {
            case 0:
                profile.setImageResource(R.drawable.pelosi);
                break;
            case 1:
                profile.setImageResource(R.drawable.mccarthy);
                break;
            default:
                profile.setImageResource(R.drawable.chu);
                break;
        }

        linkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, rep.email);
                startActivity(intent);
            }
        });
        linkWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, rep.website);
                startActivity(intent);
            }
        });
        linkTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, rep.twitter);
                startActivity(intent);
            }
        });
    }

}
