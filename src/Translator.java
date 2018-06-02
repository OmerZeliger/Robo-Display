import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Predicate;

// a class to translate a CSV file to Spreadsheet and Row format
public class Translator {
  // fields

  // all header rows will have a timestamp of -1
  Predicate<Row> headerChooser = new KeyPredicate(0, "-1");

  // methods

  // translates the given file into a Spreadsheet
  // modified from https://howtodoinjava.com/core-java/io/parse-csv-files-in-java/
  // TODO: header predicate, modify Spreadsheet constructor to take in a list of headers as well
  Spreadsheet translate(File file) {

    BufferedReader fileReader = null;
    LinkedList<Row> rows = new LinkedList<Row>();
    LinkedList<Row> headers = new LinkedList<Row>();
    try {
      fileReader = new BufferedReader(new FileReader(file));
      String line = fileReader.readLine();
      int i = 0;
      while (line != null) {
        String[] info = line.split(",");
        ArrayList<String> cells = new ArrayList<String>(info.length);
        Collections.addAll(cells, info);
        Row row = new Row(cells, i);
        // keeping track of all header rows
        if (this.headerChooser.test(row)) {
          headers.add(row);
        }
        //else { //uncomment this if the headers will always be at the start
        //  break;
        //}

        else {
          rows.add(row);
        }
        line = fileReader.readLine();
        i+=1;
      }
      fileReader.close();
      ArrayList<Row> allRows = new ArrayList<Row>(rows.size());
      allRows.addAll(rows);

      ArrayList<Row> allHeaders = new ArrayList<Row>(headers.size());
      allHeaders.addAll(headers);
      
      Spreadsheet spreadsheet = new Spreadsheet(allRows, allHeaders);
      return spreadsheet;
    }

    catch (FileNotFoundException e) {
      throw new RuntimeException("File does not exist");
    }
    catch (IOException e) {
      throw new RuntimeException("Could not close file reader");
    }
  }
}