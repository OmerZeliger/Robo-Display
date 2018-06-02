import java.util.Comparator;
import java.util.regex.Pattern;

// so this entire comparator is borked,
// but luckily if I think of a better way I can just create a 
// new class and do it there without affecting much else



// returns 0 if the column doesn't exist or is not a double,
// otherwise returns a negative int if the first double is smaller
public class NumberComparator implements Comparator<Row> {
  // fields
  int column;

  // constructor
  NumberComparator(int column) {
    this.column = column;
  }

  // my first shot at exception handling... For some reason it's really slow
  public int compare(Row a, Row b) {
    if (a.length() > this.column && b.length() > this.column//) {
        && this.maybeParseable(a.getCell(column)) && this.maybeParseable(b.getCell(column))) {
      try {
        double numA = Double.parseDouble(a.getCell(column));
        double numB = Double.parseDouble(b.getCell(column));
        // normally "return numA - numB;" would work, but compare needs to return an int
        // and rounding doesn't work because it might round down a small positive number to 0
        if (numA < numB) {
          return -1;
        }
        else if (numA == numB) {
          return 0;
        }
        else {
          return 1;
        }
      }
      catch (Exception e) {
        return 0;
      }
    }
    else {
      return 0;
    }
  }


  // this is kinda hacky, but at least sorting non-number columns doesn't take five seconds anymore
  // yay "optimization"???

  // checks whether the first letter of the given String is a number
  // this reduces the number of exceptions thrown in the try-catch inside compare
  boolean maybeParseable(String s) {
    if (s.length() <= 0) {
      return false;
    }
    String firstLetter = s.substring(0, 1);
    return firstLetter.equals("0")
        || firstLetter.equals("1")
        || firstLetter.equals("2")
        || firstLetter.equals("3")
        || firstLetter.equals("4")
        || firstLetter.equals("5")
        || firstLetter.equals("6")
        || firstLetter.equals("7")
        || firstLetter.equals("8")
        || firstLetter.equals("9")
        || firstLetter.equals("-")
        || firstLetter.equals(".");
  }
}
