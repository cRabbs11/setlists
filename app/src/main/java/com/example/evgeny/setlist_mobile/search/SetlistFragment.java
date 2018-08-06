package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SelectBottomMenuListener;
import com.example.evgeny.setlist_mobile.model.Set;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.model.Song;
import com.example.evgeny.setlist_mobile.utils.Parser;
import com.example.evgeny.setlist_mobile.utils.Threader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class SetlistFragment extends Fragment {

    private String TAG = "SearchArtistsFragment: " + SetlistFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private List<Setlist> mSetlists;
    private SetlistAdapter setlistAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;
    private SelectBottomMenuListener selectBottomMenuListener;
    private Setlist setlist;
    Threader threader;
    private Parser parser;

    @SuppressLint("ValidFragment")
    public SetlistFragment(Setlist setlist) {
        this.setlist = setlist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setlists, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(setlistAdapter);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetlists = new ArrayList<>();
        parser = Parser.getInstance();
        threader = Threader.getInstance();
        setlistAdapter = new SetlistAdapter();
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

    class SetlistAdapter extends RecyclerView.Adapter<SetlistAdapter.SetlistHolder> implements OnSetlistClickListener {

        @Override
        public void onSetlistClick(Song song) {
            Log.d("BMTH", "нажали на сетлист");
        }

        class SetlistHolder extends RecyclerView.ViewHolder{

            TextView name;
            OnSetlistClickListener onSetlistClickListener;

            public SetlistHolder(View itemView, OnSetlistClickListener onSetlistClickListener) {
                super(itemView);
                this.onSetlistClickListener = onSetlistClickListener;
                name = itemView.findViewById(R.id.artistName);
            }
        }

        @Override
        public SetlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.artist_layout_item, parent, false);
            return new SetlistHolder(view, this);
        }

        @Override
        public void onBindViewHolder(SetlistHolder holder, int position) {
            //Song song = setlist.set.songs.get(position);
            Set set = setlist.sets.get(0);
            Song song = setlist.sets.get(0).songs.get(position);
            String encore = " ";
            String cover = " ";
            String info = " ";
            String name = song.name;
            String header = name;
            if (song.cover.name!=null) {
                cover =  song.cover.name;
                header = header + " (" + cover + " cover)";
            }

            if (song.info!=null) {
                info = song.info;
                header = header + " (" + info + " )";
            }

            if (set.encore!=null) {
                encore = set.encore;
                header = header + " " + encore;
            }

            holder.name.setText(header);
            holder.name.setOnClickListener(view -> {
                holder.onSetlistClickListener.onSetlistClick(song);
            });
        }

        @Override
        public int getItemCount() {return setlist.sets.get(0).songs.size();}
    }

    interface OnSetlistClickListener {
        void onSetlistClick(Song song);
    }
}
