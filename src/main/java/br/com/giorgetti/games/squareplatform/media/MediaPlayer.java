package br.com.giorgetti.games.squareplatform.media;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;

public class MediaPlayer extends JFXPanel {

    private Media music;
    private javafx.scene.media.MediaPlayer mediaPlayer;

    public MediaPlayer(String audioResource) {

       try {
           this.music = new Media(getClass().getResource(audioResource).toURI().toString());
           this.mediaPlayer = new javafx.scene.media.MediaPlayer(this.music);
           this.mediaPlayer.setVolume(.02);
       } catch (Exception e) {}

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

   public double getVolume() {
        return this.mediaPlayer.getVolume();
   }

   public void setVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
   }

}