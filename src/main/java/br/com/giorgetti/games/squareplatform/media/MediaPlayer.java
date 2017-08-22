package br.com.giorgetti.games.squareplatform.media;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Map;

public class MediaPlayer extends JFXPanel {

    private static final Map<String, Media> sounds = new HashMap<>();

    private static double volume = .02;
    private Media music;
    private javafx.scene.media.MediaPlayer mediaPlayer;

    public MediaPlayer(String audioResource) {

       try {

           if ( sounds.containsKey(audioResource) ) {
               this.music = sounds.get(audioResource);
           } else {
               this.music = new Media(getClass().getResource(audioResource).toURI().toString());
               sounds.put(audioResource, this.music);
           }

           this.mediaPlayer = new javafx.scene.media.MediaPlayer(this.music);
           this.mediaPlayer.setVolume(volume);

       } catch (Exception e) {
           System.err.println("Unable to use audio: " + audioResource);
           e.printStackTrace();
           System.exit(1);
       }

   }

   public void play() {
        play(true);
   }
   public void play(boolean forever) {
        play(forever, 0);
   }
   public void play(final boolean forever, final int delayMs) {

        if ( this.mediaPlayer == null ) {
            return;
        }

        if ( this.mediaPlayer.getStatus() == javafx.scene.media.MediaPlayer.Status.PLAYING ) {
            this.mediaPlayer.stop();
        }

        if ( forever ) {
            this.mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
        }

        if ( delayMs == 0 ) {
            this.mediaPlayer.play();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(delayMs);
                        mediaPlayer.play();
                    } catch (InterruptedException e) {}
                }
            }).start();
        }
   }

   public void stop() {
        this.mediaPlayer.stop();
   }

   public static double getVolume() {
        return volume * 100;
   }

   public static void setVolume(double v) {
        volume = v / 100;
   }

}
