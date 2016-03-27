// DayColumnCellRenderer.java

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * DayColumnCellRenderer is used to render the day column in the timesheet grid.
 */
public class DayColumnCellRenderer extends DefaultTableCellRenderer {
   public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {
      setValue(value);
      setBackground(Color.lightGray);

      if ((column == 0) && value.equals("Total")) {
         setFont(new Font("Dialog", Font.ITALIC, 12));
      } else {
         setFont(new Font("Dialog", Font.PLAIN, 12));
      }

      return this;
   }
}
