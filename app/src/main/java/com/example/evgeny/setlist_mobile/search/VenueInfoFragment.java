package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.R;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class VenueInfoFragment extends Fragment {

    private String TAG = "ArtistInfoFragment: " + VenueInfoFragment.class.getSimpleName();
    private String mbid_name;
    private TextView bandName;

    @SuppressLint("ValidFragment")
    public VenueInfoFragment(String mbid_name) {
        this.mbid_name = mbid_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_info, container, false);
        bandName = rootView.findViewById(R.id.bandName);
        bandName.setText(mbid_name);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                //handleSearch(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
