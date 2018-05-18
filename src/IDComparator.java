import java.util.Comparator;

public class IDComparator implements Comparator<Row> {
  // methods
  
  // returns a negative number if a's id is smaller than b's
  // and a positive number if a's id is larger than b's
  public int compare(Row a, Row b) {
    return a.getID() - b.getID();
  }
}
