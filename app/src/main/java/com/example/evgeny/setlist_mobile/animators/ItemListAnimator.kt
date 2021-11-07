package com.example.evgeny.setlist_mobile.animators

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.evgeny.setlist_mobile.R

class ItemListAnimator(private val context: Context): DefaultItemAnimator() {

    private val animAdd = AnimationUtils.loadAnimation(context, R.anim.slide_from_top_left)
    private val animRemove = AnimationUtils.loadAnimation(context, R.anim.slide_to_right)

    override fun onAddStarting(item: RecyclerView.ViewHolder?) {
        item?.itemView?.startAnimation(animAdd)
    }

    override fun getAddDuration(): Long {
        return animAdd.duration
    }

    override fun onRemoveStarting(item: RecyclerView.ViewHolder?) {
        item?.itemView?.startAnimation(animRemove)
    }

    override fun getRemoveDuration(): Long {
        return animRemove.duration
    }
}