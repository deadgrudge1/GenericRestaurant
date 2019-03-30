package com.example.genericrestaurant;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

public class RequestJava extends AsyncTask<Void, Void, DetectIntentResponse> {
    fragment_speak fragment;
    private SessionName session;
    private SessionsClient sessionsClient;
    private QueryInput queryInput;

    RequestJava(fragment_speak fragment, SessionName session, SessionsClient sessionsClient, QueryInput queryInput) {
        this.fragment = fragment;
        this.session = session;
        this.sessionsClient = sessionsClient;
        this.queryInput = queryInput;
    }

    protected DetectIntentResponse doInBackground(Void... voids) {
        try{
            DetectIntentRequest detectIntentRequest =
                    DetectIntentRequest.newBuilder()
                            .setSession(session.toString())
                            .setQueryInput(queryInput)
                            .build();
            return sessionsClient.detectIntent(detectIntentRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(DetectIntentResponse response) {
        fragment.callbackV2(response);
    }

}
