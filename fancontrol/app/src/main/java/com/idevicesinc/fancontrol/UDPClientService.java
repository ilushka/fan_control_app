package com.idevicesinc.fancontrol;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by ilushka on 3/19/19.
 */

public class UDPClientService extends IntentService {
    public static final String TAG = "UDPClientService";

    public static final String EXTRA_MESSAGE = "com.idevicesinc.fancontrol.extra.MESSAGE";

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public UDPClientService() {
        super("UDPClientService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            port = 5555;
            socket = new DatagramSocket(port);
            address = InetAddress.getByName("192.168.1.101");
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
        byte[] buf = intent.getStringExtra(EXTRA_MESSAGE).getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
            Log.d(TAG, "sent: " + Arrays.toString(buf));
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    static public void sendMessage(Context context, String message) {
        Intent intent = new Intent(context, UDPClientService.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startService(intent);
    }

    /* MONKEY:
    public UDPClientS() throws SocketException, UnknownHostException {

    }

    public void send(String str) throws IOException {
        byte[] buf = str.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    public void close() {

    }
    */
}
