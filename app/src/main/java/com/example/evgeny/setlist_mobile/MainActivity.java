package com.example.evgeny.setlist_mobile;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.example.evgeny.setlist_mobile.search.SearchFragment;

/**
 * Created by Evgeny on 03.07.2018.
 */

public class MainActivity extends BottomNavigationActivity {

    SetlistConnection setlistConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openLaunchFragment();
        setlistConnection = new SetlistConnection();
        setlistConnection.execute();

    }

}
