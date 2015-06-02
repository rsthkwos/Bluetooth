package com.rd2.tw.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
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

        /*
        Button btn = (Button)v;
        btn.setText(mBluetoothAdapter.isEnabled() ?"11":"22");
        */
    }

//==============================================================================================
}//activityEnd
