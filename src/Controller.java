import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javafx.application.Application;
import javafx.geometry.Orientation;
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

public class Controller {
  // fields
  Spreadsheet spreadsheet;
  View view;
  
  // constructor
  Controller(Spreadsheet spreadsheet, View view) {
    this.spreadsheet = spreadsheet;
    this.view = view;
  }
  
  // methods
  
  // EFFECT: filters the spreadsheet
  void filter(List<Predicate<Row>> filters) {
    this.spreadsheet.filter(filters);
  }
  
  // EFFECT: sorts the spreadsheet
  void sort(List<Comparator<Row>> sorters, boolean sortAll) {
    this.spreadsheet.sort(sorters, sortAll);
  }
  
  // finds the longest row and returns its length
  int maxRowLength() {
    int length = 0;
    for (int i = 0; i < this.spreadsheet.length(); i++) {
      length = Math.max(length, this.spreadsheet.getRow(i).length());
    }
    return length;
  }
  
  // returns the number of rows in the spreadsheet
  int length() {
    return this.spreadsheet.length();
  }
  
  // returns the number of locked rows
  int lockedRowsLength() {
    return this.spreadsheet.lockedRowsLength();
  }
  
  // returns the number of headers
  int headersLength() {
    return this.spreadsheet.headersLength();
  }
  
  // returns a CheckBox that controls whether the given row is locked
  CheckBox getCheckBox(int r) { // TODO: THE BUG
    Row row = this.spreadsheet.getRow(r);
    CheckBox lockButton = new CheckBox();
    lockButton.setSelected(row.getLocked());
    lockButton.setOnAction(actionEvent -> {
      this.spreadsheet.switchLocked(row);
      this.view.resetScreen();
    });
    return lockButton;
  }
  
 // returns a CheckBox that controls whether the given row is locked
 CheckBox getLockedCheckBox(int r) {
   Row row = this.spreadsheet.getLockedRow(r);
   CheckBox lockButton = new CheckBox();
   lockButton.setSelected(row.getLocked());
   lockButton.setOnAction(actionEvent -> {
     this.spreadsheet.switchLocked(row);
     this.view.resetScreen();
   });
   return lockButton;
 }
 
 // returns a Button that jumps to the given row when clicked
 Button getJumpToButton(int r) {
   Row row = this.spreadsheet.getLockedRow(r);
   Button jumpToButton = new Button(" Find ");
   jumpToButton.setOnAction(actionEvent -> {
     int loc = 0;
     int rowID = row.getID();
     for (int i = 0; i < this.length(); i++) {
       if (rowID == this.spreadsheet.getRow(i).getID()) {
         loc = i;
       }
     }
     this.view.jumpTo(loc);
   });
   if (! row.getDisplay()) {
     jumpToButton.setDisable(true);
   }
   return jumpToButton;
 }
 
 // is the given header row visible?
 boolean headerVisible(int row) {
   return this.spreadsheet.getHeader(row).getDisplay();
 }
  
  // returns a list of strings representing the data in the given row
  ArrayList<String> getRow(int row) {
    return this.spreadsheet.getRow(row).getData();
  }
  
  // returns a list of strings representing the data in the locked row at the given index
  ArrayList<String> getLockedRow(int row) {
    return this.spreadsheet.getLockedRow(row).getData();
  }
  
  // returns a list of strings representing the requested header
  ArrayList<String> getHeader(int row) {
    return this.spreadsheet.getHeader(row).getData();
  }
  
  // returns the number of visible rows
  int numRowsVisible() {
    return this.spreadsheet.getNumRowsVisible();
  }
}