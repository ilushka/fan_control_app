package com.idevicesinc.fancontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    public static final String TAG = "SettingsActivity";

    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";
    
    public static final String PREF_OCEAN_SPRAY = "com.idevicesinc.fancontrol.preferences.OCEAN_SPRAY";
    public static final String PREF_OCEAN_FAN = "com.idevicesinc.fancontrol.preferences.OCEAN_FAN";
    public static final String PREF_OCEAN_COLOR = "com.idevicesinc.fancontrol.preferences.OCEAN_COLOR";
    public static final String PREF_MOUNTAINS_SPRAY = "com.idevicesinc.fancontrol.preferences.MOUNTAINS_SPRAY";
    public static final String PREF_MOUNTAINS_FAN = "com.idevicesinc.fancontrol.preferences.MOUNTAINS_FAN";
    public static final String PREF_MOUNTAINS_COLOR = "com.idevicesinc.fancontrol.preferences.MOUNTAINS_COLOR";
    public static final String PREF_WIND_SPRAY = "com.idevicesinc.fancontrol.preferences.WIND_SPRAY";
    public static final String PREF_WIND_FAN = "com.idevicesinc.fancontrol.preferences.WIND_FAN";
    public static final String PREF_WIND_COLOR = "com.idevicesinc.fancontrol.preferences.WIND_COLOR";
    
    public static final int DEFAULT_OCEAN_SPRAY = 50;
    public static final int DEFAULT_OCEAN_FAN = 50;
    public static final int DEFAULT_OCEAN_COLOR = 0xFFFFFF;
    public static final int DEFAULT_MOUNTAINS_SPRAY = 50;
    public static final int DEFAULT_MOUNTAINS_FAN = 50;
    public static final int DEFAULT_MOUNTAINS_COLOR = 0xFFFFFF;
    public static final int DEFAULT_WIND_SPRAY = 50;
    public static final int DEFAULT_WIND_FAN = 50;
    public static final int DEFAULT_WIND_COLOR = 0xFFFFFF;

    private SharedPreferences sharedPreferences;
    private long fanSpeed;
    private long sprayPeriod;
    private long color;
    private int theme;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        theme = intent.getIntExtra(SettingsActivity.EXTRA_THEME_ID, 0);

        // set background of activity
        /* MONKEY:
        Bitmap bitmap = null;
        switch (theme) {
            case R.id.ocean_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ocean);
                break;
            case R.id.mountains_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mountains);
                break;
            case R.id.wind_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wind);
                break;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        int newWidth = bitmapWidth * (screenHeight / bitmapHeight);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, screenHeight, true);
        ImageView bgImage = (ImageView) findViewById(R.id.settings_background);
        bgImage.setImageBitmap(newBitmap);
        */

        // initialize widgets based on stored preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        sprayPeriod = sharedPreferences.getInt(SettingsActivity.getPrefNameForSprayPeriod(theme),
                SettingsActivity.getDefaultForSprayPeriod(theme));
        fanSpeed = sharedPreferences.getInt(SettingsActivity.getPrefNameForFanSpeed(theme),
                SettingsActivity.getDefaultForFanSpeed(theme));
        color = sharedPreferences.getInt(SettingsActivity.getPrefNameForColor(theme),
                SettingsActivity.getDefaultForColor(theme));
        SeekBar fanBar = (SeekBar) findViewById(R.id.fan_speed);
        fanBar.setProgress((int)fanSpeed);
        SeekBar sprayBar = (SeekBar) findViewById(R.id.spray_period);
        sprayBar.setProgress((int)(256 - sprayPeriod));

        // add listeners to seek bars
        sprayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    sprayPeriod = 256 - progress;
                } else {
                    sprayPeriod = progress;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "final spray period: " + sprayPeriod);
                storeThemeToPreference();
                sendThemeOverUDP();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
        });
        fanBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fanSpeed = progress;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "final fan speed: " + fanSpeed);
                storeThemeToPreference();
                sendThemeOverUDP();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
        });
    }

    static String getPrefNameForSprayPeriod(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_SPRAY;
            case R.id.mountains_button:
                return SettingsActivity.PREF_MOUNTAINS_SPRAY;
            case R.id.wind_button:
                return SettingsActivity.PREF_WIND_SPRAY;
        }
        return "";
    }

    static String getPrefNameForFanSpeed(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_FAN;
            case R.id.mountains_button:
                return SettingsActivity.PREF_MOUNTAINS_FAN;
            case R.id.wind_button:
                return SettingsActivity.PREF_WIND_FAN;
        }
        return "";
    }

    static String getPrefNameForColor(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_COLOR;
            case R.id.mountains_button:
                return SettingsActivity.PREF_MOUNTAINS_COLOR;
            case R.id.wind_button:
                return SettingsActivity.PREF_WIND_COLOR;
        }
        return "";
    }

    static int getDefaultForSprayPeriod(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_SPRAY;
            case R.id.mountains_button:
                return SettingsActivity.DEFAULT_MOUNTAINS_SPRAY;
            case R.id.wind_button:
                return SettingsActivity.DEFAULT_WIND_SPRAY;
        }
        return 0;
    }

    static int getDefaultForFanSpeed(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_FAN;
            case R.id.mountains_button:
                return SettingsActivity.DEFAULT_MOUNTAINS_FAN;
            case R.id.wind_button:
                return SettingsActivity.DEFAULT_WIND_FAN;
        }
        return 0;
    }

    static int getDefaultForColor(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_COLOR;
            case R.id.mountains_button:
                return SettingsActivity.DEFAULT_MOUNTAINS_COLOR;
            case R.id.wind_button:
                return SettingsActivity.DEFAULT_WIND_COLOR;
        }
        return 0;
    }

    void storeThemeToPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getPrefNameForSprayPeriod(theme), (int)sprayPeriod);
        editor.putInt(getPrefNameForFanSpeed(theme), (int) fanSpeed);
        editor.putInt(getPrefNameForColor(theme), (int)color);
        editor.apply();
        Log.d(TAG, "storeThemeToPreferences: spray period: " + sprayPeriod + ", fanSpeed: " + fanSpeed + ", color: " + color);
    }

    void sendThemeOverUDP() {
        UDPClientService.sendTheme(SettingsActivity.this, color, (byte)fanSpeed, sprayPeriod);
    }
}
