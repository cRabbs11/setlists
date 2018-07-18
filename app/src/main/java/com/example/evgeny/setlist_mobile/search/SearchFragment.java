package com.example.evgeny.setlist_mobile.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.Artist;
import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SetlistConnection;

import java.util.ArrayList;
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
    private List<Artist> mArtists;
    private ArtistsAdapter artistsAdapter;
    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        editSearch.setHint("artists...");

        btnSearch.setOnClickListener(this);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(artistsAdapter);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtists = new ArrayList<>();

        artistsAdapter = new ArtistsAdapter();
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
            hideKeyboard(editSearch);
        }
    }

    void search(String bandName) {
        setlistConnection = new SetlistConnection(this);
        setlistConnection.setArtistName(bandName);
        setlistConnection.execute();
    }

    /**
     * скрывает экранную клавиатуру
     * @param view активный view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public void getArtistList(List<Artist> artists) {
        mArtists.clear();
        for (Artist artist: artists) {
            mArtists.add(artist);
            Log.d("BMTH", "in fragment name: " + artist.name);
            Log.d("BMTH", "in fragment sortName: " + artist.sortName);
            Log.d("BMTH", "in fragment url: " + artist.url);
        }
        Log.d("BMTH", " mArtists.size(): " + mArtists.size());
        artistsAdapter.notifyDataSetChanged();
    }

    class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistHolder> {

        class ArtistHolder extends RecyclerView.ViewHolder{

            TextView name;

            public ArtistHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.artistName);
            }
        }

        @Override
        public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.artist_layout_item, parent, false);
            return new ArtistHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistHolder holder, int position) {
            Artist artist = mArtists.get(position);
            holder.name.setText(artist.name);
        }

        @Override
        public int getItemCount() {
            return mArtists.size();
        }
    }
}
