package com.example.colby.represent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by colby on 2/29/16.
 */
public class VoteFragment extends Fragment {
    NumberFormat fmt = new DecimalFormat("#0.00");
    public static VoteFragment newInstance(String county, String state, double d_percent, double r_percent) {
        VoteFragment f = new VoteFragment();
        Bundle args = new Bundle();
        args.putString("county", county);
        args.putString("state", state);
        args.putDouble("d_percent", d_percent);
        args.putDouble("r_percent", r_percent);
        f.setArguments(args);

        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vote, container, false);
        String county = getArguments().getString("county");
        String state = getArguments().getString("state");
        double d_percent = getArguments().getDouble("d_percent");
        double r_percent = getArguments().getDouble("r_percent");

        TextView countyView = (TextView) v.findViewById(R.id.county);
        TextView stateView  = (TextView) v.findViewById(R.id.subtext);
        View d_line = v.findViewById(R.id.voteAmount);
        TextView democratInfo = (TextView) v.findViewById(R.id.democratInfo);
        TextView republicanInfo = (TextView) v.findViewById(R.id.republicanInfo);

        countyView.setText(county);
        stateView.setText(state + " - 2012 Vote");


        Log.d("VoteFragment", "setting d_line " + d_line.getLayoutParams().width);
        democratInfo.setText("Obama\n" + fmt.format(d_percent) + "%");
        republicanInfo.setText("Romney\n" + fmt.format(r_percent) + "%");

        ViewGroup.LayoutParams lp = d_line.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        lp.width = (int) (d_percent * scale + 0.5f);
        d_line.setLayoutParams(lp);

        if (d_percent == 0 && r_percent == 0) {
            stateView.setText(state + " - 2012 Vote\nUnable to get vote data");
            lp.width = (int) (50 * scale + 0.5f);
            d_line.setLayoutParams(lp);
        }


        return v;
    }
}
