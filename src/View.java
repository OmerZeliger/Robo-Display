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
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
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
import javafx.scene.paint.Color;
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
  Pane controlRowScreen;
  Pane lockedScreen;
  Pane headerScreen; // TODO: label.setTextFill(Color.WHITE);?

  int defaultRowWidth = 75;
  int baseTextSize = 12;
  String alternatingBGColor1 = "FFEEEE";
  String alternatingBGColor2 = "E0EEFF";
  String lockColor = "B0CCFF";
  String headerColor = "8899EE";
  
  List<Integer> rowWidth;
  double zoom;
  
  int keyColumn = 2;
  List<CheckBox> keys;
  List<MenuButton> sorts;
  
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
    
    this.sorts = new ArrayList<MenuButton>(maxWidth);
    for (int i = 0; i < maxWidth; i++) {
      sorts.add(this.sortButton(i));
    }
    
    // creating the locked rows pane
    this.lockedScreen = new Pane();
    //this.lockedScreen.setPrefHeight(this.controller.lockedRowsLength() * this.rowHeight());
    
    // creating the headers pane
    this.headerScreen = new Pane();
    
    // creating the control row!
    this.controlRowScreen = new Pane();
    
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
  // TODO: a comparator builder (or three)!
  void sort() {
    LinkedList<Comparator<Row>> sorters = new LinkedList<>();
    for (int i = 0; i < this.sorts.size(); i++) {
      MenuButton b = this.sorts.get(i);
      if (((RadioMenuItem) b.getItems().get(1)).isSelected()) {
        //sorters.add(new RowExistsComparator(i));
        sorters.add(new Alphabetize(i));
      }
      else if (((RadioMenuItem) b.getItems().get(2)).isSelected()) {
        //sorters.add(new RowExistsComparator(i));
        //sorters.add(new NumberComparator(i));
      }
      else if (((RadioMenuItem) b.getItems().get(3)).isSelected()) {
        //sorters.add(new RowExistsComparator(i));
        //sorters.add(new ReverseComparator(new NumberComparator(i)));
      }
    }
    this.controller.sort(sorters);
    this.resetScreen();
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
  // TODO: combine with drawRows?
  // TODO: filtering
  ArrayList<HBox> drawHeaders() {
    ArrayList<HBox> headers = new ArrayList<HBox>(this.controller.headersLength());
    int depth = 0;
    for (int i = 0; i < this.controller.headersLength(); i++) {
      if (this.controller.headerVisible(i)) {
        HBox header = new HBox();
        CheckBox lockButton = new CheckBox();
        lockButton.setDisable(true);
        header.getChildren().add(lockButton);
        header.getChildren().addAll(this.translate(this.controller.getHeader(i)));
        header.relocate(0, depth * this.rowHeight());
        header.setStyle("-fx-background-color: #" + this.headerColor + ";");
        headers.add(header);
        depth++;
      }
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
  
  // draws the control rows for determining column width and sorting
  HBox drawControlRow() {
    HBox controlRow = new HBox();
    CheckBox lockButton = new CheckBox();
    lockButton.setDisable(true);
    controlRow.getChildren().add(lockButton);
    for (int i = 0; i < this.rowWidth.size(); i++) {
      HBox cell = new HBox();
      cell.getChildren().add(this.minusButton(i));
      cell.getChildren().add(this.plusButton(i));
      cell.getChildren().add(this.sorts.get(i));
      //cell.setPrefWidth(this.rowWidth(i));
      cell.setMaxWidth(this.rowWidth(i));
      cell.setMinWidth(this.rowWidth(i));
      cell.setStyle("-fx-border-color: black; -fx-background-color: blue;");
      controlRow.getChildren().add(cell);
    }
    return controlRow;
  }
  
  // returns a button that reduces the cell size at the given column
  // TODO: remove the minimum row width (30) and change (5)
  Button minusButton(int column) {
    Button minus = new Button("-");
    minus.setPrefSize(this.rowHeight(), this.rowHeight());
    minus.setMinSize(this.rowHeight(), this.rowHeight());
    minus.setPadding(Insets.EMPTY);
    minus.setOnAction(actionEvent -> {
      if (this.rowWidth(column) >= 3 * this.rowHeight() + 5) {
        this.rowWidth.set(column, this.rowWidth.get(column) - 5);
        this.resetScreen();
      }
    });
    return minus;
  }
  
  // returns a button that increases the cell width at the given column
  // TODO: remove the minimum row width (30) and change (5)
  Button plusButton(int column) {
    Button plus = new Button("+");
    plus.setPrefSize(this.rowHeight(), this.rowHeight());
    plus.setMinSize(this.rowHeight(), this.rowHeight());
    plus.setPadding(Insets.EMPTY);
    plus.setOnAction(actionEvent -> {
      this.rowWidth.set(column, this.rowWidth.get(column) + 5);
      this.resetScreen();
    });
    return plus;
  }
  
  // returns a ChoiceBox for sorting
  // TODO: add actual sorting
  // TODO: figure out a way to do priority?
  MenuButton sortButton(int column) {
    MenuButton sort = new MenuButton("Sort:");
    sort.setPrefHeight(this.rowHeight());
    sort.setMinHeight(this.rowHeight());
    sort.setMinWidth(this.rowHeight());
    sort.setPadding(new Insets(-5)); // TODO: does this work with zooming?
    
    RadioMenuItem noSelection = new RadioMenuItem("None");
    RadioMenuItem alphabetize = new RadioMenuItem("Alphabetize");
    RadioMenuItem sortIncreasing = new RadioMenuItem("Sort small to large");
    RadioMenuItem sortDecreasing = new RadioMenuItem("Sort large to small");
    
    noSelection.setOnAction(actionEvent -> {
      this.sort();
    });
    
    alphabetize.setOnAction(actionEvent -> {
      this.sort();
    });
    
    sortIncreasing.setOnAction(actionEvent -> {
      this.sort();
    });
    
    sortDecreasing.setOnAction(actionEvent -> {
      this.sort();
    });
    
    ToggleGroup menuItems = new ToggleGroup();
    menuItems.getToggles().add(noSelection);
    menuItems.getToggles().add(alphabetize);
    menuItems.getToggles().add(sortIncreasing);
    menuItems.getToggles().add(sortDecreasing);
    
    sort.getItems().add(noSelection);
    sort.getItems().add(alphabetize);
    sort.getItems().add(sortIncreasing);
    sort.getItems().add(sortDecreasing);
    
    noSelection.setSelected(true);
    
    return sort;
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
    // TODO: store this as a variable? it will be helpful for dragging to change row width if I decide to do that.
    int width = 0;
    for (int i = 0; i < this.rowWidth.size(); i++) {
      width += this.rowWidth(i);
    }
    
    // drawing the rows
    this.dataScreen.setPrefHeight(this.controller.numRowsVisible() * this.rowHeight());
    //this.dataScreen.setPreFWidth(width);
    this.dataScreen.getChildren().clear();
    this.dataScreen.getChildren().addAll(this.drawRows(this.firstVisibleRow(), this.lastVisibleRow()));
    
    // setting ScrollScreen's width
    //this.scrollScreen.setMinWidth(this.dataScreen.getWidth());
    
    // drawing the locked rows
    // TODO: separate these out?
    
    //this.lockedScreen.setMinHeight(this.controller.lockedRowsLength() * this.rowHeight());
    
    // drawing the header rows
    this.headerScreen.getChildren().clear();
    this.headerScreen.getChildren().addAll(this.drawHeaders());
    // TODO: is setting it to "width" ok? The checkbox on the left adds width too...
    //this.headerScreen.setMaxWidth(width);
    //this.headerScreen.setMinWidth(width);
    //this.headerScreen.setPrefWidth(width);
    this.headerScreen.setMaxHeight(this.headerScreen.getChildren().size() * this.rowHeight());
    this.headerScreen.setMinHeight(this.headerScreen.getChildren().size() * this.rowHeight());
    this.headerScreen.setPrefHeight(this.controller.headersLength() * this.rowHeight());
    
    // drawing the locked rows
    //this.lockedScreen.setMaxWidth(width);
    //this.lockedScreen.setMinWidth(width);
    //this.lockedScreen.setPrefWidth(width);
    this.lockedScreen.setMaxHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.setMinHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.setPrefHeight(this.controller.lockedRowsLength() * this.rowHeight());
    this.lockedScreen.getChildren().clear();
    this.lockedScreen.getChildren().addAll(this.drawLockedRows());
    
    // drawing the control row
    //this.controlRowScreen.setMaxWidth(width);
    //this.controlRowScreen.setMinWidth(width);
    this.controlRowScreen.setMaxHeight(this.rowHeight());
    this.controlRowScreen.setMinHeight(this.rowHeight());
    this.controlRowScreen.getChildren().clear();
    this.controlRowScreen.getChildren().add(this.drawControlRow());
    
    //this.scrollScreen.setPrefWidth(width + 30); // TODO: fix this
  }
}