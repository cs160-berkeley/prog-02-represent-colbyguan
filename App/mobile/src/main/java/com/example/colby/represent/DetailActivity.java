package com.example.colby.represent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    Rep rep;
    final int maxBillLength = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        rep = RepCollection.getInstance().get(intent.getIntExtra("INDEX", 0));

        Button linkEmail = (Button) findViewById(R.id.linkEmail);
        Button linkWebsite = (Button) findViewById(R.id.linkWebsite);
        Button linkTwitter = (Button) findViewById(R.id.linkTwitter);
        TextView name = (TextView) findViewById(R.id.name);
        CircleImageView profile = (CircleImageView) findViewById(R.id.profile);

        TextView cmts = (TextView) findViewById(R.id.cmts);
        TextView bill1 = (TextView) findViewById(R.id.bill1);
        TextView bill2 = (TextView) findViewById(R.id.bill2);
        TextView bill3 = (TextView) findViewById(R.id.bill3);
        TextView termEnd = (TextView) findViewById(R.id.endDate);

        if (rep.party.equals(Rep.REPUBLICAN)) {
            int repub = Color.parseColor("#F0002F");
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

            tray.setBackgroundColor(repub);
            timeline.setBackgroundColor(repub);
            party.setImageResource(R.drawable.ic_republican);
            email.setImageResource(R.drawable.ic_email_r);
            website.setImageResource(R.drawable.ic_website_r);
            twitter.setImageResource(R.drawable.ic_twitter_r);
            endMarker.setImageResource(R.drawable.ic_blank_r);
            check1.setImageResource(R.drawable.ic_bill_r);
            check2.setImageResource(R.drawable.ic_bill_r);
            check3.setImageResource(R.drawable.ic_bill_r);
            profile.setBorderColor(repub);
        } else if (rep.party.equals(Rep.INDEP)) {
            int indep = Color.parseColor("#673AB7");
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

            tray.setBackgroundColor(indep);
            timeline.setBackgroundColor(indep);
            party.setImageResource(R.drawable.ic_indep);
            email.setImageResource(R.drawable.ic_email_i);
            website.setImageResource(R.drawable.ic_website_i);
            twitter.setImageResource(R.drawable.ic_twitter_i);
            endMarker.setImageResource(R.drawable.ic_blank_i);
            check1.setImageResource(R.drawable.ic_bill_i);
            check2.setImageResource(R.drawable.ic_bill_i);
            check3.setImageResource(R.drawable.ic_bill_i);
            profile.setBorderColor(indep);
        }

        name.setText(rep.getDisplayName());
        if (rep.bitmap != null) {
            profile.setImageBitmap(rep.bitmap);
        }
        String cmtText = "";
        for (int i = 0; i < rep.cmts.size() && i < 3; i++) {
            String cmtName = rep.cmts.get(i);
            int sublength = 35;
            String pad = "...";

            if (cmtName.length() < sublength) {
                sublength = cmtName.length();
                pad = "";
            }
            cmtText += rep.cmts.get(i).substring(0, sublength) + pad + "\n";
        }
        if (rep.cmts.size() > 3) {
            cmtText += "(And " + (rep.cmts.size() - 3) + " more)";
        }
        cmts.setText(cmtText);

        if (rep.bills.get(0).length() > maxBillLength) {
            bill1.setText(rep.bills.get(0).substring(0, maxBillLength) + "...");
        } else {
            bill1.setText(rep.bills.get(0));
        }

        if (rep.bills.get(1).length() > 200) {
            bill2.setText(rep.bills.get(1).substring(0, maxBillLength) + "...");
        } else {
            bill2.setText(rep.bills.get(1));
        }

        if (rep.bills.get(2).length() > 200) {
            bill3.setText(rep.bills.get(2).substring(0, maxBillLength) + "...");
        } else {
            bill3.setText(rep.bills.get(2));
        }
        termEnd.setText(rep.termEnd + " - End of Term");

        linkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: " + rep.email));
                startActivity(Intent.createChooser(intent, "Send email using: "));
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

        name.setTypeface(CustomFont.bold);
        cmts.setTypeface(CustomFont.regular);
        bill1.setTypeface(CustomFont.regular);
        bill2.setTypeface(CustomFont.regular);
        bill3.setTypeface(CustomFont.regular);
        termEnd.setTypeface(CustomFont.regular);
    }

}
