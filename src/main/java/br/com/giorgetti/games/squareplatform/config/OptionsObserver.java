package br.com.giorgetti.games.squareplatform.config;

/**
 * Interface that should be implemented if a given class needs to be notified
 * when a configuration option is modified.
 */
public interface OptionsObserver extends Comparable<OptionsObserver> {

    void optionsChanged(OptionsConfig options);

    @Override
    default int compareTo(OptionsObserver o) {
        if ( this.equals(o) ) {
            return 0;
        } else {
            return 1;
        }
    }

}
