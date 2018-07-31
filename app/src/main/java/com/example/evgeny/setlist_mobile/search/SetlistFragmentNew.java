package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
public class SetlistFragmentNew extends Fragment {

    private String TAG = "SearchArtistsFragment: " + SetlistFragmentNew.class.getSimpleName();
    private ExpandableListView expListView;
    private TextView emptySearchText;
    private List<Setlist> mSetlists;
    private SetlistAdapter setlistAdapter;
    private ExpListAdapter expListAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;
    private SelectBottomMenuListener selectBottomMenuListener;
    private Setlist setlist;
    Threader threader;
    private Parser parser;

    @SuppressLint("ValidFragment")
    public SetlistFragmentNew(Setlist setlist) {
        this.setlist = setlist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setlists_new, container, false);

        expListView = (ExpandableListView) rootView.findViewById(R.id.expListView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);
        expListAdapter = new ExpListAdapter();
        //LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(llm);
        //recyclerView.setAdapter(setlistAdapter);
        expListView.setGroupIndicator(null);
        expListView.setAdapter(expListAdapter);
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

    class ExpListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return setlist.sets.size();
        }

        @Override
        public int getChildrenCount(int group) {
            return setlist.sets.get(group).songs.size();
        }

        @Override
        public Object getGroup(int group) {
            return setlist.sets.get(group);
        }

        @Override
        public Object getChild(int group, int child) {
            return setlist.sets.get(group).songs.get(child);
        }

        @Override
        public long getGroupId(int group) {
            return group;
        }

        @Override
        public long getChildId(int group, int child) {
            return child;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override
        public View getGroupView(int group, boolean b, View groupView, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
            groupView = inflater.inflate(R.layout.set_layout_item, null);

            TextView setName = (TextView) groupView.findViewById(R.id.setName);
            Set set = setlist.sets.get(group);

            if (set.name!=null) {
                setName.setText(set.name);
            }

            if (set.encore!=null) {
                setName.setText(set.encore);
            }
            return groupView;
        }

        @Override
        public View getChildView(int group, int child, boolean b, View childView, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
            childView = inflater.inflate(R.layout.song_layout_item, null);
            TextView songName = (TextView) childView.findViewById(R.id.songName);
            ImageView tape = (ImageView) childView.findViewById(R.id.tape);
            TextView descriptionView = (TextView) childView.findViewById(R.id.description);
            Song song = setlist.sets.get(group).songs.get(child);

            String cover = " ";
            String info = " ";
            String name = song.name;
            String descrition = " ";
            if (song.cover.name!=null) {
                cover =  song.cover.name;
                descrition = descrition + " (" + cover + " song)";
            }

            if (song.info!=null) {
                info = song.info;
                descrition = descrition + " (" + info + " )";
            }

            if (song.tape==true) {
                tape.setVisibility(View.VISIBLE);
            }

            songName.setText(name);

            if (!descriptionView.equals(" ")) {
                descriptionView.setText(descrition);
            }
            return childView;
        }

        @Override
        public boolean isChildSelectable(int group, int child) {
            return false;
        }

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
