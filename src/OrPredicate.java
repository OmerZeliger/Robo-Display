import java.util.List;
import java.util.function.Predicate;

public class OrPredicate implements Predicate<Row> {
  // fields
  List<Predicate<Row>> predicates;
  
  // constructor
  OrPredicate(List<Predicate<Row>> predicates) {
    this.predicates = predicates;
  }
  
  // methods
  
  // returns true if any of the predicates are true
  public boolean test(Row row) {
    for (Predicate<Row> pred : this.predicates) {
      if (pred.test(row)) {
        return true;
      }
    }
    return false;
  }
}
