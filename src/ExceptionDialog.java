// ExceptionDialog.java

import java.awt.*;
import javax.swing.*;

/**
 * ExceptionDialog is used to show exception messages. It is a wrapper for MessageDialog.
 */
public class ExceptionDialog {
   public static void show(Component parent, String title, String message, Throwable ex) {
      StringBuffer sb = new StringBuffer();

      sb.append(message);
      sb.append(" ");
      sb.append(ex.toString());

      MessageDialog.show(
         parent,
         title,
         sb.toString(),
         JOptionPane.WARNING_MESSAGE);
   }

   /*
   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.setSize(500, 300);
      frame.setLocation(0, 0);
      // frame.setVisible(true);

      ExceptionDialog.show(
         frame,
         "Exception",
         "Test",
         new Exception(
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message. " +
            "This is an exception with a long multiline message."));

      System.exit(0);
   }
   */
}
