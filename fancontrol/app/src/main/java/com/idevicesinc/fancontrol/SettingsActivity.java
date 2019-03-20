package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

public class SettingsActivity extends AppCompatActivity {
    public static final String TAG = "SettingsActivity";

    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";

    // names of preferences for each theme
    public static final String PREF_OCEAN_SPRAY = "com.idevicesinc.fancontrol.preferences.OCEAN_SPRAY";
    public static final String PREF_OCEAN_FAN = "com.idevicesinc.fancontrol.preferences.OCEAN_FAN";
    public static final String PREF_OCEAN_COLOR = "com.idevicesinc.fancontrol.preferences.OCEAN_COLOR";
    public static final String PREF_FOREST_SPRAY = "com.idevicesinc.fancontrol.preferences.FOREST_SPRAY";
    public static final String PREF_FOREST_FAN = "com.idevicesinc.fancontrol.preferences.FOREST_FAN";
    public static final String PREF_FOREST_COLOR = "com.idevicesinc.fancontrol.preferences.FOREST_COLOR";
    public static final String PREF_SUNSET_SPRAY = "com.idevicesinc.fancontrol.preferences.SUNSET_SPRAY";
    public static final String PREF_SUNSET_FAN = "com.idevicesinc.fancontrol.preferences.SUNSET_FAN";
    public static final String PREF_SUNSET_COLOR = "com.idevicesinc.fancontrol.preferences.SUNSET_COLOR";

    // preferences defaults for each theme
    public static final byte DEFAULT_OCEAN_SPRAY = 0x7F;
    public static final byte DEFAULT_OCEAN_FAN = 0x7F;
    public static final long DEFAULT_OCEAN_COLOR = 0xFFFFFF;
    public static final byte DEFAULT_FOREST_SPRAY = 0x7F;
    public static final byte DEFAULT_FOREST_FAN = 0x7F;
    public static final long DEFAULT_FOREST_COLOR = 0xFFFFFF;
    public static final byte DEFAULT_SUNSET_SPRAY = 0x7F;
    public static final byte DEFAULT_SUNSET_FAN = 0x7F;
    public static final long DEFAULT_SUNSET_COLOR = 0xFFFFFF;

    private SharedPreferences sharedPreferences;
    private byte fanSpeed;
    private byte sprayPeriod;
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
            case R.id.forest_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
                break;
            case R.id.sunset_button:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunset);
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
        sprayPeriod = (byte)(sharedPreferences.getInt(SettingsActivity.getPrefNameForSprayPeriod(theme),
                SettingsActivity.getDefaultForSprayPeriod(theme)) & 0xff);
        fanSpeed = (byte)(sharedPreferences.getInt(SettingsActivity.getPrefNameForFanSpeed(theme),
                SettingsActivity.getDefaultForFanSpeed(theme)) & 0xff);
        color = (long)(sharedPreferences.getInt(SettingsActivity.getPrefNameForColor(theme),
                (int)SettingsActivity.getDefaultForColor(theme)) & 0x00ffffff);
        SeekBar fanBar = (SeekBar) findViewById(R.id.fan_speed);
        fanBar.setProgress(fanSpeed & 0xff);
        SeekBar sprayBar = (SeekBar) findViewById(R.id.spray_period);
        Log.d(TAG, "onCreate: sprayPeriod: " + Integer.toHexString((int)sprayPeriod) + ", fanSpeed: " +
                Integer.toHexString((int)fanSpeed) + ", color: " + Integer.toHexString((int)color));
        if (sprayPeriod == 0) {
            sprayBar.setProgress(0);
        } else {
            sprayBar.setProgress((byte)(256 - sprayPeriod) & 0xff);
        }

        // add listeners to seek bars
        sprayBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    sprayPeriod = (byte)((256 - progress) & 0xff);
                } else {
                    sprayPeriod = 0;
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
                fanSpeed = (byte)progress;
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

        // color picker
        ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                //colorEnvelope.getColorRGB();
            }
        });
    }

    static String getPrefNameForSprayPeriod(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_SPRAY;
            case R.id.forest_button:
                return SettingsActivity.PREF_FOREST_SPRAY;
            case R.id.sunset_button:
                return SettingsActivity.PREF_SUNSET_SPRAY;
        }
        return "";
    }

    static String getPrefNameForFanSpeed(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_FAN;
            case R.id.forest_button:
                return SettingsActivity.PREF_FOREST_FAN;
            case R.id.sunset_button:
                return SettingsActivity.PREF_SUNSET_FAN;
        }
        return "";
    }

    static String getPrefNameForColor(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.PREF_OCEAN_COLOR;
            case R.id.forest_button:
                return SettingsActivity.PREF_FOREST_COLOR;
            case R.id.sunset_button:
                return SettingsActivity.PREF_SUNSET_COLOR;
        }
        return "";
    }

    static byte getDefaultForSprayPeriod(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_SPRAY;
            case R.id.forest_button:
                return SettingsActivity.DEFAULT_FOREST_SPRAY;
            case R.id.sunset_button:
                return SettingsActivity.DEFAULT_SUNSET_SPRAY;
        }
        return 0;
    }

    static byte getDefaultForFanSpeed(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_FAN;
            case R.id.forest_button:
                return SettingsActivity.DEFAULT_FOREST_FAN;
            case R.id.sunset_button:
                return SettingsActivity.DEFAULT_SUNSET_FAN;
        }
        return 0;
    }

    static long getDefaultForColor(int theme) {
        switch (theme) {
            case R.id.ocean_button:
                return SettingsActivity.DEFAULT_OCEAN_COLOR;
            case R.id.forest_button:
                return SettingsActivity.DEFAULT_FOREST_COLOR;
            case R.id.sunset_button:
                return SettingsActivity.DEFAULT_SUNSET_COLOR;
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
        UDPClientService.sendTheme(SettingsActivity.this, color, fanSpeed,
                sprayPeriodToLong(sprayPeriod, theme), (byte)0);
    }

    public static long sprayPeriodToLong(byte sprayPeriod, int theme) {
        switch (theme) {
            case R.id.sunset_button:
                return ((sprayPeriod << 16) & 0x00FF0000);
            case R.id.ocean_button:
                return ((sprayPeriod <<  8) & 0x0000FF00);
            case R.id.forest_button:
                return ((sprayPeriod <<  0) & 0x000000FF);
        }
        return 0;
    }
}
