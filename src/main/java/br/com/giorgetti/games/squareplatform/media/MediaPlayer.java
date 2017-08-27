package br.com.giorgetti.games.squareplatform.media;

import br.com.giorgetti.games.squareplatform.config.OptionsConfig;
import br.com.giorgetti.games.squareplatform.config.OptionsObserver;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MediaPlayer extends JFXPanel implements OptionsObserver {

    public enum MediaType {
        MUSIC, SFX
    }

    private static final Map<String, Media> sounds = new ConcurrentHashMap<>();

    private Media music;
    private MediaType type;
    private OptionsConfig options;
    private boolean forever;
    private boolean removed = false;

    private javafx.scene.media.MediaPlayer mediaPlayer;

    public MediaPlayer(String audioResource, MediaType type) {

       try {

           if ( sounds.containsKey(audioResource) ) {
               this.music = sounds.get(audioResource);
           } else {
               this.music = new Media(getClass().getResource(audioResource).toURI().toString());
               sounds.put(audioResource, this.music);
           }

           this.mediaPlayer = new javafx.scene.media.MediaPlayer(this.music);
           this.type = type;
           this.options = OptionsConfig.getInstance();
           this.options.addOptionsObserver(this);

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

        this.forever = forever;

        if ( this.mediaPlayer.getStatus() == javafx.scene.media.MediaPlayer.Status.PLAYING ) {
            this.mediaPlayer.stop();
        }

        if ( forever ) {
            this.mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
        }

        // Adjust volume before playing
        mediaPlayer.setVolume(getVolume());

        if ( delayMs == 0 ) {
            this.mediaPlayer.play();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(delayMs);
                        if ( !removed ) {
                            mediaPlayer.play();
                        }
                    } catch (InterruptedException e) {}
                }
            }).start();
        }
   }

   public void stop() {
        this.mediaPlayer.stop();
   }

   public double getVolume() {
        return this.type == MediaType.MUSIC
                ? options.getMusicVolume()
                : options.getSfxVolume();
   }

    @Override
    public void optionsChanged(OptionsConfig options) {
        double volume = getVolume();
        this.mediaPlayer.setVolume(volume);
    }

    /**
     * Removes the Options Observer asynchronously. If media playing forever, it will be
     * stopped and observer removed. If not playing forever, it waits till media is played
     * before removing the observer.
     */
    public void remove() {

        final MediaPlayer player = this;

        new Thread(new Runnable() {
            @Override
            public void run() {

                if ( !forever ) {
                    // Wait till it finishes
                    while (mediaPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING)) {}
                } else {
                    mediaPlayer.stop();
                }

                removed = true;
                OptionsConfig.getInstance().removeObserver(player);

            }
        }).start();

    }

}
