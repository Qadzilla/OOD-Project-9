import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import discs.DiscColor;
import discs.DiscType;
import model.ReversiModel;
import model.ReversiSquareModel;
import player.PlayerTurn;
import view.ReversiTextualView;
import view.ReversiView;

public class ReversiSquareModelTests {
  ReversiModel model;
  PlayerTurn player1;
  PlayerTurn player2;
  ReversiView textualView;

  @Before
  public void initData() {
    this.model = new ReversiSquareModel();
    this.player1 = PlayerTurn.PLAYER1;
    this.player2 = PlayerTurn.PLAYER2;
    this.textualView = new ReversiTextualView(model, new StringBuilder());
  }

  @Test
  public void testBoardInitOddMiddle() {
    model.startGame(6);
    Assert.assertEquals(model.getDiscAt(2,2).getColor(), DiscColor.BLACK);
    Assert.assertEquals(model.getDiscAt(3,3).getColor(), DiscColor.BLACK);
    Assert.assertEquals(model.getDiscAt(2,3).getColor(), DiscColor.WHITE);
    Assert.assertEquals(model.getDiscAt(3,2).getColor(), DiscColor.WHITE);
  }

  @Test
  public void testBoardInitEvenMiddle() {
    model.startGame(8);
    Assert.assertEquals(model.getDiscAt(3,3).getColor(), DiscColor.BLACK);
    Assert.assertEquals(model.getDiscAt(4,4).getColor(), DiscColor.BLACK);
    Assert.assertEquals(model.getDiscAt(3,4).getColor(), DiscColor.WHITE);
    Assert.assertEquals(model.getDiscAt(4,3).getColor(), DiscColor.WHITE);
  }

  @Test
  public void testInvalidStarts() {
   Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(-1));
   Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(7));
  }

  @Test
  public void testErrorBeforeStart() {
    Assert.assertThrows(IllegalStateException.class, () -> model.pass());
  }

  @Test
  public void testRightLeftMove() {
    model.startGame(8);
    // right
    model.makeMove(5,3);
    Assert.assertEquals(model.getDiscAt(3,3).getColor(), DiscColor.BLACK);
    // pass
    model.pass();
    // left
    model.makeMove(2,4);
    Assert.assertEquals(model.getDiscAt(2,4).getColor(), DiscColor.BLACK);
    Assert.assertSame(model.getGameType(), DiscType.SQUARE);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testDiaganonlUpRightMove() {
    model.startGame(8);
    // right
    model.makeMove(5,3);
    Assert.assertEquals(model.getDiscAt(5,3).getColor(), DiscColor.BLACK);
    // diagonal right
    model.makeMove(5,2);
    Assert.assertEquals(model.getDiscAt(5,2).getColor(), DiscColor.WHITE);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testUpAndDown() {
    model.startGame(8);
    // right
    model.makeMove(3,5);
    Assert.assertEquals(model.getDiscAt(3,5).getColor(), DiscColor.BLACK);
    // diagonal right
    model.pass();
    model.makeMove(4,2);
    Assert.assertEquals(model.getDiscAt(4,2).getColor(), DiscColor.BLACK);
    Assert.assertTrue(model.isGameOver());
  }
  @Test
  public void testDiaganolDownLeftMove() {
    model.startGame(8);
    model.makeMove(3,5);
    Assert.assertEquals(model.getDiscAt(3,5).getColor(), DiscColor.BLACK);
    model.makeMove(2,5);
    Assert.assertEquals(model.getDiscAt(2,5).getColor(), DiscColor.WHITE);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testDiaganolDownRightMove() {
    model.startGame(8);
    model.pass();
    model.makeMove(4,5);
    Assert.assertEquals(model.getDiscAt(4,5).getColor(), DiscColor.WHITE);
    model.makeMove(5,5);
    Assert.assertEquals(model.getDiscAt(5,5).getColor(), DiscColor.BLACK);
    Assert.assertFalse(model.isGameOver());

  }

  @Test
  public void testDiaganolUpLeftMove() {
    model.startGame(8);
    model.pass();
    model.makeMove(3,2);
    Assert.assertEquals(model.getDiscAt(3,2).getColor(), DiscColor.WHITE);
    model.makeMove(2,2);
    Assert.assertEquals(model.getDiscAt(2,2).getColor(), DiscColor.BLACK);
    Assert.assertFalse(model.isGameOver());
  }
}
