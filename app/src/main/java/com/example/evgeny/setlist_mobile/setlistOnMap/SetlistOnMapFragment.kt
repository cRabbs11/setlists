package com.example.evgeny.setlist_mobile.setlistOnMap

import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*

import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.example.evgeny.setlist_mobile.R
import com.example.evgeny.setlist_mobile.setlists.Coords

import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.utils.OnItemClickListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class SetlistOnMapFragment : Fragment(), OnItemClickListener<Setlist>, SetlistOnMapContract.View, OnMapReadyCallback {
    override fun showSetlist(setlist: Setlist) {

    }

    override fun updateSetlist(setlist: Setlist) {

    }


    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun addMarker(coords: Coords) {
        Log.d(TAG, "addMarker")
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(coords.coord_lat.toDouble(), coords.coord_long.toDouble())
        googleMap.addMarker(MarkerOptions()
                .position(sydney)
                .title("Marker of concert"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private val TAG = SetlistOnMapFragment::class.java.name + " BMTH "

    lateinit var presenter: SetlistOnMapPresenter
    lateinit var googleMap: GoogleMap
    //lateinit var emptyRecyclerMessageLayout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        initView(rootView)
        return rootView
    }

    fun initView(rootView: View ) {

        Log.d(TAG, " запустили")
        val mapFragment = childFragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val setlistsRepository = SetlistsRepository

        presenter = SetlistOnMapPresenter(setlistsRepository)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun onItemClick(t: Setlist) {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        presenter.mapIsReady()

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //googleMap.addMarker(MarkerOptions()
        //        .position(sydney)
        //        .title("Marker in Sydney"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }
}