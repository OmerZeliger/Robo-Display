import java.util.Comparator;

public class ColumnExistsComparator implements Comparator<Row> {
  // fields
  int column;
  
  // constructor
  ColumnExistsComparator(int column) {
    this.column = column;
  }
  
  // returns a negative int if the first row has column i and the second doesn't,
  // returns a positive int if the second has a column i and the first doesn't,
  // otherwise returns 0
  public int compare(Row a, Row b) {
    int aLength = a.length();
    int bLength = b.length();
    if ((aLength <= this.column && bLength <= this.column)
        || (aLength > this.column && bLength > this.column)) {
      return 0;
    }
    else {
      return bLength - aLength;
    }
  }
}
