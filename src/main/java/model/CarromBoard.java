package model;

import exception.NoCoinException;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CarromBoard {

  public static final int CONSECUTIVE_NO_STRIKE_LIMIT = 3;
  public static final int CONSECUTIVE_LOOSING_LIMIT = 3;
  public static final int NO_OF_COINS = 9;
  public static final int NO_OF_RED_COINS = 1;
  public static final int MIN_WINNING_POINTS = 5;
  public static final int WIN_POINT_DIFF = 3;


  private int coins;
  private int redCoin;
  private Player striker;

  private Player player1;
  private Player player2;

  public CarromBoard() {
    this(true);
  }

  public CarromBoard(boolean player1Strikes) {
    this(new Player(), new Player(), player1Strikes);
  }

  public CarromBoard(@NonNull Player player1, @NonNull Player player2, boolean player1Strikes) {
    this.player1 = player1;
    this.player2 = player2;
    this.coins = NO_OF_COINS;
    this.redCoin = NO_OF_RED_COINS;
    selectPlayerStrikesFirst(player1Strikes);
  }

  private void selectPlayerStrikesFirst(boolean player1Strikes) {
    if (player1Strikes) {
      this.striker = this.player1;
    } else {
      this.striker = this.player2;
    }
  }

  public Player getWinner() {
    if (((player1.getScore() >= MIN_WINNING_POINTS) || (player2.getScore() >= MIN_WINNING_POINTS))
        && Math.abs(player1.getScore() - player2.getScore()) >= WIN_POINT_DIFF) {
      return player1.getScore() > player2.getScore() ? player1 : player2;
    }
    return null;
  }

  public boolean isConsecutiveNoStrike() {
    boolean isConsecutive = false;
    List<StrikeType> points = striker.getStrikes();
    int size = points.size();
    if (size >= CONSECUTIVE_NO_STRIKE_LIMIT) {
      boolean isAllNoStrike = true;
      for (int i = size - CONSECUTIVE_NO_STRIKE_LIMIT; i < size; i++) {
        if (!StrikeType.NO_STRIKE.equals(points.get(i))) {
          isAllNoStrike = false;
        }
      }
      isConsecutive = isAllNoStrike;
    }
    return isConsecutive;
  }

  public boolean isFoul() {
    boolean isLoosingPoint = false;
    List<StrikeType> points = striker.getStrikes();
    int size = points.size();
    if (size >= CONSECUTIVE_LOOSING_LIMIT) {
      boolean isAllNoStrike = true;
      for (int i = size - CONSECUTIVE_LOOSING_LIMIT; i < size; i++) {
        if (!isLoosingAPoint(points.get(i))) {
          isAllNoStrike = false;
        }
      }
      isLoosingPoint = isAllNoStrike;
    }
    return isLoosingPoint;
  }

  public boolean isDraw() {
    boolean draw = false;
    if (isGameOver()) {
      Player winner = getWinner();
      if (Objects.isNull(winner)) {
        draw = true;
      }
    }
    return draw;
  }

  public void changePlayer() {
    if (striker.getName().equals(player1.getName())) {
      striker = player2;
    } else {
      striker = player1;
    }
  }

  public void addStrike(StrikeType strikeType) {
    striker.getStrikes().add(strikeType);
    addPoints(strikeType.getPoints());
  }

  public void addPoints(int points) {
    striker.addScore(points);
  }

  public boolean isLastRedCoin() {
    if (!striker.getStrikes().isEmpty()) {
      return StrikeType.RED_STRIKE
          .equals(striker.getStrikes().get(striker.getStrikes().size() - 1));
    }
    return false;
  }

  public void redCoinsStriked() {
    if (redCoin != 0) {
      redCoin--;
    } else {
      throw new NoCoinException();
    }
  }

  public void coinsStriked() {
    if (coins != 0) {
      coins--;
    } else {
      throw new NoCoinException();
    }
  }

  public boolean isGameOver() {
    return coins == 0 && redCoin == 0;
  }

  private boolean isLoosingAPoint(StrikeType strikeType) {
    return StrikeType.FOUL.equals(strikeType) || StrikeType.STRIKER_STRIKE.equals(strikeType)
        || StrikeType.CONSECUTIVE_3_NO_STRIKE.equals(strikeType) ||
        StrikeType.DEFUNCT_COIN.equals(strikeType);
  }

}
