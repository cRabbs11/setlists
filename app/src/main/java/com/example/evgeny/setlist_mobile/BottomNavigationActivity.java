package com.example.evgeny.setlist_mobile;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.evgeny.setlist_mobile.search.SearchFragment;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomMenu;

    private SearchFragment searchFragment;
    public FragmentTransaction ftrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomMenu = (BottomNavigationView) findViewById(R.id.bottomMenu);
        bottomMenu.setOnNavigationItemSelectedListener(this);
        searchFragment = new SearchFragment();
        //openLaunchFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ftrans = getSupportFragmentManager().beginTransaction();
        switch(item.getItemId()) {
            case R.id.menu_bottom_search:
                ftrans.replace(R.id.fragment_container, searchFragment);
                Log.d("BMTH", " поиск: ");
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
