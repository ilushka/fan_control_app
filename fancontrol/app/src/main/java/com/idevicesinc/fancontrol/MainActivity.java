package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.idevicesinc.fancontrol.R.drawable.ocean;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // assign long and short press listeners to photos
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UDPClientService.sendMessage(MainActivity.this, "MONKEY");
            }
        };
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(v.getId());
                return true;
            }
        };
        ImageView ocean = (ImageView) findViewById(R.id.ocean_button);
        ocean.setOnClickListener(clickListener);
        ocean.setOnLongClickListener(longClickListener);
        ImageView mountains = (ImageView) findViewById(R.id.mountains_button);
        mountains.setOnClickListener(clickListener);
        mountains.setOnLongClickListener(longClickListener);
        ImageView wind = (ImageView) findViewById(R.id.wind_button);
        wind.setOnClickListener(clickListener);
        wind.setOnLongClickListener(longClickListener);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_THEME_ID, id);
        startActivity(intent);
    }
}
