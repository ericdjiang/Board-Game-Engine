package ooga.game_view.board;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import ooga.controller.GameController;
import ooga.game_view.board.availableShapes.ComponentShape;
import ooga.game_view.board.tile.RectangleTile;
import ooga.models.GridModel;
import ooga.piece.Coordinate;

public class GameBoard extends BorderPane {
  private GameController gameController;
  private GridModel gridModel;
  private String currentStyleSheet;
  private String pieceShape;
  private String tileShape;
  private double tileWidth;
  private double tileHeight;
  private double displayWidth;
  private double displayHeight;
  private double boardSideLength;
  private Background boardBackground;

  private ExtraPiecesDisplay pieceDisplayBox;
  private StackPane boardDisplay;

  private int numRowTiles;
  private int numColTiles;
  private List<Color> availableColors;
  private List<String> PieceNames;

  public GameBoard(List<Color> gameColors){
    this.getStyleClass().add("GameBoard");
    boardBackground = this.getBackground();
    availableColors = gameColors;
    pieceShape = "Ellipse";
    tileShape = "Circle";
  }

  public void createGameBoard(GameController gameController, String styleSheet, double width, double height){
    this.gameController = gameController;
    this.currentStyleSheet = styleSheet;
    this.gridModel = gameController.getGridModel();

    boardDisplay = new StackPane();
    boardDisplay.setBackground(boardBackground);
    boardDisplay.setMaxSize(boardSideLength, boardSideLength);
    BorderPane.setAlignment(boardDisplay, Pos.TOP_CENTER);
    calculateSize(width, height);

    numRowTiles = gridModel.getGrid().length;
    numColTiles = gridModel.getGrid()[0].length;
    tileWidth = boardSideLength/numRowTiles;
    tileHeight = boardSideLength/numColTiles;
    pieceDisplayBox = new ExtraPiecesDisplay(displayWidth, displayHeight, gameController, currentStyleSheet);
    populateBoard();
    this.setCenter(boardDisplay);
    this.setBottom(pieceDisplayBox);
  }

  public void updateDisplay () {
    boardDisplay.getChildren().clear();
    populateBoard();
  }

  public void updateColors(List<Color> newColors){
    availableColors = newColors;
    updateDisplay();
  }

  public ExtraPiecesDisplay getPieceDisplayBox(){
    return pieceDisplayBox;
  }

  private void calculateSize(double dispWidth, double dispHeight) {
    displayWidth = dispWidth * 0.80;
    displayHeight = dispHeight * 0.14;
    boardSideLength = dispHeight - displayHeight;
    if (displayWidth < boardSideLength) {
      boardSideLength = displayWidth * 0.62;
    }
    this.setMaxSize(displayWidth, dispHeight);
  }

  private void populateBoard(){
    Group tileGroup = new Group();
    Group pieceGroup = new Group();
    for (int col = 0; col < numColTiles; col++){
      for (int row= 0; row < numRowTiles; row++) {
        Color tileColor = availableColors.get(0);
        if ((col + row) % 2 == 0) {
          tileColor = availableColors.get(1);
        }
        createTileAndPiece(tileGroup, pieceGroup, row, col, tileColor);
      }
    }
    boardDisplay.getChildren().addAll(tileGroup, pieceGroup);
  }

  private void createTileAndPiece(Group tileGroup, Group pieceGroup, int row, int col, Color tileColor){
    List<Coordinate> validCoordinates = gameController.getValidMoves();
    Shape tile = createTile(row, col, tileColor);
    if(new Coordinate(row, col).equals(gameController.getSelectedPiecePosition())){
      tile.setFill(Color.LIGHTBLUE);
    }else if (validCoordinates.contains(new Coordinate(row,col))) {
      tile.setFill(Color.LIGHTGREEN);
    }
    ComponentShape piece = createPiece(row, col, tileColor);
    if(gridModel.getGrid()[row][col]!= null){
      piece.setColor(availableColors.get(2));
      piece.addName(gridModel.getGrid()[row][col].getPieceName());
      if(gridModel.getGrid()[row][col].getSide() == -1){
        piece.setColor(availableColors.get(3));
      }
    }
    pieceGroup.getChildren().addAll(piece);
    tileGroup.getChildren().addAll(tile);
  }

  private ComponentShape createPiece(int row, int col, Color tileColor) {
    try {
      Class<?> cls = Class.forName("ooga.game_view.board.pieceType."+pieceShape+"Piece");
      Object objectPiece;
      Constructor constructor = cls.getConstructor(GameController.class, double.class, double.class, int.class, int.class, Color.class);
      objectPiece = constructor.newInstance(gameController, tileWidth, tileHeight, row, col, tileColor);
      return (ComponentShape) objectPiece;
    }catch (ClassNotFoundException| NoSuchMethodException| IllegalAccessException| InvocationTargetException| InstantiationException e) {
      //Never Reached because user choices are limited under settings
      return null;
    }
  }

  private Shape createTile (int row, int col, Color tileColor) {
    try {
      Class<?> cls = Class.forName("ooga.game_view.board.tile."+tileShape+"Tile");
      Object objectTile;
      Constructor constructor = cls.getConstructor(GameController.class, double.class, double.class, int.class, int.class, Color.class);
      objectTile = constructor.newInstance(gameController, tileWidth, tileHeight, row, col, tileColor);
      return (Shape) objectTile;
    }catch (ClassNotFoundException| NoSuchMethodException| IllegalAccessException| InvocationTargetException| InstantiationException e) {
      //Never Reached because user choices are limited under settings
      return null;
    }
  }
}
