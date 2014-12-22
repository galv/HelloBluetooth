package com.danieltgalvez.bluetoothhello;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private BluetoothSocket mSocket;

    private int REQUEST_ENABLE_BT = 0;
    private final String uuid_str = "00001101-0000-1000-8000-00805F9B34FB";
    private final UUID uuid = UUID.fromString(uuid_str);
    private String MACAddress = "20:13:09:11:23:71"; //Not final. Possibly may want to change it.

    public final String ioLogTag = "IO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(MACAddress);
        BluetoothSocket tmpSocket = null;
        try {
            tmpSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(ioLogTag, "Did not establish connection with bluetooth device.");
            // TODO: Notify user
        }
        mSocket = tmpSocket;

        InputStream tmpIs = null;
        OutputStream tmpOs = null;
        try {
            tmpIs = mSocket.getInputStream();
            tmpOs = mSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(ioLogTag, "Failure getting external device's input stream.");
        }
        InputStream is = tmpIs;
        OutputStream os = tmpOs;
        while (true) {
            try {
                Log.i("3", "About to read byte.");
                //os.write(42);
                //os.write(72);
                //Log.i("4", "wrote byte");
                int input = is.read();
                Log.i("1", "Read byte: " + input);
            } catch (IOException e) {
                Log.e(ioLogTag, "Failure while reading from external device.");
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
