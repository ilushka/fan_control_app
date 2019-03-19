package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // set background of activity
        Intent intent = getIntent();
        int theme = intent.getIntExtra(MainActivity.EXTRA_THEME_ID, 0);
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

        /*
        SeekBar seekBar = (SeekBar) findViewById(R.id.fan_speed);
        seekBar.setThumbOffset(0);
        */

        // findViewById(R.id.fan_speed).setPadding(15, 0, 15, 0);

        ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        colorPickerView.setColorListener(new ColorListener() {
            @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {
                  //colorEnvelope.getColorRGB();
            }
        });

    }
}
