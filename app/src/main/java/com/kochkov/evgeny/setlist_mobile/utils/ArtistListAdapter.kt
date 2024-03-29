package com.kochkov.evgeny.setlist_mobile.utils


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.data.Artist

class ArtistListAdapter(clickListener: OnItemClickListener<Artist>) : RecyclerView.Adapter<ArtistHolder>() {

    private val TAG = ArtistListAdapter::class.simpleName + " BMTH"
    private var clickListener: OnItemClickListener<Artist>
    val artists = arrayListOf<Artist>()

    init {
        Log.d(TAG, "init")
        this.clickListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder {
        Log.d(TAG, "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.artist_item_constraint, parent, false)
        return ArtistHolder(view)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun setItems(list: List<Artist>) {
        list.forEach {
            artists.add(it)
        }
    }

    fun clearItems() {
        artists.clear()
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        val artist = artists[position]

        holder.mainText.text = artist.name

        holder.mainText.setOnClickListener {
            clickListener.onItemClick(artist)
        }
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemId(position: Int): Long {
        return artists.get(position).hashCode().toLong()
    }
}

class ArtistItemDecoration(private val context: Context): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val hSide = context.resources.getDimension(R.dimen.h_side).toInt()
        val vSide = context.resources.getDimension(R.dimen.v_side).toInt()
        outRect.set(hSide, vSide, hSide, vSide)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val paint = Paint()
        paint.color = context.getColor(R.color.colorBlue)
        paint.strokeWidth= 40F
        c.drawLine(0F, 0F, 1000F, 1000F, paint)
        paint.color = context.getColor(R.color.colorRed)
        paint.strokeWidth= 40F
        c.drawLine(0F, 60F, 1000F, 1060F, paint)


    }
}

class ArtistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mainText: TextView
    //var secondaryText: TextView
    //var left_view: ImageView
    //var right_view: ImageView
    //var rightLayout: LinearLayout
    //var category_item: ConstraintLayout

    init {
        mainText = itemView.findViewById(R.id.artistName)
        //secondaryText = itemView.findViewById(R.id.secondaryText)
        //left_view = itemView.findViewById(R.id.leftImage)
        //right_view = itemView.findViewById(R.id.rightImage)
        //category_item = itemView.findViewById(R.id.category_item)
        //rightLayout = itemView.findViewById(R.id.right_layout)
    }
}