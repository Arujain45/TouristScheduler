package com.ericsson.project.tescheduler.AsyncTasks;

import android.os.AsyncTask;

import com.ericsson.project.tescheduler.Interfaces.GenerateScheduleInterface;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.ServerRequest;

public class GenerateScheduleTask extends AsyncTask<Void,Void,String>{
    public GenerateScheduleInterface callingClass;

    public GenerateScheduleTask(GenerateScheduleInterface callingClass){
        this.callingClass = callingClass;
    }

    @Override
    protected String doInBackground(Void... params) {
        String service = "/generate_schedule";
        String[] poiInfo = Request.getFormattedPoIInfo();

        String parameters = "start_location_latitude=" + Request.getStartLocationLat()
                + "&start_location_longitude=" + Request.getStartLocationLong()
                + "&trip_start=" + Request.getTripStart().getTimeInMillis()
                + "&trip_end=" + Request.getTripEnd().getTimeInMillis()
                + "&budget=" + Request.getAvailableBudget()
                + "&travelers=" + Request.getNumberOfTravelers()
                + "&mobility=" + Request.getMobility()
                + "&poilist=" + poiInfo[0]
                + "&poi_visitduration=" + poiInfo[1];

        return ServerRequest.postRequest(service, parameters);
    }

    /* Deal with the response returned by the server */
    @Override
    protected void onPostExecute(String response) {
        callingClass.processGenerateScheduleResponse(response);
    }
}
