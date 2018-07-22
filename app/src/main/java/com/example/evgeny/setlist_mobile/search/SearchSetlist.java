package com.example.evgeny.setlist_mobile.search;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SelectBottomMenuListener;
import com.example.evgeny.setlist_mobile.model.Artist;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.net.SetlistConnectNew;
import com.example.evgeny.setlist_mobile.utils.Parser;
import com.example.evgeny.setlist_mobile.utils.Threader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

@SuppressLint("ValidFragment")
public class SearchSetlist extends Fragment implements View.OnClickListener{

    private String TAG = "SearchFragment: " + SearchSetlist.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private EditText editSearch;
    private Button btnSearch;
    private List<Setlist> mSetlists;
    private SetlistsAdapter setlistsAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;
    private SelectBottomMenuListener selectBottomMenuListener;
    private Artist artist;
    Threader threader;
    private Parser parser;

    @SuppressLint("ValidFragment")
    public SearchSetlist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptySearchText = (TextView) rootView.findViewById(R.id.emptySearchText);
        editSearch = (EditText) rootView.findViewById(R.id.edit_search);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        editSearch.setHint("setlists...");
        btnSearch.setOnClickListener(this);


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


    @Override
    public void onClick(View view) {
        if (!editSearch.getText().equals("")) {
            searchSetlist(editSearch.getText().toString());
            hideKeyboard(editSearch);
        }
    }

    void searchSetlist(String bandName) {

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

    /**
     * скрывает экранную клавиатуру
     * @param view активный view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    class SetlistsAdapter extends RecyclerView.Adapter<SetlistsAdapter.SetlistHolder> {

        class SetlistHolder extends RecyclerView.ViewHolder{

            TextView name;

            public SetlistHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.artistName);
            }
        }

        @Override
        public SetlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.artist_layout_item, parent, false);
            return new SetlistHolder(view);
        }

        @Override
        public void onBindViewHolder(SetlistHolder holder, int position) {
            Setlist setlist = mSetlists.get(position);
            holder.name.setText(setlist.eventDate);
        }

        @Override
        public int getItemCount() {
            return mSetlists.size();
        }
    }
}
