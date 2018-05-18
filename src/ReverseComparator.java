import java.util.Comparator;

public class ReverseComparator implements Comparator<Row> {
  // fields
  Comparator<Row> comp;
  
  // constructor
  ReverseComparator(Comparator<Row> comp) {
    this.comp = comp;
  }
  
  // methods
  
  // returns the opposite of how comp would compare the two
  public int compare(Row a, Row b) {
    return comp.compare(b,  a);
  }
}
