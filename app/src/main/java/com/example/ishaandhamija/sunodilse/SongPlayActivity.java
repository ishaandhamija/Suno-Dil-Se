package com.example.ishaandhamija.sunodilse;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

public class SongPlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBarCompat.OnSeekBarChangeListener {

    ImageView play,rewind,forward,shuffle,loop;
    TextView songName, songArtist, currTime, endTime;
    ImageView songImage;
    SeekBarCompat seekbar;
    static MediaPlayer mp;
    Handler mHandler = new Handler();;
    Utilities utils;
    int seekForwardTime = 5000;
    int seekBackwardTime = 5000;
    int currentSongIndex = 0;
    boolean isShuffle = false;
    boolean isRepeat = false;
    static ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_play);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        play = (ImageView) findViewById(R.id.play);
        rewind = (ImageView) findViewById(R.id.rewind);
        forward = (ImageView) findViewById(R.id.forward);
        shuffle = (ImageView) findViewById(R.id.shuffle);
        loop = (ImageView) findViewById(R.id.loop);
        songImage = (ImageView) findViewById(R.id.songImage);
        songName = (TextView) findViewById(R.id.songName);
        songArtist = (TextView) findViewById(R.id.songArtist);
        currTime = (TextView) findViewById(R.id.currTime);
        endTime = (TextView) findViewById(R.id.endTime);
        seekbar = (SeekBarCompat) findViewById(R.id.seekbar);
        songImage = (ImageView) findViewById(R.id.songImage);

        utils = new Utilities();
        mp = ((PlaylistActivity)getParent()).getMPlayer();

        seekbar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);

        songs = ((PlaylistActivity)getParent()).getSongs();

        try {
            playSong(getIntent().getIntExtra("position",0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        songImage.setOnTouchListener(new OnSwipeTouchListener(SongPlayActivity.this){
            public void onSwipeRight() throws IOException {
                if(isRepeat){
                    try {
                        playSong(currentSongIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(isShuffle){
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt((songs.size() - 1) - 0 + 1) + 0;
                    try {
                        playSong(currentSongIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    if(currentSongIndex > 0){
                        try {
                            playSong(currentSongIndex - 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentSongIndex = currentSongIndex - 1;
                    }else{
                        try {
                            playSong(songs.size() - 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentSongIndex = songs.size() - 1;
                    }
                }
            }
            public void onSwipeLeft() throws IOException {
                if(isRepeat){
                    try {
                        playSong(currentSongIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(isShuffle){
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt((songs.size() - 1) - 0 + 1) + 0;
                    try {
                        playSong(currentSongIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    if(currentSongIndex < (songs.size() - 1)){
                        try {
                            playSong(currentSongIndex + 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentSongIndex = currentSongIndex + 1;
                    }else{
                        try {
                            playSong(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentSongIndex = 0;
                    }
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mp.getCurrentPosition();
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mp.getCurrentPosition();
                if(currentPosition - seekBackwardTime >= 0){
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    mp.seekTo(0);
                }
            }
        });

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRepeat){
                    isRepeat = false;
                    loop.setImageResource(R.mipmap.ic_launcher_loop1);
                }else{
                    isRepeat = true;
                    isShuffle = false;
                    loop.setImageResource(R.mipmap.ic_launcher_loop2);
                    shuffle.setImageResource(R.mipmap.ic_launcher_shuffle1);
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShuffle){
                    isShuffle = false;
                    shuffle.setImageResource(R.mipmap.ic_launcher_shuffle1);
                }else{
                    isShuffle= true;
                    isRepeat = false;
                    shuffle.setImageResource(R.mipmap.ic_launcher_shuffle2);
                    loop.setImageResource(R.mipmap.ic_launcher_loop1);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        play.setImageResource(R.mipmap.ic_launcher_play1);
                    }
                }else{
                    if(mp!=null){
                        mp.start();
                        play.setImageResource(R.mipmap.ic_launcher_pause4);
                    }
                }
            }
        });

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(isRepeat){
            try {
                playSong(currentSongIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(isShuffle){
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songs.size() - 1) - 0 + 1) + 0;
            try {
                playSong(currentSongIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            if(currentSongIndex < (songs.size() - 1)){
                try {
                    playSong(currentSongIndex + 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentSongIndex = currentSongIndex + 1;
            }else{
                try {
                    playSong(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentSongIndex = 0;
            }
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        mp.seekTo(currentPosition);
        updateSeekbar();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            endTime.setText(""+utils.milliSecondsToTimer(totalDuration));
            currTime.setText(""+utils.milliSecondsToTimer(currentDuration));

            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            seekbar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };


    public void playSong(int pos) throws IOException {


        play.setImageResource(R.mipmap.ic_launcher_pause4);

        long currSong = songs.get(pos).getId();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        mp.reset();
        mp.setDataSource(getApplicationContext(),trackUri);
        mp.prepare();
        mp.start();

        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this,trackUri);
        byte [] data = mmr.getEmbeddedPicture();
        if (data != null){
            bitmap = BitmapFactory.decodeByteArray(data, 0 ,data.length);
        }

        songName.setText(songs.get(pos).getTitle());
        songArtist.setText(songs.get(pos).getArtist());
        if (bitmap != null) {
            songImage.setImageBitmap(bitmap);
        }
        else{
            songImage.setImageResource(R.drawable.ddi);
        }
        seekbar.setProgress(0);
        seekbar.setMax(100);

        updateSeekbar();

    }

    public void updateSeekbar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public class Utilities {

        public String milliSecondsToTimer(long milliseconds){
            String finalTimerString = "";
            String secondsString = "";

            int hours = (int)( milliseconds / (1000*60*60));
            int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
            int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
            if(hours > 0){
                finalTimerString = hours + ":";
            }

            if(seconds < 10){
                secondsString = "0" + seconds;
            }else{
                secondsString = "" + seconds;}

            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            return finalTimerString;
        }

        public int getProgressPercentage(long currentDuration, long totalDuration){
            Double percentage = (double) 0;

            long currentSeconds = (int) (currentDuration / 1000);
            long totalSeconds = (int) (totalDuration / 1000);

            percentage =(((double)currentSeconds)/totalSeconds)*100;

            return percentage.intValue();
        }

        public int progressToTimer(int progress, int totalDuration) {
            int currentDuration = 0;
            totalDuration = (int) (totalDuration / 1000);
            currentDuration = (int) ((((double)progress) / 100) * totalDuration);

            return currentDuration * 1000;
        }
    }

}
