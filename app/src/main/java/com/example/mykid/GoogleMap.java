package com.example.mykid;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMap extends FragmentActivity implements OnMapReadyCallback {
    com.google.android.gms.maps.GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Context context;

    public GoogleMap() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate (LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
//        searchView = view.findViewById(R.id.sv_location);
//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
//        return view;
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                String location = searchView.getQuery().toString();
//                List<Address> addressList = null;
//
//                if (location != null || !location.equals("")){
//                    Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
//                    try{
//                        addressList = geocoder.getFromLocationName(location, 1);
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                    Address address = addressList.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
//                    map.addMarker(new MarkerOptions().position(latLng).title(location));
//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//
//        mapFragment.getMapAsync(this);
//    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        map = googleMap;
    }
}