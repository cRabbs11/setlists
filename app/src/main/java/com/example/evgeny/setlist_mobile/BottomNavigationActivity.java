package com.example.evgeny.setlist_mobile;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.evgeny.setlist_mobile.search.SearchFragment;
import com.example.evgeny.setlist_mobile.search.SearchSetlist;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomMenu;

    private SearchFragment searchFragment;
    private SearchSetlist searchSetlist;
    public FragmentTransaction ftrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomMenu = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomMenu.setOnNavigationItemSelectedListener(this);
        searchFragment = new SearchFragment();
        searchSetlist = new SearchSetlist();
        //openLaunchFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ftrans = getSupportFragmentManager().beginTransaction();
        switch(item.getItemId()) {
            case R.id.menu_bottom_artists:
                ftrans.replace(R.id.fragment_container, searchFragment);
                Log.d("BMTH", " поиск: ");
                break;
            case R.id.menu_bottom_setlists:
                ftrans.replace(R.id.fragment_container, searchSetlist);
                break;

        }
        ftrans.commit();
        return true;
    }

    public void openLaunchFragment() {
        ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, searchFragment);
        ftrans.commit();
    }
}
