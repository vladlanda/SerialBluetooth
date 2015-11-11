package vld.serialbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity implements BluetoothSPP.OnDataReceivedListener,BluetoothSPP.BluetoothConnectionListener{
    private BluetoothSPP bt;
    private final String STATUS = "Status : ";
    private TextView statusTv;
    //private View pannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTv = (TextView)findViewById(R.id.status_tv);
        //pannel = (View)findViewById(R.id.pannel);

        bt = new BluetoothSPP(this);
        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        bt.setOnDataReceivedListener(this);
        bt.setBluetoothConnectionListener(this);
        bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);

    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        /*
        * Do whatever you need here.
        * */
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        statusTv.setText(STATUS+"Connected to : "+name);
    }

    @Override
    public void onDeviceDisconnected() {
        statusTv.setText(STATUS + "Disconnected!");
    }

    @Override
    public void onDeviceConnectionFailed() {
        statusTv.setText(STATUS + "Fail to connect!");
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                //setup();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        }else{
            finish();
        }
    }

/*    public void connectClick(View v){
        bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        statusTv.setText(STATUS + "Not Connected!");
    }*/
}
