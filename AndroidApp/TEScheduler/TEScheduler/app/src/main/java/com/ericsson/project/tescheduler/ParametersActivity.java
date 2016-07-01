package com.ericsson.project.tescheduler;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ericsson.project.tescheduler.Interfaces.GetUpdatedDate;
import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Tools.DatePickerFragment;
import com.ericsson.project.tescheduler.Tools.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ParametersActivity extends MenuedActivity implements GetUpdatedDate,
        AdapterView.OnItemSelectedListener, AppCompatImageButton.OnClickListener {

    private SimpleDateFormat dateFormat;
    private static Calendar dayStart;
    private static Calendar dayEnd;

    private EditText textBudget;
    private EditText textTravelersAdult;
    private EditText textTravelersKid;
    private EditText textTripStart;
    private EditText textTripEnd;
    private AppCompatImageButton buttonTripStart;
    private AppCompatImageButton buttonTripEnd;
    private RadioButton radioButtonAddress;
    private RadioButton radioButtonResidences;
    private AutoCompleteTextView textStartLocation;
    private ImageButton buttonMyLocation;
    private Spinner spinnerResidences;
    private Spinner spinnerMobility;

    //Maintain the value according to the order of the string array "parameters_mobility_options"
    private final static int PUBLIC_TRANSPORT_SPINNER_INDEX = 0;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSIONS_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        dateFormat = new SimpleDateFormat(AppData.DATE_FORMAT, Locale.getDefault());

        //init: Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //init: UI elements
        textBudget = (EditText) findViewById(R.id.parameters_budget);
        textTravelersAdult = (EditText) findViewById(R.id.parameters_numtravelers_adults);
        textTravelersKid = (EditText) findViewById(R.id.parameters_numtravelers_kids);

        //init: Trip Duration
        textTripStart = (EditText) findViewById(R.id.parameters_tripstart);
        textTripEnd = (EditText) findViewById(R.id.parameters_tripend);
        buttonTripStart = (AppCompatImageButton) findViewById(R.id.parameters_tripstart_button);
        buttonTripEnd = (AppCompatImageButton) findViewById(R.id.parameters_tripend_button);
        dayStart = Calendar.getInstance();
        dayEnd = Calendar.getInstance();
        updateDate(textTripStart.getId(), dayStart);
        updateDate(textTripEnd.getId(), dayEnd);
        buttonTripStart.setOnClickListener(this);
        buttonTripEnd.setOnClickListener(this);

        //init: Start Location
        textStartLocation = (AutoCompleteTextView) findViewById(R.id.parameters_address);
        ArrayList<String> addresses = AppData.getAddresses();
        if (addresses != null){
            ArrayAdapter<String> adapterString =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addresses);
            textStartLocation.setAdapter(adapterString);
        }
        buttonMyLocation = (ImageButton) findViewById((R.id.parameters_buttonmylocation));
        spinnerResidences = (Spinner) findViewById(R.id.parameters_residences);
        radioButtonAddress = (RadioButton) findViewById(R.id.parameters_radio_address);
        radioButtonResidences = (RadioButton) findViewById(R.id.parameters_radio_select);
        ArrayAdapter<CharSequence> adapterResidences = ArrayAdapter.createFromResource(this,
                R.array.parameters_residences, android.R.layout.simple_spinner_item);
        adapterResidences.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResidences.setAdapter(adapterResidences);
        spinnerResidences.setOnItemSelectedListener(this);

        radioButtonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonResidences.setChecked(false);
                spinnerResidences.setEnabled(false);
                textStartLocation.setEnabled(true);
                buttonMyLocation.setEnabled(true);
            }
        });
        radioButtonResidences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonAddress.setChecked(false);
                textStartLocation.setEnabled(false);
                buttonMyLocation.setEnabled(false);
                spinnerResidences.setEnabled(true);
            }
        });

        //init: Mobility
        spinnerMobility = (Spinner) findViewById(R.id.parameters_mobility);
        ArrayAdapter<CharSequence> adapterMobility = ArrayAdapter.createFromResource(this,
                R.array.parameters_mobility_options, android.R.layout.simple_spinner_item);
        adapterMobility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMobility.setAdapter(adapterMobility);
        spinnerMobility.setOnItemSelectedListener(this);
        spinnerMobility.setSelection(PUBLIC_TRANSPORT_SPINNER_INDEX);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void useCurrentPosition(View view){
        Log.w(AppData.LOG_TAG, "useCurrentPosition");
        if (checkPlayServices()){
            Log.w(AppData.LOG_TAG, "useCurrentPosition: true checkPlayServices");
            if (Build.VERSION.SDK_INT >= 23){
                checkForPermission();
            } else {
                startService(new Intent(this, LocationService.class));
                Log.w(AppData.LOG_TAG, "useCurrentPosition service started");
            }
            Log.w(AppData.LOG_TAG, "useCurrentPosition: checking current position" + AppData.currentPosition);
            if (AppData.currentPosition != new LatLng(0.0, 0.0)){
                Log.w(AppData.LOG_TAG, "useCurrentPosition service started");
                //textStartLocation.setText(getString(R.string.parameters_mylocation));
                textStartLocation.setText("(" + AppData.currentPosition + ")");
                Selection.setSelection(textStartLocation.getText(), textStartLocation.length());
            } else {
                Log.w(AppData.LOG_TAG, "useCurrentPosition toast, location is 0.0,0.0");
                CharSequence text = getString(R.string.java_locationfailed);
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            CharSequence text = getString(R.string.java_google_location_warning);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void checkForPermission(){
        Log.w(AppData.LOG_TAG, "checkForPermission");
        if (ContextCompat.checkSelfPermission(ParametersActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ParametersActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            Log.w(AppData.LOG_TAG, "checkForPermission: ask for permission");
        } else {
            startService(new Intent(this, LocationService.class));
            Log.w(AppData.LOG_TAG, "checkForPermission: locationservice started");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.w(AppData.LOG_TAG, "onRequestPermissionResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, LocationService.class));
                    Log.w(AppData.LOG_TAG, "onRequestPermissionResult: started locationservice");
                } else {
                    // Permission denied, boo! Disable the functionality that depends on this permission.
                    CharSequence text = getString(R.string.java_google_location_warning);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                    Log.w(AppData.LOG_TAG, "onRequestPermissionResult: denied, warn toast should show");
                }
                break;
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }



    public void configureScheduleRequest(View v) {
        String budget = textBudget.getText().toString();
        String travelersAdult = textTravelersAdult.getText().toString();
        String travelersKid = textTravelersKid.getText().toString();
        String address = textStartLocation.getText().toString();
        String mobility = spinnerMobility.getSelectedItem().toString();
        Date dateStart = new Date();
        Date dateEnd = new Date();

        //Check if the dates are in the right format
        try{
            dateStart = dateFormat.parse(textTripStart.getText().toString());
            dateEnd = dateFormat.parse(textTripEnd.getText().toString());
        } catch (ParseException ex){
            Snackbar.make(v, "Please enter the dates in the format YYYY-MM-DD",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        //Check if all required fields are filled
        if(budget.isEmpty() || travelersAdult.isEmpty() || travelersKid.isEmpty()
                || address.isEmpty() || mobility.isEmpty() ){
            Snackbar.make(v, "Please fill in all required fields before you proceed again",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        //Check if there is at least one traveler
        else if(Integer.parseInt(travelersAdult) <= 0 && Integer.parseInt(travelersKid) <= 0){
            Snackbar.make(v, "Please specify the number of travelers",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        else if(dateStart.after(dateEnd)){
            Snackbar.make(v, "Please enter a valid date range",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        else {
            Request.setBudget(Double.valueOf(budget));
            AppData.budgetAvailable = Request.getAvailableBudget();
            Request.setNumberOfTravelers(Integer.valueOf(travelersAdult)
                    + Integer.valueOf(travelersKid));
            Calendar tripStart = Calendar.getInstance();
            Calendar tripEnd = Calendar.getInstance();
            tripStart.setTime(dateStart);
            tripEnd.setTime(dateEnd);
            Request.setTripStart(tripStart);
            Request.setTripEnd(tripEnd);

            if(radioButtonAddress.isChecked() && !radioButtonResidences.isChecked()) {
                setStartingLocation(textStartLocation.getText().toString());
            }
            else if(!radioButtonAddress.isChecked() && radioButtonResidences.isChecked()) {
                setStartingLocation(spinnerResidences.getSelectedItem().toString());
            }

            Request.setMobility(spinnerMobility.getSelectedItem().toString());
            startActivity(new Intent(this, ConfirmPoIActivity.class));
        }
    }

    //TODO change this method with a true translator
    private void setStartingLocation(String locationName) {
        if(locationName.equals(getString(R.string.parameters_mylocation))){
            Request.setStartLocationLat(AppData.getCurrentPosition().latitude);
            Request.setStartLocationLong(AppData.getCurrentPosition().longitude);
        }
        else{
            PointOfInterest poi = AppData.findPoIbyName(locationName);
            if(poi != null){
                Request.setStartLocationLat(poi.getLocationLat());
                Request.setStartLocationLat(poi.getLocationLat());
            } else {
                //Stockholm Centralstation by default
                Request.setStartLocationLat(AppData.STOCKHOLM_CENTRAL_COORDINATES.latitude);
                Request.setStartLocationLong(AppData.STOCKHOLM_CENTRAL_COORDINATES.longitude);
            }
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_parameters || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Bundle b = new Bundle();
        b.putInt("callerViewID", v.getId());
        if(v.equals(buttonTripStart)){
            b.putLong("selectedTime", dayStart.getTimeInMillis());
        }
        else if(v.equals(buttonTripEnd)){
            b.putLong("selectedTime", dayEnd.getTimeInMillis());
        }
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.setArguments(b);
        dateFragment.registerListener(this);
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    public void updateDate(int callerViewID, Calendar selectedDate) {
        if(callerViewID == buttonTripStart.getId() || callerViewID == textTripStart.getId()){
            dayStart = selectedDate;
            String formattedDate = dateFormat.format(dayStart.getTime());
            textTripStart.setText(formattedDate);
        }
        else if(callerViewID == buttonTripEnd.getId() || callerViewID == textTripEnd.getId()){
            dayEnd = selectedDate;
            String formattedDate = dateFormat.format(dayEnd.getTime());
            textTripEnd.setText(formattedDate);
        }
    }
}