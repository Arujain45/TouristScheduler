package com.ericsson.project.tescheduler;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ericsson.project.tescheduler.AsyncTasks.GenerateScheduleTask;
import com.ericsson.project.tescheduler.Interfaces.GenerateScheduleInterface;
import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Objects.Route;
import com.ericsson.project.tescheduler.Tools.ConfirmPoIRVA;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ConfirmPoIActivity extends MenuedActivity implements GenerateScheduleInterface{
    private RecyclerView recyclerView;
    private ArrayList<PointOfInterest> cancelledPoIs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_poi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_piduration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing request...");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean[] selected = new boolean[1];
        if(!cancelledPoIs.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Remove cancelled PIs?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Request.removeCancelledPoIs(cancelledPoIs);
                    selected[0] = selectedOption(item);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return selected[0];
        } else {
            return item.getItemId() == R.id.action_confirmpoi || super.onOptionsItemSelected(item);
        }
    }

    private boolean selectedOption(MenuItem item){
        int id = item.getItemId();
        return id == R.id.action_confirmpoi || super.onOptionsItemSelected(item);
    }

    public void sendScheduleRequest(View view) {
        progressDialog.show();
        Request.removeCancelledPoIs(cancelledPoIs);
        Request.printValues();
        try{
            new GenerateScheduleTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //AppData.generateResultRoute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void processGenerateScheduleResponse(String response) {
        if(response == null || response.startsWith("ERROR") ){
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Failed!")
                    .setMessage(response);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            try {
                progressDialog.setTitle("Generating response...");
                Gson gson = new Gson();
                AppData.setResultRoute(gson.fromJson(response, Route.class));
                progressDialog.dismiss();
                startActivity(new Intent(this, DirectionsActivity.class));
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
}