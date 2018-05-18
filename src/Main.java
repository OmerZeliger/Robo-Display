import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javafx.application.Application;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
  // fields
  String buttonText = "";
  
  // methods
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Robo Display");
    
    /*
    // just some text
    Label label = new Label("Here's the label");
    
    // a normal, run-of-the-mill button
    Button button = new Button("Button here!");
    button.setWrapText(true);
    button.setOnAction(actionEvent -> {
      this.buttonText = this.buttonText + "Ow! ";
      button.setText(this.buttonText);
    });
    
    // a button that can be turned on or off
    ToggleButton toggle = new ToggleButton("Toggle Button");
    toggle.setOnAction(actionEvent -> {
      toggle.setText(Boolean.toString(toggle.isSelected()));
    });
    
    // checkbox - for locking rows?
    CheckBox checkBox = new CheckBox("Checkbox here! There doesn't need to be text at all :)");
    
    // choicebox - for choosing alphabetize, sort, etc? or is a MenuButton better for this?
    ChoiceBox<String> choiceBox = new ChoiceBox<String>();
    choiceBox.getItems().add("Choice 1");
    choiceBox.getItems().add("Choice 2");
    choiceBox.getItems().add("Choice 3");
    
    // textfield - for filtering/finding
    TextField textField = new TextField();
    
    //VBox vbox = new VBox(textField);
    
    // selecting files:
    FileChooser fileChooser = new FileChooser();
    Button filer = new Button("Click to choose a file");
    filer.setOnAction(actionEvent -> {
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
    });
    
    //VBox vbox = new VBox(filer);
    
    // just for fun
    Label labler = new Label("Click the button!");
    Button textPrinter = new Button("Click here!");
    textPrinter.setOnAction(actionEvent -> {
      labler.setText("You wrote \"" + textField.getText() + "\"");
    });
    
    ToolBar toolBar = new ToolBar();
    toolBar.setOrientation(Orientation.HORIZONTAL);
    toolBar.getItems().add(textField);
    toolBar.getItems().add(new Separator());
    toolBar.getItems().add(textPrinter);
    
    Tooltip tooltip = new Tooltip("This is the text you wrote");
    labler.setTooltip(tooltip);
    
    VBox vbox = new VBox(toolBar, labler);
    
    Scene scene = new Scene(vbox, 200, 200);
    
    primaryStage.setScene(scene);
    primaryStage.show();
     */
    
    /*
    Label one = new Label("1");
    one.setPrefSize(100, 20);
    one.setStyle("-fx-border-color: black;");
    Label two = new Label("2");
    two.setPrefSize(100, 20);
    two.setStyle("-fx-border-color: black;");
    
    Label three = new Label("3");
    three.setPrefSize(100, 20);
    three.setStyle("-fx-border-color: black;");
    Label four = new Label("4");
    four.setPrefSize(100, 20);
    four.setStyle("-fx-border-color: black;");
    
    Label five = new Label("5");
    five.setPrefSize(100, 20);
    five.setStyle("-fx-border-color: black;");
    
    HBox row1 = new HBox();
    row1.getChildren().addAll(one, two);
    row1.relocate(0, 0);
    HBox row2 = new HBox();
    row2.getChildren().addAll(three, four);
    row2.relocate(0, 20);
    HBox row3 = new HBox();
    row3.getChildren().add(five);
    row3.relocate(0, 40);
    
    Pane sheet = new Pane();
    sheet.getChildren().addAll(row1, row2, row3);
     */
    
    /*
    ScrollPane scroll = new ScrollPane(sheet);
    
    Button sortButton = new Button("Sort");
    sortButton.setOnAction(actionEvent -> {
      Heapsort<HBox> heapsort = new Heapsort<HBox>();
      heapsort.heapsort(listy, new LengthComparator());
      for (int i = 0; i < listy.size(); i++) {
        listy.get(i).relocate(0, 20 * i);
      }
    });
    
    Pane sheet = new Pane();
    int length = 30000;
    sheet.setMinHeight(length * 20);
    ArrayList<HBox> listy = new ArrayList<HBox>(length);
    for (int i = 0; i < length; i++) {
      Label a = new Label(Double.toString(i) + "a");
      a.setPrefSize(100, 20);
      a.setStyle("-fx-border-color: black;");
      Label b = new Label(Double.toString(i) + "b");
      b.setPrefSize(100, 20);
      b.setStyle("-fx-border-color: black;");
      Label c = new Label(Double.toString(i) + "c");
      c.setPrefSize(100, 20);
      c.setStyle("-fx-border-color: black;");
      HBox box = new HBox(a, b, c);
      box.relocate(0, 20 * i);
      //sheet.getChildren().add(box);
      listy.add(box);
    }
    
    HBox all = new HBox(scroll, sortButton);
    
    Scene scene = new Scene(all, 200, 200);
    
    scroll.setOnScrollFinished(ScrollEvent -> {
      double position = scroll.getVvalue();
      double screenHeight = scroll.getHeight();
      sheet.getChildren().clear();
      int min = (int) ((length * position) - (position * (screenHeight / 20)));
      min = Math.max(min - 2, 0);
      int max = (int) ((length * position) + ((1 - position) * (screenHeight / 20)));
      max = Math.min(max + 2, length);
      sheet.getChildren().addAll(listy.subList(min, max));
    });
     */
    int length = 50000;
    ArrayList<Row> rows = new ArrayList<Row>(length);
    rows.add(new Row(new ArrayList<String>(Arrays.asList("-1", "-1", "HOI", "head")), 0));
    
    for (int i = 0; i < length; i++) {
      String s = Integer.toString(i);
      Row row;
      if (i % 3 == 0) {
        row = new Row(new ArrayList<String>(Arrays.asList(s, s, "HI", "INFOOOOOOOOOO")), i + 1);
      }
      else if (i % 3 == 1) {
        row = new Row(new ArrayList<String>(Arrays.asList(s, s, "HOI", "info", "infoh")), i + 1);
      }
      else {
        row = new Row(new ArrayList<String>(Arrays.asList(s, s, "HEYA")), i + 1);
      }
      rows.add(row);
    }
    //rows.add(new Row(new ArrayList<String>(Arrays.asList("HOI i'm a long info message rEad mE!!1!")), length));
    
    
    Spreadsheet spreadsheet = new Spreadsheet(rows);
    View view = new View(spreadsheet);
    view.resetScreen();
    //view.zoom = 1.5;
    
    //Button reset = new Button("Click me");
    //reset.setOnAction(actionEvent -> {
    //  view.resetScreen();
    //});
    
    VBox sidebar = view.filterScreen;
    //sidebar.getChildren().add(reset);
    
    VBox lockAtTop = new VBox();
    lockAtTop.getChildren().add(view.headerScreen);
    lockAtTop.getChildren().add(view.lockedScreen);
    lockAtTop.setMaxWidth(view.headerScreen.getMaxWidth());
    lockAtTop.setMinWidth(view.headerScreen.getMaxWidth());
    //lockAtTop.setPrefWidth(view.headerScreen.getMaxWidth());
    //lockAtTop.setMaxHeight(view.headerScreen.getMaxHeight() + view.lockedScreen.getMaxHeight());
    //lockAtTop.setMinHeight(view.headerScreen.getMaxHeight() + view.lockedScreen.getMaxHeight());
    //lockAtTop.setPrefHeight(view.headerScreen.getMaxHeight() + view.lockedScreen.getMaxHeight());
    
    VBox data = new VBox();
    data.getChildren().add(lockAtTop);
    data.getChildren().add(view.scrollScreen);
    data.setAlignment(Pos.TOP_LEFT);
    
    HBox thingy = new HBox();
    thingy.getChildren().add(sidebar);
    thingy.getChildren().add(data);
    
    Scene scene = new Scene(thingy, 525, 500);
    
    primaryStage.setScene(scene);
    primaryStage.show();
    view.resetScreen();
    
    // let's test git :)
  }
  
  public static void main(String[] args) {
    Application.launch(args);
  }
}

class LengthComparator implements Comparator<HBox> {
  public int compare(HBox a, HBox b) {
    return a.getChildren().size() - b.getChildren().size();
  }
}

/* WISHLISH
 * use HBox for rows, VBox for the spreadsheet?
 * ScrollPane for scrolling
 * MenuBar? zooming, opening, etc.
 */
