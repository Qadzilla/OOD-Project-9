package model;

import discs.Disc;
import discs.DiscColor;
import discs.DiscType;
import discs.GameDisc;

public class ReversiSquareModel extends ReversiHexModel {
  public ReversiSquareModel() {
    super();
    super.type = DiscType.SQUARE;
  }

  @Override
  public void startGame(int boardSize) {
    checkStartGameConditions(boardSize);
    super.numRows = boardSize;
    super.numColumns = boardSize;
    super.gameBoard = new Disc[super.numRows][super.numColumns];
    initBoard();
    super.gameOn = true;
  }
  private void checkStartGameConditions(int boardSize) {
    // if start game has already been called
    if (super.gameOn) {
      throw new IllegalStateException("Game has started already");
    }
    // if the board is negative or odd
    if (boardSize <= 0 || boardSize % 2 == 1 ) {
      throw new IllegalArgumentException("Board is negative or odd");
    }
  }
  private void initBoard() {
    for (int i = 0; i < super.gameBoard.length; i++) {
      for (int j = 0; j < super.gameBoard[0].length; j++) {
        super.gameBoard[i][j] = new GameDisc(super.type, DiscColor.FACEDOWN);
      }
    }
    placeStartingPieces();
  }

  private void placeStartingPieces() {
    int middle = super.gameBoard.length / 2;
    super.gameBoard[middle][middle] = new GameDisc(super.type, DiscColor.BLACK);
    super.gameBoard[middle-1][middle-1] = new GameDisc(super.type,DiscColor.BLACK);
    super.gameBoard[middle-1][middle] = new GameDisc(super.type,DiscColor.WHITE);
    super.gameBoard[middle][middle-1] = new GameDisc(super.type,DiscColor.WHITE);

  }
}
