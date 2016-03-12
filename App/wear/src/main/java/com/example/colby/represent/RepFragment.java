package com.example.colby.represent;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by colby on 2/28/16.
 */
public class RepFragment extends Fragment {
    BroadcastReceiver receiver;
    ImageView profile;

    public static RepFragment newInstance(String name, String d, int idx) {
        RepFragment f = new RepFragment();
        Bundle args = new Bundle();
        args.putString("title", name.substring(0, 4));
        args.putString("name", name.substring(5));
        args.putString("party", d);
        args.putInt("index", idx);
        f.setArguments(args);

        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rep, container, false);
        String title = getArguments().getString("title");
        String name = getArguments().getString("name");
        String party = getArguments().getString("party");
        final int index = getArguments().getInt("index");

        TextView titleView = (TextView) v.findViewById(R.id.title);
        TextView nameView = (TextView) v.findViewById(R.id.name);
        ImageView partyView = (ImageView) v.findViewById(R.id.party);
        profile = (ImageView) v.findViewById(R.id.profile);
        titleView.setText(title);
        nameView.setText(name);
        profile.setImageResource(R.drawable.initial_profile);

        if (party.equals("R")) {
            partyView.setImageResource(R.drawable.ic_republican);
        } else if (party.equals("I")) {
            partyView.setImageResource(R.drawable.ic_indep);
        }
        updateImage(index);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("RepFragmentReceive", "Got broadcast bitmap");
                updateImage(index);
            }
        };

        RelativeLayout repPadding = (RelativeLayout) v.findViewById(R.id.repPadding);
        repPadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Wear", "Got click event");
                Intent intent = new Intent(getActivity().getBaseContext(), WatchToPhoneService.class);
                intent.putExtra("PATH", "/rep");
                intent.putExtra("INDEX", index);
                getActivity().startService(intent);
            }
        });

        return v;
    }

    public void updateImage(int index) {
        if (WatchListenerService.bitmaps[index] != null && profile != null) {
            profile.setScaleType(ImageView.ScaleType.FIT_XY);
            profile.setImageBitmap(WatchListenerService.bitmaps[index]);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver), new IntentFilter("imageUpdate"));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        super.onStop();
    }
}
