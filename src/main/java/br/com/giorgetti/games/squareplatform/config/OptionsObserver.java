package br.com.giorgetti.games.squareplatform.config;

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
