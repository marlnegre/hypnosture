package com.google.hangouts.hypnosture;

/**
 * Created by cath on 2/19/18.
 */
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class Activity_Bluetooth extends AppCompatActivity {

    Button show_paired;
    ListView paired_list;

    public static Boolean selected = false;
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private OutputStream outStream = null;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bluetooth);

        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        setBackBtn();

        refs();

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //show message that device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device not Available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (myBluetooth.isEnabled()) {

            } else {
                //Ask user to turn the bluetooth on
                Intent turnBTOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTOn, 1);
            }
        }

        show_paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device name and address
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired bluetooth devices found.", Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        paired_list.setAdapter(adapter);
        paired_list.setOnItemClickListener(myListClickListener); ////Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            //Make an intent to start next activity.
            selected = true;
            Intent in = new Intent(Activity_Bluetooth.this, Activity_Homescreen.class);
            //Change Activity
            in.putExtra(EXTRA_ADDRESS, address); //this will received at DisplayData (class) Activity
            startActivity(in);

        }
    };

    public void refs() {
        show_paired = (Button) findViewById(R.id.show_paired_button);
        paired_list = (ListView) findViewById(R.id.bluetooth_list);
    }

    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}