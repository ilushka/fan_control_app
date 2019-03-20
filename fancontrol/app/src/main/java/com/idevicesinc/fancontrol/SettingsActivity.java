package com.idevicesinc.fancontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

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

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // set background of activity
        /* MONKEY:
        Intent intent = getIntent();
        int theme = intent.getIntExtra(SettingsActivity.EXTRA_THEME_ID, 0);
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
    }
}
