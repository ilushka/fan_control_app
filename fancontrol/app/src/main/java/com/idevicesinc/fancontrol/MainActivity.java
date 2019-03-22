package com.idevicesinc.fancontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView nav = (NavigationView) findViewById(R.id.drawer);
        int count = nav.getMenu().size();
        for (int ii = 0; ii < count; ii++) {
            nav.getMenu().getItem(ii).setChecked(false);
        }
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.EXTRA_THEME_ID, id);
        startActivity(intent);
    }

    void startSecretConfigActivity() {
        Intent intent = new Intent(this, SecretConfigActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    void sendThemeFromPreferences(int theme) {
        SharedPreferences prefs = MainActivity.this.getSharedPreferences(
                SettingsActivity.getThemePreferenceFilename(theme), Context.MODE_PRIVATE);
        byte spray = (byte)(prefs.getInt(SettingsActivity.PREFERENCE_SPRAY_PERIOD,
                SettingsActivity.DEFAULT_SPRAY_PERIOD) & 0xff);
        byte fan = (byte)(prefs.getInt(SettingsActivity.PREFERENCE_FAN_SPEED,
                SettingsActivity.DEFAULT_FAN_SPEED) & 0xff);
        long color = (long)(prefs.getInt(SettingsActivity.PREFERENCE_COLOR,
                (int)SettingsActivity.DEFAULT_COLOR) & 0xffffff);
        byte music = SettingsActivity.getMusicEnable(theme);
        UDPClientService.sendTheme(MainActivity.this, color, fan,
                SettingsActivity.sprayPeriodToLong(spray, theme), music);
        ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    }
}
