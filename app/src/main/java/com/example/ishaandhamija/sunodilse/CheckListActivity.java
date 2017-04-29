package com.example.ishaandhamija.sunodilse;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckListActivity extends AppCompatActivity {

    RecyclerView rvList3;
    ArrayList<Check> feedList3;
    public static ArrayList<String> genreList;
    ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        next = (ImageView) findViewById(R.id.next);

        String[] proj3 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};

        feedList3 = new ArrayList<>();

        Cursor musicCursor = managedQuery(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,proj3,null,null,null);
        if ((musicCursor!=null) &&(musicCursor.moveToFirst())) {
            do {
                int index = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                int y = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                long genreId = Long.parseLong(musicCursor.getString(y));
                Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
                feedList3.add(new Check(musicCursor.getString(index),false));
            } while (musicCursor.moveToNext());
        }
        else {
            Log.d("Hello", "getSongList: No Genres");
        }

        rvList3 = (RecyclerView) findViewById(R.id.rvList3);
        FeedAdapter feedAdapter = new FeedAdapter();

        rvList3.setLayoutManager(new LinearLayoutManager(this));

        rvList3.setAdapter(feedAdapter);

        genreList = new ArrayList<>();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<feedList3.size();i++){
                    if (feedList3.get(i).isTicked() == true){
                        genreList.add(feedList3.get(i).getName());
                    }
                }
                startActivity(new Intent(CheckListActivity.this, CheckListActivity2.class));
            }
        });
    }

    class FeedHolder extends RecyclerView.ViewHolder{

        TextView genreName;
        CheckBox ticked;

        public FeedHolder(View itemView) {
            super(itemView);

            this.genreName = (TextView) itemView.findViewById(R.id.genreName);
            this.ticked = (CheckBox) itemView.findViewById(R.id.ticked);

        }
    }

    class FeedAdapter extends RecyclerView.Adapter<FeedHolder>{

        @Override
        public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View itemView = li.inflate(R.layout.smallview2, parent, false);

            return new FeedHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final FeedHolder holder, int position) {

            final Check feed = feedList3.get(position);

            holder.genreName.setText(feed.getName());
            holder.ticked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    feed.setTicked(isChecked);
                }
            });

        }

        @Override
        public int getItemCount() {
            return feedList3.size();
        }
    }

    public static ArrayList<String> getGenreList(){
        return genreList;
    }
}
