// TimesheetTableModel.java

import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * TimesheetTableModel is the table model is for the timesheet grid. It is a simple
 * extension of DefaultTableModel is conditional cell editing.
 */
public class TimesheetTableModel extends DefaultTableModel
     implements TableModelListener {

   private Vector columnNames =  new Vector();

   /**
    * Constructs a new TimesheetTableModel.
    */
   public TimesheetTableModel() {
      columnNames.add("Day");
      columnNames.add("Start");
      columnNames.add("End");
      columnNames.add("Meals");
      columnNames.add("Hours");

      addTableModelListener(this);
   }

   public void setDataVector(Object[][] data) {
      setDataVector(data, columnNames.toArray());
   }

   public void setDataVector(Vector data) {
      setDataVector(data, columnNames);
   }

   /**
    * Returns true if the cell at the given row and column is editable. Cells in the
    * first column are not editable, and cells in the bottom row are not editable,
    * except for the bottom-right cell.
    */
   public boolean isCellEditable(int row, int col) {
      return ((row < getLastRow()) && (col > 0)) ||
         ((row == getLastRow()) && (col == getLastColumn()));
   }

   public void tableChanged(TableModelEvent event) {
      if ((event.getColumn() >= 1) && (event.getColumn() <= 3)) {
         calculate();
      }
   }

   public void clear() {
      for (int i = 0; i < getRowCount(); i ++) {
         for (int j = 1; j < getColumnCount(); j ++) {
            setValueAt("", i, j);
         }
      }
   }

   public void load40Hours() {
      for (int i = 0; i < getRowCount(); i ++) {
         for (int j = 1; j <= 3; j ++) {
            if ((i >= 1) && (i <= 5)) {
               switch (j) {
                  case 1:
                     setValueAt("8:00 AM", i, j);
                     break;

                  case 2:
                     setValueAt("5:00 PM", i, j);
                     break;

                  case 3:
                     setValueAt("1:00", i, j);
                     break;
               }
            } else {
               setValueAt("", i, j);
            }
         }
      }
   }

   public void calculate() {
      String       startTime;
      String       endTime;
      String       mealsTime;
      String       hoursTime;
      String       totalHoursTime;
      int          start;
      int          end;
      int          meals;
      float        hours       = 0;
      float        totalHours  = 0;
      NumberFormat hoursFormat = new DecimalFormat("###.##");

      // Calculate hours for current row.

      for (int i = 0; i < getRowCount(); i ++) {
         startTime = ((String) getValueAt(i, 1)).trim().toUpperCase();
         endTime   = ((String) getValueAt(i, 2)).trim().toUpperCase();
         mealsTime = ((String) getValueAt(i, 3)).trim();

         if ((startTime.length() > 0) && (endTime.length() > 0)) {
            start     = timeToMinutes(startTime, true);
            end       = timeToMinutes(endTime, true);
            meals     = timeToMinutes(mealsTime, false);

            if (start == -1) {
               hoursTime = "? (Start)";

            } else if (end == -1) {
               hoursTime = "? (End)";

            } else if (meals == -1) {
               hoursTime = "? (Meals)";

            } else {
               hours      = ((float) (end - start - meals)) / (float) 60;
               totalHours += hours;

               hoursTime  = hoursFormat.format(hours);
            }
         } else {
            hoursTime = "";
         }

         setValueAt(hoursTime, i, getLastColumn());
      }

      totalHoursTime = hoursFormat.format(totalHours);
      setValueAt(totalHoursTime, getLastRow(), getLastColumn());
   }

   public int getLastRow() {
      return getRowCount() - 1;
   }

   public int getLastColumn() {
      return getColumnCount() - 1;
   }

   private int timeToMinutes(String stime, boolean isTwelveHour) {
      String temp;
      int    p1 = -1;
      int    p2 = -1;
      int    p3 = -1;
      int    h;
      int    m;
      int    minutes;

      if (stime.length() == 0) {
         return 0;
      }

      p1 = stime.indexOf(":");

      if ((p1 == -1) || (p1 == (stime.length() - 1))) {
         return -1;
      }

      if (isTwelveHour) {
         p2 = stime.indexOf("AM");
         p3 = stime.indexOf("PM");

         if ((p2 == -1) && (p3 == -1)) {
            return -1;
         }
      }

      try {
         h = Integer.parseInt(stime.substring(0, p1));

         // If AM
         //
         if (p2 > -1) {
            temp = stime.substring(p1 + 1, p2).trim();

            if (temp.length() != 2) {
               return -1;
            }

            m = Integer.parseInt(temp);

            if ((h < 1) || (h > 12) || (m < 0) || (m > 59)) {
               return -1;
            }

            if (h == 12) {
               h = 0;
            }

         // Else if PM

         } else if (p3 > -1) {
            temp = stime.substring(p1 + 1, p3).trim();

            if (temp.length() != 2) {
               return -1;
            }

            m = Integer.parseInt(temp);

            if ((h < 1) || (h > 12) || (m < 0) || (m > 59)) {
               return -1;
            }

            if (h < 12) {
               h = h + 12;
            }

         // Else 24 hour
         //
         } else {
            temp = stime.substring(p1 + 1).trim();

            if (temp.length() != 2) {
               return -1;
            }

            m = Integer.parseInt(temp);

            if ((h < 0) || (h > 23) || (m < 0) || (m > 59)) {
               return -1;
            }
         }

         minutes = (h * 60) + m;
      } catch(NumberFormatException ex) {
         return -1;
      }

      return minutes;
   }

   public void save(File outFile) throws IOException {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));

      try {
         out.writeObject(getDataVector());
      } finally {
         out.close();
      }
   }

   public void load(File inFile) throws IOException, ClassNotFoundException {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(inFile));
      Vector            data;

      try {
         data = (Vector) in.readObject();
         setDataVector(data);
         // calculate();
      } finally {
         in.close();
      }
   }
}
