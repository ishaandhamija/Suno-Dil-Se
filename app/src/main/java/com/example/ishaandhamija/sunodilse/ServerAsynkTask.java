package com.example.ishaandhamija.sunodilse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


class ServerAsynkTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>> {

    public static ArrayList<String> genre;
    public static ArrayList<String> artist;
    SongsDataBase mDataBase;
    public AsyncResponse delegate = null;
    Context context;

    public ServerAsynkTask(Context context) {
        this.context=context;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Void... params) {
        try {
            if(Looper.myLooper()==null)
                Looper.prepare();

            Log.e("NETWORKKK","doinnnn");
            mDataBase=new SongsDataBase(context);
            Boolean end = false;
            ServerSocket ss = new ServerSocket(12344);
            Log.e("NETWORKKK","server created");
            while(!end){
                try
                {
                    //Server is waiting for client here, if needed
                    Log.e("NETWORKKK","inloop");
                    Socket s = ss.accept();
                    Log.d("NETWORKKK", "connected to client");
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    genre = (ArrayList<String>) ois.readObject();
                    artist= (ArrayList<String>) ois.readObject();
                    Log.e("server",genre.toString());
                    Log.e("doInBackground: ",artist.toString());
                    s.close();
                    for(int i=0;i<genre.size();i++){
                        mDataBase.updateWeightByGenre(genre.get(i));
                    }
                    for(int i=0;i<artist.size();i++){
                        mDataBase.updateWeightByArtist(artist.get(i));
                    }

                    //break;

                }
                catch (Exception e)
                {
                    Log.e("EXXCEE",e.getMessage());
                }
            }
            ss.close();


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("ERROR", "onCreate: "+ e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("ERROR", "onCreate: "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
        delegate.processFinish(arrayLists);
    }

    public static ArrayList<String> getGList(){
        return genre;
    }

    public static ArrayList<String> getAList(){
        return artist;
    }
}