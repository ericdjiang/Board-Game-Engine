package ooga.piece;

public class Piece {
    private List<Movement> movements;
    private boolean capturable;
    private boolean placeable;
    private int xcoord;
    private int ycoord;
    public Piece(List<Movement> movements, boolean capturable, boolean placeable){
        this.movements = movements;
        this.capturable = capturable;
        this.placeable = placeable;
    }

}
