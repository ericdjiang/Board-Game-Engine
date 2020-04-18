package ooga.game_view.board;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import ooga.game_view.board.tile.CircleTile;
import ooga.game_view.board.tile.RectangleTile;
import ooga.models.GridModel;

public class GameBoard extends BorderPane {
  private double tileWidth;
  private double tileHeight;
  private double displayWidth;
  private double displayHeight;
  private double boardSideLength;
  private Background boardBackground;

  private HBox displayBox = new HBox();
  private StackPane everything;

  public GameBoard(){
    this.getStyleClass().add("GameBoard");
    boardBackground = this.getBackground();
  }

  public void createGameBoard(GridModel gridModel, List<Color> colors, double width, double height){
    everything = new StackPane();
    everything.setBackground(boardBackground);
    everything.setMaxSize(boardSideLength, boardSideLength);
    BorderPane.setAlignment(everything, Pos.TOP_CENTER);
    calculateSize(width, height);
    int numRowTiles = gridModel.getGrid().length;
    int numColTiles = gridModel.getGrid()[0].length;
    tileWidth = boardSideLength/numRowTiles;
    tileHeight = boardSideLength/numColTiles;
    createPieceDisplay();
    createTiles(gridModel, numRowTiles, numColTiles, colors);
    this.setCenter(everything);
    this.setBottom(displayBox);
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

  private void createPieceDisplay() {
    displayBox.setPrefSize(displayWidth, displayHeight);
    displayBox.getStyleClass().add("displayBox");
  }

  private void createTiles(GridModel grid, int numRow, int numCol, List<Color> colors){
    Group tileGroup = new Group();
    Group pieceGroup = new Group();
    System.out.println(numRow);
    System.out.println(numCol);
    for (int x = 0; x < numCol; x++){
      for (int y= 0; y < numRow; y++){
        System.out.println("row:" + x + "col:" + y);
        Color main = colors.get(0);
        if ((x+y) % 2 == 0){main = colors.get(1);}
          RectangleTile tile = new RectangleTile(tileWidth, tileHeight, x, y, main);
          //CircleTile tile = new CircleTile(tileWidth, tileHeight, x, y, main);
        if(grid.getGrid()[y][x]!= null){
          System.out.println(grid.getGrid()[y][x].getPieceName());
          PieceView piece = new PieceView(tileWidth, tileHeight, x, y);
          pieceGroup.getChildren().addAll(piece);
        }
        tileGroup.getChildren().addAll(tile);
      }
    }
    everything.getChildren().addAll(tileGroup, pieceGroup);
  }

}
