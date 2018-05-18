import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Spreadsheet {
  // fields
  List<Row> data;
  List<Row> lockedRows;
  List<Predicate<Row>> highlighted;
  int numRowsVisible; // the number of visible rows
  
  // constructor
  Spreadsheet(List<Row> data) {
    this.data = data;
    this.lockedRows = new LinkedList<Row>();
    this.highlighted = new LinkedList<Predicate<Row>>();
    this.numRowsVisible = this.data.size();
  }
  
  // methods
  
  // filtering, sorting, highlighting
  // EFFECT: displays only rows that match one of the given predicates
  void filter(List<Predicate<Row>> filters) {
    Predicate<Row> pred = new OrPredicate(filters);
    this.numRowsVisible = 0;
    for (Row r : this.data) {
      boolean display = pred.test(r);
      r.setDisplay(display);
      if (display) {
        this.numRowsVisible += 1;
      }
    }
  }
  
  // TODO: is this necessary?
  // EFFECT: displays all of the rows
  // TODO: also resets the predicates?
  /*
  void resetFilter() {
    for (Row r : this.data) {
      r.setDisplay(true);
    }
  }
   */
  
  // EFFECT: sorts the rows by order of the comparators (highest priority is first)
  void sort(List<Comparator<Row>> sorters) {
    ArrayList<Comparator<Row>> comparators = new ArrayList<>(sorters.size() + 2);
    comparators.add(new VisibleComparator());
    comparators.addAll(sorters);
    comparators.add(new IDComparator());
    Heapsort<Row> heapsort = new Heapsort<Row>();
    heapsort.heapsort(this.data, new StackingComparator(comparators));
  }
  
  // EFFECT: sorts the rows by their serial ID
  // TODO: also resets the comparators?
  // TODO: is this necessary?
  /*
  void resetSort() {
    Heapsort<Row> heapsort = new Heapsort<Row>();
    heapsort.heapsort(this.data, new IDComparator());
  }
   */
  
  // TODO: is this necessary?
  // EFFECT: highlights any rows that match the predicates
  /*
  void highlight() {
    Predicate<Row> pred = new OrPredicate(this.highlighted);
    for (Row r : this.data) {
      r.setHighlight(pred.test(r));
    }
  }
   */
  
  // EFFECT: makes all of the rows not highlighted
  // TODO: also resets the predicates?
  // TODO: is this necessary?
  /*
  void resetHighlight() {
    for (Row r : this.data) {
      r.setHighlight(false);
    }
  }
   */
  
  // if the row is locked, adds it to the "locked" list. If it's not, removes it.
  // with this method, you always have to make sure the locked boolean lines up with whether it's in the lockedRows list.
  // maybe getting rid of the CheckBoxes and locked boolean altogether would be better?
  // in the normal space, have a button that adds the row to the list, 
  // and have a separate button in the "locked rows" section that removes the row.
  // that might be less bug prone, but it would also mean you can lock a row multiple times
  // (it also means I have to have separate getButton methods for lock and unlock)
  void switchLocked(Row row) {
    boolean contained = this.lockedRows.contains(row);
    boolean locked;
    if (contained) {
      this.lockedRows.remove(row);
      locked = false;
    }
    else {
      this.lockedRows.add(row);
      locked = true;
    }
    row.setLocked(locked);
  }
  
  // returns the requested row
  Row getRow(int i) {
    return this.data.get(i);
  }
  
  // returns the requested locked row
  Row getLockedRow(int i) {
    return this.lockedRows.get(i);
  }
  
  // returns the number of rows in the spreadsheet
  int length() {
    return this.data.size();
  }
  
  int lockedRowsLength() {
    return this.lockedRows.size();
  }
  
  // returns rowsVisible
  int getNumRowsVisible() {
    return this.numRowsVisible;
  }
}

/* WISHLIST
 * first two columns are time stamps
 * third column is key (a string)
 * fourth column and on can vary
 * 
 * need to be able to highlight rows,
 * filter by key,
 * sort specific columns alphabetically or by numeric value,
 * lock specific rows to the top of the screen
 * 
 * ~~~~~~~~~~~~~~~~
 * 
 * create a main that can read CSV and launch the program.
 * needs to be able to create a spreadsheet.
 * what should Main draw and what should Spreadsheet draw?
 * 
 * ~~~~~~~~~~~~~~~~
 * 
 * DISPLAY WISHLIST:
 * draw rows (with lock buttons) (alternate colors?)
 * (do the values need to be text (need to copy/paste)?
 * images probably take up more space but might be faster???)
 * stack rows
 * create layers (GUI versus data) (BorderPane?)
 * scrolling for the stacked rows only
 * 
 * CONSIDER drawing the spreadsheet by columns instead of rows?
 * 
 * inside the GUI:
 * draw a super header with buttons for sorting/filtering
 * draw active filters
 * draw active sorts
 * draw active highlights
 * draw the locked rows at the top
 * add a search bar
 * 
 * how to handle the search function?
 * 
 * there will be several hundred thousand cells - try using a GridView? TableView?
 */