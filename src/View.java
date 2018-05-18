import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// displays stuff on the screen
public class View {
  // fields
  Spreadsheet spreadsheet;
  Controller controller;
  
  Pane dataScreen;
  ScrollPane scrollScreen;
  VBox filterScreen;
  Pane lockedScreen;
  Pane headerScreen;

  int defaultRowWidth = 75;
  int baseTextSize = 12;
  String alternatingBGColor1 = "FFEEEE";
  String alternatingBGColor2 = "E0EEFF";
  String lockColor = "B0CCFF";
  String headerColor = "2255BB";
  
  List<Integer> rowWidth;
  double zoom;
  
  int keyColumn = 2;
  List<CheckBox> keys;
  List<HBox> lockedRows;
  
  // constructor
  View(Spreadsheet spreadsheet) {
    // easy to find values
    this.controller = new Controller(spreadsheet, this);
    this.spreadsheet = spreadsheet;
    this.zoom = 1;
    this.dataScreen = new Pane();
    this.dataScreen.setPrefHeight(this.controller.numRowsVisible() * this.rowHeight());
    this.scrollScreen = new ScrollPane(this.dataScreen);
    
    // I don't really understand how this next part works. Research change listeners and observable value later.
    this.scrollScreen.vvalueProperty().addListener(changeListener -> {
      this.resetScreen();
    });
    
    /*
    this.scrollScreen.vvalueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> ov,
          Number old_val, Number new_val) {
        System.out.println(new_val.doubleValue());
      }
    });
     */
    
    this.scrollScreen.setPrefWidth(450); // TODO: revisit, make this not terrible
    
    // harder to find values
    int maxWidth = this.controller.maxRowLength();
    ArrayList<Integer> rowWidth = new ArrayList<Integer>(maxWidth);
    for (int i = 0; i < maxWidth; i++) {
      rowWidth.add(this.defaultRowWidth);
    }
    this.rowWidth = rowWidth;
    
    // finding all unique keys
    // TODO: make the sidebar zoomable
    LinkedList<String> uniqueKeys = new LinkedList<String>();
    for (int i = 0; i < this.controller.length(); i++) {
      String key = this.controller.getRow(i).get(this.keyColumn);
      if (! uniqueKeys.contains(key)) {
        uniqueKeys.add(key);
      }
    }
    this.keys = new ArrayList<CheckBox>(uniqueKeys.size());
    for (String s : uniqueKeys) {
      CheckBox filterButton = new CheckBox(s);
      filterButton.setSelected(true);
      filterButton.setOnAction(actionEvent -> {
        this.filter();
      });
      this.keys.add(filterButton);
    }
    this.filterScreen = new VBox();
    this.filterScreen.setPrefWidth(75); // TODO: revisit this
    this.filterScreen.getChildren().add(new Label("Show:"));
    this.filterScreen.getChildren().addAll(this.keys);
    
    // creating the locked rows pane
    this.lockedScreen = new Pane();
    //this.lockedScreen.setPrefHeight(this.controller.lockedRowsLength() * this.rowHeight());
    
    // creating the headers pane
    this.headerScreen = new Pane();
    
    // drawing all the stuff
    // TODO: add all necessary components to a scene
    // so far: the central scroll pane, the filter sidebar
    
    // TODO: is resetScreen necessary here?
    this.resetScreen();
  }
  
  // TODO: BorderPane for the outside?
  
  // TODO: delegate predicate creation to the controller
  // EFFECT: tells the controller to filter (passes all necessary predicates to the model's OrPredicate)
  // must always sort after filtering!
  void filter() {
    PredicateBuilder builder = new PredicateBuilder();
    // goes through the key-filters first
    for (CheckBox b : this.keys) {
      if (b.isSelected()) {
        builder.addPredicate(b.getText(), this.keyColumn);
      }
    }
    // TODO: add custom filters too?
    
    this.controller.filter(builder.getPredicates());
    this.sort();
    this.resetScreen();
  }
  
  // EFFECT: sorts the data
  // this is a stub for now
  void sort() {
    this.controller.sort(new ArrayList<Comparator<Row>>());
  }
  
  // methods for zooming
  // returns the current text size
  int textSize() {
    return (int) (this.baseTextSize * this.zoom);
  }
  
  // returns the current row height
  int rowHeight() {
    return (int) (1.5 * this.baseTextSize * this.zoom);
  }
  
  // returns the row width at the given column
  int rowWidth(int column) {
    return (int) (this.rowWidth.get(column) * this.zoom);
  }
  
  // TODO: make less messy!
  /**
   * Translates the requested rows into VBoxes with the appropriate positions.
   * Start inclusive, end not inclusive.
   * 
   * @param start
   * @param end
   */
  ArrayList<HBox> drawRows(int start, int end) {
    ArrayList<HBox> rows = new ArrayList<HBox>(end - start);
    if (end <= start) { return rows; }
    for (int i = start; i < end; i++) {
      HBox row = new HBox();
      //row.setAlignment(Pos.CENTER_LEFT);
      CheckBox lockButton = this.controller.getCheckBox(i);
      //lockButton.setPrefSize(this.textSize(), this.textSize());
      row.getChildren().add(lockButton);
      row.getChildren().addAll(this.translate(this.controller.getRow(i)));
      row.relocate(0, i * this.rowHeight());
      if (i % 2 == 1) {
        row.setStyle("-fx-background-color: #" + this.alternatingBGColor2 + ";");
      }
      else {
        row.setStyle("-fx-background-color: #" + this.alternatingBGColor1 + ";");
      }
      rows.add(row);
    }
    return rows;
  }
  
