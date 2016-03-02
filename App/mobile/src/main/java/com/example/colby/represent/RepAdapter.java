package com.example.colby.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colby.common.Rep;

import java.util.List;

/**
 * Created by colby on 2/27/16.
 */
public class RepAdapter extends BaseAdapter {
    private final Context context;
    private final List<Rep> reps;

    public RepAdapter(Context context, List<Rep> reps) {
        this.context = context;
        this.reps = reps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.rep_row, parent, false);

        final Rep rep = reps.get(position);

        if (rep != null) {
            Button linkEmail = (Button) v.findViewById(R.id.linkEmail);
            Button linkWebsite = (Button) v.findViewById(R.id.linkWebsite);
            Button linkTwitter = (Button) v.findViewById(R.id.linkTwitter);
            TextView name = (TextView) v.findViewById(R.id.name);
            ImageView profile = (ImageView) v.findViewById(R.id.profile);

            if (!rep.d) {
                View tray = v.findViewById(R.id.tray);
                ImageView party = (ImageView) v.findViewById(R.id.party);
                ImageView email = (ImageView) v.findViewById(R.id.email);
                ImageView website = (ImageView) v.findViewById(R.id.website);
                ImageView twitter = (ImageView) v.findViewById(R.id.twitter);


                tray.setBackgroundColor(Color.parseColor("#F0002F"));
                party.setImageResource(R.drawable.ic_republican);
                email.setImageResource(R.drawable.ic_email_r);
                website.setImageResource(R.drawable.ic_website_r);
                twitter.setImageResource(R.drawable.ic_twitter_r);
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
                    context.startActivity(intent);
                }
            });
            linkWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, rep.website);
                    context.startActivity(intent);
                }
            });
            linkTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, rep.twitter);
                    context.startActivity(intent);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Phone", "Going to detail view");
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("rep", rep);
                    context.startActivity(intent);
                }
            });
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Rep getItem(int position) {
        return reps.get(position);
    }

    @Override
    public int getCount() {
        return reps.size();
    }
}
