package com.example.milkywaymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

        private TextView textView;
        private ImageView playImage,nextImage,prevImage,logoImage;
        SeekBar seekBar;
        ArrayList<File> songsList;
        MediaPlayer mediaPlayer;
        String textContent;
        int position;
        int songPosition = 0;
        Thread updateSeekbar;

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        //mediaPlayer.release();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        textView = findViewById(R.id.textView);
        playImage = findViewById(R.id.play);
        nextImage = findViewById(R.id.next);
        prevImage = findViewById(R.id.prev);
        //logoImage = findViewById(R.id.logo);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songsList =(ArrayList) bundle.getParcelableArrayList("songlist");

        textContent = intent.getStringExtra("currentsong");
        textView.setText(textContent);
        textView.setSelected(true);

        position = intent.getIntExtra("position" , 0);
        Uri uri = Uri.parse(songsList.get(position).toString());
        mediaPlayer = MediaPlayer.create(this ,uri);
        mediaPlayer.start();
        playImage.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                super.run();
                int currentPosition = 0;
                try {
                    while(currentPosition < mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeekbar.start();

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    playImage.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    playImage.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                if(songPosition < songsList.size() - 1){
                    songPosition++;
                }
                else {
                    songPosition = 0;
                }
                    mediaPlayer = MediaPlayer.create(getApplicationContext() , Uri.fromFile(songsList.get(songPosition)));
                    textContent = songsList.get(songPosition).getName();
                    textView.setText(textContent);
                    mediaPlayer.start();
                    playImage.setImageResource(R.drawable.pause);

            }
        });

        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if(songPosition > 0){
                    songPosition--;
                }
                else {
                    songPosition = songsList.size() - 1;
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext() , Uri.fromFile(songsList.get(songPosition)));
                textContent = songsList.get(songPosition).getName();
                textView.setText(textContent);
                mediaPlayer.start();
                playImage.setImageResource(R.drawable.pause);
            }
        });
    }
}