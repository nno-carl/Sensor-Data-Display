package com.example.sensordatadisplay;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class DeviceActivity extends FragmentActivity {

    private final static String TAG = DeviceActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private int fragmentn = 0;

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                TextFragment.Connection = "Connected";
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                TextFragment.Connection = "Disconnected";
                clearUI();
                invalidateOptionsMenu();
                Toast connect = Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT);
                connect.setGravity(Gravity.CENTER, 0, 0);
                connect.show();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //display received data in three forms
                String msg = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if (msg != null){
                    //upload(msg);
                    Read_Write.writeData(msg);
                    String[] msgs = msg.split(",");
                    if (fragmentn == 0){
                        displayData(msg);
                    }
                    if (fragmentn == 2){
                        computeAngle(Float.valueOf(msgs[1]), Float.valueOf(msgs[2]), Float.valueOf(msgs[3]), Float.valueOf(msgs[4]));
                    }
                    if (fragmentn == 1){
                        ArrayList angles = new ArrayList<Float>();
                        angles.add(Float.valueOf(msgs[5]));
                        angles.add(Float.valueOf(msgs[6]));
                        angles.add(Float.valueOf(msgs[7]));
                        CurveFragment.dynamicLineChartManager.addEntry(angles);
                        String angle = msgs[5] + msgs[6] + msgs[7];
                    }
                }
            }
        }
    };
    //switch pages
    private final View.OnClickListener TextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment tfragment = new TextFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.textlayout, tfragment).commit();
            fragmentn = 0;
        }
    };

    private final View.OnClickListener TempClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment cfragment = new CurveFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.textlayout, cfragment).commit();
            fragmentn = 1;
        }
    };

    private final View.OnClickListener ModelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment mfragment = new ModelFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.textlayout, mfragment).commit();
            fragmentn = 2;
        }
    };

    private void clearUI() {
        TextFragment.mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_layout);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        TextFragment.mDeviceAddress = mDeviceAddress;

        Button textButton = (Button) findViewById(R.id.text_button);
        textButton.setOnClickListener(TextClickListener);

        Button curveButton = (Button) findViewById(R.id.curve_button);
        curveButton.setOnClickListener(TempClickListener);

        Button modelButton = (Button) findViewById(R.id.model_button);
        modelButton.setOnClickListener(ModelClickListener);


        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        if (fragmentn == 0){
            Fragment tfragment = new TextFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.textlayout, tfragment).commit();
            fragmentn = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                mBluetoothLeService.disconnect();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextFragment.mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            String msgs[] = data.split(",");
            TextFragment.mDataField.setText("temperature =" + msgs[0] + " \n" + "rotation angle: " + msgs[5] + "," + msgs[6] + "," + msgs[7]);
        }
    }
    public void computeAngle(float w, float x, float y, float z) {
        ModelFragment.render.setAngleX(x);
        ModelFragment.render.setAngleY(y);
        ModelFragment.render.setAngleZ(z);
        ModelFragment.render.setW(w);
        ModelFragment.gLView.requestRender();
    }

    // If a given GATT characteristic is selected, check for supported features.  Open 'Notify' features.
    public void OpenData(){
        if (mGattCharacteristics != null) {
            final BluetoothGattCharacteristic characteristic =
                    mGattCharacteristics.get(0).get(0);
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String suuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
        String cuuid = "0000ffe1-0000-1000-8000-00805f9b34fb";
        String uuid = null;
        String ServiceString = "Sensor";
        String CharaString = "Detect";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            if (gattService.getUuid().toString().equals(suuid)) {
                HashMap<String, String> currentServiceData = new HashMap<String, String>();
                uuid = gattService.getUuid().toString();
                currentServiceData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, ServiceString));
                currentServiceData.put(LIST_UUID, uuid);
                gattServiceData.add(currentServiceData);

                ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                        new ArrayList<HashMap<String, String>>();
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> charas =
                        new ArrayList<BluetoothGattCharacteristic>();

                // Loops through available Characteristics.
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    if (gattCharacteristic.getUuid().toString().equals(cuuid)){
                        charas.add(gattCharacteristic);
                        HashMap<String, String> currentCharaData = new HashMap<String, String>();
                        uuid = gattCharacteristic.getUuid().toString();
                        currentCharaData.put(
                                LIST_NAME, SampleGattAttributes.lookup(uuid, CharaString));
                        currentCharaData.put(LIST_UUID, uuid);
                        gattCharacteristicGroupData.add(currentCharaData);
                    }

                }
                mGattCharacteristics.add(charas);
                gattCharacteristicData.add(gattCharacteristicGroupData);
            }

        }

        Toast connect = Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT);
        connect.setGravity(Gravity.CENTER, 0, 0);
        connect.show();
        OpenData();
    }
    //upload the data to Firebase cloud
    public void upload(String sensordata) {
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String date = year + "." + month + "." + day;
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        String time = hour + ":" + minute + ":" + second;
        DocumentReference docRef = db.collection("Users").document(user.getUid()).collection("Sensor Data").document(date);
        Map<String, Object> data = new HashMap<>();
        data.put(time, sensordata);
        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("temp", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("temp", "Error writing document", e);
                    }
                });
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
