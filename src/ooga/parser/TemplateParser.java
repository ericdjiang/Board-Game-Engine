package ooga.parser;

import java.time.Period;
import ooga.exceptions.InvalidGridException;
import ooga.exceptions.InvalidPieceException;
import ooga.models.GridModel;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class TemplateParser {
  private JSONObject template;
  private GridParser myGridParser;
  private GridModel myGridModel;

  public TemplateParser(GridModel gridModel) {
    this.myGridModel = gridModel;
  }

  public void parseTemplate (String fileName)
      throws FileNotFoundException, InvalidGridException, InvalidPieceException {
    template = new JSONObject(new JSONTokener(new FileReader(fileName)));
    String title = template.getString("title");

    myGridModel.initGrid(template.getInt("rows"), template.getInt("columns"));

    myGridParser = new GridParser(
        myGridModel,
        template.getJSONArray("grid"),
        template.getJSONObject("pieces")
    );
  }

}
