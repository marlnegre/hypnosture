package com.google.hangouts.hypnosture;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class Fragment_Home extends Fragment {

    Button disconnect;
    TextView display_angle;
    ImageView angle_image;

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

        disconnect.setVisibility(View.GONE);
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
                                    break;
                                case "77":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.b);
                                    break;
                                case "76":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.c);
                                    break;
                                case "75":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.d);
                                    break;
                                case "74":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.e);
                                    break;
                                case "73":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.f);
                                    break;
                                case "72":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.g);
                                    break;
                                case "71":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.h);
                                    break;
                                case "70":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.i);
                                    break;
                                case "69":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.j);
                                    break;
                                case "68":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.k);
                                    break;
                                case "67":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.l);
                                    break;
                                case "66":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.m);
                                    break;
                                case "65":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.n);
                                    break;
                                case "64":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.o);
                                    break;
                                case "63":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.p);
                                    break;
                                case "62":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.q);
                                    break;
                                case "61":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.r);
                                    break;
                                case "60":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.s);
                                    break;
                                case "59":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.t);
                                    break;
                                case "58":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.u);
                                    break;
                                case "57":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.v);
                                    break;
                                case "56":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.w);
                                    break;
                                case "55":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.x);
                                    break;
                                case "54":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.y);
                                    break;
                                case "53":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.z);
                                    break;
                                case "52":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.za);
                                    break;
                                case "51":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zb);
                                    break;
                                case "50":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zc);
                                    break;
                                case "49":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zd);
                                    break;
                                case "48":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.ze);
                                    break;
                                case "47":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zf);
                                    break;
                                case "46":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zg);
                                    break;
                                case "45":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zh);
                                    break;
                                case "44":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zi);
                                    break;
                                case "43":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zj);
                                    break;
                                case "42":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zk);
                                    break;
                                case "41":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zl);
                                    break;
                                case "40":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zm);
                                    break;
                                case "39":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zn);
                                    break;
                                case "38":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zo);
                                    break;
                                case "37":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zp);
                                    break;
                                case "36":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zq);
                                    break;
                                case "35":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zr);
                                    break;
                                case "34":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zs);
                                    break;
                                case "33":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zt);
                                    break;
                                case "32":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zu);
                                    break;
                                case "31":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zv);
                                    break;
                                case "30":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zw);
                                    break;
                                case "29":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zx);
                                    break;
                                case "28":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zy);
                                    break;
                                case "27":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zz);
                                    break;
                                case "26":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zza);
                                    break;
                                case "25":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzb);
                                    break;
                                case "24":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzc);
                                    break;
                                case "23":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzd);
                                    break;
                                case "22":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zze);
                                    break;
                                case "21":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzf);
                                    break;
                                case "20":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzg);
                                    break;
                                case "19":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzh);
                                    break;
                                case "18":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzi);
                                    break;

                                case "79":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzza);
                                    break;
                                case "80":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzb);
                                    break;
                                case "81":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzc);
                                    break;
                                case "82":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzd);
                                    break;
                                case "83":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzze);
                                    break;
                                case "84":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzf);
                                    break;
                                case "85":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzg);
                                    break;
                                case "86":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzh);
                                    break;
                                case "87":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzi);
                                    break;
                                case "88":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzj);
                                    break;
                                case "89":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzk);
                                    break;
                                case "90":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzl);
                                    break;
                                case "91":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzm);
                                    break;
                                case "92":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzn);
                                    break;
                                case "93":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzo);
                                    break;
                                case "94":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzp);
                                    break;
                                case "95":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzq);
                                    break;
                                case "96":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzr);
                                    break;
                                case "97":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzs);
                                    break;
                                case "98":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzt);
                                    break;
                                case "99":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzu);
                                    break;
                                case "100":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzv);
                                    break;
                                case "101":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzw);
                                    break;
                                case "102":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzx);
                                    break;
                                case "103":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzy);
                                    break;
                                case "104":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzz);
                                    break;
                                case "105":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzza);
                                    break;
                                case "106":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzb);
                                    break;
                                case "107":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzc);
                                    break;
                                case "108":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzd);
                                    break;
                                case "109":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzze);
                                    break;
                                case "110":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzf);
                                    break;
                                case "111":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzg);
                                    break;
                                case "112":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzh);
                                    break;
                                case "113":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzi);
                                    break;
                                case "114":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzj);
                                    break;
                                case "115":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzk);
                                    break;
                                case "116":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzl);
                                    break;
                                case "117":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzm);
                                    break;
                                case "118":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzn);
                                    break;
                                case "119":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzo);
                                    break;
                                case "120":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzp);
                                    break;
                                case "121":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzq);
                                    break;
                                case "122":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzr);
                                    break;
                                case "123":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzs);
                                    break;
                                case "124":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzt);
                                    break;
                                case "125":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzu);
                                    break;
                                case "126":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzv);
                                    break;
                                case "127":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzw);
                                    break;
                                case "128":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzx);
                                    break;
                                case "129":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzy);
                                    break;
                                case "130":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzz);
                                    break;
                                case "131":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzza);
                                    break;
                                case "132":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzb);
                                    break;
                                case "133":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzc);
                                    break;
                                case "134":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzd);
                                    break;
                                case "135":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzze);
                                    break;
                                case "136":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzf);
                                    break;
                                case "137":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzg);
                                    break;
                                case "138":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzh);
                                    break;
                                case "139":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzi);
                                    break;
                                case "140":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzj);
                                    break;
                                case "141":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzk);
                                    break;
                                case "142":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzl);
                                    break;
                                case "143":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzm);
                                    break;
                                case "144":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzn);
                                    break;
                                case "145":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzo);
                                    break;
                                case "146":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzp);
                                    break;
                                case "147":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzq);
                                    break;
                                case "148":
                                    angle_image.setVisibility(View.VISIBLE);
                                    angle_image.setImageResource(R.drawable.zzzzzr);
                                    break;
                                default:
                                    break;
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
}