package com.ericsson.project.tescheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.ericsson.project.tescheduler.Interfaces.GetUpdatedDate;
import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Tools.DatePickerFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private EditText textAddress;
    private ImageButton buttonMyLocation;
    private Spinner spinnerResidences;
    private Spinner spinnerMobility;

    //Maintain the value according to the order of the string array "parameters_mobility_options"
    private final static int PUBLIC_TRANSPORT_SPINNER_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        dateFormat = new SimpleDateFormat(AppData.DATE_FORMAT, Locale.getDefault());

        //init: Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);

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
        textAddress = (EditText) findViewById(R.id.parameters_address);
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
                Snackbar.make(view, "radio selected ID is: " + view.getId() + "address",
                        Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
                radioButtonResidences.setChecked(false);
                spinnerResidences.setEnabled(false);
                textAddress.setEnabled(true);
                buttonMyLocation.setEnabled(true);
            }
        });
        radioButtonResidences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "radio selected ID is: " + view.getId() + "select",
                        Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
                radioButtonAddress.setChecked(false);
                textAddress.setEnabled(false);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void useCurrentPosition(View view){
        //if (Storage.getLatitude() != 0.0 && Storage.getLongitude() != 0.0){
        textAddress.setText(getString(R.string.parameters_mylocation));
        //Selection.setSelection(positionEditText.getText(), positionEditText.length());
        /*} else {
            CharSequence text = getString(R.string.java_search_locationfailed);
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }*/
    }

    public void configureScheduleRequest(View v) {
        Request.reset();
        String budget = textBudget.getText().toString();
        String travelersAdult = textTravelersAdult.getText().toString();
        String travelersKid = textTravelersKid.getText().toString();
        String address = textAddress.getText().toString();
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

        else if(dateStart.after(Calendar.getInstance().getTime()) || dateStart.after(dateEnd)){
            Snackbar.make(v, "Please enter a valid date range",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        else {
            Request.setBudget(Double.valueOf(budget));
            Request.setNumberOfTravelers(Integer.valueOf(travelersAdult)
                    + Integer.valueOf(travelersKid));
            Log.w(AppData.LOG_MESSAGE_HEADER, "Start: " + textTripStart.getText().toString()
                    + "\nEnd: " + textTripEnd.getText().toString());
            Calendar tripStart = Calendar.getInstance();
            Calendar tripEnd = Calendar.getInstance();
            tripStart.setTime(dateStart);
            tripEnd.setTime(dateEnd);
            Log.w(AppData.LOG_MESSAGE_HEADER, "Start: " + tripStart.toString() + "\nEnd: " + tripEnd.toString());
            Request.setTripStart(tripStart);
            Request.setTripEnd(tripEnd);

            if(radioButtonAddress.isChecked() && !radioButtonResidences.isChecked()) {
                setStartingLocation(textAddress.getText().toString());
            }
            else if(!radioButtonAddress.isChecked() && radioButtonResidences.isChecked()) {
                setStartingLocation(spinnerResidences.getSelectedItem().toString());
            }

            Request.setMobility(spinnerMobility.getSelectedItem().toString());

            //Snackbar.make(v, "Schedule Request was Sent!", Snackbar.LENGTH_LONG)
              //      .setAction("Action", null).show();

            //fixme: only add selected PoIs in MainMapActivity when the map is functional
            for(int i=0; i<AppData.getPointsOfInterest().size(); i++){
                Request.addSelectedPoI(i);
            }

            Request.printValues();
            startActivity(new Intent(this, ConfirmPoIActivity.class));
        }
    }

    //TODO change this method with a true translator
    private void setStartingLocation(String locationName) {
        if(locationName.equals(getString(R.string.parameters_mylocation))){
            Request.setStartLocationLat(59.3324501);
            Request.setStartLocationLong(18.0642306); //Sergei's Torg
        }
        else{
            Request.setStartLocationLat(59.3302994);
            Request.setStartLocationLong(18.0561089); //Stockholm Centralstation
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
}