package com.example.evgeny.setlist_mobile.search;

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

import com.example.evgeny.setlist_mobile.Artist;
import com.example.evgeny.setlist_mobile.R;
import com.example.evgeny.setlist_mobile.SetlistConnection;
import com.example.evgeny.setlist_mobile.net.SetlistConnectNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 03.07.2018.
 */

public class SearchSetlist extends Fragment implements View.OnClickListener, SetlistConnectNew.AnswerListener {

    private RecyclerView recyclerView;
    private TextView emptySearchText;
    private EditText editSearch;
    private Button btnSearch;
    private SetlistConnection setlistConnection;
    private List<Artist> mArtists;
    private ArtistsAdapter artistsAdapter;
    private InputMethodManager inputMethodManager;
    private String artistName;

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
            searchSetlist(editSearch.getText().toString());
            hideKeyboard(editSearch);
        }
    }

    void searchSetlist(String bandName) {
        Bundle data = new Bundle();
        data.putString("artist", bandName);
        SetlistConnectNew setlistConnectNew = new SetlistConnectNew("getArtists", data, this);
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
    public void getAnswer(String answer) {
        mArtists.clear();
        unParse(answer);
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

    /**
     * распарсивание полученного ответа
     */
    public List<Artist> unParse(String response) {
        JSONObject dataJsonObj = null;

        try {
            dataJsonObj = new JSONObject(response);
            JSONArray artists = dataJsonObj.getJSONArray("artist");

            // 2. перебираем и выводим контакты каждого друга
            for (int i = 0; i < artists.length(); i++) {
                JSONObject artistJson = artists.getJSONObject(i);

                String name = artistJson.getString("name");
                String sortName = artistJson.getString("sortName");
                String url = artistJson.getString("url");
//                JSONObject contacts = artist.getJSONObject("contacts");
//
//
//                String phone = contacts.getString("mobile");
//                String email = contacts.getString("email");
//                String skype = contacts.getString("skype");

                Log.d("BMTH", "name: " + name);
                Log.d("BMTH", "sortName: " + sortName);
                Log.d("BMTH", "url: " + url);

                Artist artist = new Artist();
                artist.name = name;
                artist.sortName = sortName;
                artist.url = url;

                mArtists.add(artist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mArtists;
    }
}
