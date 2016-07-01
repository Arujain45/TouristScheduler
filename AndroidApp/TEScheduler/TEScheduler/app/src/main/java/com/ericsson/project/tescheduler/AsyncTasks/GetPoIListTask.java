package com.ericsson.project.tescheduler.AsyncTasks;

import android.os.AsyncTask;

import com.ericsson.project.tescheduler.Interfaces.GetPoIListInterface;
import com.ericsson.project.tescheduler.ServerRequest;

public class GetPoIListTask extends AsyncTask<Void, Void, String> {
    public GetPoIListInterface callingClass;

    public GetPoIListTask(GetPoIListInterface callingClass){
        this.callingClass = callingClass;
    }

    @Override
    protected String doInBackground(Void... params) {
        String service = "/get_poilist";
        //TODO Remove params and use a formatted current system time
        String parameters = "lastUpdate=" + "-1";
        return ServerRequest.postRequest(service, parameters);
    }

    /* Deal with the response returned by the server */
    @Override
    protected void onPostExecute(String response) {
        callingClass.processGetPoIListResponse(response);
    }
}
