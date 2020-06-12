package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.setDefaultCloseOperation(Game.EXIT_ON_CLOSE);
        game.setExtendedState(JFrame.MAXIMIZED_BOTH);
        game.setVisible(true);




    }
}
