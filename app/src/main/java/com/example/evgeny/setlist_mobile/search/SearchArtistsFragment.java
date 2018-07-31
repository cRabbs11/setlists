package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SelectBottomMenuListener;
import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.net.SetlistConnectNew;
import com.example.evgeny.setlist_mobile.utils.Parser;
import com.example.evgeny.setlist_mobile.utils.Threader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class SearchArtistsFragment extends Fragment implements View.OnClickListener {

    private String TAG = "SearchArtistsFragment: " + SearchArtistsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private EditText editSearch;
    private Button btnSearch;
    private List<Artist> mArtists;
    private ArtistsAdapter artistsAdapter;
    private InputMethodManager inputMethodManager;
    private Threader threader;
    Parser parser;
    public BottomNavigationView bottomMenu;
    private SelectBottomMenuListener selectBottomMenuListener;
    private SearchSetlistsFragment searchSetlistsFragment;
    private FragmentTransaction ftrans;

    @SuppressLint("ValidFragment")
    public SearchArtistsFragment(SelectBottomMenuListener selectBottomMenuListener) {
        this.selectBottomMenuListener = selectBottomMenuListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        editSearch.setHint("artists...");
        editSearch.setText("roger waters");
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
        parser = Parser.getInstance();

        artistsAdapter = new ArtistsAdapter();

        threader = Threader.getInstance();
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
            //search(editSearch.getText().toString());
            getArtists(editSearch.getText().toString());
            hideKeyboard(editSearch);
        }
    }

    private void getArtists(String artist) {
        Bundle data = new Bundle();
        data.putString("artist", artist);
        threader.getArtists(data, new SetlistConnectNew.AnswerListener() {
            @Override
            public void getAnswer(String answer) {
                Log.d(TAG, "getAnswer... ");
                mArtists = parser.parseArtists(answer);
                callbackArtists.addArtists(mArtists);
            }
        }, callbackArtists);
    }

    Threader.CallbackArtists callbackArtists = new Threader.CallbackArtists() {
        @Override
        public void addArtists(List<Artist> artists) {
            Log.d(TAG, "addArtists... ");
            mArtists = artists;
            artistsAdapter.notifyDataSetChanged();
        }
    };

    /**
     * скрывает экранную клавиатуру
     * @param view активный view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistHolder> implements ArtistClickListener {

        @Override
        public void onClick(Artist artist) {
            //selectBottomMenuListener.setMenuItem("setlists", artist);
            openSearchSetlists(artist);
        }

        class ArtistHolder extends RecyclerView.ViewHolder{

            TextView name;
            ArtistClickListener artistClickListener;

            public ArtistHolder(View itemView, ArtistClickListener artistClickListener) {
                super(itemView);
                this.artistClickListener = artistClickListener;
                name = itemView.findViewById(R.id.artistName);
            }
        }

        @Override
        public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.artist_layout_item, parent, false);
            return new ArtistHolder(view, this);
        }

        @Override
        public void onBindViewHolder(ArtistHolder holder, int position) {
            Artist artist = mArtists.get(position);
            holder.name.setText(artist.name);
            holder.name.setOnClickListener(
                    (view) -> {
                holder.artistClickListener.onClick(artist);
            });
        }

        @Override
        public int getItemCount() {
            return mArtists.size();
        }
    }

    interface ArtistClickListener {
        void onClick(Artist artist);
    }

    void openSearchSetlists(Artist artist) {
        searchSetlistsFragment = new SearchSetlistsFragment(artist);
        ftrans = getActivity().getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, searchSetlistsFragment);
        ftrans.commit();
    }
}
