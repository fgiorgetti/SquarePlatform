package br.com.giorgetti.games.squareplatform.main;

import javax.swing.*;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class SquarePlatform {

    public static void main(String [] args ) {

        JFrame window = new JFrame("Square Platform");
        window.setContentPane(new GamePanel());

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

    }

}
