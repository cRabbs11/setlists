package com.example.evgeny.setlist_mobile;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.evgeny.setlist_mobile.model.BaseModel;
import com.example.evgeny.setlist_mobile.model.Setlist;
import com.example.evgeny.setlist_mobile.search.SearchArtistsFragment;
import com.example.evgeny.setlist_mobile.search.SearchSetlistsFragment;

import java.time.temporal.Temporal;
import java.util.regex.Pattern;

public class BottomNavigationActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        SelectBottomMenuListener {

    public BottomNavigationView bottomMenu;

    private SearchArtistsFragment searchArtistsFragment;
    private SearchSetlistsFragment searchSetlistsFragment;
    public FragmentTransaction ftrans;
    private Setlist setlist;
    private TextView toSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomMenu = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomMenu.setOnNavigationItemSelectedListener(this);
        searchArtistsFragment = new SearchArtistsFragment(this);
        toSource = (TextView) findViewById(R.id.toSource);
        Pattern pattern = Pattern.compile("www.setlist.fm");
        Linkify.addLinks(toSource ,pattern,"http://");
        //searchSetlistsFragment = new SearchSetlistsFragment(this);
        //openLaunchFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ftrans = getSupportFragmentManager().beginTransaction();
        switch(item.getItemId()) {
            case R.id.menu_bottom_artists:
                ftrans.replace(R.id.fragment_container, searchArtistsFragment);
                Log.d("BMTH", " поиск: ");
                break;
            case R.id.menu_bottom_setlists:
                //ftrans.replace(R.id.fragment_container, searchSetlistsFragment);
                break;

        }
        ftrans.commit();
        return true;
    }

    public void openLaunchFragment() {
        ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, searchArtistsFragment);
        ftrans.commit();
    }

    @Override
    public <T extends BaseModel> void setMenuItem(String item, T object) {
        Log.d("BMTH", " нажали: " + item);
        bottomMenu.setSelectedItemId(R.id.menu_bottom_setlists);
        setlist = (Setlist) object;
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
