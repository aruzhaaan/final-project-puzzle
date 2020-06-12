package com.company;

import java.awt.*;
import java.awt.Color;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;




class PuzzleButton extends JButton {
    private boolean isLastButton;

    public PuzzleButton(){
        super();
        initUI();
    }
    public PuzzleButton(Image i){
        super(new ImageIcon(i));
        initUI();
    }
    private void initUI(){
        isLastButton = false;
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }

            @Override
            public void mouseExited(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.darkGray));
            }
        });
    }

    public boolean isLastButton(){
        return isLastButton;
    }

    public void setLastButton() {
        this.isLastButton = true;
    }


}

public class Game extends JFrame implements ActionListener {
    private ArrayList<Point> solution;
    private ArrayList<PuzzleButton> buttons;
    private JPanel panel;
    private BufferedImage resized;

    private PuzzleButton lastButton;

    public Game(){
        initUI();
    }

    public void initUI() {
        solution = new ArrayList<>();

        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));

        buttons = new ArrayList<>();

        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        panel.setLayout(new GridLayout(4, 4));

        try {
            BufferedImage source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, h);
        } catch (IOException ex) {
            System.err.println("Error" + ex);
        }

        int width = resized.getWidth();
        int height = resized.getHeight();

        add(panel, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 3; j++){
                Image image = createImage(new FilteredImageSource(resized.getSource(), new CropImageFilter(j * width / 3, i * height / 4, width / 3, height / 4)));

                PuzzleButton button = new PuzzleButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2){
                    lastButton = new PuzzleButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setLastButton();
                    lastButton.setContentAreaFilled(false);
                    lastButton.putClientProperty("position", new Point(i, j));

                } else{
                    buttons.add(button);
                }
            }
        }
        Collections.shuffle(buttons);
        buttons.add(lastButton);

        int NUMBER_OF_BUTTONS = 12;
        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {
            PuzzleButton btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }

        pack();
        setTitle("Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private BufferedImage resizeImage(BufferedImage originImage, int height){
        BufferedImage resizedImage = new BufferedImage(600, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originImage, 0,0, 600, height, null);
        g.dispose();
        return resizedImage;
    }
    private BufferedImage loadImage() throws IOException {
        return ImageIO.read(new File("puzzle.png"));
    }
    private int getNewHeight(int w, int h){
        int DESIRED_WIDTH = 600;
        double ratio = DESIRED_WIDTH /(double)w;
        return (int)(h*ratio);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            checkButton(e);
            checkSolution();
        }
        private void checkButton(ActionEvent e){
            int lidx = 0;

            for (PuzzleButton button : buttons) {
                if (button.isLastButton()) {
                    lidx = buttons.indexOf(button);
                }
            }
            JButton btn;
            btn = (JButton) e.getSource();
            int bid = buttons.indexOf(btn);
            if ((bid - 1 == lidx) || (bid + 1 == lidx)
                    || (bid - 3 == lidx) || (bid + 3 == lidx)) {
                Collections.swap(buttons,
                        bid,
                        lidx);
                updateButtons();
            }

        }
        private void updateButtons() {

            panel.removeAll();

            for (JComponent btn : buttons) {
                panel.add(btn);
            }
            panel.validate();
        }
        private void checkSolution() {

            ArrayList<Point> current = new ArrayList<>();
            for (JComponent btn : buttons) {
                current.add((Point) btn.getClientProperty("position"));
            }
            if (compareList(solution, current)) {
                JOptionPane.showMessageDialog(panel, "Finished",
                        "Congratulation", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        public boolean compareList(ArrayList<Point> ls1, ArrayList<Point> ls2) {
            return ls1.toString().contentEquals(ls2.toString());
        }
        public void main(String[] args) {
            EventQueue.invokeLater(() -> {
                Game puzzle = new Game();
                puzzle.setVisible(true);
            });
        }
    }

}

