package engine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;

public final class SoundManager {
    public enum Sound_ID{
        eLaser,
        eGetDamage,
        eDestroyed
    }
    private AudioInputStream streams;
    private Clip clip;
    File[] files=new File[3];
    private SoundManager(){
        try {
            files[0] = new File("../../Resources/Sounds/SFX/sfx_laser1.wav");
            files[1] = new File("../../Resources/Sounds/SFX/sfx_laser2.wav");
            files[2] = new File("../../Resources/Sounds/SFX/587196__derplayer__explosion-06.wav");
        }catch(Exception e) {

            e.printStackTrace();
        }
    }
    public void playSound(Sound_ID id){
        int i=0;
        try {
            switch (id){
                case eLaser:
                    i=0;
                    break;
                case eGetDamage:
                    i=1;
                    break;
                case eDestroyed:
                    i=2;
                    break;
                default:
                    break;
            }
            AudioInputStream stream = AudioSystem.getAudioInputStream(files[i]);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

}
