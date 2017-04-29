package com.example.ishaandhamija.sunodilse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DifferentPlaylistsActivity extends AppCompatActivity {
    private static final int HOTSPOT_ACTIVITY = 1234;
    private static final int REQUEST_CODE = 3065;
    ImageView songs, artists, genres, albums,customSongs;
    TextView client,server;
    SongsDataBase mDataBase;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    ServerAsynkTask serverAsynkTask = new ServerAsynkTask(DifferentPlaylistsActivity.this);
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    WifiManager wifi;

    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_playlists);
        wifi=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        mDataBase=new SongsDataBase(this);
        client= (TextView) findViewById(R.id.Client);
        server= (TextView) findViewById(R.id.Server);
        songs = (ImageView) findViewById(R.id.songs);
        artists = (ImageView) findViewById(R.id.artists);
        genres = (ImageView) findViewById(R.id.genres);
        albums = (ImageView) findViewById(R.id.albums);
        customSongs= (ImageView) findViewById(R.id.selected_song);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))== PackageManager.PERMISSION_DENIED && (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(DifferentPlaylistsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else{
            activityStart();
            SQLiteAsyncTask task = new SQLiteAsyncTask(DifferentPlaylistsActivity.this);
            task.execute(getApplicationContext());
        }
    }
    private boolean isHotspotEnable() {
        Method[] wmMethods = wifi.getClass().getDeclaredMethods();
        for (Method method: wmMethods) {
            if (method.getName().equals("isWifiApEnabled")) {

                try {
                    boolean isWifiAPenabled = (boolean) method.invoke(wifi);
                    return isWifiAPenabled;
                } catch (IllegalArgumentException e) {
                    Log.d("state", "onClick: " + e.getMessage());
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d("state", "onClick: " + e.getMessage());
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Log.d("state", "onClick: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void activityStart() {
        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DifferentPlaylistsActivity.this, PlaylistActivity.class);
                i.putExtra("allSongs",1);
                startActivity(i);
            }
        });

        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DifferentPlaylistsActivity.this, CategoryActivity.class);
                i.putExtra("pos",1);
                startActivity(i);
            }
        });

        genres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DifferentPlaylistsActivity.this, CategoryActivity.class);
                i.putExtra("pos",2);
                startActivity(i);
            }
        });

        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DifferentPlaylistsActivity.this, CategoryActivity.class);
                i.putExtra("pos",3);
                startActivity(i);
            }
        });
        customSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DifferentPlaylistsActivity.this,CustomActivity.class);
                intent.putExtra("pos",4);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DifferentPlaylistsActivity.this, "Fab 1", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DifferentPlaylistsActivity.this,CheckListActivity.class));
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isHotspotEnable()){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,HOTSPOT_ACTIVITY);
                }
                else{
//                    serverAsynkTask.delegate = (AsyncResponse) PlaylistActivity.this;
                    //serverAsynkTask.delegate = DifferentPlaylistsActivity.this;
                    serverAsynkTask.execute();
                   // AlgorithmToSelectSongs algorithmToSelectSongs = new AlgorithmToSelectSongs(genres, artists);
//                    finalList = algorithmToSelectSongs.
                    //new ServerAsynkTask().execute();
                    Toast.makeText(DifferentPlaylistsActivity.this, "Server is created", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(DifferentPlaylistsActivity.this, "Fab 2", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                activityStart();
                SQLiteAsyncTask task = new SQLiteAsyncTask(DifferentPlaylistsActivity.this);
                task.execute(getApplicationContext());
            }else{
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        Log.d("DELETE", "onDestroy: in it");
        mDataBase.deleteEntries();
        Log.d("DELETE", "onDestroy: after in it");
        mDataBase.closeDB();
        super.onDestroy();

    }
    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            client.startAnimation(fab_close);
            server.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            server.startAnimation(fab_open);
            client.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;

        }
    }
}
