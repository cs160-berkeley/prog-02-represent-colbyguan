package com.example.colby.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by colby on 2/28/16.
 */
public class RepGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context context;
    private List<String> repNames;
    private List<String> repParties;
    String county, state;
    double dpercent, rpercent;

    public RepGridPagerAdapter(Context context, FragmentManager fm, String serial) {
        super(fm);
        this.context = context;
        repNames = new ArrayList();
        repParties = new ArrayList();

        if (serial == null) {
            return;
        }
        try {
            Log.d("wear JSON", serial);
            JSONObject root = new JSONObject(serial);
            if (root.has("county")) {
                county = root.getString("county");
            } else {
                county = "Unknown";
            }
            if (root.has("state")) {
                state = root.getString("state");
            } else {
                state = "Unknown";
            }
            dpercent = root.getDouble("dvotes");
            rpercent = root.getDouble("rvotes");

            if (dpercent == 0 && rpercent == 0) {
                Toast.makeText(context, "Unable to retrieve vote data for this county", Toast.LENGTH_SHORT).show();
            }
            JSONArray reps = root.optJSONArray("reps");
            for (int i = 0; i < reps.length(); i++ ) {
                JSONObject repObject = reps.getJSONObject(i);
                repNames.add(repObject.getString("name"));
                repParties.add(repObject.getString("party"));
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Error receiving data from phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public int getRowCount() {
        return repNames.size();
    }

    @Override
    public Fragment getFragment(int row, int col) {
        if (col == 0 && row < repNames.size()) {
            RepFragment f = RepFragment.newInstance(repNames.get(row), repParties.get(row), row);
            f.updateImage(row);
            return f;
        } else if (col == 1 && row < repNames.size()) {
            return VoteFragment.newInstance(county, state, dpercent, rpercent);
        }
        return null;
    }
}
