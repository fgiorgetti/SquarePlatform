package br.com.giorgetti.games.squareplatform.config;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Holds the configuration that can be customized by the user.
 */
public class OptionsConfig {

    private static Set<OptionsObserver> observers = new ConcurrentSkipListSet<>();

    private double musicVolume = 0.02;
    private double sfxVolume = 0.02;

    private static OptionsConfig instance;

    private OptionsConfig() {}

    public static OptionsConfig getInstance() {
        if ( instance == null ) {
            instance = new OptionsConfig();
        }
        return instance;
    }

    /* Methods */
    public void updateObservers() {
        for ( OptionsObserver observer : observers ) {
            observer.optionsChanged(this);
        }
    }
    public void addOptionsObserver(OptionsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OptionsObserver observer) {
        observers.remove(observer);
    }

    /* Getters and Setters */

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
        updateObservers();
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = sfxVolume;
        updateObservers();
    }

}
