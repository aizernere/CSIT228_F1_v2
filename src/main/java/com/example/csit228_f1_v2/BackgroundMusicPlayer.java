package com.example.csit228_f1_v2;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class BackgroundMusicPlayer {
    private Clip clip;

    public BackgroundMusicPlayer(String musicFilePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(musicFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
            clip = null;
        }
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }
}