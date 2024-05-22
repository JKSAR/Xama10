package com.example.xama10;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Handler mainHandler = new Handler();
    String data = "";

    TextView tv1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //new DownloadJsonAsyncTask()
               // .execute("http://192.168.18.3:8080/json.xama1/xama");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Load and use views afterwards
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv1 = (TextView)findViewById(R.id.textView1);
        tv1.setText("Julio Cesar");
        this.LedOff();
        //this.content(tv1);
    }

    public void content(TextView tv1) {
        refresh( 3000, tv1 );
    }

    public void LedOff( )
    {
        try
        {
            URL url = new URL("http://192.168.18.3:8080/json.xama1/xama");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET"); // Or any method you need
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d( "DEBUG_TAG", "The response code is: " + response);

            if ((response >= 200) && (response < 300)) {
                // We are assuming here that whatever the response is, it can be parsed as a String
                Log.d("DEBUG_TAG", "The response is: " + conn.getResponseMessage());
            }
        } catch(Exception ex) {
            String err = ex.getMessage();
            Log.d("DEBUG_TAG", "ERRO: " + err );
                    // Handle any exceptions
        }
    }

    private void refresh( int milliseconds , TextView tv1 )
    {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override

            public void run()
            {
                String line = "";
                BufferedReader bufferedReader = null;

                try
                {
                    //URL url = new URL("http://192.168.18.3:8080/json.xama1/xama");
                    URL url = new URL("https://api.npoint.io/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    String l_err = httpURLConnection.getErrorStream().toString();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        data = data + line;
                    }
                } catch (IOException e) {
                     //e.printStackTrace();
                    throw new RuntimeException(e);
                }



                //new DownloadJSON(new DownloadJSON.AsyncCallback() {
                 //   @Override
                 //   public void onComplete(String result) {
                 //       tv1.setText(result);
                 //   }
                 // }).execute("http://192.168.18.3:8080/json.xama1/xama");

                tv1.setText("Eloisa Helena");
                //new FetchData().start();
                content(tv1);
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }
}