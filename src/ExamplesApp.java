import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.Predicate;

import tester.*;

public class ExamplesApp {
  // examples of rows
  Row row1;
  Row row2;
  Row row3;
  
  Row rowA;
  Row rowB;
  Row rowC;
  
  // examples of spreadsheets
  Spreadsheet sheet1;
  Spreadsheet sheetA;
  
  // examples of comparators
  IDComparator id;
  Alphabetize abc2;
  Alphabetize abc3;
  
  // examples of predicates
  KeyPredicate a2;
  KeyPredicate b2;
  
  // initializing the data
  void init() {
    this.row1 = new Row(new ArrayList<String>(Arrays.asList("0.0", "0.0", "a", "40.5")), 0);
    this.row2 = new Row(new ArrayList<String>(Arrays.asList("1.0", "0.0", "a", "20.5")), 1);
    this.row3 = new Row(new ArrayList<String>(Arrays.asList("2.0", "0.0", "b", "21.5")), 2);
    
    this.rowA = new Row(new ArrayList<String>(Arrays.asList("0.0", "0.0", "v")), 0);
    this.rowB = new Row(new ArrayList<String>(Arrays.asList("1.0", "0.0", "a", "f")), 1);
    this.rowC = new Row(new ArrayList<String>(Arrays.asList("2.0", "0.0", "z", "g")), 2);
    
    this.sheet1 = new Spreadsheet(new ArrayList<Row>(Arrays.asList(row1, row2, row3)));
    this.sheetA = new Spreadsheet(new ArrayList<Row>(Arrays.asList(rowA, rowB, rowC)));
    
    this.id = new IDComparator();
    this.abc2 = new Alphabetize(2);
    this.abc3 = new Alphabetize(3);
    
    this.a2 = new KeyPredicate(2, "a");
    this.b2 = new KeyPredicate(2, "b");
  }
  
  // tests for sort
  void testSort(Tester t) {
    // sorting alphabetically
    this.init();
    LinkedList<Comparator<Row>> sorters = new LinkedList<>();
    sorters.add(this.abc2);
    this.sheetA.sort(sorters, true);
    t.checkExpect(sheetA.data, 
        new ArrayList<Row>(Arrays.asList(rowB, rowA, rowC)));
    
    sorters = new LinkedList<>();
    sorters.add(this.abc2);
    sorters.add(this.abc3);
    this.sheet1.sort(sorters, true);
    t.checkExpect(sheet1.data, 
        new ArrayList<Row>(Arrays.asList(row2, row1, row3)));
    
    // sorting alphabetically with priorities
    this.init();
    sorters = new LinkedList<>();
    sorters.add(this.abc3);
    this.sheetA.sort(sorters, true);
    t.checkExpect(sheetA.data, 
        new ArrayList<Row>(Arrays.asList(rowB, rowC, rowA)));
  }
  
  // tests for filter
  void testFilter(Tester t) {
    // filtering by keys
    this.init();
    LinkedList<Predicate<Row>> filters = new LinkedList<>();
    filters.add(this.a2);
    this.sheet1.filter(filters);
    t.checkExpect(this.row1.display, true);
    t.checkExpect(this.row2.display, true);
    t.checkExpect(this.row3.display, false);
    
    this.init();
    filters = new LinkedList<>();
    this.sheet1.filter(filters);
    t.checkExpect(this.row1.display, false);
    t.checkExpect(this.row2.display, false);
    t.checkExpect(this.row3.display, true);
    
    // filtering using two criteria (OR)
    this.init();
    filters = new LinkedList<>();
    filters.add(this.b2);
    this.sheet1.filter(filters);
    t.checkExpect(this.row1.display, true);
    t.checkExpect(this.row2.display, true);
    t.checkExpect(this.row3.display, true);
  }
}
