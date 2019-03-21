package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class LoadingActivity extends AppCompatActivity {

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            PreloadedData preloadedData = PreloadedData.getInstance();

            Bitmap sunsetBmp = BitmapFactory.decodeResource(getResources(), R.drawable.sunset);
            int bitmapWidth = sunsetBmp.getWidth();
            int bitmapHeight = sunsetBmp.getHeight();
            int screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
            int newWidth = bitmapWidth * (screenHeight / bitmapHeight);
            preloadedData.sunsetBackground = Bitmap.createScaledBitmap(sunsetBmp, newWidth,
                    screenHeight, true);
            Bitmap oceanBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ocean);
            bitmapWidth = oceanBmp.getWidth();
            bitmapHeight = oceanBmp.getHeight();
            screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
            newWidth = bitmapWidth * (screenHeight / bitmapHeight);
            preloadedData.oceanBackground = Bitmap.createScaledBitmap(oceanBmp, newWidth,
                    screenHeight, true);
            Bitmap forestBmp = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
            bitmapWidth = forestBmp.getWidth();
            bitmapHeight = forestBmp.getHeight();
            screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
            newWidth = bitmapWidth * (screenHeight / bitmapHeight);
            preloadedData.forestBackground = Bitmap.createScaledBitmap(forestBmp, newWidth,
                    screenHeight, true);
            return null;
        }

        protected void onPostExecute(Void result) {
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new LoadTask().execute();
    }
}
