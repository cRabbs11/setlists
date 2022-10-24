package com.kochkov.evgeny.setlist_mobile.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.kochkov.evgeny.setlist_mobile.data.entity.Venue
import com.kochkov.evgeny.setlist_mobile.databinding.FragmentMapBinding
import com.kochkov.evgeny.setlist_mobile.utils.Constants
import com.kochkov.evgeny.setlist_mobile.viewmodel.MapFragmentViewModel
import com.kochkov.evgeny.setlist_mobile.viewmodel.factory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment() : Fragment() {

    lateinit var googleMap: GoogleMap
    lateinit var binding : FragmentMapBinding
    private val viewModel: MapFragmentViewModel by viewModels{ factory(venue = arguments?.get(
        Constants.KEY_BUNDLE_VENUE) as Venue
    )}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            //notify that map is ready
            observeVenue()
        }
    }

    private fun addMarker(venue: Venue) {
        if (venue.city?.coords!=null) {
            val marker = LatLng(venue.city.coords.coord_lat.toDouble(), venue.city.coords.coord_long.toDouble())
            googleMap.addMarker(
                MarkerOptions()
                    .position(marker)
                    .title(venue.city.name))
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker))

        } else {
            Toast.makeText(requireContext(), Constants.VENUE_DATA_NOT_FOUND, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeVenue() {
        viewModel.venueLiveData.observe(viewLifecycleOwner) { venue ->
            addMarker(venue)
        }
    }
}