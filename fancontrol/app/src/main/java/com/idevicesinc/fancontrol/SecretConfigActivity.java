package com.idevicesinc.fancontrol;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;


public class SecretConfigActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_config);

        // init edit text with stored preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SecretConfigActivity.this);
        String addrStr = sharedPreferences.getString(UDPClientService.PREF_DEVICE_IP,
                            UDPClientService.DEFAULT_DEVICE_IP);
        int port = sharedPreferences.getInt(UDPClientService.PREF_DEVICE_PORT,
                            UDPClientService.DEFAULT_DEVICE_PORT);
        ((EditText) findViewById(R.id.edit_address)).setText(addrStr, TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.edit_port)).setText(Integer.toString(port), TextView.BufferType.EDITABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String addr = ((EditText) findViewById(R.id.edit_address)).getText().toString();
        editor.putString(UDPClientService.PREF_DEVICE_IP, addr);
        String portStr = ((EditText) findViewById(R.id.edit_port)).getText().toString();
        editor.putInt(UDPClientService.PREF_DEVICE_PORT, Integer.parseInt(portStr));
        editor.apply();
    }
}
