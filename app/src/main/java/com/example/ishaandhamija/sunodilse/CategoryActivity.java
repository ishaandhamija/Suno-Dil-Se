package com.example.ishaandhamija.sunodilse;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView rvList2;
    ArrayList<Genre> feedList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        feedList2 = new ArrayList<>();
        Cursor musicCursor = null;

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] proj1 = {"distinct " + MediaStore.Audio.Media.ARTIST};
//        String[] proj2 = {"distinct" + MediaStore.Audio.Media.ALBUM};
        String[] proj2 = {"distinct " + MediaStore.Audio.Media.ALBUM};
        String[] proj3 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};


        if (getIntent().getIntExtra("pos", 0) == 0) {
            musicCursor = musicResolver.query(musicUri, null, null, null, null);
        }
        if (getIntent().getIntExtra("pos", 0) == 1){
            musicCursor = musicResolver.query(musicUri, proj1, null, null, null);
//            musicCursor = managedQuery(musicUri, null, null, null, null);
        }
        if (getIntent().getIntExtra("pos", 0) == 2) {
//            musicCursor = musicResolver.query(musicUri, null, null, null, null);
            musicCursor = managedQuery(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,proj3,null,null,null);
        }
        if (getIntent().getIntExtra("pos", 0) == 3){
            musicCursor = musicResolver.query(musicUri, proj2, null, null, null);
//            musicCursor = managedQuery(musicUri, null, null, null, null);
        }

//       Cursor musicCursor = musicResolver.query(musicUri, proj1, null, new String[]{""=""}, null);

//        Cursor musicCursor = musicResolver.query(musicUri, proj1, null, new String[]{""=""}, null);

        if(musicCursor == null){
            Toast.makeText(this, "No Songs", Toast.LENGTH_SHORT).show();
            Log.d("", "getSongList: No Songs");
        }

//        if (getIntent().getIntExtra("pos", 0) == 1) {
//            if (musicCursor.moveToFirst()) {
//                do {
//                    Log.d("Hello", "getSongList: " + musicCursor.getString(0));
//                    feedList2.add(new Genre(musicCursor.getString(0),null));
//                } while (musicCursor.moveToNext());
//            } else {
//                Log.d("Hello", "getSongList: No Artists");
//            }
//        }
//
//        else if (getIntent().getIntExtra("pos", 0) == 1) {
//            if (musicCursor.moveToFirst()) {
//                do {
////                Log.d("Hello", "getSongList: " + musicCursor.getString(0));
//                    int index = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//                    int y = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
////                    long genreId = Long.parseLong(musicCursor.getString(y));
//                    long genreId = Long.parseLong(musicCursor.getString(y));
//                    Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
////                    Uri uri = MediaStore.Audio.Media.getContentUri("external");
////                feedList2.add(musicCursor.getString(index));
//                    feedList2.add(new Genre(musicCursor.getString(index), uri));
//                } while (musicCursor.moveToNext());
//            } else {
//                Log.d("Hello", "getSongList: No Artists");
//            }
//        }


        if (getIntent().getIntExtra("pos", 0) == 1) {
            if (musicCursor.moveToFirst()) {
                do {
                    Log.d("Hello", "getSongList: " + musicCursor.getString(0));
                    feedList2.add(new Genre(musicCursor.getString(0),null));
                } while (musicCursor.moveToNext());
            } else {
                Log.d("Hello", "getSongList: No Artists");
            }
        }

        if (getIntent().getIntExtra("pos", 0) == 3) {
            if (musicCursor.moveToFirst()) {
                do {
                    Log.d("Hello", "getSongList: " + musicCursor.getString(0));
                    feedList2.add(new Genre(musicCursor.getString(0),null));
                } while (musicCursor.moveToNext());
            } else {
                Log.d("Hello", "getSongList: No Artists");
            }
        }
        else if (getIntent().getIntExtra("pos", 0) == 2) {
            if (musicCursor.moveToFirst()) {
                do {
//                Log.d("Hello", "getSongList: " + musicCursor.getString(0));
                    int index = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                    int y = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                    long genreId = Long.parseLong(musicCursor.getString(y));
                    Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
//                feedList2.add(musicCursor.getString(index));
                    feedList2.add(new Genre(musicCursor.getString(index), uri));
                } while (musicCursor.moveToNext());
            } else {
                Log.d("Hello", "getSongList: No Artists");
            }
        }

        rvList2 = (RecyclerView) findViewById(R.id.rvList2);
        FeedAdapter feedAdapter = new FeedAdapter();

        rvList2.setLayoutManager(new LinearLayoutManager(this));

        rvList2.setAdapter(feedAdapter);

    }

    class FeedHolder extends RecyclerView.ViewHolder{

        TextView catName;
        View meraView;

        public FeedHolder(View itemView) {
            super(itemView);

            this.catName = (TextView) itemView.findViewById(R.id.catName);
            this.meraView = itemView;
        }
    }

    class FeedAdapter extends RecyclerView.Adapter<FeedHolder>{

        @Override
        public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View itemView = li.inflate(R.layout.smallview3, parent, false);

            return new FeedHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final FeedHolder holder, final int position) {

            final String feed = feedList2.get(position).getName();

            holder.catName.setText(feed.toString());
            holder.meraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CategoryActivity.this,PlaylistActivity.class);
//                    if (getIntent().getIntExtra("pos",0) == 1) {
//                        i.putExtra("artist", feed.toString());
//                    }
//                    else if (getIntent().getIntExtra("pos",0) == 2) {
//                        i.putExtra("album", feed.toString());
//                    }
                    i.putExtra("pos",getIntent().getIntExtra("pos",0));
                    if (getIntent().getIntExtra("pos",0) == 1){
                        i.putExtra("artist",feedList2.get(position).getName());
                    }
                    if (getIntent().getIntExtra("pos",0) == 2){
                        i.putExtra("uri",feedList2.get(position).getUri());
                    }
                    if (getIntent().getIntExtra("pos",0) == 3){
                        i.putExtra("album",feedList2.get(position).getName());
                    }
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return feedList2.size();
        }
    }
}
