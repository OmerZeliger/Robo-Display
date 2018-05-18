import java.util.Comparator;

public class VisibleComparator implements Comparator<Row> {
  public int compare(Row a, Row b) {
    if (a.getDisplay() == b.getDisplay()) {
      return 0;
    }
    else if (a.getDisplay()) {
      return -1;
    }
    else {
      return 1;
    }
  }
}
