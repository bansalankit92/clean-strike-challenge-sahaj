package model;

import lombok.Getter;

public enum StrikeType {

  STRIKE(1),
  MULTI_STRIKE(2),
  RED_STRIKE(3),
  STRIKER_STRIKE(-1),
  DEFUNCT_COIN(-2),
  NO_STRIKE(0),
  FOUL(-1),
  CONSECUTIVE_3_NO_STRIKE(-1);

  @Getter
  private int points;

  StrikeType(int points) {
    this.points = points;
  }

}
