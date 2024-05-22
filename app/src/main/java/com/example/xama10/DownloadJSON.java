package com.example.xama10;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadJSON extends AsyncTask<String, String, String>
{
    private AsyncCallback mCallback;

    public DownloadJSON(AsyncCallback callback) {
        mCallback = callback;
    }

    protected String doInBackground(String... args) {
        // process background task
        return null;
    }


    protected void onPostExecute(String result) {
        if (mCallback != null)
            mCallback.onComplete(result);
    }

    public interface AsyncCallback {
        void onComplete(String result);
    }
}