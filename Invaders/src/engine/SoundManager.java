package engine;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import javax.sound.sampled.*;
import javax.swing.*;
public class SoundManager {
    public SoundManager() {
    }
    public void playBGM(){
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    System.out.println("try music");
                    URL url = this.getClass().getResource("/Sounds/BGM.wav");
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
