/*
 * Author: Grace Pu
 * Date: June 15
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * Music class defines behaviours for the music, separated for readability and organization.
 */

import java.awt.Image;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music extends ImageButton {

    // Variable Declaration
    private Clip[] musicClips = new Clip[11];
    private int clipNum = 0;
    private long start = 0;

    // constructor creates stopwatch at current time as start
    public Music(int x, int y, Image on, Image onMouse, Image off, Image offMouse) {
        super(x, y, on, onMouse, off, offMouse);
    }

    public void load() {
        for (int i = 0; i <= 10; i++)
            musicClips[i] = loadClip(musicClips[i], "music/1.wav");
        musicClips[10] = loadClip(musicClips[10], "music/hallOfFame.wav");
        playSound(musicClips[0]);
    }

    public Clip loadClip(Clip var, String path) {
        try {
            var = AudioSystem.getClip();
            var = openSound(var, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return var;
    }

    public void toggleUpdate() {
        if (getValue())
            playSound(musicClips[clipNum]);
        else
            stopSound(musicClips[clipNum]);
    }

    public void update(int maxTile) throws InterruptedException {
        toggleUpdate();
        Thread.sleep(musicClips[clipNum].getMicrosecondLength() - musicClips[clipNum].getMicrosecondPosition());
        if (maxTile < 10 && maxTile != clipNum)
            clipNum++;
        playSound(musicClips[clipNum]);
        System.out.println(clipNum);
    }

    // method for opening sound based on path
    private Clip openSound(Clip sound, String path) {
        try { // try to load the sound
            sound.open(AudioSystem.getAudioInputStream(new File(path)));
        } catch (Exception e) { // exception
            e.printStackTrace();
        }
        return sound;
    }

    // method for playing sound
    private void playSound(Clip sound) {
        sound.setMicrosecondPosition(start);
        sound.start(); // start the sound
    }

    // method for playing sound
    private void stopSound(Clip sound) {
        start = sound.getMicrosecondPosition();
        sound.stop(); // start the sound
    }

}
