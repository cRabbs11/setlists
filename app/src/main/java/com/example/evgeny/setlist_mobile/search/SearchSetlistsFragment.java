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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeny.setlist_mobile.MainActivity;
import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SelectBottomMenuListener;
import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.net.SetlistConnect;
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
    private LinearLayout listLayout;
    private LinearLayout waitLayout;
    private TextView waitText;
    private List<Setlist> mSetlists;
    private SetlistsAdapter setlistsAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;
    private SelectBottomMenuListener selectBottomMenuListener;
    private Artist artist;
    Threader threader;
    private Parser parser;
    private SetlistFragmentNew setlistFragmentNew;
    private ArtistInfoFragment artistInfoFragment;
    private FragmentTransaction ftrans;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d("BMTH", "newState: " + newState);
            LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            Log.d("BMTH", "mSetlists.size(): " + mSetlists.size());
            Log.d("BMTH", "lastVisiblePosition: " + lastVisiblePosition);

            if ((mSetlists.size()-1)==lastVisiblePosition) {
                Log.d("BMTH", "need update! ");
            }

        }
    };

    @SuppressLint("ValidFragment")
    public SearchSetlistsFragment(Artist artist) {
        this.artist = artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setlists, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        listLayout = (LinearLayout) rootView.findViewById(R.id.listLayout);
        waitLayout = (LinearLayout) rootView.findViewById(R.id.waitLayout);
        waitText = (TextView) rootView.findViewById(R.id.waitText);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(setlistsAdapter);
        recyclerView.addOnScrollListener(onScrollListener);
        waitMessage(true, R.string.search);
        getSetlists(artist);

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle(artist.name);
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
        //menuInflater.inflate(R.menu.menu_more_artist, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_artist_info:
                openStatistic(artist.mbid);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openStatistic(String mbid_name) {
        artistInfoFragment = new ArtistInfoFragment(mbid_name);
        ftrans = getActivity().getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, artistInfoFragment);
        ftrans.addToBackStack("");
        ftrans.commit();
    }

    private void getSetlists(Artist artist) {
        Bundle data = new Bundle();
        String mbid = artist.mbid;
        data.putString("mbid", mbid);
        threader.getSetlists(data, new SetlistConnect.AnswerListener() {
            @Override
            public void getAnswer(String answer) {
                if (!answer.equals("0")) {
                    mSetlists = parser.parseSetlists(answer);
                    callbackSetlists.addSetlists(mSetlists);
                } else {
                    callbackSetlists.error();
                }
            }
        }, callbackSetlists);
    }

    Threader.CallbackSetlists callbackSetlists = new Threader.CallbackSetlists() {
        @Override
        public void addSetlists(List<Setlist> setlists) {
            Log.d(TAG, "addSetlists... ");
            mSetlists = setlists;
            setlistsAdapter.notifyDataSetChanged();
            waitMessage(false, R.string.search);
        }

        @Override
        public void error() {
            waitMessage(true, R.string.search_setlist_error);
        }
    };

    private void waitMessage(boolean show, int stringId) {
        if (show) {
            waitLayout.setVisibility(View.VISIBLE);
            waitText.setText(stringId);
            listLayout.setVisibility(View.GONE);
        } else {
            waitLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        }
    }

    class SetlistsAdapter extends RecyclerView.Adapter<SetlistsAdapter.SetlistHolder> implements OnSetlistClickListener {

        @Override
        public void onSetlistClick(Setlist setlist) {
            Log.d("BMTH", "нажали на сетлист");
            if (setlist.sets.isEmpty()) {
                Toast.makeText(getActivity(), "Сетлист неизвестен", Toast.LENGTH_SHORT).show();
            } else {
                openSetlist(setlist);
            }
        }

        class SetlistHolder extends RecyclerView.ViewHolder{

            View setlistView;
            LinearLayout setlist;
            LinearLayout layout_tour;
            TextView month;
            TextView day;
            TextView year;
            TextView artist;
            TextView tour;
            TextView venue;
            OnSetlistClickListener onSetlistClickListener;

            public SetlistHolder(View itemView, OnSetlistClickListener onSetlistClickListener) {
                super(itemView);
                setlistView = itemView;
                this.onSetlistClickListener = onSetlistClickListener;
                setlist = itemView.findViewById(R.id.setlist);
                layout_tour = itemView.findViewById(R.id.layout_tour);
                artist = itemView.findViewById(R.id.artist);
                tour = itemView.findViewById(R.id.tour);
                venue = itemView.findViewById(R.id.venue);
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

            //if (setlist.sets.isEmpty()) {
            //    holder.setlistView.setBackgroundResource(R.drawable.cell_selector_red);
            //} else {
            //    holder.setlistView.setBackgroundResource(R.drawable.cell_selector_green);
            //}

            String tourName = " not tour name";
            if (setlist.tour.name!=null) {
                tourName = setlist.tour.name;
            } else {
                holder.layout_tour.setVisibility(View.GONE);
            }
            String tour = tourName;
            String venue = setlist.venue.name + ": " + setlist.venue.city.name + ", " + setlist.venue.city.country.name;
            String header = name + " at: " + venue;
            holder.artist.setText(name);
            holder.setlist.setOnClickListener(view -> {
                holder.onSetlistClickListener.onSetlistClick(setlist);
            });
            holder.tour.setText(tour);
            holder.venue.setText(venue);
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
        setlistFragmentNew = new SetlistFragmentNew(setlist);
        ftrans = getActivity().getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, setlistFragmentNew);
        ftrans.addToBackStack("");
        ftrans.commit();
    }
}
