package com.example.colby.represent;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by colby on 3/11/16.
 */
public class CustomFont {
    public static Typeface regular;
    public static Typeface bold;
    public static Typeface extrabold;

    public static void init(Context context) {
        regular = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf");
        extrabold = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-ExtraBold.ttf");
    }
}
