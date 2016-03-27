// MessageDialog.java

import java.awt.*;
import java.text.*;
import javax.swing.*;

/**
 * MessageDialog is used to show messages. It is a convenience wrapper for
 * JOptionPane with word wrapping and multiple show methods.
 */
public class MessageDialog {
   public static void show(Component parent, String title, String message) {
      show(parent, title, message, JOptionPane.INFORMATION_MESSAGE);
   }

   public static void show(Component parent,
         String title, String message, int type) {
      JOptionPane.showMessageDialog(
         parent,
         wrap(message, 50),
         title,
         type);
   }

   /**
    * Build and return a new string that is wrapped at the given width.
    */
   private static String wrap(String aString, int aWidth) {
      StringBuffer sbReturn      = new StringBuffer();
      int          intLength     = aString.length();
      int          intColsAcross = 0;
      char         letter;

      if (aString == null) {
         return "";
      }

      for (int intIdx = 0; intIdx < intLength; intIdx++) {

         // Get letter that we are on.

         letter = aString.charAt(intIdx);

         // If a space near the end of the line then throw a new line. Else,
         // if a carriage return or new line then reset line count. Else, append
         // the letter.

         if  ((intColsAcross >= aWidth) && (letter == ' ')) {
            sbReturn.append('\n');
            intColsAcross = 0;
         } else {
            if ((letter == '\r') || (letter == '\n')) {
               sbReturn.append(letter);
               intColsAcross = 0;
            } else {
               sbReturn.append(letter);
               intColsAcross ++;
            }
         }
      }

      // Return the new string.

      return sbReturn.toString();
   }

   /*
   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.setSize(500, 300);
      frame.setLocation(0, 0);
      // frame.setVisible(true);

      MessageDialog.show(frame, "Test",
         "This is a short message.");

      MessageDialog.show(frame, "Test",
         "This is the first line.\n" +
         "This is a long line. " +
         "This is a long line. " +
         "This is a long line. " +
         "This is a long line. " +
         "This is a long line. " +
         "This is a long line.\n" +
         "This is a the last line.");

      System.exit(0);
   }
   */
}

