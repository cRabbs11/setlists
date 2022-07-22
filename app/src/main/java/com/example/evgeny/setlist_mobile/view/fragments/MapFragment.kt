package com.example.evgeny.setlist_mobile.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.evgeny.setlist_mobile.data.entity.Coords
import com.example.evgeny.setlist_mobile.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment() : Fragment() {

    lateinit var googleMap: GoogleMap
    lateinit var binding : FragmentMapBinding

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
        (binding.root as SupportMapFragment).getMapAsync(object: OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                googleMap = p0
                //notify that map is ready
            }
        })
    }

    fun addMarker(coords: Coords) {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(coords.coord_lat.toDouble(), coords.coord_long.toDouble())
        googleMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker of concert"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}