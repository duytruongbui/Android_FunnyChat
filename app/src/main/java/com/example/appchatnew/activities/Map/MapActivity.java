package com.example.appchatnew.activities.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.appchatnew.R;
import com.example.appchatnew.activities.Chat.MainMain;

import com.example.appchatnew.activities.Coin.CoinActivity;
import com.example.appchatnew.activities.MainActivity;
import com.example.appchatnew.activities.Profile.ProFileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    public static SearchView searchView;
    MarkerOptions markerOptions;
    Marker marker;
    static String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        markerOptions = new MarkerOptions();
        this.searchView = (SearchView) findViewById(R.id.sv_location);
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        this.client = LocationServices.getFusedLocationProviderClient((Activity) this);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 44);
        }

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    try {
                        map.clear();
                        addressList = new Geocoder(getApplicationContext()).getFromLocationName(location, 1);
                        if (addressList.size() > 0) {
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            map.addMarker(markerOptions.position(latLng).title(getAddress(getApplicationContext(), address.getLatitude(), address.getLongitude())));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
                            searchView.setQuery(getAddress(getApplicationContext(), address.getLatitude(), address.getLongitude()), false);
                            s = searchView.getQuery().toString();
                        }
                        for (int i = 0; i < addressList.size(); i++) {
                            Toast.makeText(getApplicationContext(), "" + addressList.get(i).getAddressLine(i), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        this.mapFragment.getMapAsync(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),
                                MainMain.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.money:
                        startActivity(new Intent(getApplicationContext(),
                                CoinActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(),
                                ProFileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public String getAddress(Context ctx, double lat, double lng) {
        String fullAdd = null;
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);

                // if you want only city or pin code use following code //
           /* String Location = address.getLocality();
            String zip = address.getPostalCode();
            String Country = address.getCountryName(); */
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullAdd;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            public void onSuccess(final Location location) {
                if (location != null) {
                    map.clear();
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = markerOptions.position(latLng).title(getAddress(getApplicationContext(), location.getLatitude(), location.getLongitude()));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
                            googleMap.addMarker(options);
                            searchView.setQuery(getAddress(getApplicationContext(), location.getLatitude(), location.getLongitude()), false);
                            s = searchView.getQuery().toString();
                        }
                    });
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44 && grantResults.length > 0 && grantResults[0] == 0) {
            getCurrentLocation();
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}