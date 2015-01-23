package nl.mprog.hasamishogi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends Activity implements AdapterView.OnItemClickListener {

    ArrayAdapter<String> listAdapter;
    ListView listView;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;
    ArrayList<String> pairedDevices;
    public static UUID MY_UUID;
    IntentFilter filter;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        init();

        //if phone without bluetooth tries to use the bluetooth function
        if(btAdapter == null){
            Toast.makeText(getApplicationContext(),"No BlueTooth detected", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            // Check if bluetooth is enabled, if not make request to enable
            if(!btAdapter.isEnabled()){
                turnOnBluetooth();
            }
            getPairedDevices();
            startDiscovery();
        }
    }

    private void startDiscovery() {
        btAdapter.cancelDiscovery(); // If it is already discovering connections cancel it
        btAdapter.startDiscovery();
    }

    private void turnOnBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,0);
        listView.setAdapter(listAdapter);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();
        DeviceUuidFactory deviceUUID = new DeviceUuidFactory(getApplicationContext());
        MY_UUID = deviceUUID.getDeviceUuid();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String s = "";
                    // Check if a device in the list is already paired
                    for(int j = 0; j < pairedDevices.size(); j++){
                        if(device.getName().equals(pairedDevices.get(j))){
                            // append
                            s = "(Paired)";
                            break;
                        }
                    }
                    listAdapter.add(device.getName() + " "+s+" " + "\n" + device.getAddress());
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                    // Do Something
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    // Do Something
                }
                else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                    if(btAdapter.getState() == btAdapter.STATE_OFF){
                        turnOnBluetooth();
                    }
                }
            }
        };
        registerReceiver(receiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If user cancels the bluetooth connection
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "BlueTooth must be enabled", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    // Shows all paired devices in the ListView
    public void getPairedDevices() {
        devicesArray = btAdapter.getBondedDevices();
        if(devicesArray.size() > 0){
            for(BluetoothDevice device : devicesArray){
                pairedDevices.add(device.getName());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }
        if(listAdapter.getItem(position).contains("Paired")){
            Object[] o = devicesArray.toArray();
            BluetoothDevice selectedDevice = (BluetoothDevice)o[position];
            ConnectThread connect = new ConnectThread(selectedDevice);
            connect.start();
        }else{
            Toast.makeText(getApplicationContext(), "Device is not paired", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            btAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        private void manageConnectedSocket(BluetoothSocket mmSocket) {
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
