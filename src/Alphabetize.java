import java.util.Comparator;

public class Alphabetize implements Comparator<Row> {
  // fields
  int column;
  
  // constructor
  Alphabetize(int column) {
    this.column = column;
  }
  
  // methods
  
  // compares the two given rows alphabetically by the data in column (ignoring case).
  // if one or both rows have no data in column, returns zero.
  public int compare(Row a, Row b) {
    if (a.length() > this.column && b.length() > this.column) {
      return a.data(column).toLowerCase().compareTo(b.data(column).toLowerCase());
    }
    else {
      return 0;
    }
  }
}
