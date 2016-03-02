package com.example.colby.represent;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.colby.common.Rep;

/**
 * Created by colby on 2/28/16.
 */
public class RepFragment extends Fragment {

    public static RepFragment newInstance(String title, String name, boolean d, int idx) {
        RepFragment f = new RepFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("name", name);
        args.putBoolean("party", d);
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
        boolean party = getArguments().getBoolean("party");
        final int index = getArguments().getInt("index");

        TextView titleView = (TextView) v.findViewById(R.id.title);
        TextView nameView = (TextView) v.findViewById(R.id.name);
        ImageView partyView = (ImageView) v.findViewById(R.id.party);
        ImageView profile = (ImageView) v.findViewById(R.id.profile);
        titleView.setText(title);
        nameView.setText(name);

        if (!party) {
            partyView.setImageResource(R.drawable.ic_republican);
        }
        switch(Rep.nameHash(name)) {
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
}
