import java.util.function.Predicate;

public class KeyPredicate implements Predicate<Row> {
  // fields
  int column;
  String key;
  
  // constructor
  KeyPredicate(int column, String key) {
    this.column = column;
    this.key = key;
  }
  
  // methods

  @Override
  // returns true if the row's data in column column is the same as key
  // returns false if there is no data in column column
  public boolean test(Row row) {
    return this.column < row.length() && row.data(column).equals(this.key);
  }
}
