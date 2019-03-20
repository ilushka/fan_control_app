package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;

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
        ImageView forest = (ImageView) findViewById(R.id.forest_button);
        forest.setOnClickListener(clickListener);
        forest.setOnLongClickListener(longClickListener);
        ImageView sunset = (ImageView) findViewById(R.id.sunset_button);
        sunset.setOnClickListener(clickListener);
        sunset.setOnLongClickListener(longClickListener);

        // drawer stuff
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        if (menuItem.getItemId() == R.id.item_settings) {
                            startSecretConfigActivity();
                        }
                        return true;
                    }
                });
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.EXTRA_THEME_ID, id);
        startActivity(intent);
    }

    void startSecretConfigActivity() {
        Intent intent = new Intent(this, SecretConfigActivity.class);
        startActivity(intent);
    }

    void sendThemeFromPreferences(int theme) {
        byte spray = (byte)(sharedPreferences.getInt(SettingsActivity.getPrefNameForSprayPeriod(theme),
                SettingsActivity.getDefaultForSprayPeriod(theme)) & 0xff);
        byte fan = (byte)(sharedPreferences.getInt(SettingsActivity.getPrefNameForFanSpeed(theme),
                SettingsActivity.getDefaultForFanSpeed(theme)) & 0xff);
        long color = (long)(sharedPreferences.getInt(SettingsActivity.getPrefNameForColor(theme),
                (int)SettingsActivity.getDefaultForColor(theme)) & 0xffffff);
        UDPClientService.sendTheme(MainActivity.this, color, fan,
                SettingsActivity.sprayPeriodToLong(spray, theme), (byte)0);
    }
}
