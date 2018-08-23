package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.model.City;
import com.example.evgeny.setlist_mobile.model.Country;
import com.example.evgeny.setlist_mobile.model.Venue;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class VenueInfoFragment extends Fragment {

    private String TAG = "VenueInfoFragment: " + VenueInfoFragment.class.getSimpleName();
    private TextView id;
    private TextView name;
    private TextView url;
    private Venue venue;
    private City city;
    private Country country;

    @SuppressLint("ValidFragment")
    public VenueInfoFragment(Venue venue) {
        this.venue = venue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_info, container, false);
        id = rootView.findViewById(R.id.id);
        name = rootView.findViewById(R.id.name);
        url = rootView.findViewById(R.id.url);

        id.setText(venue.id);
        name.setText(venue.name);
        url.setText(venue.url);

        if (venue.city!=null) {
            city = venue.city;
            Log.d(TAG, "city.name: " + city.name);
            Log.d(TAG, "city long: " + city.coords.coord_long);
            Log.d(TAG, "city.lat: " + city.coords.coord_lat);
        } else {
            Log.d(TAG, "city is null");
        }

        if (city.country!=null) {
            country = city.country;
            Log.d(TAG, "country.name: " + country.name);
        } else {
            Log.d(TAG, "country is null");
        }
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
