import java.util.ArrayList;

public class Row {
  // fields
  int id; // the original order. Starts from 0
  ArrayList<String> data;
  boolean display;
  boolean highlight;
  boolean locked;
  
  // constructor
  Row(ArrayList<String> data, int id) {
    this.id = id;
    this.data = data;
    this.display = true;
    this.highlight = false;
    this.locked = false;
  }
  
  // methods
  
  // EFFECT: sets highlight
  void setHighlight(boolean highlight) {
    this.highlight = highlight;
  }
  
  // returns the number of columns in the row
  int length() {
    return this.data.size();
  }
  
  // returns the data
  ArrayList<String> getData() {
    return this.data;
  }
  
  // returns the data at the specified column
  String data(int column) {
    return this.data.get(column);
  }
  
  // EFFECT: sets display
  void setDisplay(boolean display) {
    this.display = display;
  }
  
  // gets display
  boolean getDisplay() {
    return this.display;
  }
  
  // returns whether this row is locked or not
  boolean getLocked() {
    return this.locked;
  }
  
  // EFFECT: sets locked
  void setLocked(boolean locked) {
    this.locked = locked;
  }
  
  // returns this Row's id
  int getID() {
    return this.id;
  }
  
  // returns the data at index i
  String getCell(int i) {
    return this.data.get(i);
  }
  
  // overrides equals - two rows are equal if their serial IDs are the same
  @Override
  public boolean equals(Object other) {
    if (other instanceof Row) {
      return this.id == ((Row) other).id;
    }
    else {
      return false;
    }
  }
}
