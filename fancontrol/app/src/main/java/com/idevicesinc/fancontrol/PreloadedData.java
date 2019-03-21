package com.idevicesinc.fancontrol;

import android.graphics.Bitmap;

public class PreloadedData {
    private static PreloadedData instance = null;

    public Bitmap sunsetBackground = null;
    public Bitmap oceanBackground = null;
    public Bitmap forestBackground = null;

    private PreloadedData() {
    }

    public synchronized static PreloadedData getInstance() {
        if (instance == null) {
            instance = new PreloadedData();
        }
        return instance;
    }
}
