package com.example.colby.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by colby on 2/27/16.
 */
public class RepAdapter extends BaseAdapter {
    private final Context context;
    private final RepCollection reps;

    public RepAdapter(Context context) {
        this.context = context;
        reps = RepCollection.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.rep_row, parent, false);

        final Rep rep = reps.get(position);
        final int idx = position;

        if (rep != null) {
            Button linkEmail = (Button) v.findViewById(R.id.linkEmail);
            Button linkWebsite = (Button) v.findViewById(R.id.linkWebsite);
            Button linkTwitter = (Button) v.findViewById(R.id.linkTwitter);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView tweet = (TextView) v.findViewById(R.id.tweet);
            CircleImageView profile = (CircleImageView) v.findViewById(R.id.profile);

            if (rep.party.equals(Rep.REPUBLICAN)) {
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
                profile.setBorderColor(Color.parseColor("#F0002F"));
            } else if (rep.party.equals(Rep.INDEP)) {
                int indep = Color.parseColor("#673AB7");
                View tray = v.findViewById(R.id.tray);
                ImageView party = (ImageView) v.findViewById(R.id.party);
                ImageView email = (ImageView) v.findViewById(R.id.email);
                ImageView website = (ImageView) v.findViewById(R.id.website);
                ImageView twitter = (ImageView) v.findViewById(R.id.twitter);

                tray.setBackgroundColor(indep);
                party.setImageResource(R.drawable.ic_indep);
                email.setImageResource(R.drawable.ic_email_i);
                website.setImageResource(R.drawable.ic_website_i);
                twitter.setImageResource(R.drawable.ic_twitter_i);
                profile.setBorderColor(indep);
            }

            name.setText(rep.getDisplayName());
            if (rep.bitmap != null) {
                profile.setImageBitmap(rep.bitmap);
            }
            tweet.setText(Html.fromHtml("<font color=#00aced>@" + rep.twitterId + "</font> " + rep.lastTweet));

            linkEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto: " + rep.email));
                    context.startActivity(Intent.createChooser(intent, "Send email using: "));
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
                    Log.d("RepAdapter", "Opening " + rep.twitter);
                    context.startActivity(intent);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Phone", "Going to detail view");
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("INDEX", idx);
                    context.startActivity(intent);
                }
            });
            tweet.setTypeface(CustomFont.regular);
            name.setTypeface(CustomFont.bold);
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
