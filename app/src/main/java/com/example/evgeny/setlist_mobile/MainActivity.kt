package com.example.evgeny.setlist_mobile

import android.os.Bundle

class MainActivity: BottomNavigationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openLaunchFragment()
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }
}