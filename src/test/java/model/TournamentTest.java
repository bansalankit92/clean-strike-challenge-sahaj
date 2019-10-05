package model;

import static model.CarromBoard.NO_OF_COINS;
import static model.CarromBoard.NO_OF_RED_COINS;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;

public class TournamentTest {

  static final String PLAYER_1_NAME = "Player1";
  static final String PLAYER_2_NAME = "Player2";
  static final int TOTAL_COINS = NO_OF_COINS + NO_OF_RED_COINS;
  private Tournament tournament;

  public static void assertEqualsPlayer1(Player actualPlayer) {
    assertEquals(PLAYER_1_NAME, actualPlayer.getName());
  }

  public static void assertEqualsPlayer2(Player actualPlayer) {
    assertEquals(PLAYER_2_NAME, actualPlayer.getName());
  }

  private void assertScore(int player1Score, int player2Score) {
    assertEquals(player1Score, tournament.getPlayer1().getScore());
    assertEquals(player2Score, tournament.getPlayer2().getScore());
  }

  private void makeStrikes(int times, StrikeType strikeType) {
    IntStream.range(0, times).forEach(i -> tournament.strikes(strikeType));
  }

  @Before
  public void setUp() throws Exception {
    tournament = new Tournament(PLAYER_1_NAME, PLAYER_2_NAME);
  }

  @Test
  public void test_Strike() {
    tournament.strikes(StrikeType.STRIKE);
    testStrike();
  }

  private void testStrike() {
    assertEqualsPlayer2(tournament.getCurrentPlayer());
    assertScore(1, 0);
    assertEquals(TOTAL_COINS - 1, tournament.getTotalCoins());
  }

  @Test
  public void test_redStrike() {
    tournament.strikes(StrikeType.RED_STRIKE);
    testRedStrike();
  }

  private void testRedStrike() {
    assertEqualsPlayer1(tournament.getCurrentPlayer());
    assertScore(3, 0);
    assertEquals(TOTAL_COINS - 1, tournament.getTotalCoins());
  }

  @Test
  public void test_Strike_afterRed() {
    tournament.strikes(StrikeType.RED_STRIKE);
    tournament.strikes(StrikeType.STRIKE);
    assertEqualsPlayer2(tournament.getCurrentPlayer());
    assertScore(4, 0);
    assertEquals(TOTAL_COINS - 1, tournament.getTotalCoins());
  }

  @Test
  public void test_NoStrike() {
    tournament.strikes(StrikeType.NO_STRIKE);
    assertEqualsPlayer2(tournament.getCurrentPlayer());
    assertScore(0, 0);
    assertEquals(TOTAL_COINS, tournament.getTotalCoins());
  }

  @Test
  public void test_Consecutive3NoStrike_player1() {
    makeStrikes(6, StrikeType.NO_STRIKE);
    assertEqualsPlayer1(tournament.getCurrentPlayer());
    assertScore(-1, 0);
    assertEquals(TOTAL_COINS, tournament.getTotalCoins());
  }

  @Test
  public void test_foul_player1() {
    makeStrikes(6, StrikeType.NO_STRIKE);
    tournament.strikes(StrikeType.STRIKER_STRIKE);
    tournament.strikes(StrikeType.STRIKE);
    tournament.strikes(StrikeType.STRIKER_STRIKE);
    tournament.strikes(StrikeType.STRIKE);
    assertEqualsPlayer1(tournament.getCurrentPlayer());
    assertScore(-4, 1);
    assertEquals(TOTAL_COINS - 2, tournament.getTotalCoins());
  }


  @Test
  public void test_defunct_player1() {
    tournament.strikes(StrikeType.DEFUNCT_COIN);
    assertEqualsPlayer2(tournament.getCurrentPlayer());
    assertScore(-2, 0);
    assertEquals(TOTAL_COINS - 1, tournament.getTotalCoins());
  }

  @Test
  public void test_multiStrike_player1() {
    tournament.strikes(StrikeType.MULTI_STRIKE);
    testMultiStrike();
  }

  private void testMultiStrike() {
    assertEqualsPlayer2(tournament.getCurrentPlayer());
    assertScore(2, 0);
    assertEquals(TOTAL_COINS, tournament.getTotalCoins());
  }

  @Test
  public void test_printScore() {
    tournament.strikes(StrikeType.MULTI_STRIKE);
    tournament.strikes(StrikeType.MULTI_STRIKE);
    assertEquals(tournament.getPlayer1().getName() + " : " + tournament.getPlayer1().getScore()
            + " " + tournament.getPlayer2().getName() + " : " + tournament.getPlayer2().getScore(),
        tournament.printScore());
  }

  private InputStream getInputStream(String input) {
    return new ByteArrayInputStream(input.getBytes());
  }

  @Test
  public void testCommandLine_Strike() {
    String input = "1 7";
    tournament.play(getInputStream(input));
    testStrike();
  }

  @Test
  public void testCommandLine_RedStrike() {
    String input = "3 7";
    tournament.play(getInputStream(input));
    testRedStrike();
  }

  @Test
  public void testCommandLine_MultiStrike() {
    String input = "2 7";
    tournament.play(getInputStream(input));
    testMultiStrike();
  }

  @Test
  public void testCommandLine_StrikerStrike() {
    String input = "4 7";
    tournament.play(getInputStream(input));
    assertScore(-1, 0);
    assertEquals(TOTAL_COINS, tournament.getTotalCoins());
  }

  @Test
  public void testCommandLine_DefunctStrike() {
    String input = "5 7";
    tournament.play(getInputStream(input));
    assertScore(-2, 0);
    assertEquals(TOTAL_COINS - 1, tournament.getTotalCoins());
  }

  @Test
  public void testCommandLine_NoStrike() {
    String input = "6 7";
    tournament.play(getInputStream(input));
    assertScore(0, 0);
    assertEquals(TOTAL_COINS, tournament.getTotalCoins());
  }

  @Test
  public void testCommandLine_Winner() {
    String input = "1 6 1 6 1 6 3 6 7";
    tournament.play(getInputStream(input));
    assertScore(6, 0);
    assertEquals(TOTAL_COINS -4, tournament.getTotalCoins());
  }

}