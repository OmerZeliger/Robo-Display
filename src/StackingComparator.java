import java.util.List;
import java.util.Comparator;

public class StackingComparator  implements Comparator<Row> {
  // fields
  List<Comparator<Row>> comparators;
  
  // constructor
  StackingComparator(List<Comparator<Row>> comparators) {
    this.comparators = comparators;
  }
  
  // methods
  
  // compares the two given rows with each comparator in the list (with decreasing priority)
  // a negative int means the first Row comes first, a positive int means the second Row comes first
  public int compare(Row a, Row b) {
    int result = 0;
    for (Comparator<Row> comp : this.comparators) {
      if (result == 0) {
        result = comp.compare(a, b);
      }
      else {
        return result;
      }
    }
    return result;
  }
}
