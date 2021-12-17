package com.example.evgeny.setlist_mobile

import android.os.Bundle
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.evgeny.setlist_mobile.artistSearch.ArtistSearchFragment
import java.util.regex.Pattern

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toSource = findViewById<TextView>(R.id.toSource)
        val pattern = Pattern.compile("www.setlist.fm")
        Linkify.addLinks(toSource ,pattern,"http://")

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
}