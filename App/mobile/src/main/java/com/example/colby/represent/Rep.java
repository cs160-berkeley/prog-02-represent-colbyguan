package com.example.colby.represent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by colby on 2/27/16.
 */
public class Rep implements Parcelable {
    // PARCELABLE CREATION OUT OF DATE
    public String name, party, chamber, id, email, twitterId, termEnd, lastTweet = null, imageUrl = null;
    public Uri website, twitter;
    public Bitmap bitmap;
    public ArrayList<String> cmts;
    public ArrayList<String> bills;

    public final static String DEMOCRAT = "D";
    public final static String REPUBLICAN = "R";
    public final static String INDEP = "I";
    public final static String SENATE = "senate";
    public final static String HOUSE = "house";
    final static String TWITTER_BASE = "https://twitter.com/";

    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dfOut = new SimpleDateFormat("MMM dd yyyy");

    public Rep(String name, String party, String chamber, String email, String website, String twitter, String termEnd, String id) {
        this.name = name;
        this.party = party;
        this.chamber = chamber;
        this.email = email;
        this.website = Uri.parse(website);
        this.twitterId = twitter;
        try {
            this.termEnd = dfOut.format(df.parse(termEnd));
        } catch (ParseException e) {
            this.termEnd = "Unknown";
        }
        this.id = id;

        this.twitter = Uri.parse(TWITTER_BASE + twitter);
    }

    public void setLastTweet(String lastTweet) {
        int sublength = 80;
        if (lastTweet.length() < sublength) {
            sublength = lastTweet.length();
        }
        this.lastTweet = lastTweet.substring(0, sublength) + " ...";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl.replaceAll("_normal", "");
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    public void setCmts(ArrayList<String> cmts) {
        this.cmts = cmts;
    }

    public void setBills(ArrayList<String> rawBills) {
        bills = new ArrayList<>();

        for (String rawBill: rawBills) {
            try {
                Date date = df.parse(rawBill.substring(0, 10));
                bills.add(dfOut.format(date) + rawBill.substring(10));
            } catch (ParseException e) {
                bills.add("Unknown Date" + rawBill.substring(10));
            }
        }
    }

    public String getDisplayName() {
        switch(chamber) {
            case HOUSE:
                return "Rep. " + name;
            default:
                return "Sen. " + name;
        }
    }

    private Rep(Parcel in) {
        this.name = in.readString();
        this.party = in.readString();
        this.chamber = in.readString();
        this.email = in.readString();
        this.website = Uri.parse(in.readString());
        this.twitterId = in.readString();
        this.id = in.readString();

        this.twitter = Uri.parse(TWITTER_BASE + twitterId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(party);
        out.writeString(chamber);
        out.writeString(email.toString());
        out.writeString(website.toString());
        out.writeString(twitterId.toString());
        out.writeString(id);
    }

    public static final Parcelable.Creator<Rep> CREATOR = new Parcelable.Creator<Rep>() {
        public Rep createFromParcel(Parcel in) {
            return new Rep(in);
        }

        public Rep[] newArray(int size) {
            return new Rep[size];
        }
    };

    // Something for dummy data
    public static int nameHash(String name) {
        if (name.indexOf('y') != -1) {
            switch(name.charAt(name.length() - 1)) {
                case 'i':
                    return 0;
                case 'y':
                    return 1;
                default:
                    return 2;
            }
        } else {
            switch(name.charAt(name.length() - 1)) {
                case 'l':
                    return 1;
                case 'k':
                    return 0;
                default:
                    return 2;
            }
        }
    }
}
