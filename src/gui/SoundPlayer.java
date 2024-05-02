package gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundPlayer {
    String musicFile;
    Media sound;
    MediaPlayer mediaPlayer;

    public SoundPlayer(String musicFileURL){
        try{
            musicFile = musicFileURL;
            sound = new Media(new File(musicFile).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
        }
        catch(Exception e){
            System.out.println("Can't read the file");
            System.out.println(e);
        }
    }

    public void startPlay(){
        try{
            mediaPlayer.play();
        }
        catch(Exception e){
            System.out.println("Can't play the music");
            System.out.println(e);
        }
    }

    public void stopPlay(){
        mediaPlayer.stop();
    }
}
