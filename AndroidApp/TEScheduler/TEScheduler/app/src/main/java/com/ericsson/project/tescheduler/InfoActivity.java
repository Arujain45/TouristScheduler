package com.ericsson.project.tescheduler;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Tools.ExceptionScheduleRVA;

public class InfoActivity extends AppCompatActivity {

    private LinearLayout scheduleFragment, specialDaysFragment, priceFragment;
    private TextView textOpeningStatus, textOpeningMon, textOpeningTue, textOpeningWed, textOpeningThu
            , textOpeningFri, textOpeningSat, textOpeningSun, textSpecialDays, textVisitTime
            , textPriceDefault, textPriceKid, textPriceStudent, textPriceSenior, textPriceGroup
            , textWebsite, textPhone, textAddress, textDescription;
    private RecyclerView recyclerView;

    //The index to access the right PoI in AppData's ArrayList
    private PointOfInterest poi;
    private int indexPoI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        indexPoI = getIntent().getIntExtra("PoIIndex", -1);
        if( indexPoI == -1){
            Snackbar.make(textDescription, "Error loading Point of Interest",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else {
            poi = AppData.getPointOfInterestAt(indexPoI);
            setTitle(poi.getName());

            //init: Schedule
            scheduleFragment = (LinearLayout) findViewById(R.id.fragment_info_schedule);
            textOpeningStatus = (TextView) findViewById(R.id.infovalue_isopen);
            textOpeningMon = (TextView) findViewById(R.id.info_open1);
            textOpeningTue = (TextView) findViewById(R.id.info_open2);
            textOpeningWed = (TextView) findViewById(R.id.info_open3);
            textOpeningThu = (TextView) findViewById(R.id.info_open4);
            textOpeningFri = (TextView) findViewById(R.id.info_open5);
            textOpeningSat = (TextView) findViewById(R.id.info_open6);
            textOpeningSun = (TextView) findViewById(R.id.info_open7);
            specialDaysFragment = (LinearLayout) findViewById(R.id.fragment_info_specialdays);
            textSpecialDays = (TextView) findViewById(R.id.label_specialdays);

            //init: price
            priceFragment = (LinearLayout) findViewById(R.id.fragment_info_pricelist);
            textPriceDefault = (TextView) findViewById(R.id.infovalue_price);
            textPriceKid = (TextView) findViewById(R.id.info_price1);
            textPriceStudent = (TextView) findViewById(R.id.info_price2);
            textPriceSenior = (TextView) findViewById(R.id.info_price3);
            textPriceGroup = (TextView) findViewById(R.id.info_price4);

            //init: other info
            textVisitTime = (TextView) findViewById(R.id.infovalue_visittime);
            textWebsite = (TextView) findViewById(R.id.infovalue_website);
            textPhone = (TextView) findViewById(R.id.infovalue_phone);
            textAddress = (TextView) findViewById(R.id.infovalue_address);
            textDescription = (TextView) findViewById(R.id.infovalue_description);

            recyclerView = (RecyclerView) findViewById(R.id.specialday_rva);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            fillContent();
        }
    }

    private void fillContent() {
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
        textOpeningMon.setText(poi.getFormattedSchedule(0));
        textOpeningTue.setText(poi.getFormattedSchedule(1));
        textOpeningWed.setText(poi.getFormattedSchedule(2));
        textOpeningThu.setText(poi.getFormattedSchedule(3));
        textOpeningFri.setText(poi.getFormattedSchedule(4));
        textOpeningSat.setText(poi.getFormattedSchedule(5));
        textOpeningSun.setText(poi.getFormattedSchedule(6));

        textPriceDefault.setText(getString(R.string.info_price_value
                , String.valueOf((int) poi.getPriceByCategory("Adult"))));
        textPriceKid.setText(getString(R.string.info_price_value
                , String.valueOf((int) poi.getPriceByCategory("Kid"))));
        textPriceStudent.setText(getString(R.string.info_price_value
                , String.valueOf((int) poi.getPriceByCategory("Student"))));
        textPriceSenior.setText(getString(R.string.info_price_value
                , String.valueOf((int) poi.getPriceByCategory("Senior"))));
        textPriceGroup.setText(getString(R.string.info_price_value
                , String.valueOf((int) poi.getPriceByCategory("Group"))));

        textVisitTime.setText(String.valueOf(poi.getFormattedVisitTime()));
        textWebsite.setText(String.valueOf(poi.getWebsite()));
        textPhone.setText(String.valueOf(poi.getPhone()));
        textAddress.setText(String.valueOf(poi.getAddress()));
        textDescription.setText(poi.getGetDescriptionLong());
    }

    public void showSpecialDaysFragment(View view) {
        if(specialDaysFragment.getVisibility() == View.GONE){
            specialDaysFragment.setVisibility(View.VISIBLE);
        }
        else{
            specialDaysFragment.setVisibility(View.GONE);
        }
    }

    public void showScheduleFragment(View view) {
        if(scheduleFragment.getVisibility() == View.GONE){
            scheduleFragment.setVisibility(View.VISIBLE);
            textOpeningStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0);
        }
        else{
            specialDaysFragment.setVisibility(View.GONE);
            scheduleFragment.setVisibility(View.GONE);
            textOpeningStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        }
    }

    public void showPriceFragment(View view) {
        if(priceFragment.getVisibility() == View.GONE){
            priceFragment.setVisibility(View.VISIBLE);
            textPriceDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0);
        }
        else{
            priceFragment.setVisibility(View.GONE);
            textPriceDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleFragment.setVisibility(View.GONE);
        specialDaysFragment.setVisibility(View.GONE);
        priceFragment.setVisibility(View.GONE);
        if(poi.getSchedule().getExceptionSchedule().size() == 0){
            textSpecialDays.setVisibility(View.GONE);
        } else {
            ExceptionScheduleRVA adapter = new ExceptionScheduleRVA(
                    poi.getSchedule().getExceptionSchedule());
            recyclerView.setAdapter(adapter);
        }
    }

    public void addPoIToRequest(View view) {
        boolean successFlag = Request.addSelectedPoI(indexPoI);
        if(successFlag){
            double poiCost = AppData.getPointOfInterestAt(indexPoI).getPriceByCategory("Adult");
            if(Request.getBudget() != -1) {
                AppData.budgetAvailable -= poiCost;
            }
            AppData.budgetExpenses += poiCost;
            Snackbar.make(view, AppData.getPointOfInterestAt(indexPoI).getName()
                    + " has been added to your list",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
            finish();
        } else {
            Snackbar.make(view, "This PI is already in your list",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}