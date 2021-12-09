package engine;

//import jdk.javadoc.internal.doclets.formats.html.markup.Head;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import javax.sound.sampled.*;
import javax.swing.*;
public class SoundManager {
    public static final SoundManager instance = new SoundManager();
    public enum eSFX{
        eGetItem,
        eBullet,
        eDamage,
        eDie,
        eBumb,
        eHeadShot,
        eGameOver,
        eStageClear
    };
    private String BGM="/Sounds/BGM.wav";
    private String GetItem = "/Sounds/SFX/GetItem.wav";
    private String Bullet = "/Sounds/SFX/Bullet.wav";
    private String Damage = "/Sounds/SFX/Damage.wav";
    private String Die = "/Sounds/SFX/Die.wav";
    private String Bumb = "/Sounds/SFX/Bumb.wav";
    private String HeadShot = "/Sounds/SFX/HeadShot.wav";
    private String GameOver = "/Sounds/SFX/GameOver.wav";
    private String StageClear = "/Sounds/SFX/StageClear.wav";
    public void playBGM(){
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    System.out.println("try music");
                    URL url = this.getClass().getResource(BGM);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                    clip.open(inputStream);
                    clip.start();
                    clip.loop(50);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
    public void playSFX(final eSFX effect){
        new Thread(new Runnable() {
            public void run() {
                try {
                    if(effect==eSFX.eGetItem) {
                        URL url = this.getClass().getResource(GetItem);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eBullet){
                        URL url = this.getClass().getResource(Bullet);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eDamage){
                        URL url = this.getClass().getResource(Damage);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eDie){
                        URL url = this.getClass().getResource(Die);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eBumb){
                        URL url = this.getClass().getResource(Bumb);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eHeadShot){
                        URL url = this.getClass().getResource(HeadShot);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eGameOver){
                        URL url = this.getClass().getResource(GameOver);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                    else if(effect==eSFX.eStageClear){
                        URL url = this.getClass().getResource(StageClear);
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
