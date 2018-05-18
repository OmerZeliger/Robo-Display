import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

// accumulates a list of Predicate<Row>
public class PredicateBuilder {
  // fields
  List<Predicate<Row>> predicates;
  
  // constructor
  PredicateBuilder() {
    this.predicates = new LinkedList<Predicate<Row>>();
  }
  
  // methods
  
  // EFFECT: adds a KeyPredicate to the list
  void addPredicate(String key, int column) {
    this.predicates.add(new KeyPredicate(column, key));
  }
  
  // I'll leave this open to add different types of predicates, but I probably won't need them
  
  // returns all of the predicates
  List<Predicate<Row>> getPredicates() {
    return this.predicates;
  }
}
