package com.example.mykid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    com.google.android.gms.maps.GoogleMap map;
    SupportMapFragment mapFragment;
    //SearchView searchView;
    EditText editText;
    private static final String TAG = "GoogleMap Fragment - ";

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        //searchView = view.findViewById(R.id.sv_location);
        editText = view.findViewById(R.id.edit_txt);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        //return view;

        //Initialize places
        Places.initialize(getContext(), "AIzaSyBvCp2gr6SrB9TAfZhrAVlLMbpb81fKrNg");

        //set edit text non focusable
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getActivity());

                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){//-1
            //when success
            //Initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on edit text
            String address = place.getAddress();
            editText.setText(address);
            List<Address> addresses = null;
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try{
                addresses = geocoder.getFromLocationName(address,1);
            }catch(IOException e){
                        e.printStackTrace();
            }
            Address location = addresses.get(0);
            Log.d(TAG, "Entered Address location: "+ location);
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            Log.d(TAG, "Entered Address Latlng: "+ latLng);
                    map.addMarker(new MarkerOptions().position(latLng).title(address));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){ //0
            //when error
            //Initiaslize status
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.d(TAG, "Search fail: ");
            //Display toast
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "sinisinisini " + resultCode);
        Log.d(TAG, "Intent Data " + data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Bundle bundle = getArguments();
        String currentLocation = bundle.getString("currentLocation");
        Log.d(TAG, "current location passed here!: "+ currentLocation);

        try{
            addresses = geocoder.getFromLocationName(currentLocation,1);
            Address location = addresses.get(0);
            LatLng latLng =  new LatLng(location.getLatitude(),location.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title("You are here!"));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}