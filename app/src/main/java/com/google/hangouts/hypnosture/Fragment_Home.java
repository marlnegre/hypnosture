package com.google.hangouts.hypnosture;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.hangouts.hypnosture.model.Statistics;
import com.google.hangouts.hypnosture.model.statistics_helper.StatisticsHelper;
import com.google.hangouts.hypnosture.util.Helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Fragment_Home extends Fragment {

    Button disconnect;
    TextView display_angle;
    ImageView angle_image, emoticon;
    RelativeLayout rootContent;
    ScreenShot ss = new ScreenShot();
    Boolean done = false;
    int counter = 0;
    int pic = 0;

    private String angle;
    String TAG = "Fragment_Home";
    private ConnectedThread mConnectedThread;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static HashMap<String, Drawable> mDrawables = new HashMap<String, Drawable>();

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
        angle_image = view.findViewById(R.id.imageView2);
        rootContent = view.findViewById(R.id.root_content);
        emoticon = view.findViewById(R.id.emoticons);

        disconnect.setVisibility(View.INVISIBLE);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect(); //close connection
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Activity_Bluetooth.selected) {
            //Retrieve address from the bluetooth device
            Intent newInt = getActivity().getIntent();
            address = newInt.getStringExtra(Activity_Bluetooth.EXTRA_ADDRESS);


            new ConnectBt().execute();

        }
        else {
            Toast.makeText(getActivity(),"Please Connect to Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    private void startThreadConnected(BluetoothSocket socket) {

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    private class ConnectBt extends AsyncTask<Void, Void, Void> //UI Thread
    {
        private boolean ConnectSuccess = true; //If it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Fragment_Home.this.getActivity(), "Connecting...", "Please wait!!!");//Show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //While the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter(); //get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address); //connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect(); //start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false; //If the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                getActivity().finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
                startThreadConnected(btSocket);


                disconnect.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
        }

    }

    private void msg(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
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
            //display_angle.setText("1");
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
                    angle = readMessage;
                    // Send the obtained bytes to the UI Activity via handler
                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            display_angle.append(readMessage);
                            display_angle.setText(readMessage);

                            switch (readMessage) {
                                case "78":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.a);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "77":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.b);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "76":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.c);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "75":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.d);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "74":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.e);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "73":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.f);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "72":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.g);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "71":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.h);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "70":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.i);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "69":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.j);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "68":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.k);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "67":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.l);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "66":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.m);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "65":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.n);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "64":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.o);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "63":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.p);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "62":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.q);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "61":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.r);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "60":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.s);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "59":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.t);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "58":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.u);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "57":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.v);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "56":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.w);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "55":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.x);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "54":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.y);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "53":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.z);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "52":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.za);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "51":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zb);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "50":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zc);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "49":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zd);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "48":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.ze);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "47":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zf);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "46":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zg);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "45":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zh);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "44":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zi);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "43":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zj);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "42":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zk);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "41":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zl);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "40":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zm);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "39":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zn);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "38":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zo);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "37":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zp);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "36":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zq);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "35":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zr);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "34":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zs);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "33":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zt);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "32":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zu);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "31":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zv);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "30":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zw);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "29":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zx);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "28":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zy);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "27":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zz);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "26":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zza);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "25":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzb);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "24":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzc);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "23":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzd);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);

                                    break;
                                case "22":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zze);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "21":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzf);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "20":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzg);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "19":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzh);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "18":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzi);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;

                                case "79":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzza);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "80":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzb);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_blushedcheeks);
                                    break;
                                case "81":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzc);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "82":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzd);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "83":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzze);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "84":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzf);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "85":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzg);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "86":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzh);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "87":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzi);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "88":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzj);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "89":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzk);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "90":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzl);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "91":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzm);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "92":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzn);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "93":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzo);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "94":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzp);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "95":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzq);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_neutral);
                                    break;
                                case "96":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzr);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "97":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzs);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "98":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzt);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "99":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzu);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "100":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzv);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "101":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzw);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "102":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzx);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "103":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzy);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "104":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzz);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "105":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzza);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "106":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzb);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "107":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzc);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "108":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzd);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "109":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzze);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "110":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzf);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "111":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzg);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "112":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzh);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "113":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzi);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "114":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzj);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "115":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzk);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "116":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzl);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "117":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzm);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "118":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzn);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "119":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzo);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "120":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzp);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_disappointed);
                                    break;
                                case "121":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzq);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "122":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzr);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "123":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzs);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "124":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzt);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "125":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzu);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "126":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzv);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "127":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzw);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "128":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzx);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "129":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzy);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "130":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzz);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "131":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzza);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "132":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzb);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "133":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzc);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "134":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzd);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "135":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzze);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "136":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzf);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "137":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzg);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "138":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzh);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "139":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzi);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "140":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzj);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "141":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzk);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "142":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzl);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "143":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzm);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "144":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzn);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "145":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzo);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "146":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzp);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "147":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzq);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                case "148":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzr);
                                    emoticon.setVisibility(View.VISIBLE);
                                    emoticon.setImageResource(R.drawable.emoticon_sad);
                                    break;
                                default:
                                    break;
                            }

                            int angled = Integer.parseInt(display_angle.getText().toString());

                            // static pa ni. Time sa. para ni sa delay sa settings. daghan ma affected.

                            //For Screenshot
                            if(angled < 55 || angled > 96){
                                if(counter == 5){
                                    takeScreenshot(ScreenshotType.FULL);
                                }
                                counter++;
                            }
                            else
                            {
                                counter = 0;
                            }
                        }
                    });
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

    public void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        switch (screenshotType) {
            case FULL:
                //If Screenshot type is FULL take full page screenshot i.e our root content.
                b = com.google.hangouts.hypnosture.ScreenshotUtils.getScreenShot(rootContent);
                break;
            case CUSTOM:
                //If Screenshot type is CUSTOM

                //fullPageScreenshot.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
//                hiddenText.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of hidden text

                b = com.google.hangouts.hypnosture.ScreenshotUtils.getScreenShot(rootContent);

                //After taking screenshot reset the button and view again
                //fullPageScreenshot.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of first button again
//                hiddenText.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of hidden text

                //NOTE:  You need to use visibility INVISIBLE instead of GONE to remove the view from frame else it wont consider the view in frame and you will not get screenshot as you required.
                break;
        }

        //If bitmap is not null
        if (b != null) {
            //showScreenShotImage(b);//show bitmap over imageview


            sendNotification();

            File saveFile = com.google.hangouts.hypnosture.ScreenshotUtils.getMainDirectoryName(getActivity());//get the path to save screenshot
            File file = com.google.hangouts.hypnosture.ScreenshotUtils.store(b, "ImproperPosture" + pic + ".jpg", saveFile);//save the screenshot to selected path
            //shareScreenshot(file); //finally share screenshot
            pic++;
            
            StatisticsHelper.writeNewStatistics(1);
        } else
            //If bitmap is null show toast message
            Toast.makeText(getActivity(), R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();

    }

    public void sendNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Hypnosture")
                        .setContentText("improper posture");

        int notificationId = 101;

        NotificationManager notifyMgr =
                (NotificationManager)
                        getActivity().getSystemService(NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }
}