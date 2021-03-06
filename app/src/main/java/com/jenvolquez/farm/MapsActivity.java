package com.jenvolquez.farm;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jenvolquez.farm.fragments.AboutFragment;
import com.jenvolquez.farm.fragments.AllMedicineFragment;
import com.jenvolquez.farm.fragments.CartFragment;
import com.jenvolquez.farm.fragments.ContactFragment;
import com.jenvolquez.farm.fragments.PharmacyListFragment;
import com.jenvolquez.farm.fragments.MedicineListFragment;
import com.jenvolquez.farm.fragments.SufferingFragment;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Map<Marker, Pharmacy> markerPharmacyDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_maps);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mapFragment)
                .commit();
        loadMapAsync(mapFragment);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        markerPharmacyDictionary = new HashMap<Marker, Pharmacy>();
    }

//    public boolean serviceOK(){
//        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        if (isAvailable == ConnectionResult.SUCCESS){
//            return true;
//        }
//        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, 2))
//                    dialog.show();
//        }
//        else{
//            Toast.makeText(this, "No se puede conectar a los servicios de google", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }


    public void loadMapAsync(SupportMapFragment mapFragment) {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.med_cart){
            Fragment fragment = new CartFragment();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        return true;
     }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment myFragment = null;

        if (id == R.id.nav_home) {
            myFragment = new SupportMapFragment();
            loadMapAsync((SupportMapFragment) myFragment);
        }  if (id == R.id.nav_farm) {
            myFragment= new PharmacyListFragment();

        } else if (id == R.id.nav_pills) {
            myFragment= new AllMedicineFragment();


        } else if (id == R.id.nav_padecimientos) {
            myFragment= new SufferingFragment();

        } else if (id == R.id.nav_cont) {
            myFragment= new ContactFragment();

        } else if (id == R.id.nav_about) {
            myFragment= new AboutFragment();

        }else if (id == R.id.med_cart){
            myFragment= new CartFragment();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myFragment)
                .commit();

        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        ParseQuery<Pharmacy> query = ParseQuery.getQuery(Pharmacy.class);
        final MapsActivity self = this;


        query.findInBackground(new FindCallback<Pharmacy>() {

            @Override
            public void done(List<Pharmacy> pharmacies, ParseException e) {
                for (Pharmacy p : pharmacies) {
                    Marker m = googleMap.addMarker(new MarkerOptions()
                            .position(p.getLocation())
                            .title(p.getName())
                            .snippet(p.getAddress()));
                    markerPharmacyDictionary.put(m, p);
                }
            }
        });

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Poscición actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

        mMap.setOnInfoWindowClickListener(this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Poscición actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        MedicineListFragment fragment = new MedicineListFragment();
        Pharmacy pharmacy = markerPharmacyDictionary.get(marker);

        Bundle bundle = new Bundle();
        bundle.putString("pharmacyId", pharmacy.getObjectId());
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("MedicineListFragment")
                .replace(R.id.container, fragment)
                .commit();
    }
}
