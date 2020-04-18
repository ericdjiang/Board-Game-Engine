package ooga.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ooga.exceptions.InvalidGridException;
import ooga.exceptions.InvalidPieceException;
import ooga.models.GridModel;
import ooga.parser.TemplateParser;
import ooga.piece.Coordinate;
import ooga.piece.Piece;
import ooga.piece.movement.Movement;
import ooga.piece.movement.MovementFactory;

public class GameController {
  private TemplateParser myTemplateParser;
  private GridModel myGridModel;
  private int playerTurn = 1;
  private Piece selectedPiece;

  public GameController () {
    this.myGridModel = new GridModel();
    this.myTemplateParser = new TemplateParser(myGridModel);
  }

  public void parseFile (String fileName) {
    try {
      myTemplateParser.parseTemplate(fileName);
    } catch (FileNotFoundException | InvalidGridException | InvalidPieceException e) {
      System.out.println(e.getMessage());
    }
    pieceSelected(3,4);
  }

  public List<Coordinate> pieceSelected (int x, int y){
//    this.selectedPiece = piece;

    Set <Coordinate> allValidIndices = new HashSet<>();
//    for (Movement movement: piece.getNormalAnyMovements()) {
//      allValidIndices.addAll(movement.getValidIndices(new Coordinate(x, y), 1, myGridModel ));
//    }
    try {
      Movement forward = new MovementFactory().getMovement("fd", -1);
      allValidIndices.addAll(forward.getValidIndices(new Coordinate(x, y), 1, myGridModel));
      for (Coordinate c: allValidIndices) {
        System.out.println(c.getRow() + " " + c.getCol());
      }
    } catch (Exception e){

    }

    return new ArrayList (allValidIndices);
  }

//  private Set<Coordinate> validateMoves (Set<Coordinate> indicesToCheck) {
//    for (Coordinate c: indicesToCheck) {
//
//    }
//  }
//
//  private Set<Coordinate> removeMovesOutOfBounds (Set <Coordinate> allPossibleIndices) {
//    Set <Coordinate> ret = new HashSet<>();
//    Coordinate bounds = myGridModel.getDimensions();
//    for (Coordinate c: allPossibleIndices) {
//      if(c.getXpos() >= 0 && c.getXpos() < bounds.getXpos()
//          && c.getYpos() >= 0 && c.getYpos() < bounds.getYpos()){
//        ret.add(c);
//      }
//    }
//    return ret;
//  }

  public void moveSelectedPiece (int x, int y) {

  }

  public GridModel getGridModel() {
    return myGridModel;
  }

  public String getGameName() {return myTemplateParser.getGameName();}
}
