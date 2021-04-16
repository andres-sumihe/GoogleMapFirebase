package com.andflow.googlemapfirebase;
// Nama : Andres Freixa Sumihe
// NIM  : 672018136
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener,  OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText lat, lang;
    SupportMapFragment mapFragment;
    boolean mapWasClicked = false;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lang = (EditText) findViewById(R.id.lang);
        lat = (EditText) findViewById(R.id.lat);

        Button saveBtn = (Button) findViewById(R.id.save);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference = database.getReference();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Randomise color of Marker
                Random rand = new Random();
                float color = (float) rand.nextInt(360);
                MarkerOptions markerOptions = new MarkerOptions();
                String latString = lat.getText().toString();
                String langString = lang.getText().toString();
                Double la = Double.parseDouble(latString);
                Double lo = Double.parseDouble(langString);
                LatLng latLng = new LatLng(la, lo);
                markerOptions.position(latLng);
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String countryName = "";
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            Double.parseDouble(latString),
                            Double.parseDouble(langString),1);
                    Address address = addresses.get(0);
                    countryName = address.getAdminArea();
                    markerOptions.title(countryName);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.addMarker(markerOptions);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LocationModel locationModel = new LocationModel(latLng.latitude, latLng.longitude);
                getReference.child("locations").child("location").setValue(locationModel)
                        .addOnSuccessListener(MapsActivity.this, new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MapsActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
    public void onMapClick(LatLng latLng) {
        // Randomise color of Marker
        Random rand = new Random();
        float color = (float) rand.nextInt(360);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference = database.getReference();
        if(!mapWasClicked){
            mapWasClicked = true;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String countryName = "";
            try {
                List<Address> addresses= geocoder.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,1);
                Address address = addresses.get(0);
                countryName = address.getAdminArea();
                markerOptions.title(countryName);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }catch (Exception e){
                e.printStackTrace();
            }
            LocationModel locationModel = new LocationModel(latLng.latitude, latLng.longitude);
            getReference.child("locations").child("location").setValue(locationModel)
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(MapsActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
            mMap.getUiSettings().setScrollGesturesEnabled(false);//  For disabled scroll, but zoom still work
            lat.setText(String.valueOf(latLng.latitude));
            lang.setText(String.valueOf(latLng.longitude));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        LatLng koskosan = new LatLng(-7.315812334354483, 110.49724884331226);
        mMap.addMarker(new MarkerOptions().position(koskosan)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title("Alamat Kos Saya Pak Hehe"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koskosan,13.0f));
    }



}