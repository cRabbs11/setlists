package com.example.evgeny.setlist_mobile.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evgeny.setlist_mobile.data.Artist
import com.example.evgeny.setlist_mobile.data.entity.Setlist
import com.example.evgeny.setlist_mobile.utils.Constants

class ViewModelFactory(private val artist: Artist?, private val setlist: Setlist?): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SetlistsFragmentViewModel::class.java -> {
                if (artist!=null) {
                    SetlistsFragmentViewModel(artist)
                } else {
                    throw IllegalStateException(Constants.EXCEPTION_MESSAGE_ARGUMENT_IS_NULL)
                }
            }
            SingleSetlistFragmentViewModel::class.java -> {
                if (setlist!=null) {
                    SingleSetlistFragmentViewModel(setlist)
                } else {
                    throw IllegalStateException(Constants.EXCEPTION_MESSAGE_ARGUMENT_IS_NULL)
                }
            }
            else -> {
                throw IllegalStateException(Constants.EXCEPTION_MESSAGE_UNKNOWN_VIEW_MODEL)
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory(artist: Artist? = null, setlist: Setlist? = null) = ViewModelFactory(artist = artist, setlist = setlist)