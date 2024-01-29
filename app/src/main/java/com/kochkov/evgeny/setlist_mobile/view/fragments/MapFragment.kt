package com.kochkov.evgeny.setlist_mobile.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

import com.kochkov.evgeny.setlist_mobile.data.entity.Venue
import com.kochkov.evgeny.setlist_mobile.databinding.FragmentMapBinding
import com.kochkov.evgeny.setlist_mobile.utils.Constants
import com.kochkov.evgeny.setlist_mobile.viewmodel.MapFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.factory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kochkov.evgeny.setlist_mobile.R
import com.kochkov.evgeny.setlist_mobile.data.entity.Setlist
import com.kochkov.evgeny.setlist_mobile.view.activities.MainActivity

class MapFragment() : Fragment() {

    lateinit var googleMap: GoogleMap
    lateinit var binding : FragmentMapBinding
    private val viewModel: MapFragmentViewModel by viewModels{ factory(setlist = arguments?.get(
        Constants.KEY_BUNDLE_SETLIST) as Setlist
    )}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            //notify that map is ready
            observeVenue()
            observeTour()
        }

        (activity as MainActivity).supportActionBar?.let {
            it.title = (arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist).venue?.name?: getString(R.string.title_venue)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).closeFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMarker(venue: Venue) {
        if (venue.city?.coords!=null) {
            val marker = LatLng(venue.city.coords.coord_lat.toDouble(), venue.city.coords.coord_long.toDouble())
            googleMap.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .position(marker)
                    .title(venue.city.name))
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker))

        } else {
            Toast.makeText(requireContext(), Constants.VENUE_DATA_NOT_FOUND, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTour(tour: List<Setlist>) {
        for (i in tour.indices) {
            val icon = if (tour[i]==arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist) {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            } else {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            }
            tour[i].venue?.city?.coords?.let { coords ->
                val marker = LatLng(coords.coord_lat.toDouble(), coords.coord_long.toDouble())
                googleMap.addMarker(
                    MarkerOptions()
                        .icon(icon)
                        .position(marker)
                        .title(tour[i].venue!!.name)
                )
                if (tour[i]==arguments?.get(Constants.KEY_BUNDLE_SETLIST) as Setlist)  {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker))
                }

                if (i<tour.size-1) {
                    tour[i].venue?.city?.coords?.let { endCoords ->
                    }
                }
            }
            if (i < tour.size-1) {
                googleMap.addPolyline(
                    PolylineOptions()
                        .add(
                            LatLng(tour[i].venue?.city?.coords?.coord_lat!!.toDouble(), tour[i].venue?.city?.coords?.coord_long!!.toDouble()),
                            LatLng(tour[i+1].venue?.city?.coords?.coord_lat!!.toDouble(), tour[i+1].venue?.city?.coords?.coord_long!!.toDouble()))
                )
            }
        }
    }

    private fun observeVenue() {
        viewModel.venueLiveData.observe(viewLifecycleOwner) { venue ->
            addMarker(venue)
        }
    }

    private fun observeTour() {
        viewModel.tourLiveData.observe(viewLifecycleOwner) { tour ->
            if (tour!=null) {
                addTour(tour)
            }
        }
    }

    fun bitmapDescriptorFromVector(fragmentContext: Context, vectorResId: Int): BitmapDescriptor? =
    // Some VectorDrawables do not display when using ContextCompat.
    // Either ResourcesCompat or VectorDrawableCompat seem to work.
        // VectorDrawableCompat was chosen because it is backed by ResourcesCompat under the hood
        VectorDrawableCompat.create(fragmentContext.resources, vectorResId, fragmentContext.theme)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
}