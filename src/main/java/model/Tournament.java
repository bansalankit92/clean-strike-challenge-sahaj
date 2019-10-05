package model;


import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import lombok.Getter;
import lombok.NonNull;


public class Tournament {

  @Getter
  private Player player1;
  @Getter
  private Player player2;
  private Player winner;

  private CarromBoard carromBoard;

  public Tournament(@NonNull String player1Name, @NonNull String player2Name) {
    this.player1 = new Player(player1Name);
    this.player2 = new Player(player2Name);
    this.carromBoard = new CarromBoard(player1, player2, true);
  }

  public String printScore() {
    return (player1.getName() + " : " + player1.getScore()
        + " " + player2.getName() + " : " + player2.getScore());
  }

  public void playCommandLine() {
    play(System.in);
  }

  public void play(InputStream in) {
    int caseType = -1;
    Scanner scanner = new Scanner(in);

    do {
      System.out
          .println(carromBoard.getStriker().getName() + " Choose an outcome from the list below");
      System.out.println("1. STRIKE");
      System.out.println("2. Multistrike");
      System.out.println("3. Red strike");
      System.out.println("4. Striker strike");
      System.out.println("5. Defunct coin");
      System.out.println("6. None");
      System.out.println("7. Exit");
      caseType = scanner.nextInt();
      play(caseType);
      winner = carromBoard.getWinner();
      if (Objects.nonNull(winner)) {
        caseType = 7;
      }
      System.out.println(printScore());
    } while (!(carromBoard.isGameOver() || caseType == 7));

    System.out.println(printScore());
  }

  private void play(int caseType) {
    switch (caseType) {
      case 1:
        strikes(StrikeType.STRIKE);
        break;
      case 2:
        strikes(StrikeType.MULTI_STRIKE);
        break;
      case 3:
        strikes(StrikeType.RED_STRIKE);
        break;
      case 4:
        strikes(StrikeType.STRIKER_STRIKE);
        break;
      case 5:
        strikes(StrikeType.DEFUNCT_COIN);
        break;
      case 6:
        strikes(StrikeType.NO_STRIKE);
        break;
    }
  }

  public void strikes(StrikeType strikeType) {

    switch (strikeType) {
      case NO_STRIKE:
        carromBoard.addStrike(StrikeType.NO_STRIKE);
        carromBoard.changePlayer();
        break;
      case STRIKER_STRIKE:
        carromBoard.addStrike(StrikeType.STRIKER_STRIKE);
        carromBoard.changePlayer();
        break;
      case RED_STRIKE:
        carromBoard.addStrike(StrikeType.RED_STRIKE);
        carromBoard.redCoinsStriked();
        break;
      case STRIKE:
        if (!carromBoard.isLastRedCoin()) {
          carromBoard.coinsStriked();
        }
        carromBoard.addStrike(StrikeType.STRIKE);
        carromBoard.changePlayer();
        break;
      case MULTI_STRIKE:
        carromBoard.addStrike(StrikeType.MULTI_STRIKE);
        carromBoard.changePlayer();
        break;
      case DEFUNCT_COIN:
        carromBoard.addStrike(StrikeType.DEFUNCT_COIN);
        carromBoard.coinsStriked();
        carromBoard.changePlayer();
        break;
    }
    if (carromBoard.isConsecutiveNoStrike()) {
      carromBoard.addStrike(StrikeType.CONSECUTIVE_3_NO_STRIKE);
    }
    if (carromBoard.isFoul()) {
      carromBoard.addStrike(StrikeType.FOUL);
    }
  }

  public Player getCurrentPlayer() {
    return carromBoard.getStriker();
  }

  public int getTotalCoins() {
    return carromBoard.getRedCoin() + carromBoard.getCoins();
  }
}
