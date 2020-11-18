
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201111
 * date         20201108
 * @filename	MusicPlayer.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


// This class sets up all the sound effects and background music
public class MusicPlayer {
    File bgMusic;
    File collisionMusic;
    File bottomCollisionMusic;
    File winMusic;
    
    
    public MusicPlayer(){
        bgMusic = new File(".\\backgroundAudio.wav");
        
        collisionMusic = new File(".\\collisionSoundEffect.wav");
        
        bottomCollisionMusic = new File(".\\hitBottomSoundEffect.wav");
        
        winMusic = new File(".\\winSoundEffect.wav");
    }
    
    
    // referencing: https://stackoverflow.com/questions/42955509/how-to-play-a-simple-audio-file-java/42961964#42961964
    public void playBackgroundAudio(){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(bgMusic));
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
        }catch(Exception e){
            System.out.println("Unable to load music file");
        }
    }
    
    
    public void playCollisionAudio(){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(collisionMusic));
            clip.start();
            
        }catch(Exception e){
            System.out.println("Unable to load music file");
        }
    }
    
    
    public void bottomCollisionAudio(){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(bottomCollisionMusic));
            clip.start();
            
        }catch(Exception e){
            System.out.println("Unable to load music file");
        }
    }
    
    
    public void winAudio(){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(winMusic));
            clip.start();
            
        }catch(Exception e){
            System.out.println("Unable to load music file");
        }
    }
}
