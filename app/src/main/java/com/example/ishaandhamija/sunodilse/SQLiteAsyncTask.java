package com.example.ishaandhamija.sunodilse;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ishaandhamija on 21/04/17.
 */

public class SQLiteAsyncTask extends AsyncTask<Context, Void, ArrayList<WeightedSongs>> {

    ArrayList<WeightedSongs> songList  = new ArrayList<>();
    SongsDataBase db;
    Context context;

    public SQLiteAsyncTask(Context context) {
        this.context=context;
    }

    @Override
    protected ArrayList<WeightedSongs> doInBackground(Context... params) {

        Log.d("ASY", "doInBackground: in asynk");

        ContentResolver musicResolver = params[0].getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        db=new SongsDataBase(params[0]);
        String[] genresProjection = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor == null) {
            Log.d("", "getSongList: No Songs");
        }

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int genreColumn = musicCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
            Cursor genresCursor = musicResolver.query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,genresProjection,null,null,null);
            genresCursor.moveToFirst();

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
//                String thisGenre = musicCursor.getString(genreColumn);
                String thisGenre = genresCursor.getString(0);
                Log.d("ASY", "doInBackground: "+thisId);
                Log.d("ASY", "doInBackground:" +thisGenre);
                db.insertData(thisId,thisTitle,thisArtist,null);

                songList.add(new WeightedSongs(thisId, thisTitle, thisArtist));
            } while (musicCursor.moveToNext());

//            genresCursor.moveToFirst();
//            do{
//                String thisGenre = genresCursor.getString(0);
//                db.insertGenre(String.valueOf(musicCursor.getLong(idColumn)),thisGenre);
//            }while (genresCursor.moveToFirst());
        }
        return songList;
    }

    @Override
    protected void onPostExecute(ArrayList<WeightedSongs> weightedSongses) {
        super.onPostExecute(weightedSongses);
    }

}
