package com.example.evgeny.setlist_mobile.search;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.Artist;
import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SetlistConnection;

import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

public class SearchFragment extends Fragment implements View.OnClickListener, SetlistConnection.ArtitstListListener {

    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private EditText editSearch;
    private Button btnSearch;
    private SetlistConnection setlistConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //уведомляем фрагмент, что он работает с optionsMenu
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);
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
    public void onClick(View view) {
        if (!editSearch.getText().equals("")) {
            search(editSearch.getText().toString());
        }
    }

    void search(String bandName) {
        setlistConnection = new SetlistConnection(this);
        setlistConnection.setArtistName(bandName);
        setlistConnection.execute();
    }

    @Override
    public void getArtistList(List<Artist> artists) {
        for (Artist artist: artists) {
            Log.d("BMTH", "in fragment name: " + artist.name);
            Log.d("BMTH", "in fragment sortName: " + artist.sortName);
            Log.d("BMTH", "in fragment url: " + artist.url);
        }
    }

    
}
