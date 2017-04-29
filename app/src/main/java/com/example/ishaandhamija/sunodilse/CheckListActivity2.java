package com.example.ishaandhamija.sunodilse;

import android.content.ContentResolver;
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
import android.widget.Toast;

import java.util.ArrayList;

public class CheckListActivity2 extends AppCompatActivity {

    RecyclerView rvList4;
    ArrayList<Check> feedList4;
    public static ArrayList<String> artistList;
    public static ArrayList<String> genreList;
    ImageView next2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list2);

        String[] proj1 = {"distinct " + MediaStore.Audio.Media.ARTIST};
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver musicResolver = getContentResolver();

        next2 = (ImageView) findViewById(R.id.next2);

        Cursor musicCursor = musicResolver.query(musicUri, proj1, null, null, null);

        feedList4 = new ArrayList<>();

        if ((musicCursor!=null) &&(musicCursor.moveToFirst())) {
            do {
                feedList4.add(new Check(musicCursor.getString(0),false));
            } while (musicCursor.moveToNext());
        } else {
            Log.d("Hello", "getSongList: No Artists");
        }

        rvList4 = (RecyclerView) findViewById(R.id.rvList4);
        FeedAdapter feedAdapter = new FeedAdapter();

        rvList4.setLayoutManager(new LinearLayoutManager(this));

        rvList4.setAdapter(feedAdapter);

        artistList = new ArrayList<>();

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<feedList4.size();i++){
                    if (feedList4.get(i).isTicked() == true){
                        artistList.add(feedList4.get(i).getName());
                    }
                }
                Toast.makeText(CheckListActivity2.this, "Done", Toast.LENGTH_SHORT).show();
                new ClientAsynTask(CheckListActivity2.this,genreList,artistList).execute();
                for (int i=0;i<genreList.size();i++){
                    Log.d("DD", "onClick: " + genreList.get(i));
                }
                for (int i=0;i<artistList.size();i++){
                    Log.d("DD", "onClick: " + artistList.get(i));
                }
            }
        });

        genreList = ((CheckListActivity)getParent()).getGenreList();
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

            final Check feed = feedList4.get(position);

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
            return feedList4.size();
        }
    }

    static public ArrayList<String> getArtistList(){
        return artistList;
    }
}
