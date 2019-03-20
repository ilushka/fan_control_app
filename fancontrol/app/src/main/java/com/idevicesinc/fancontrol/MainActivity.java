package com.idevicesinc.fancontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.idevicesinc.fancontrol.R.drawable.ocean;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        // assign long and short press listeners to photos
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendThemeFromPreferences(v.getId());
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
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.EXTRA_THEME_ID, id);
        startActivity(intent);
    }

    void sendThemeFromPreferences(int theme) {
        String sprayPref = null, fanPref = null, colorPref = null;
        int sprayDefault = 0, fanDefault = 0, colorDefault = 0;
        switch (theme) {
            case R.id.ocean_button:
                sprayPref = SettingsActivity.PREF_OCEAN_SPRAY;
                fanPref = SettingsActivity.PREF_OCEAN_FAN;
                colorPref = SettingsActivity.PREF_OCEAN_COLOR;
                sprayDefault = SettingsActivity.DEFAULT_OCEAN_SPRAY;
                fanDefault = SettingsActivity.DEFAULT_OCEAN_FAN;
                colorDefault = SettingsActivity.DEFAULT_OCEAN_COLOR;
                break;
            case R.id.mountains_button:
                sprayPref = SettingsActivity.PREF_MOUNTAINS_SPRAY;
                fanPref = SettingsActivity.PREF_MOUNTAINS_FAN;
                colorPref = SettingsActivity.PREF_MOUNTAINS_COLOR;
                sprayDefault = SettingsActivity.DEFAULT_MOUNTAINS_SPRAY;
                fanDefault = SettingsActivity.DEFAULT_MOUNTAINS_FAN;
                colorDefault = SettingsActivity.DEFAULT_MOUNTAINS_COLOR;
                break;
            case R.id.wind_button:
                sprayPref = SettingsActivity.PREF_WIND_SPRAY;
                fanPref = SettingsActivity.PREF_WIND_FAN;
                colorPref = SettingsActivity.PREF_WIND_COLOR;
                sprayDefault = SettingsActivity.DEFAULT_WIND_SPRAY;
                fanDefault = SettingsActivity.DEFAULT_WIND_FAN;
                colorDefault = SettingsActivity.DEFAULT_WIND_COLOR;
                break;
        }
        int spray = sharedPreferences.getInt(sprayPref, sprayDefault);
        int fan = sharedPreferences.getInt(fanPref, fanDefault);
        int color = sharedPreferences.getInt(colorPref, colorDefault);
        UDPClientService.sendTheme(MainActivity.this, color, (byte)fan, spray);
    }
}
