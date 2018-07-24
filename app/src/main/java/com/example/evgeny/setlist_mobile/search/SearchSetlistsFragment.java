package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SelectBottomMenuListener;
import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.net.SetlistConnectNew;
import com.example.evgeny.setlist_mobile.utils.Parser;
import com.example.evgeny.setlist_mobile.utils.Threader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class SearchSetlistsFragment extends Fragment {

    private String TAG = "SearchArtistsFragment: " + SearchSetlistsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private List<Setlist> mSetlists;
    private SetlistsAdapter setlistsAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;
    private SelectBottomMenuListener selectBottomMenuListener;
    private Artist artist;
    Threader threader;
    private Parser parser;
    private SetlistFragment setlistFragment;
    private FragmentTransaction ftrans;

    @SuppressLint("ValidFragment")
    public SearchSetlistsFragment(Artist artist) {
        this.artist = artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setlists, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(setlistsAdapter);
        getSetlists(artist);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetlists = new ArrayList<>();
        parser = Parser.getInstance();
        threader = Threader.getInstance();
        setlistsAdapter = new SetlistsAdapter();
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

    private void getSetlists(Artist artist) {
        Bundle data = new Bundle();
        String mbid = artist.mbid;
        data.putString("mbid", mbid);
        threader.getSetlists(data, new SetlistConnectNew.AnswerListener() {
            @Override
            public void getAnswer(String answer) {
                mSetlists = parser.parseSetlists(answer);
                callbackSetlists.addSetlists(mSetlists);
            }
        }, callbackSetlists);
    }

    Threader.CallbackSetlists callbackSetlists = new Threader.CallbackSetlists() {
        @Override
        public void addSetlists(List<Setlist> setlists) {
            Log.d(TAG, "addSetlists... ");
            mSetlists = setlists;
            setlistsAdapter.notifyDataSetChanged();
        }
    };

    class SetlistsAdapter extends RecyclerView.Adapter<SetlistsAdapter.SetlistHolder> implements OnSetlistClickListener {

        @Override
        public void onSetlistClick(Setlist setlist) {
            Log.d("BMTH", "нажали на сетлист");
            openSetlist(setlist);
        }

        class SetlistHolder extends RecyclerView.ViewHolder{

            TextView month;
            TextView day;
            TextView year;
            TextView name;
            OnSetlistClickListener onSetlistClickListener;

            public SetlistHolder(View itemView, OnSetlistClickListener onSetlistClickListener) {
                super(itemView);
                this.onSetlistClickListener = onSetlistClickListener;
                name = itemView.findViewById(R.id.artistName);
                month = itemView.findViewById(R.id.month);
                day = itemView.findViewById(R.id.day);
                year = itemView.findViewById(R.id.year);
            }
        }

        @Override
        public SetlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.setlist_layout_item, parent, false);
            return new SetlistHolder(view, this);
        }

        @Override
        public void onBindViewHolder(SetlistHolder holder, int position) {
            Setlist setlist = mSetlists.get(position);
            String name = setlist.artist.name;
            String eventDate = setlist.eventDate;

            SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");

            try {
                Date date = dt.parse(eventDate);
                SimpleDateFormat month = new SimpleDateFormat("mm");
                String sMonth = identMonth(month.format(date));
                SimpleDateFormat day = new SimpleDateFormat("dd");
                String sDay = day.format(date);
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                String sYear = year.format(date);

                holder.month.setText(sMonth);
                holder.day.setText(sDay);
                holder.year.setText(sYear);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String songsSize = String.valueOf(setlist.set.songs.size());
            String venue = setlist.venue.name + " " + setlist.venue.city.name + " " + setlist.venue.city.country.name + " songs: " + songsSize;
            String header = name + " at: " + eventDate + " in " + venue;
            holder.name.setText(header);
            holder.name.setOnClickListener(view -> {
                holder.onSetlistClickListener.onSetlistClick(setlist);
            });
        }

        @Override
        public int getItemCount() {
            return mSetlists.size();
        }
    }

    private String identMonth(String format) {
        switch(format) {
            case "01":
                return getResources().getString(R.string.jan);
            case "02":
                return getResources().getString(R.string.feb);
            case "03":
                return getResources().getString(R.string.mar);
            case "04":
                return getResources().getString(R.string.apr);
            case "05":
                return getResources().getString(R.string.may);
            case "06":
                return getResources().getString(R.string.jun);
            case "07":
                return getResources().getString(R.string.jul);
            case "08":
                return getResources().getString(R.string.aug);
            case "09":
                return getResources().getString(R.string.sep);
            case "10":
                return getResources().getString(R.string.oct);
            case "11":
                return getResources().getString(R.string.nov);
            case "12":
                return getResources().getString(R.string.nov);
            default:
                return getResources().getString(R.string.jan);
        }
    }

    interface OnSetlistClickListener {
        void onSetlistClick(Setlist setlist);
    }

    void openSetlist(Setlist setlist) {
        setlistFragment = new SetlistFragment(setlist);
        ftrans = getActivity().getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, setlistFragment);
        ftrans.commit();
    }
}
