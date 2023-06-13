import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {

    private String path;
    private Clip music;

    public AudioPlayer(String p) {
        path = p;
    }

    // Method for opening sound based on path
    public void openSound() {
        try { // try to load the sound
            music.open(AudioSystem.getAudioInputStream(new File(path)));
        } catch (Exception e) { // exception
            e.printStackTrace();
        }
    }

    // Method for playing sound
    public static void playSound(Clip sound) {
        sound.start(); // start the sound
    }

}
