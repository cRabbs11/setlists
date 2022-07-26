package com.example.evgeny.setlist_mobile.view.activities

import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.data.entity.Venue
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_ARTIST
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_SETLIST
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_TRANSITION
import com.example.evgeny.setlist_mobile.utils.Constants.KEY_BUNDLE_VENUE
import com.example.evgeny.setlist_mobile.view.fragments.ArtistSearchFragment
import com.example.evgeny.setlist_mobile.view.fragments.MapFragment
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

        launchFragment(fragment)
    }

    fun openSetlistsSearchFragment(artist: Artist) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_BUNDLE_ARTIST, artist)
        val fragment = SetlistsFragment()
        fragment.arguments = bundle

        launchFragment(fragment)
    }

    fun openMapFragment(venue: Venue) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_BUNDLE_VENUE, venue)
        val fragment = MapFragment()
        fragment.arguments = bundle

        launchFragment(fragment)
    }

    fun openSingleSetlistFragment(sharedView: View, setlist: Setlist) {
        val bundle = Bundle()
        bundle.putString(KEY_BUNDLE_TRANSITION, sharedView.transitionName)
        bundle.putSerializable(KEY_BUNDLE_SETLIST, setlist)

        val fragment = SingleSetlistFragment()
        fragment.arguments = bundle

        launchFragment(fragment = fragment, sharedView = sharedView)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount<=1) {
            super.onBackPressed()
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun launchFragment(fragment: Fragment, tag: String?=null, sharedView: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(tag)
            .addSharedElement(sharedView, sharedView.transitionName)
            .commit()
    }

    private fun launchFragment(fragment: Fragment, tag: String?=null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(tag)
            .commit()
    }
}