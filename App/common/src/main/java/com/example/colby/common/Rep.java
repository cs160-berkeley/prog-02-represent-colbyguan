package com.example.colby.common;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colby on 2/27/16.
 */
public class Rep implements Parcelable {
    public String name;
    public Uri email, website, twitter;
    // Is democrat?
    public boolean d;

    public Rep(String name, boolean d, String email, String website, String twitter) {
        this.name = name;
        this.d = d;
        this.email = Uri.parse(email);
        this.website = Uri.parse(website);
        this.twitter = Uri.parse(twitter);
    }

    private Rep(Parcel in) {
        this.name = in.readString();
        this.d = in.readByte() != 0;
        this.email = Uri.parse(in.readString());
        this.website = Uri.parse(in.readString());
        this.twitter = Uri.parse(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeByte((byte) (d ? 1 : 0));
        out.writeString(email.toString());
        out.writeString(website.toString());
        out.writeString(twitter.toString());
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
