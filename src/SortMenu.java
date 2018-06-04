import javafx.geometry.Insets;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

// a class containing a menu button for sorting
public class SortMenu {
  // fields
  MenuButton sortButton;

  RadioMenuItem alphabetize;
  RadioMenuItem sortIncreasing;
  RadioMenuItem sortDecreasing;
  
  String highlightColor = "AABBFF";

  // constructor
  SortMenu(View view, ToggleGroup singleSort) {
    this.sortButton = new MenuButton("Sort");
    this.sortButton.setPadding(new Insets(-5));
    this.sortButton.setPrefHeight(view.rowHeight());
    this.sortButton.setMinSize(view.rowHeight(), view.rowHeight());
    
    this.alphabetize = new RadioMenuItem("Alphabetize");
    this.sortIncreasing = new RadioMenuItem("Sort small to large");
    this.sortDecreasing = new RadioMenuItem("Sort large to small");

    this.alphabetize.setOnAction(actionEvent -> {
      view.sort(false);
    });

    this.sortIncreasing.setOnAction(actionEvent -> {
      view.sort(false);
    });

    this.sortDecreasing.setOnAction(actionEvent -> {
      view.sort(false);
    });

    singleSort.getToggles().add(this.alphabetize);
    singleSort.getToggles().add(this.sortIncreasing);
    singleSort.getToggles().add(this.sortDecreasing);

    this.sortButton.getItems().add(this.alphabetize);
    this.sortButton.getItems().add(this.sortIncreasing);
    this.sortButton.getItems().add(this.sortDecreasing);
  }

  // methods

  // gets the sortButton
  MenuButton getSortButton() {
    return this.sortButton;
  }

  // deselects all of the options
  void deselectAll() {
    this.alphabetize.setSelected(false);
    this.sortIncreasing.setSelected(false);
    this.sortDecreasing.setSelected(false);
  }

  // is alphabetize selected?
  boolean alphabetizeSelected() {
    return this.alphabetize.isSelected();
  }

  // is sortIncreasing selected?
  boolean sortIncreasingSelected() {
    return this.sortIncreasing.isSelected();
  }

  // is sortDecreasing selected?
  boolean sortDecreasingSelected() {
    return this.sortDecreasing.isSelected();
  }
  
  // sets the text to the proper value according to which option is selected
  void rename() {
    if (this.alphabetize.isSelected()) {
      this.sortButton.setText("abc");
      this.sortButton.setStyle("-fx-background-color: #" + this.highlightColor + ";");
    }
    else if (this.sortIncreasing.isSelected()) {
      this.sortButton.setText("123");
      this.sortButton.setStyle("-fx-background-color: #" + this.highlightColor + ";");
    }
    else if (this.sortDecreasing.isSelected()) {
      this.sortButton.setText("321");
      this.sortButton.setStyle("-fx-background-color: #" + this.highlightColor + ";");
    }
    else {
      this.sortButton.setText("Sort");
      this.sortButton.setStyle(null);
    }
  }
}
