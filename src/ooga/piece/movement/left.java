package ooga.piece.movement;

public class left implements Movement {
    private int units;
    public left(int units){
        this.units = units;
    }

    @Override
    public String toString() {
        return "LeftWard{" +
                "units=" + units +
                '}';
    }
}
