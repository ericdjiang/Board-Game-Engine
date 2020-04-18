package ooga.piece.movement;

import ooga.models.GridModel;
import ooga.piece.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class bk implements Movement {
    private int units;
    public bk(int units){
        this.units = units;
    }

    @Override
    public List<Coordinate> getValidIndices(Coordinate position, int playerSide,
        GridModel gridModel) {
        List<Coordinate> moves = new ArrayList<>();
        for(int i=1;i<=units;i++){
            moves.add(new Coordinate(position.getRow(),position.getCol()-i));
        }
        return moves;
    }

    @Override
    public String toString() {
        return "Backward{" +
                "units=" + units +
                '}';
    }
}
