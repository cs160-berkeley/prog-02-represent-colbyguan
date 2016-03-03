package com.example.colby.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by colby on 2/28/16.
 */
public class RepGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context context;
    private List<String> repTitles;
    private List<String> repNames;
    private List<Boolean> repParties;
    private List<String> counties;
    private List<String> states;
    private List<Double> d_percents;
    private List<Double> r_percents;


    public RepGridPagerAdapter(Context context, FragmentManager fm, String serial) {
        super(fm);
        this.context = context;
        repTitles = new ArrayList();
        repNames = new ArrayList();
        repParties = new ArrayList();

        if (serial == null) {
            return;
        }

        String[] repSerials = serial.split(";");
        for (String rep: repSerials) {
            String[] data = rep.split(",");
            if (data[0].substring(0, 3).equalsIgnoreCase("sen")) {
                repTitles.add("Senator");
            } else {
                repTitles.add("Representative");
            }
            repNames.add(data[0].substring(5));
            repParties.add(data[1].equals("1") ? true : false);
        }


        counties = new ArrayList(Arrays.asList("Kern County", "Orange County", "Santa Clara County", "The Bronx", "Queens", "Dutchess County"));
        states = new ArrayList(Arrays.asList("California", "California", "California", "New York", "New York", "New York"));
        d_percents = new ArrayList(Arrays.asList(25.0, 50.0, 75.0, 91.2, 78.8, 52.5));
        r_percents = new ArrayList(Arrays.asList(75.0, 50.0, 25.0, 8.3, 20.3, 45.8));
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        if (col == 0 && row < repTitles.size()) {
            return RepFragment.newInstance(repTitles.get(row), repNames.get(row), repParties.get(row), row);
        } else if (col == 1 && row < repTitles.size()) {
            if (repNames.get(0).charAt(repNames.get(0).length() - 1) == 'i') {
                return VoteFragment.newInstance(counties.get(row), states.get(row), d_percents.get(row), r_percents.get(row));
            } else {
                row = row + 3;
                return VoteFragment.newInstance(counties.get(row), states.get(row), d_percents.get(row), r_percents.get(row));
            }
        }
        return null;
    }
}
