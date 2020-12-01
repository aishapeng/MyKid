package com.example.mykid;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
    EditText editText;
    Button selectBtn;
    Marker marker;
    LatLng mLatLng;
    Address mLocation;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        editText = view.findViewById(R.id.edit_txt);
        selectBtn=view.findViewById(R.id.selectBtn);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        if(savedInstanceState!=null){
            mLatLng= new LatLng(savedInstanceState.getDouble("lat"),savedInstanceState.getDouble("lng"));
            if(mLatLng!=null){
                marker = map.addMarker(new MarkerOptions().position(mLatLng).title(mLocation.getAddressLine(0)));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,10));
            }

        }
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
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("location", editText.getText().toString());
                getParentFragmentManager().setFragmentResult("location", result);
                getParentFragmentManager().popBackStack();
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
            mLocation = addresses.get(0);
            mLatLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());

            if(marker!=null){
                marker.remove();
            }

            marker = map.addMarker(new MarkerOptions().position(mLatLng).title(mLocation.getAddressLine(0)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,10));
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){ //0
            //when error
            //Initiaslize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Bundle bundle = getArguments();
        String mlocation = bundle.getString("currentLocation"); //for add and edit fragment
        String title = "Current Location";

        if (mlocation == null){ // if current location is not available
            mlocation = bundle.getString("selectedLocation"); //for details fragment, edit
            title = mlocation;
            editText.setText(mlocation);
            String editable = bundle.getString("editable");
            if(editable == "false"){ // for details fragment
                setVisibility();
            }
        }

        try{
            addresses = geocoder.getFromLocationName(mlocation,1);
            Address location = addresses.get(0);
            LatLng latLng =  new LatLng(location.getLatitude(),location.getLongitude());
            marker = map.addMarker(new MarkerOptions().position(latLng).title(title));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void setVisibility() {
        editText.setVisibility(View.GONE);
        selectBtn.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mLatLng!=null){
            outState.putDouble("lat",mLatLng.latitude);
            outState.putDouble("lng",mLatLng.longitude);
            //outState.putString("latLng",mLatLng.toString());
        }
    }
}