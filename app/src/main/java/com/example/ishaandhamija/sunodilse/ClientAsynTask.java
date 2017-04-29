package com.example.ishaandhamija.sunodilse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

class ClientAsynTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "IP";
    private WeakReference<Context> mContextRef;
    private String ipString;
    private ArrayList<String> genre;
    private ArrayList<String> artist;
    //private ArrayList<S>

    public ClientAsynTask(Context context, ArrayList<String>gen,ArrayList<String>art) {
        mContextRef = new WeakReference<Context>(context);
        genre=gen;
        artist=art;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Context context = mContextRef.get();
            if (context != null) {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);

                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                ipString = Formatter.formatIpAddress(ipAddress);

                Log.d(TAG, "ipString: " + String.valueOf(ipString));
                Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));

                try {
                    Socket s = null;
                    //final WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
                    final DhcpInfo dhcp = wm.getDhcpInfo();
                    final String address = Formatter.formatIpAddress(dhcp.gateway);
                    s = new Socket(address, 12344);

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                    ObjectOutputStream oos = new ObjectOutputStream(bao);
                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(genre);
                    oos.writeObject(artist);
//                    byte[] byteToTransfer = oos;
                    oos.close();

                    /*DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

                    dOut.writeUTF("Hello hello");
                    dOut.flush(); // Send off the data
                    dOut.close();*/
                    //outgoing stream redirect to socket
//                        OutputStream out = s.getOutputStream();

//                        PrintWriter output = new PrintWriter(out);
//                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    //read line(s)
//                        String st = input.readLine();
//                        Log.d("Connection", "onCreate: " + st);
                    //Close connection*/
                    s.close();


                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
        }

        return null;
    }
}