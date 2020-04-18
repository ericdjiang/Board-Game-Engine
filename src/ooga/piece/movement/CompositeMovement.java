package ooga.piece.movement;

import ooga.piece.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CompositeMovement implements Movement {
    private List<Movement> movements;

    public CompositeMovement(){
        movements = new ArrayList<>();
    }

    public void add(Movement nextMovement){
        movements.add(nextMovement);
    }

    public List<Movement> movements(){
        return this.movements;
    }

    @Override
    public List<Coordinate> validMoves(Coordinate position, int playerSide) {
        List<Coordinate> validCoordinates = new ArrayList<>();
        validCoordinates.add(position);
        for(int i=0;i<movements.size();i++){
            List<Coordinate> newValidMovements = new ArrayList<>();
            for(int k=0;k<validCoordinates.size();k++){
                newValidMovements.addAll(movements.get(i).validMoves(validCoordinates.get(k), 1));
            }
            validCoordinates = new ArrayList<>(newValidMovements);
        }
        return validCoordinates;
    }

}
