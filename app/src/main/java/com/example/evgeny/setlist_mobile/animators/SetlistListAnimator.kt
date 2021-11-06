package com.example.evgeny.setlist_mobile.animators

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.R

class SetlistListAnimator(private val context: Context): DefaultItemAnimator() {

    private val animAdd = AnimationUtils.loadAnimation(context, R.anim.slide_from_top_left)

    override fun onAddStarting(item: RecyclerView.ViewHolder?) {
        item?.itemView?.startAnimation(animAdd)
    }

    override fun getAddDuration(): Long {
        return animAdd.duration
    }
}