package com.idevicesinc.fancontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class SettingsActivity extends AppCompatActivity {
    public static final String TAG = "SettingsActivity";

    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";

    // names of preferences
    public static final String PREFERENCE_SPRAY_PERIOD = "com.idevicesinc.fancontrol.preferences.SPRAY_PERIOD";
    public static final String PREFERENCE_FAN_SPEED = "com.idevicesinc.fancontrol.preferences.FAN_SPEED";
    public static final String PREFERENCE_COLOR = "com.idevicesinc.fancontrol.preferences.COLOR";
    public static final String PREFERENCE_COLOR_X = "com.idevicesinc.fancontrol.preferences.COLOR_X";
    public static final String PREFERENCE_COLOR_Y = "com.idevicesinc.fancontrol.preferences.COLOR_Y";

    // preferences filename
    public static final String SUNSET_PREFERENCES = "com.idevicesinc.fancontrol.preferences.SUNSET_PREFERENCES";
    public static final String OCEAN_PREFERENCES = "com.idevicesinc.fancontrol.preferences.OCEAN_PREFERENCES";
    public static final String FOREST_PREFERENCES = "com.idevicesinc.fancontrol.preferences.FOREST_PREFERENCES";

    // preferences defaults
    public static final byte DEFAULT_SPRAY_PERIOD = 0x7F;
    public static final byte DEFAULT_FAN_SPEED = 0x7F;
    public static final long DEFAULT_COLOR = 0xFFFFFF;
    public static final int DEFAULT_COLOR_X = 400;
    public static final int DEFAULT_COLOR_Y = 400;

    public static final int COLOR_PICKER_RING_WIDTH = 20;   // in dp

    private SharedPreferences sharedPreferences;
    private byte fanSpeed;
    private byte sprayPeriod;
    private long color;
    private int theme;
    private int pickerRingWidth;    // in px
    private GradientDrawable ringDrawable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        theme = intent.getIntExtra(SettingsActivity.EXTRA_THEME_ID, 0);

        // set background
        PreloadedData preloadedData = PreloadedData.getInstance();
        ImageView bgImage = (ImageView) findViewById(R.id.settings_background);
        switch (theme) {
            case R.id.ocean_button:
                bgImage.setImageBitmap(preloadedData.oceanBackground);
                break;
            case R.id.forest_button:
                bgImage.setImageBitmap(preloadedData.forestBackground);
                break;
            case R.id.sunset_button:
                bgImage.setImageBitmap(preloadedData.sunsetBackground);
                break;
        }

        // data for changing color picker's ring color
        pickerRingWidth = (int)(COLOR_PICKER_RING_WIDTH *
                SettingsActivity.this.getResources().getDisplayMetrics().density);
        ringDrawable = (GradientDrawable)((ImageView) findViewById(R.id.picker_ring)).getDrawable();

        // load preferences
        sharedPreferences = SettingsActivity.this.getSharedPreferences(getThemePreferenceFilename(theme),
                Context.MODE_PRIVATE);
        sprayPeriod = (byte)(sharedPreferences.getInt(PREFERENCE_SPRAY_PERIOD, DEFAULT_SPRAY_PERIOD) & 0xff);
        fanSpeed = (byte)(sharedPreferences.getInt(PREFERENCE_FAN_SPEED, DEFAULT_FAN_SPEED) & 0xff);
        color = (long)(sharedPreferences.getInt(PREFERENCE_COLOR, (int)DEFAULT_COLOR) & 0x00ffffff);
        setColorPickerBackground(color);

        // initialize widgets
        SeekBar fanBar = (SeekBar) findViewById(R.id.fan_speed);
        fanBar.setProgress(fanSpeed & 0xff);
        SeekBar sprayBar = (SeekBar) findViewById(R.id.spray_period);
        if (sprayPeriod == 0) {
            sprayBar.setProgress(0);
        } else {
            sprayBar.setProgress((byte)(256 - sprayPeriod) & 0xff);
        }
        Log.d(TAG, "onCreate: sprayPeriod: " + Integer.toHexString((int)sprayPeriod) + ", fanSpeed: " +
                Integer.toHexString((int)fanSpeed) + ", color: " + Integer.toHexString((int)color));

        // initialize color picker widget
        final ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        colorPickerView.setActionMode(ActionMode.ALWAYS);
        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int inColor, boolean fromUser) {
                if (fromUser) {
                    color = inColor & 0x00ffffff;
                    setColorPickerBackground(color);
                }
            }
        });
        colorPickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    storeThemeToPreference();
                    sendThemeOverUDP();
                    Log.d(TAG, "colorPickerView: MotionEvent.ACTION_UP: " + Integer.toHexString((int) (color & 0x00ffffff)));
                }
                return false;
            }
        });


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MONKEY:
        ((ColorPickerView) findViewById(R.id.colorPickerView)).setCoordinate(300, 300);
    }

    void storeThemeToPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREFERENCE_SPRAY_PERIOD, (int)sprayPeriod);
        editor.putInt(PREFERENCE_FAN_SPEED, (int) fanSpeed);
        editor.putInt(PREFERENCE_COLOR, (int)color);
        editor.apply();
        Log.d(TAG, "storeThemeToPreferences: spray period: " + Integer.toHexString((sprayPeriod & 0xff)) +
                ", fanSpeed: " + Integer.toHexString((fanSpeed & 0xff)) +
                ", color: " + Integer.toHexString((int)(color * 0xffffff)));
    }

    void sendThemeOverUDP() {
        UDPClientService.sendTheme(SettingsActivity.this, color, fanSpeed,
            sprayPeriodToLong(sprayPeriod, theme), getMusicEnable(theme));
        ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
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

    public static String getThemePreferenceFilename(int theme) {
        switch (theme) {
            case R.id.sunset_button:
                return SUNSET_PREFERENCES;
            case R.id.ocean_button:
                return OCEAN_PREFERENCES;
            case R.id.forest_button:
                return FOREST_PREFERENCES;
        }
        return "";
    }

    public static byte getMusicEnable(int theme) {
        if (theme == R.id.sunset_button) {
            return 1;
        }
        return 0;
    }

    void setColorPickerBackground(long color) {
        ringDrawable.setStroke(pickerRingWidth, (int)((0xff << 24) | (color & 0x00ffffff)));
    }
}
