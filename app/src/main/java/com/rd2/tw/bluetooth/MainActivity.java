package com.rd2.tw.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

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

    //==============================================================================================
    //判斷android的藍芽是否有開啟，或者可不可以用，以及利用該物件去搜尋裝置
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   //用來搜尋、管理藍芽裝置
    private static BluetoothSocket mBluetoothSocket = null;     //用來連結藍芽裝置、以及傳送指令
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static OutputStream mOutputStream = null;
    private final int REQUEST_ENABLE_BT = 1;

    private ArrayList<String> mArrayAdapter;
    private BroadcastReceiver mReceiver;

    public void buttonOnClick(View v) {

        //如果裝置不支援bluetooth
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        //如果bluetooth沒有開啟
        if (!mBluetoothAdapter.isEnabled()) {
            //發出一個intent去開啟bluetooth
            Intent mIntentOpenBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mIntentOpenBT, REQUEST_ENABLE_BT);
        }

        //取得目前已經配對過的裝置
        Set<BluetoothDevice> setPairedDevices = mBluetoothAdapter.getBondedDevices();

        //如果已經有配對過的裝置
        if (setPairedDevices.size() > 0) {
            // 把裝置名稱以及MAC Address印出來
            for (BluetoothDevice device : setPairedDevices) {
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        //註冊一個BroadcastReceiver，主要是用來接收【搜尋到附近藍芽裝置】的訊息
        // 註冊一個 BroadcastReceiver，等等會用來接收搜尋到裝置的消息
        IntentFilter filter = new IntentFilter((BluetoothDevice.ACTION_FOUND));
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery(); //開始搜尋裝置

        //宣告上段code使用到的BroadcastReceiver
        final BroadcastReceiver mReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                //當收尋到裝置時
                if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                    //取得藍芽裝置這個物件
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //判斷那個裝置是不是你要連結的裝置，根據藍芽裝置名稱判斷
                    if(device.getName().equals("MY_DEVICE_ID")){
                        try{
                            //一進來一定要停止搜尋
                            mBluetoothAdapter.cancelDiscovery();
                            //連結到該裝置
                            mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                            mBluetoothSocket.connect();
                            //取得outputstream
                            mOutputStream = mBluetoothSocket.getOutputStream();
                            //送出訊息
                            String message = "hello";
                            mOutputStream.write(message.getBytes());
                        }catch(IOException e){

                        }
                    }
                }
            }
        };

        /*
        Button btn = (Button)v;
        btn.setText(mBluetoothAdapter.isEnabled() ?"11":"22");
        */
    }

//==============================================================================================
}//activityEnd
