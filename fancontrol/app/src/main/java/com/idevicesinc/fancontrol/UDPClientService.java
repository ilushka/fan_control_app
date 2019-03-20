package com.idevicesinc.fancontrol;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ilushka on 3/19/19.
 */

public class UDPClientService extends IntentService {
    public static final String TAG = "UDPClientService";

    public static final String PREF_DEVICE_IP = "com.idevicesinc.fancontrol.preferences.DEVICE_IP";
    public static final String PREF_DEVICE_PORT = "com.idevicesinc.fancontrol.preferences.DEVICE_PORT";

    public static final String EXTRA_MESSAGE = "com.idevicesinc.fancontrol.extra.MESSAGE";

    public static final int MESSAGE_SIZE = 7;   // in bytes
    public static final String DEFAULT_DEVICE_IP = "192.168.1.101";
    public static final int DEFAULT_DEVICE_PORT = 666;

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public UDPClientService() {
        super("UDPClientService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // retrieve preferences for the udp client service
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UDPClientService.this);
        String addrStr = sharedPreferences.getString(PREF_DEVICE_IP, DEFAULT_DEVICE_IP);
        port = sharedPreferences.getInt(PREF_DEVICE_PORT, DEFAULT_DEVICE_PORT);

        try {
            socket = new DatagramSocket(port);
            address = InetAddress.getByName(addrStr);
        } catch (SocketException e) {
            Log.e(TAG, "SocketException", e);
        } catch (UnknownHostException e) {
            Log.e(TAG, "UnknownHostException", e);
        }
    }

    @Override
    public void onDestroy() {
        socket.close();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        byte[] buf = intent.getByteArrayExtra(EXTRA_MESSAGE);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
            Log.d(TAG, "sent: " + byteBufferToString(buf));
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    String byteBufferToString(byte[] buf) {
        String str = "";
        for (byte b : buf) {
            if (str.length() > 0) {
                str += ", ";
            }
            str += Long.toHexString(((long)b & 0xff));
        }
        return str;
    }

    static public void sendTheme(Context context, long rgb, byte fanSpeed, long sprayPeriod) {
        Intent intent = new Intent(context, UDPClientService.class);
        byte[] buf = new byte[MESSAGE_SIZE];
        buf[0] = (byte)((rgb >> 16) & 0xff);
        buf[1] = (byte)((rgb >>  8) & 0xff);
        buf[2] = (byte)((rgb >>  0) & 0xff);
        buf[3] = fanSpeed;
        buf[4] = (byte)((sprayPeriod >> 16) & 0xff);
        buf[5] = (byte)((sprayPeriod >>  8) & 0xff);
        buf[6] = (byte)((sprayPeriod >>  0) & 0xff);
        intent.putExtra(EXTRA_MESSAGE, buf);
        context.startService(intent);
    }
}
