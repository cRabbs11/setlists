package com.example.evgeny.setlist_mobile

import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.evgeny.setlist_mobile.artistSearch.ArtistSearchFragment
import com.example.evgeny.setlist_mobile.setlists.BaseModel
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Pattern

open class BottomNavigationActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, SelectBottomMenuListener {

    private lateinit var artistSearchFragment: ArtistSearchFragment
    public lateinit var ftrans: FragmentTransaction
    private lateinit var setlist: Setlist
    private lateinit var toSource: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        artistSearchFragment = ArtistSearchFragment()
        toSource = findViewById(R.id.toSource)
        val pattern = Pattern.compile("www.setlist.fm")
        Linkify.addLinks(toSource ,pattern,"http://");

    }

    fun openLaunchFragment() {
        ftrans = getSupportFragmentManager().beginTransaction()
        //ftrans.replace(R.id.fragment_container, searchArtistsFragment)
        ftrans.replace(R.id.fragment_container, artistSearchFragment)
        ftrans.commit()
    }

    override fun onBackPressed() {
        var count = getFragmentManager().getBackStackEntryCount()
        if (count == 0) {
            super.onBackPressed()
        } else {
            getFragmentManager().popBackStack()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        ftrans = getSupportFragmentManager().beginTransaction()
        when(item.getItemId()) {
            R.id.menu_bottom_artists-> {
                ftrans.replace(R.id.fragment_container, artistSearchFragment)
                Log.d("BMTH", " поиск: ")
            }
            R.id.menu_bottom_setlists -> {
                //ftrans.replace(R.id.fragment_container, searchSetlistsFragment);
            }
        }
        ftrans.commit();
        return true
    }

    override fun <T : BaseModel?> setMenuItem(item: String?, selistItem: T) {
        Log.d("BMTH", " нажали: " + item)
        setlist = selistItem as Setlist
    }

}