  // draws all of the locked rows
  // TODO: combine with drawRows?
  ArrayList<HBox> drawLockedRows() {
    ArrayList<HBox> rows = new ArrayList<HBox>(this.controller.lockedRowsLength());
    for (int i = 0; i < this.controller.lockedRowsLength(); i++) {
      HBox row = new HBox();
      CheckBox lockButton = this.controller.getLockedCheckBox(i); // TODO: combine with getCheckBox?
      row.getChildren().add(lockButton);
      row.getChildren().addAll(this.translate(this.controller.getLockedRow(i)));
      row.relocate(0, i * this.rowHeight());
      row.setStyle("-fx-background-color: #" + this.lockColor + ";");
      rows.add(row);
      // TODO: for filtering, keep track of numVisibleLockedRows?
    }
    return rows;
  }
  
  // draws all of the headers
  ArrayList<HBox> drawHeaders() {
    ArrayList<HBox> headers = new ArrayList<HBox>(this.controller.headersLength());
    for (int i = 0; i < this.controller.headersLength(); i++) {
      HBox header = new HBox();
      CheckBox lockButton = new CheckBox();
      lockButton.setDisable(true);
      header.getChildren().add(lockButton);
      header.getChildren().addAll(this.translate(this.controller.getHeader(i)));
      header.relocate(0, i * this.rowHeight());
      header.setStyle("-fx-background-color: #" + this.headerColor + "; -fx-text-color: white;");
      headers.add(header);
    }
    return headers;
  }
  
  /**
   * turns the ArrayList or Strings into an ArrayList of Labels
   * 
   * @param strings
   */
  ArrayList<Label> translate(ArrayList<String> strings) {
    ArrayList<Label> labels = new ArrayList<Label>(strings.size());
    Label cell = new Label();
    this.formatCell(cell);
    for (int i = 0; i < strings.size(); i++) {
      cell = new Label(strings.get(i));
      this.formatCell(cell);
      cell.setPrefWidth(this.rowWidth(i)); 
      labels.add(cell);
    }
    
    // making sure that the right sides of the last cell of each row line up
    int width = 0;
    int i = strings.size() - 1;
    while (i < this.rowWidth.size()) {
      width += this.rowWidth(i);
      i++;
    }
    cell.setPrefWidth(width);
    return labels;
  }
  
  // EFFECT: properly formats the given cell
  void formatCell(Label cell) {
    Font font = new Font("Arial", this.textSize());
    cell.setFont(font);
    cell.setPrefHeight(this.rowHeight());
    cell.setStyle("-fx-border-color: black;");
  }
  
  // what is the first on-screen row?
  int firstVisibleRow() {
    double position = this.scrollScreen.getVvalue();
    double screenHeight = this.scrollScreen.getHeight();
    //TODO: simplify this? It wouldn't be much less efficient to just draw all screenHeight rows in both directions...
    return Math.max(0, 
        (int) ((this.controller.numRowsVisible() * position) - (position * screenHeight / this.rowHeight())) - 2);
  }
  
  // what is the last on-screen row?
  int lastVisibleRow() {
    double position = this.scrollScreen.getVvalue();
    double screenHeight = this.scrollScreen.getHeight();
    return Math.min(this.controller.numRowsVisible(), 
        (int) ((this.controller.numRowsVisible() * position) + ((1 - position) * (screenHeight / this.rowHeight()))) + 2);
  }
  
  // EFFECT: resets the screen
  void resetScreen() {
    // drawing the rows
    this.dataScreen.setPrefHeight(this.controller.numRowsVisible() * this.rowHeight());
    this.dataScreen.getChildren().clear();
    this.dataScreen.getChildren().addAll(this.drawRows(this.firstVisibleRow(), this.lastVisibleRow()));
    
    // drawing the locked rows
    // TODO: separate these out?
    
    //this.lockedScreen.setMinHeight(this.controller.lockedRowsLength() * this.rowHeight());
    
    // TODO: store this as a variable? it will be helpful for dragging to change row width if I decide to do that.
    int width = 0;
    for (int i = 0; i < this.rowWidth.size(); i++) {
      width += this.rowWidth(i);
    }
    
    this.headerScreen.setMaxWidth(width);
    this.headerScreen.setMinWidth(width);
    this.headerScreen.setPrefWidth(width);
    this.headerScreen.setMaxHeight(this.controller.headersLength() * this.rowHeight());
    this.headerScreen.setMinHeight(this.controller.headersLength() * this.rowHeight());
    this.headerScreen.setPrefHeight(this.controller.headersLength() * this.rowHeight());
    this.headerScreen.getChildren().clear();
    this.headerScreen.getChildren().addAll(this.drawHeaders());
    
    this.lockedScreen.setMaxWidth(width);
    this.lockedScreen.setMinWidth(width);
    this.lockedScreen.setPrefWidth(width);
    this.lockedScreen.setMaxHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.setMinHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.setPrefHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.getChildren().clear();
    this.lockedScreen.getChildren().addAll(this.drawLockedRows());
  }
}