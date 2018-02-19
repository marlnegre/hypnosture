package com.google.hangouts.hypnosture;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Fragment_Home extends Fragment {

    Button disconnect;
    TextView display_angle;
    Handler bluetoothIn;

    private int bytes_in;
    final int handlerState = 0;     //used to identify handler message
    String TAG = "DisplayData";
    private StringBuilder recDataString = new StringBuilder();
    private int counter = 0;
    private ConnectedThread mConnectedThread;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Handler mHandler;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disconnect = view.findViewById(R.id.btnDisconnect);
        display_angle = view.findViewById(R.id.angle);

        disconnect.setVisibility(View.GONE);
        disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(Activity_Bluetooth.selected) {
            //Retrieve address from the bluetooth device
            Intent newInt = getActivity().getIntent();
            address = newInt.getStringExtra(Activity_Bluetooth.EXTRA_ADDRESS);



            new ConnectBt().execute();
        }
    }
    private void startThreadConnected(BluetoothSocket socket){

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    private class ConnectBt extends AsyncTask<Void, Void, Void> //UI Thread
    {
        private boolean ConnectSuccess = true; //If it's here, it's almost connected

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(Fragment_Home.this.getActivity(), "Connecting...", "Please wait!!!");//Show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //While the progress dialog is shown, the connection is done in background
        {
            try
            {
                if(btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter(); //get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address); //connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect(); //start connection


                }
            }
            catch (IOException e){
                ConnectSuccess = false; //If the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                getActivity().finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
                startThreadConnected(btSocket);
                disconnect.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
        }

    }
    private void msg(String s)
    {
        Toast.makeText(getActivity().getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        getActivity().finish(); //return to the first layout
    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            display_angle.setText("1");
            try {
                //Create I/O streams for connection
                if (this.mmSocket.isConnected()) {
                    Log.i(Fragment_Home.this.TAG, "getInputStream");
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes = 0;
            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    final String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            display_angle.append(readMessage);
                            display_angle.setText(readMessage);
                            //textByteCnt.append(strByteCnt);
                        }});
                } catch (IOException e) {
                    break;
                }
            }
        }


        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getActivity().getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                getActivity().finish();

            }
        }
    }

}