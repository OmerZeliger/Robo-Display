
// returns a negative int if the first String is a double and the second is not,
// returns zero if they are the same,
// and returns a positive int if the second is a double but the first is not.
public class IsNumberComparator extends NumberComparator {
  // constructor
  IsNumberComparator(int column) {
    super(column);
  }

  // methods
  public int compare(Row a, Row b) {
    if (a.length() > this.column && b.length() > this.column) {
      boolean aIsNum = false;
      boolean bIsNum = false;
      if (this.maybeParseable(a.getCell(this.column))) {
        try {
          Double.parseDouble(a.getCell(this.column));
          aIsNum = true;
        }
        catch (Exception e) { }
      }
      if (this.maybeParseable(b.getCell(this.column))) {
        try {
          Double.parseDouble(b.getCell(this.column));
          bIsNum = true;
        }
        catch (Exception e) { }
      }
      if (aIsNum == bIsNum) {
        return 0;
      }
      else if (aIsNum) {
        return -1;
      }
      else {
        return 1;
      }
    }
    else {
      return 0;
    }
  }
}
