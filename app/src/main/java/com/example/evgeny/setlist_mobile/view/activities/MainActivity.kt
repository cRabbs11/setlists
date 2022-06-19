package com.example.evgeny.setlist_mobile.view.activities

import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_ARTIST
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_SETLIST
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_TRANSITION
import com.example.evgeny.setlist_mobile.view.fragments.ArtistSearchFragment
import com.example.evgeny.setlist_mobile.view.fragments.SetlistsFragment
import com.example.evgeny.setlist_mobile.view.fragments.SingleSetlistFragment
import java.util.regex.Pattern

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toSource = findViewById<TextView>(R.id.toSource)
        val pattern = Pattern.compile(this.resources.getString(R.string.setlist_link))
        Linkify.addLinks(toSource ,pattern, this.resources.getString(R.string.setlist_link_prefix))

        openArtistSearchFragment()
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    fun openArtistSearchFragment() {

        val fragment = ArtistSearchFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun openSetlistsSearchFragment(artist: Artist) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_BUNDLE_ARTIST, artist)
        val fragment = SetlistsFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun openSingleSetlistFragment(sharedView: View, setlist: Setlist) {
        val bundle = Bundle()
        bundle.putString(KEY_BUNDLE_TRANSITION, sharedView.transitionName)
        bundle.putSerializable(KEY_BUNDLE_SETLIST, setlist)

        val fragment = SingleSetlistFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .addSharedElement(sharedView, sharedView.transitionName)
                .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount<=1) {
            super.onBackPressed()
            finish()
        } else {
            super.onBackPressed()
        }
    }
}