package ooga.piece.movement;

public class diagFdRight implements Movement{
  private int units;
  public diagFdRight(int units){
    this.units = units;
  }

    @Override
  public String toString() {
    return "Forward{" +
        "units=" + units +
        '}';
  }
}
