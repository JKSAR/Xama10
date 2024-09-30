package com.example.xama10;

//import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
//import android.widget.LinearLayout;
import android.widget.TextView;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.app.Activity;
import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //public static final String g_url_xama = "http://192.168.18.45:8080/json.xama1/xama";
    public static final String g_url_xama = "http://elois4555.c44.integrator.host/json.xama1/xama";

    //Handler mainHandler = new Handler();
    String data = "";

    TextView tv1 = null;

    View rootView;
    Handler handler;
    boolean isBlack = true;
    int flashCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Load and use views afterwards
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //tv1 = (TextView)findViewById(R.id.textView1);
        tv1 = findViewById(R.id.textView1);
        tv1.setText("Julio Cesar");
        //this.LedOff();
        //this.blinkScreen();
        this.content(tv1);
    }

    public void content(TextView tv1) {
        refresh(30000, tv1);
    }

    public void LedOff() {
        try {
            URL url = new URL( g_url_xama );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET"); // Or any method you need
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response code is: " + response);

            if ((response >= 200) && (response < 300)) {
                // We are assuming here that whatever the response is, it can be parsed as a String
                Log.d("DEBUG_TAG", "The response is: " + conn.getResponseMessage());
            }
        } catch (Exception ex) {
            String err = ex.getMessage();
            Log.d("DEBUG_TAG", "ERRO: " + err);
            // Handle any exceptions
        }
    }

    private void refresh(int milliseconds, TextView tv1) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override

            public void run() {
                String line;
                BufferedReader bufferedReader;
                data = "";
                try {
                    URL url = new URL( g_url_xama );
                    //URL url = new URL("https://api.npoint.io/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                    httpURLConnection.setRequestMethod("GET"); // Or any method you need
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    //String l_err = httpURLConnection.getErrorStream().toString();
                    //int response = httpURLConnection.getResponseCode();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((line = bufferedReader.readLine()) != null) {
                        data = data + line;
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    throw new RuntimeException(e);
                }

                String jsonString = data;

                // Cria um JSONObject a partir da string
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    int idClient = jsonObject.getInt("idClient");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                int table;
                try {
                    table = jsonObject.getInt("table");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (table > 0) {
                    tv1.setText(String.valueOf(table));
                    //sendZeroTable();
                    sendZeroTable2();
                    blinkScreen();
                    content(tv1);
                } else {
                    String l_table;
                    l_table = String.valueOf(tv1.getText());
                    tv1.setText(l_table);
                    content(tv1);
                }
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }

    private void blinkScreen() {
        //final LinearLayout layout = (LinearLayout) findViewById(R.id.textView1);
        tv1 = findViewById(R.id.textView1);
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();

        drawable.addFrame(new ColorDrawable(Color.BLACK), 1000);
        drawable.addFrame(new ColorDrawable(Color.WHITE), 1000);
        drawable.setOneShot(false);

        tv1.setBackgroundDrawable(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawable.stop();
                    }
                }, 19000);
            }
        }, 1000);
    }

    private void blinkScreen_old() {
        rootView = getWindow().getDecorView().getRootView();
        handler = new Handler();

        // Start flashing
        handler.postDelayed(flashRunnable, 1000);
    }

    private Runnable flashRunnable = new Runnable() {
        @Override
        public void run() {
            if (flashCount < 5) { // Flash only 5 times
                if (isBlack) {
                    rootView.setBackgroundColor(Color.BLACK);
                } else {
                    rootView.setBackgroundColor(Color.WHITE);
                }
                isBlack = !isBlack;
                flashCount++;
                handler.postDelayed(this, 1000); // Schedule next flash
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(flashRunnable); // Stop flashing when activity is destroyed
    }


    private void sendZeroTable() {
        //final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override

            public void run() {
                try {
                    URL url = new URL( g_url_xama );
                    //URL url = new URL("https://api.npoint.io/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                    httpURLConnection.setRequestMethod("GET"); // Or any method you need
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    //int response = httpURLConnection.getResponseCode();

                } catch (IOException e) {
                    //e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };

        handler.postDelayed(runnable, 500);
    }

    private void sendZeroTable2() {
        try {
            URL url = new URL( g_url_xama );
            //URL url = new URL("https://api.npoint.io/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET"); // Or any method you need
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            int response = httpURLConnection.getResponseCode();

        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}