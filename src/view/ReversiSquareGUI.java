package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import controller.PlayerEvent;
import controller.PlayerEventType;
import controller.PlayerListener;
import discs.Disc;
import discs.DiscColor;
import model.ReadOnlyReversiModel;
import model.ReversiHexModel;
import model.ReversiSquareModel;

public class ReversiSquareGUI extends JFrame implements ReversiView {
  private final ReadOnlyReversiModel model;
  private final JButton[][] boardButtons;
  private int prevX = -1;
  private int prevY = -1;
  private boolean showCaptured;
  private List<PlayerListener> playerListeners = new ArrayList<>();

  public ReversiSquareGUI(ReadOnlyReversiModel model) {
    this.model = model;
    this.showCaptured = false;

    setTitle("Square Reversi");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    int dimensions = model.getDimensions();
    boardButtons = new JButton[dimensions][dimensions];

    setLayout(new GridLayout(dimensions, dimensions));
    for (int i = 0; i < dimensions; i++) {
      for (int j = 0; j < dimensions; j++) {
        boardButtons[i][j] = new JButton();
        boardButtons[i][j].setOpaque(true);
        boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        int actualI = i;
        int actualJ = j;
        boardButtons[i][j].addMouseListener(new MouseListener() {

          @Override
          public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            System.out.println("Disc position at " + "(" + actualJ + ", " + actualI + ")");

            if (!model.isDiscFlipped(actualJ, actualI)) {
              if(button.getBackground().equals(Color.CYAN)) {
                button.setBackground(Color.GRAY);
              } else {
                if (prevX == -1 && prevY == -1) {
                  prevX = actualJ;
                  prevY = actualI;
                  button.setBackground(Color.CYAN);
                } else {
                  discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                  button.setBackground(Color.CYAN);
                  prevX = actualJ;
                  prevY = actualI;
                }
              }
            } else {
              if (!(prevX == -1 && prevY == -1)) {
                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                prevX = -1;
                prevY = -1;
              }
            }
          }

          @Override
          public void mouseReleased(MouseEvent e) {

          }

          @Override
          public void mouseEntered(MouseEvent e) {

          }

          @Override
          public void mouseExited(MouseEvent e) {

          }

          @Override
          public void mouseClicked(MouseEvent e) {

          }
        });
        boardButtons[i][j].addKeyListener(new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
          }

          @Override
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              System.out.println("Spacebar pressed, player wishes to pass");
              notifyListeners(new PlayerEvent(PlayerEventType.PASS, "", model.currentTurn()));
              if (!(prevX == -1 && prevY == -1)) {
                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                prevX = -1;
                prevY = -1;
              }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              if (!(prevX == -1 && prevY == -1)) {
                System.out.println("Enter key pressed, player wishes to make a move to the disc at"
                        + " (" + prevX + ", " + prevY + ")");

                discSelectorHelper(boardButtons[prevY][prevX], prevX, prevY);
                notifyListeners(new PlayerEvent(PlayerEventType.MOVE, prevX + " " +
                        prevY, model.currentTurn()));
                prevX = -1;
                prevY = -1;
              } else {
                System.out.println("No selected disc to move to");
              }
            }
          }

          @Override
          public void keyReleased(KeyEvent e) {

          }
        });
        add(boardButtons[i][j]);
      }
    }

    setSize(dimensions * 100, dimensions * 100);
    setVisible(true);

    render();
  }

  @Override
  public void addListener(PlayerListener pl) {
    playerListeners.add(pl);
  }

  /**
   * The notifyListeners method notifies relevant listeners.
   */
  public void notifyListeners(PlayerEvent playerEvent) {
    for (PlayerListener pl : playerListeners) {
      pl.update(playerEvent);
    }
  }

  private void discSelectorHelper(JButton button, int x, int y) {
    DiscColor originalColor = model.getDiscAt(x, y).getColor();
    switch (originalColor) {
      case WHITE:
        button.setBackground(Color.WHITE);
        break;
      case BLACK:
        button.setBackground(Color.BLACK);
        break;
      case FACEDOWN:
        button.setBackground(Color.GRAY);
        break;
      default:
    }
  }

  @Override
  public void render() {
    Disc[][] board = model.getCurrentBoardState();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j].getColor() == DiscColor.WHITE) {
          boardButtons[i][j].setBackground(Color.WHITE);
        } else if (board[i][j].getColor() ==  DiscColor.BLACK) {
          boardButtons[i][j].setBackground(Color.BLACK);
        } else if (board[i][j].getColor() == DiscColor.FACEDOWN) {
          boardButtons[i][j].setBackground(Color.GRAY);
        }
      }
    }
  }

  @Override
  public void showPopup(String message) {

  }

  public static void main(String[] args) {
    ReversiHexModel model = new ReversiSquareModel();
    model.startGame(10);

    ReversiView gui = new ReversiSquareGUI(model);
    gui.render();
  }
}
