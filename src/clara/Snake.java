package clara;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/** Created by Clara on 3/31/16 */

public class Snake extends TimerTask implements KeyListener {

    int height = 300; int width = 400;
    int squareSize = 50;

    int xSquares = width/squareSize;
    int ySquares = height/squareSize;

    int score = 0;

    int[] kibble;
    boolean ateKibble = false;

    int gameOver = 0;

    LinkedList<int[]> snake = new LinkedList<int[]>();
    SnakePanel snakePanel;

    int[] nextMove;
    int[] prevMove;

    public static void main(String args[]) {
        Snake snakeGame = new Snake();
    }

    public Snake() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                resetGame();

                JFrame frame = new JFrame();
                frame.setUndecorated(true);
                frame.setSize(width, height);
                frame.setResizable(false);
                frame.addKeyListener(Snake.this);

                snakePanel = new SnakePanel();
                snakePanel.setBackground(Color.BLACK);
                frame.add(snakePanel);

                frame.setVisible(true);
                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(Snake.this, 0, 500);
            }
        });
    }

    class SnakePanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            g.clearRect(0, 0, width, height);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);

            if (gameOver > 6) {
                g.setColor(Color.GREEN);
                g.drawString("!!!! YOU WON !!! score: " + score, 100, 100);
            }

            else if (gameOver > 0 ) {
                g.setColor(Color.GREEN);

                g.drawString("GAME OVER score: " + score, 100, 100);
                g.drawString("try again in " + (gameOver+1)/2 + "...", 100,150);
                g.drawString("press q to quit", 100, 200);
            }

            else {
                g.setColor(Color.BLUE);
                g.fillRect(kibble[0] * squareSize, kibble[1] * squareSize, squareSize, squareSize);

                g.setColor(Color.RED);
                for (int[] square : snake) {
                    g.fillRect(square[0] * squareSize, square[1] * squareSize, squareSize, squareSize);
                }
            }
        }
    }


    @Override
    public void run() {

        if (gameOver > 0) {
            if (gameOver == 1) {
                resetGame();
            }
            gameOver--;
        }

        else {

            prevMove = nextMove;

            int headX = snake.get(0)[0];
            int headY = snake.get(0)[1];

            int[] newHead = {headX + nextMove[0], headY + nextMove[1]};

            if (contains(newHead, snake)) {
                gameOver = 6;
            }

            snake.add(0, newHead);

            if (snake.size() == xSquares * ySquares) {
                //you won!
                gameOver = 10;
            }

            if (ateKibble) {
                ateKibble = false;
            } else {
                snake.removeLast();
            }

            headX = snake.get(0)[0];
            headY = snake.get(0)[1];

            if ((headX < 0 || headX > xSquares) || (headY < 0 || headY > ySquares)) {
                gameOver = 6;
            }

            if (headX == kibble[0] && headY == kibble[1]) {
                score++;
                ateKibble = true;

                do {
                    kibble = new int[]{(int) (Math.random() * xSquares), (int) (Math.random() * ySquares)};
                } while (contains(kibble, snake));
            }
        }

        snakePanel.repaint();
    }


    private void resetGame() {
        score = 0;
        snake = new LinkedList<int[]>();
        snake.add(new int[]{2, 2});
        snake.add(new int[]{1, 2});
        nextMove = new int[]{1, 0};

        do  {
            kibble = new int[]{(int) (Math.random() * xSquares), (int) (Math.random() * ySquares)};
        } while (snake.contains(kibble)) ;
    }


    public boolean contains(int[] test, LinkedList<int[]> list) {
        for (int[] square : list) {
            if (Arrays.equals(test, square)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        //If snake is {0, 1} should not permit {0, -1} to stop snake reversing, same for other directions
        if (e.getKeyCode() == KeyEvent.VK_DOWN && prevMove[1] != -1) {
            nextMove = new int[]{0, 1};
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && prevMove[1] != 1) {
            nextMove = new int[]{0, -1};
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && prevMove[0] != 1) {
            nextMove = new int[]{-1, 0};
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && prevMove[0] != -1) {
            nextMove = new int[]{1, 0};
        }

        if (e.getKeyChar() == 'q') {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}
