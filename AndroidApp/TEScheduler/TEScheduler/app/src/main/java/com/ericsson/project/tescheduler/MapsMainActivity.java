package com.ericsson.project.tescheduler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ericsson.project.tescheduler.AsyncTasks.GetPoIListTask;
import com.ericsson.project.tescheduler.Interfaces.GetPoIListInterface;
import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import citypulse.commons.data.Coordinate;

public class MapsMainActivity extends MenuedActivity implements OnMapReadyCallback, GetPoIListInterface {

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private CheckBox checkOpenOnly, checkFreeOnly, checkArt, checkHistory, checkLeisure, checkSightseeing;
    private GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView textOpeningStatus, textOpeningTime, textPrice, textWebsite, navDrawerBudget, navDrawerExpenses;
    private FloatingActionButton fabButton;

    private int index = -1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);

        //init: Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navopen, R.string.navclosed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }

        //init: AppData, map markers
        if (!AppData.dataInitialized) {
            AppData.init();
            new GetPoIListTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        //init: Navigation Drawer
        navDrawerBudget = (TextView) findViewById(R.id.nav_budgetavailable);
        navDrawerExpenses = (TextView) findViewById(R.id.nav_budgetexpenses);
        checkOpenOnly = (CheckBox) findViewById(R.id.nav_check_openonly);
        checkFreeOnly = (CheckBox) findViewById(R.id.nav_check_freeonly);
        checkArt = (CheckBox) findViewById(R.id.nav_check_typeart);
        checkHistory = (CheckBox) findViewById(R.id.nav_check_typehistory);
        checkLeisure = (CheckBox) findViewById(R.id.nav_check_typeleisure);
        checkSightseeing = (CheckBox) findViewById(R.id.nav_check_typesightseeing);
        navDrawerBudget.setText(String.format(
                getString(R.string.nav_budget_value), String.format(Locale.getDefault(), "%d", (int) Math.floor(AppData.budgetAvailable))));
        navDrawerExpenses.setText(String.format(
                getString(R.string.nav_budget_value), String.format(Locale.getDefault(), "%d", (int) Math.floor(AppData.budgetExpenses))));

        //all type-specific checkboxes use the same function "onCheckedChangedByType"
        checkArt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                onCheckedChangedByType(checked, getString(R.string.poitype_art));
            }
        });
        checkHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                onCheckedChangedByType(checked, getString(R.string.poitype_history));
            }
        });
        checkLeisure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                onCheckedChangedByType(checked, getString(R.string.poitype_leisure));
            }
        });
        checkSightseeing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                onCheckedChangedByType(checked, getString(R.string.poitype_sightseeing));
            }
        });
        // Open and Free status need their own functions
        checkOpenOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                        if (!AppData.getPointOfInterestAt(i).isOpenNow()) {
                            AppData.getPoiMarkerAt(i).setVisible(false);
                        }
                    }
                } else {
                    List<String> checkTypes = new ArrayList<>();
                    fillCheckTypes(checkTypes);
                    if (checkFreeOnly.isChecked()) {
                        for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                            if (checkTypes.contains(AppData.getPointOfInterestAt(i).getType().toLowerCase())
                                    && AppData.getPointOfInterestAt(i).isFree()) {
                                AppData.getPoiMarkerAt(i).setVisible(true);
                            }
                        }
                    } else {
                        for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                            if (checkTypes.contains(AppData.getPointOfInterestAt(i).getType().toLowerCase())) {
                                AppData.getPoiMarkerAt(i).setVisible(true);
                            }
                        }
                    }
                }
            }
        });

        checkFreeOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                        if (!AppData.getPointOfInterestAt(i).isFree()) {
                            AppData.getPoiMarkerAt(i).setVisible(false);
                        }
                    }
                } else {
                    List<String> checkTypes = new ArrayList<>();
                    fillCheckTypes(checkTypes);
                    if (checkOpenOnly.isChecked()) {
                        for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                            if (checkTypes.contains(AppData.getPointOfInterestAt(i).getType().toLowerCase())
                                    && AppData.getPointOfInterestAt(i).isOpenNow()) {
                                AppData.getPoiMarkerAt(i).setVisible(true);
                            }
                        }
                    } else {
                        for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                            if (checkTypes.contains(AppData.getPointOfInterestAt(i).getType().toLowerCase())) {
                                AppData.getPoiMarkerAt(i).setVisible(true);
                            }
                        }
                    }
                }
            }
        });

        fabButton = (FloatingActionButton) findViewById(R.id.fab_mapmain);
        textOpeningTime = (TextView) findViewById(R.id.bottom_sheet_openclosetime);
        textPrice = (TextView) findViewById(R.id.bottom_sheet_price);
        textWebsite = (TextView) findViewById(R.id.bottom_sheet_website);
        textOpeningStatus = (TextView) findViewById(R.id.bottom_sheet_textisopen);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (checkPlayServices()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        //init: bottomSheet, content text
        View bottomSheet = findViewById(R.id.bottom_sheet);
        assert bottomSheet != null;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        AppData.getPoiPolygonAt(index).setVisible(true);
                        fabButton.setVisibility(View.VISIBLE);
                        break;
                    default:
                        fabButton.setVisibility(View.GONE);
                        AppData.getPoiPolygonAt(index).setVisible(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void fillCheckTypes(List<String> checkTypes) {
        if (checkArt.isChecked()) {
            checkTypes.add(getString(R.string.poitype_art));
        }
        if (checkHistory.isChecked()) {
            checkTypes.add(getString(R.string.poitype_history));
        }
        if (checkLeisure.isChecked()) {
            checkTypes.add(getString(R.string.poitype_leisure));
        }
        if (checkSightseeing.isChecked()) {
            checkTypes.add(getString(R.string.poitype_sightseeing));
        }
    }

    public void onCheckedChangedByType(boolean checked, String type) {
        if (checked) {
            if (checkOpenOnly.isChecked() && checkFreeOnly.isChecked()) {
                for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                    if (AppData.getPointOfInterestAt(i).getType().toLowerCase().equals(type)
                            && AppData.getPointOfInterestAt(i).isOpenNow()
                            && AppData.getPointOfInterestAt(i).isFree()) {
                        AppData.getPoiMarkerAt(i).setVisible(true);
                    }
                }
            } else if (checkOpenOnly.isChecked()) {
                for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                    if (AppData.getPointOfInterestAt(i).getType().toLowerCase().equals(type)
                            && AppData.getPointOfInterestAt(i).isOpenNow()) {
                        AppData.getPoiMarkerAt(i).setVisible(true);
                    }
                }
            } else if (checkFreeOnly.isChecked()) {
                for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                    if (AppData.getPointOfInterestAt(i).getType().toLowerCase().equals(type)
                            && AppData.getPointOfInterestAt(i).isFree()) {
                        AppData.getPoiMarkerAt(i).setVisible(true);
                    }
                }
            } else {
                for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                    if (AppData.getPointOfInterestAt(i).getType().toLowerCase().equals(type)) {
                        AppData.getPoiMarkerAt(i).setVisible(true);
                    }
                }
            }
        } else {
            for (int i = 0; i < AppData.getPointsOfInterest().size(); i++) {
                if (AppData.getPointOfInterestAt(i).getType().toLowerCase().equals(type)) {
                    AppData.getPoiMarkerAt(i).setVisible(false);
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap == null) {
            Snackbar.make(toolbar, "Map could not be loaded.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            //mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            //LatLngBounds mapBounds = new LatLngBounds(new LatLng(59.2815, 17.9019), new LatLng(59.3813,18.2445));

            // Center the map on Stockholm's CentralStation
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AppData.STOCKHOLM_CENTRAL_COORDINATES, 12));

            if(AppData.dataInitialized){ fillMap(); }

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if (cameraPosition.zoom < AppData.STOCKHOLM_MAP_MIN_ZOOM)
                        mMap.animateCamera(CameraUpdateFactory
                                .zoomTo(AppData.STOCKHOLM_MAP_MIN_ZOOM));

                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    index = AppData.findPoIIndexByMarker(marker);
                    displayBottomSheet();
                    return false;
                }
            });
        }

        if(AppData.dataInitialized && mMap != null && AppData.getResultRoute() != null){
            List<LatLng> waypoints;
            for(int i = 0; i < AppData.getResultRoute().getSegmentToPoI().size(); i++){
                waypoints = convertCoordinates(AppData.getResultRoute()
                        .getSegmentToPoI().get(i).getTrajectory());
                if(AppData.getResultRoute().getSegmentToPoI().get(i).getMobility().equals("Walk")){
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(waypoints)
                            .color(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentTransparent))
                            .width(10.0f)
                            .clickable(false));
                } else {
                    mMap.addPolyline(new PolylineOptions()
                            .addAll(waypoints)
                            .color(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                            .width(10.0f)
                            .clickable(false));
                }
            }
        }
    }

    private void displayBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (index != -1){
            PointOfInterest poi = AppData.getPointOfInterestAt(index);
            textOpeningTime.setText(poi.getFormattedScheduleToday());
            // The explicit cast to int is needed to avoid displaying useless decimals
            textPrice.setText(getString(R.string.info_price_value
                    , String.valueOf((int) poi.getPriceByCategory("Adult"))));
            textWebsite.setText(poi.getWebsite());
            if (poi.isOpenNow()){
                textOpeningStatus.setText(getText(R.string.info_openstatus_open));
                textOpeningStatus.setTextColor(ContextCompat.getColor(
                        getApplicationContext(), R.color.green));
            }
            else {
                textOpeningStatus.setText(getText(R.string.info_openstatus_closed));
                textOpeningStatus.setTextColor(ContextCompat.getColor(
                        getApplicationContext(), R.color.red));
            }
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Snackbar.make(toolbar, "Google Play Services is required to run this application." +
                        " Please install it or enable it.",
                        Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navDrawerBudget.setText(String.format(
                getString(R.string.nav_budget_value), String.format(Locale.getDefault(), "%d",(int) Math.floor(AppData.budgetAvailable))));
        navDrawerExpenses.setText(String.format(
                getString(R.string.nav_budget_value), String.format(Locale.getDefault(), "%d",(int) Math.floor(AppData.budgetExpenses))));
    }

    public List<LatLng> convertCoordinates(List<Coordinate> list){
        List<LatLng> googleList = new ArrayList<>();
        for(Coordinate coordinate : list){
            googleList.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()));
        }
        return googleList;
    }

    /*@Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.clear();
    }

    public void navigateToSettings(View view) {
        startActivity(new Intent(this, ParametersActivity.class));
    }

    public void navigateToPoIProfile(View view){
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("PoIIndex", index);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapsmain, menu);
        final MenuItem searchMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Enter place name");
        //SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        //searchView.setSearchableInfo();
        //searchView.setAppSearchData();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PointOfInterest searchResult = AppData.findPoIbyName(query);
                if( searchResult == null ){
                    Snackbar.make(toolbar, "No results found.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    index = searchResult.getId();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(searchResult.getLocationLat(),
                                    searchResult.getLocationLong()), 15));
                    displayBottomSheet();
                }

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        return id == R.id.action_mainmap || super.onOptionsItemSelected(item);
    }

    // Deals with the response by the server
    public void processGetPoIListResponse(String response) {
        Log.v(AppData.LOG_TAG, "response size:" + response.getBytes().length + "bytes");
        try {
            Gson gson = new Gson();
            Map<Integer, PointOfInterest> temp = gson.fromJson(response,
                    new TypeToken<Map<Integer, PointOfInterest>>(){}.getType());
            AppData.setPointsOfInterest(temp);
            AppData.setAddresses();
            fillMap();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } finally {
            AppData.dataInitialized = true;
        }
    }

    private void fillMap(){
        for(int i = 0; i < AppData.getPointsOfInterest().size(); i++){
            LatLng markerLocation = new LatLng(AppData.getPointOfInterestAt(i).getLocationLat()
                    , AppData.getPointOfInterestAt(i).getLocationLong());
            List<LatLng> areaLocation = new ArrayList<>();
            for(int j = 0; j < AppData.getPointOfInterestAt(i).getAreaLat().size(); j++){
                areaLocation.add(new LatLng(AppData.getPointOfInterestAt(i).getAreaLat().get(j)
                        , AppData.getPointOfInterestAt(i).getAreaLong().get(j)));
            }
            AppData.addPoIMarker(
                    mMap.addMarker(new MarkerOptions()
                                    .position(markerLocation)
                            // .title(AppData.getPointOfInterestAt(i).getName())
                    ),
                    mMap.addPolygon(new PolygonOptions()
                            .addAll(areaLocation)
                            .strokeWidth(5.0f)
                            .strokeColor(ContextCompat
                                    .getColor(getApplicationContext(), R.color.mapsPolygonStroke))
                            .fillColor(ContextCompat
                                    .getColor(getApplicationContext(), R.color.mapsPolygonFill))
                            .visible(false)
                    )
            );
        }
    }
}