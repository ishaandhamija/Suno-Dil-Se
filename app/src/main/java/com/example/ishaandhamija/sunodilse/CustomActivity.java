package com.example.ishaandhamija.sunodilse;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class CustomActivity extends AppCompatActivity {
    SongsDataBase dataBase;
    public static ArrayList<Song> customSongsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        customSongsList=new ArrayList<>();
        dataBase=new SongsDataBase(CustomActivity.this);
        Cursor ans=dataBase.getSortedData();
        if (ans.moveToFirst()) {
            do {
                Log.d("DATA", "onCreate: " + ans.getString(0));
                Log.d("DATA", "onCreate: " + ans.getString(1));
                Log.d("DATA", "onCreate: " + ans.getInt(4));
                Log.d("DATA", "onCreate: " + ans.getString(3));
                customSongsList.add(new Song(Long.valueOf(ans.getString(0)),ans.getString(1),ans.getString(2),null));
            } while (ans.moveToNext());
        }
        Intent intent=new Intent(CustomActivity.this,PlaylistActivity.class);
        intent.putExtra("CUSTOM",4);
        startActivity(intent);
        finish();
    }

    public static ArrayList<Song> getCustomSongsList(){
        return customSongsList;
    }
}
