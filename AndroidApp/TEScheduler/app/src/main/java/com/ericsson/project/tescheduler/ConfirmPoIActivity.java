package com.ericsson.project.tescheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Tools.ConfirmPoIRVA;

import java.util.ArrayList;

public class ConfirmPoIActivity extends MenuedActivity {
    private RecyclerView recyclerView;
    private ArrayList<PointOfInterest> cancelledPoIs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_poi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_piduration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelledPoIs = new ArrayList<>();
        ConfirmPoIRVA adapter = new ConfirmPoIRVA(cancelledPoIs);
        recyclerView.setAdapter(adapter);
    }

    /*@Override
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
        return id == R.id.action_confirmpoi || super.onOptionsItemSelected(item);
    }

    //// FIXME: 2016-04-21
    public void sendScheduleRequest(View view) {
        Request.removeCancelledPoIs(cancelledPoIs);
        ServerRequest.postGenerateSchedule();
    }
}
