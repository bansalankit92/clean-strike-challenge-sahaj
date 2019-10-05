package model;

import static model.CarromBoard.NO_OF_COINS;
import static model.CarromBoard.NO_OF_RED_COINS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import exception.NoCoinException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarromBoardTest {

  private CarromBoard carromBoard;

  private static Player getPlayer(int strikeSize) {
    return getPlayer(strikeSize, StrikeType.STRIKE);
  }

  private static Player getPlayer(int strikeSize, StrikeType strikeType) {
    Player player = new Player();
    List<StrikeType> strikeTypes = player.getStrikes();
    IntStream.range(0, strikeSize).forEach(i -> strikeTypes.add(strikeType));
    return player;
  }

  private static void testCarramBoardNotNull(CarromBoard carromBoard) {
    Assert.assertNotNull(carromBoard);
    Assert.assertNotNull(carromBoard.getPlayer1());
    Assert.assertNotNull(carromBoard.getPlayer2());
    Assert.assertNotNull(carromBoard.getStriker());
    Assert.assertEquals(NO_OF_COINS, carromBoard.getCoins());
    Assert.assertEquals(CarromBoard.NO_OF_RED_COINS, carromBoard.getRedCoin());
  }

  private static void assertEqualPlayers(Player expectedPlayer, Player actualPlayer) {
    assertEquals(expectedPlayer.getName(), actualPlayer.getName());
  }

  private static void addStrike(CarromBoard board, int strikeSize, StrikeType strikeType) {
    IntStream.range(0, strikeSize).forEach(i -> board.addStrike(strikeType));
  }

  @Before
  public void setUp() throws Exception {
    carromBoard = new CarromBoard(getPlayer(0), getPlayer(0), true);
  }

  @Test
  public void testCarramBoard_nonNull() {
    CarromBoard carromBoard = new CarromBoard();
    testCarramBoardNotNull(carromBoard);
    Assert.assertEquals(carromBoard.getStriker(), carromBoard.getPlayer1());
  }

  @Test
  public void testCarramBoard_nonNull_StrikerEqualsPlayer1() {
    CarromBoard carromBoard = new CarromBoard(true);
    testCarramBoardNotNull(carromBoard);
    Assert.assertEquals(carromBoard.getStriker(), carromBoard.getPlayer1());
  }

  @Test
  public void testCarramBoard_nonNull_StrikerEqualsPlayer2() {
    CarromBoard carromBoard = new CarromBoard(false);
    testCarramBoardNotNull(carromBoard);
    Assert.assertEquals(carromBoard.getStriker(), carromBoard.getPlayer2());
  }

  @Test(expected = NullPointerException.class)
  public void testCarramBoard_nullPlayers() {
    new CarromBoard(null, null, false);
  }

  @Test
  public void testCarramBoard_nonNullPlayers() {
    CarromBoard carromBoard = new CarromBoard(getPlayer(0), getPlayer(0), true);
    testCarramBoardNotNull(carromBoard);
    Assert.assertEquals(carromBoard.getStriker(), carromBoard.getPlayer1());
  }

  @Test
  public void testGetWinner_player1Least5Points_diffLessThen3() {
    carromBoard.addPoints(5);
    carromBoard.changePlayer();
    carromBoard.addPoints(4);
    assertNull(carromBoard.getWinner());
  }

  @Test
  public void testGetWinner_playersLessThan5Points() {
    carromBoard.addPoints(3);
    carromBoard.changePlayer();
    carromBoard.addPoints(2);
    assertNull(carromBoard.getWinner());
  }

  @Test
  public void testGetWinner_player1Least5Points_diffMoreThen3() {
    carromBoard.addPoints(5);
    carromBoard.changePlayer();
    carromBoard.addPoints(2);
    assertEqualPlayers(carromBoard.getPlayer1(), carromBoard.getWinner());
  }

  @Test
  public void testGetWinner_player2Least5Points_diffMoreThen3() {
    carromBoard.addPoints(1);
    carromBoard.changePlayer();
    carromBoard.addPoints(5);
    assertEqualPlayers(carromBoard.getPlayer2(), carromBoard.getWinner());
  }

  @Test
  public void isConsecutiveNoStrike_playerLessStrike() {
    addStrike(carromBoard, 1, StrikeType.STRIKE);
    assertFalse(carromBoard.isConsecutiveNoStrike());
  }

  @Test
  public void isConsecutiveNoStrike_playerMoreThan3Strike() {
    addStrike(carromBoard, 4, StrikeType.STRIKE);
    assertFalse(carromBoard.isConsecutiveNoStrike());
  }

  @Test
  public void isConsecutiveNoStrike_playerMoreThan3NoStrike() {
    addStrike(carromBoard, 3, StrikeType.NO_STRIKE);
    assertTrue(carromBoard.isConsecutiveNoStrike());
  }

  @Test
  public void isConsecutiveNoStrike_playerMoreThan3RandomStrike() {
    addStrike(carromBoard, 2, StrikeType.STRIKE);
    carromBoard.addStrike(StrikeType.NO_STRIKE);
    carromBoard.addStrike(StrikeType.STRIKE);
    carromBoard.addStrike(StrikeType.NO_STRIKE);
    assertFalse(carromBoard.isConsecutiveNoStrike());
  }

  @Test
  public void isFoulNoStrike_playerLessStrike() {
    addStrike(carromBoard, 1, StrikeType.STRIKE);
    assertFalse(carromBoard.isFoul());
  }

  @Test
  public void isFoulNoStrike_playerMoreThan3Strike() {
    addStrike(carromBoard, 4, StrikeType.STRIKE);
    assertFalse(carromBoard.isFoul());
  }

  @Test
  public void isFoulNoStrike_playerMoreThan3FoulStrike() {
    addStrike(carromBoard, 2, StrikeType.NO_STRIKE);
    carromBoard.addStrike(StrikeType.CONSECUTIVE_3_NO_STRIKE);
    carromBoard.addStrike(StrikeType.STRIKER_STRIKE);
    carromBoard.addStrike(StrikeType.FOUL);
    assertTrue(carromBoard.isFoul());
  }

  @Test
  public void isFoulNoStrike_playerMoreThan3RandomStrike() {
    addStrike(carromBoard, 2, StrikeType.STRIKE);
    carromBoard.addStrike(StrikeType.CONSECUTIVE_3_NO_STRIKE);
    carromBoard.addStrike(StrikeType.MULTI_STRIKE);
    carromBoard.addStrike(StrikeType.STRIKER_STRIKE);
    assertFalse(carromBoard.isFoul());
  }

  @Test
  public void test_isDraw_NotGameover() {
    assertFalse(carromBoard.isDraw());
  }

  @Test
  public void test_isDraw_Gameover_Winner() {
    carromBoard.addPoints(5);
    carromBoard.changePlayer();
    carromBoard.addPoints(2);
    strikeAllCoins();
    assertFalse(carromBoard.isDraw());
  }

  @Test
  public void test_isDraw_Gameover_NoWinner() {
    carromBoard.addPoints(5);
    carromBoard.changePlayer();
    carromBoard.addPoints(4);
    strikeAllCoins();
    assertTrue(carromBoard.isDraw());
  }

  private void strikeAllCoins() {
    IntStream.range(0, NO_OF_COINS).forEach(i -> carromBoard.coinsStriked());
    IntStream.range(0, NO_OF_RED_COINS).forEach(i -> carromBoard.redCoinsStriked());
  }

  @Test
  public void test_changePlayer_player1() {
    carromBoard.changePlayer();
    assertEqualPlayers(carromBoard.getPlayer2(), carromBoard.getStriker());
  }

  @Test
  public void test_changePlayer_player2() {
    carromBoard.changePlayer();
    carromBoard.changePlayer();
    assertEqualPlayers(carromBoard.getPlayer1(), carromBoard.getStriker());
  }

  @Test(expected = NoCoinException.class)
  public void test_redCoinsStriked_moreThanPresent() {
    strikeAllCoins();
    carromBoard.redCoinsStriked();
  }

  @Test(expected = NoCoinException.class)
  public void test_coinsStriked_moreThanPresent() {
    strikeAllCoins();
    carromBoard.coinsStriked();
  }

  @Test
  public void test_isLastRedCoin_false() {
    assertFalse(carromBoard.isLastRedCoin());
    carromBoard.addStrike(StrikeType.NO_STRIKE);
    assertFalse(carromBoard.isLastRedCoin());
  }

  @Test
  public void test_isLastRedCoin_true() {
    carromBoard.addStrike(StrikeType.RED_STRIKE);
    assertTrue(carromBoard.isLastRedCoin());
  }

}