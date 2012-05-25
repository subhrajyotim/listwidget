
package com.listapp;

import android.os.AsyncTask;

import com.listapp.model.NamedList;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class FetchListsTask extends AsyncTask<Void, Void, List<NamedList>> {

    private static final String TAG = FetchListsTask.class.getName();
    private ListappActivity activity;
    private String errMsg;

    public FetchListsTask(ListappActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<NamedList> doInBackground(Void... params) {
        String namedListUrl = Util.getBaseUrl(activity) + "/resources/namedList/all";
        RestTemplate restTemplate = new RestTemplate();
        try {
            NamedList[] namedLists = restTemplate.getForObject(namedListUrl, NamedList[].class);
            return Arrays.asList(namedLists);
        } catch (RestClientException e) {
            e.printStackTrace();
            errMsg = "Log In";
            cancel(true);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<NamedList> result) {
        super.onPostExecute(result);
        activity.setLists(result);
    }

    @Override
    protected void onCancelled() {
        if ((errMsg != null) && (errMsg.contains("Log In"))) {
            activity.login();
        } else {
            activity.showError(errMsg);
        }
    }

}
