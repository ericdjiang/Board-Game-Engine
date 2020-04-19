package ooga.controller;

import java.io.FileNotFoundException;
import java.util.*;

import ooga.exceptions.InvalidGridException;
import ooga.exceptions.InvalidPieceException;
import ooga.models.GameModel;
import ooga.models.GridModel;
import ooga.parser.PieceParser;
import ooga.parser.TemplateParser;
import ooga.piece.Coordinate;
import ooga.piece.Piece;
import ooga.piece.movement.Movement;

public class GameController {
  private TemplateParser myTemplateParser;
  private GridModel myGridModel;
  private GameModel myGameModel = new GameModel();

  private int activePlayer = 1;
  private final String READY_TO_VIEW = "readyToView";
  private final String READY_TO_MOVE = "readyToMove";
  private String playerStage = READY_TO_VIEW;
  private Piece selectedPiece;
  private boolean changed = false;

  private List<Coordinate> validMoves = new ArrayList<>();

  public GameController () {
    this.myGridModel = new GridModel();
    this.myTemplateParser = new TemplateParser(myGridModel, myGameModel);
  }

  public void parseFile (String fileName) {
    try {
      myTemplateParser.parseTemplate(fileName);
    } catch (FileNotFoundException | InvalidGridException | InvalidPieceException e) {
      System.out.println(e.getMessage());
    }
  }

  public void handleClick(int row, int col) {
//    System.out.println(row+"."+col);

    Coordinate c = new Coordinate(row, col);
    if (myGameModel.isCanPlace()) {
      handlePlaceableClick(c);
    } else {
      handleNonPlaceableClick(c); // for chess style games
    };
  }

  private void handleNonPlaceableClick(Coordinate c) {
    if (activePlayer == 1) {
      switch(playerStage) {
        case READY_TO_VIEW: // get valid moves
//          if(myGridModel.checkPieceExists(c)){
//            System.out.println( myGridModel.getPiece(c).getSide() == activePlayer);
//          }
//          System.out.println(myGridModel);

          if(myGridModel.checkPieceExists(c) && myGridModel.getPiece(c).getSide() == activePlayer){
            selectedPiece = myGridModel.getPiece(c);
            System.out.println("Selected: " + selectedPiece.getPieceName());
            validMoves = myGridModel.getValidMoves(c,activePlayer);
            togglePlayerStage(READY_TO_MOVE);
            setChanged(true);
            System.out.println("Valid moves generated:");
            for (Coordinate move: validMoves) {
              System.out.println(move.getRow() + "" + move.getCol());
            }
          }
          break;
        case READY_TO_MOVE:
          if(myGridModel.checkPieceExists(c) && myGridModel.getPiece(c).getSide() == activePlayer){
            System.out.println("Deactivating selected piece.");
            resetPlayerStage();
          } else {
            System.out.println("Moved piece");
            moveSelectedPiece(c);
          }
          setChanged(true);

          break;
      }
    } else { // if it is the AI's turn
      switch(playerStage) {
        case READY_TO_VIEW:
          List<Coordinate> playerPostions = myGridModel.getPositions(activePlayer);
          while(!playerPostions.isEmpty()){
            int randomIndex = new Random().nextInt(playerPostions.size());
            Coordinate coord = playerPostions.get(randomIndex);
            playerPostions.remove(randomIndex);

            validMoves = myGridModel.getValidMoves(coord, activePlayer);
            if(!validMoves.isEmpty()) {
              selectedPiece = myGridModel.getPiece(coord);

//              System.out.println(selectedPiece.getPieceName() + " "+ selectedPiece.getSide()+ " @ "+ selectedPiece.getPosition());
//              System.out.println("Moving to: " + coord);

              moveSelectedPiece(validMoves.get(new Random().nextInt(validMoves.size())));
              break;
            }
          }
          setChanged(true);
          break;
      }
    }
    System.out.println("Player " + activePlayer + " is " + playerStage + "\n");
  }

  private void handlePlaceableClick(Coordinate c) {
    if (activePlayer == 1) {
      try {
        Piece newPiece = new PieceParser(myGameModel.getPieceJSON()).generatePiece("dime", c.getRow(), c.getCol());
        System.out.println(newPiece.getPosition());
      } catch (InvalidPieceException e) {
        e.printStackTrace();
      }
    } else {

    }
  }

  private void togglePlayerStage (String playerStage) {
//    playerStage = playerStage.equals(READY_TO_MOVE) ? READY_TO_VIEW  : READY_TO_MOVE;
    this.playerStage = playerStage;
  }

  public void moveSelectedPiece (Coordinate c) {
    if (validMoves.contains(c)){
      if(myGridModel.checkPieceExists(c) && selectedPiece.isCanCaptureJump()) {
        // don't switch sides
      } else {
        myGridModel.movePiece(selectedPiece, c);
        switchPlayers();
      }
    }

  }

  private void resetPlayerStage () {
    togglePlayerStage(READY_TO_VIEW);
    selectedPiece = null;
    validMoves.clear();
  }

  private void switchPlayers () {
    resetPlayerStage();

    activePlayer = activePlayer == 1 ? -1 : 1;
  }

  public GridModel getGridModel() {
    return myGridModel;
  }

  public void setChanged (boolean changed) {
    this.changed = changed;
  }

  public boolean isChanged() {
    return this.changed;
  }

  public List<Coordinate> getValidMoves() {
    return validMoves;
  }
  public String getGameName() {return myTemplateParser.getGameName();}

  public Coordinate getSelectedPiecePosition(){
    if (selectedPiece != null) {
      return selectedPiece.getPosition();
    }
    return null;
  }
}
