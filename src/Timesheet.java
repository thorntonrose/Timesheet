// Timesheet.java

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * Timesheet is the main class of the timesheet application.
 */
public class Timesheet extends JFrame {
   private static final String VERSION = "1.0";
   private static final String TITLE   = "Timesheet";

   private TimesheetTableModel timesheetModel = new TimesheetTableModel();
   private JFileChooser        fileChooser = new JFileChooser(System.getProperty("user.dir"));
   private File                dataFile;

   private BorderLayout    mainBorderLayout   = new BorderLayout();
   private JPanel          tableCardPanel     = new JPanel();
   private JScrollPane     tableScrollPane    = new JScrollPane();
   private JTable          timesheetTable     = new JTable();
   private JPanel          buttonCardPanel    = new JPanel();
   private CardLayout      buttonCardLayout   = new CardLayout();
   private JPanel          buttonPanel        = new JPanel();
   private BorderLayout    buttonBorderLayout = new BorderLayout();
   private CardLayout      tableCardLayout    = new CardLayout();
   private JPanel          buttonGridPanel    = new JPanel();
   private JButton         clearButton        = new JButton();
   private GridLayout      buttonGridLayout   = new GridLayout();
   private JButton         fortyHoursButton   = new JButton();
   private JMenuBar        menuBar            = new JMenuBar();
   private JMenu           fileMenu           = new JMenu();
   private JMenu           helpMenu           = new JMenu();
   private JMenuItem       newMenuItem        = new JMenuItem();
   private JMenuItem       openMenuItem       = new JMenuItem();
   private JMenuItem       saveMenuItem       = new JMenuItem();
   private JMenuItem       exitMenuItem       = new JMenuItem();
   private JMenuItem       aboutMenuItem      = new JMenuItem();
   private SoftBevelBorder tableBorder        = new SoftBevelBorder(BevelBorder.LOWERED);

   //------------------------------------------------------------------------------------

   public Timesheet() {
      jbInit();

      timesheetModel.setDataVector(
         new String[][] {
            { "Sunday",    "",         "",         "",     "" },
            { "Monday",    "8:30 AM",  "10:00 AM", "",     "" },
            { "Tuesday",   "8:00 am",  "5:00 pm",  "1:00", "" },
            { "Wednesday", "12:15 PM", "5:00 PM",  "0:45", "" },
            { "Thursday",  "8:00",     "5:00 PM",  "1:00", "" },
            { "Friday",    "8:00 AM",  "13:00 PM", "1:00", "" },
            { "Saturday",  "8:00 AM",  "5:00 PM",  ":15",  "" },
            { "Total",     "",         "",         "",     "" }
         });

      timesheetModel.calculate();

      timesheetTable.setModel(timesheetModel);
      timesheetTable.getColumn("Day").setCellRenderer(new DayColumnCellRenderer());

      // Add window listener.

      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            exit();
         }
      });

      // Add menu item listeners.

      newMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            newMenuItemActionPerformed(event);
         }
      });

      openMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            openMenuItemActionPerformed(event);
         }
      });

      saveMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            saveMenuItemActionPerformed(event);
         }
      });

      exitMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            exit();
         }
      });

      aboutMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            aboutMenuItemActionPerformed(event);
         }
      });

      // Add button listeners.

      fortyHoursButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            fortyHoursButtonActionPerformed(event);
         }
      });

      clearButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            clearButtonActionPerformed(event);
         }
      });
   }

   //------------------------------------------------------------------------------------

   private void jbInit() {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      this.setTitle(TITLE);
      this.setSize(new Dimension(400, 250));
      this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      this.setJMenuBar(menuBar);
      this.getContentPane().setLayout(mainBorderLayout);

      setLocation(
         (screenSize.width - getSize().width) / 2,
         ((screenSize.height - getSize().height) / 2) - 20);

      menuBar.add(fileMenu);
      menuBar.add(helpMenu);

      fileMenu.setText("File");
      fileMenu.setMnemonic('F');
      fileMenu.add(newMenuItem);
      fileMenu.add(openMenuItem);
      fileMenu.add(saveMenuItem);
      fileMenu.addSeparator();
      fileMenu.add(exitMenuItem);

      newMenuItem.setText("New");
      newMenuItem.setMnemonic('N');

      openMenuItem.setText("Open");
      openMenuItem.setMnemonic('O');

      saveMenuItem.setText("Save");
      saveMenuItem.setMnemonic('S');

      exitMenuItem.setText("Exit");
      exitMenuItem.setMnemonic('X');

      helpMenu.setText("Help");
      helpMenu.setMnemonic('H');
      helpMenu.add(aboutMenuItem);

      aboutMenuItem.setText("About");
      aboutMenuItem.setMnemonic('A');

      this.getContentPane().add(tableCardPanel, BorderLayout.CENTER);
      this.getContentPane().add(buttonCardPanel, BorderLayout.SOUTH);

      tableCardLayout.setHgap(3);
      tableCardLayout.setVgap(3);

      tableCardPanel.setLayout(tableCardLayout);
      tableCardPanel.add(tableScrollPane, "");

      tableScrollPane.setBorder(tableBorder);
      tableScrollPane.getViewport().add(timesheetTable, null);

      timesheetTable.setBorder(null);

      buttonCardLayout.setHgap(3);
      buttonCardLayout.setVgap(3);

      buttonCardPanel.setLayout(buttonCardLayout);
      buttonCardPanel.add(buttonPanel, "jPanel2");

      buttonPanel.setMinimumSize(new Dimension(90, 25));
      buttonPanel.setPreferredSize(new Dimension(90, 25));
      buttonPanel.setLayout(buttonBorderLayout);
      buttonPanel.add(buttonGridPanel, BorderLayout.EAST);

      buttonGridLayout.setColumns(2);
      buttonGridLayout.setHgap(4);

      buttonGridPanel.setMaximumSize(new Dimension(155, 25));
      buttonGridPanel.setMinimumSize(new Dimension(155, 25));
      buttonGridPanel.setPreferredSize(new Dimension(155, 25));
      buttonGridPanel.setLayout(buttonGridLayout);
      buttonGridPanel.add(fortyHoursButton, null);
      buttonGridPanel.add(clearButton, null);

      fortyHoursButton.setMaximumSize(new Dimension(85, 25));
      fortyHoursButton.setMinimumSize(new Dimension(85, 25));
      fortyHoursButton.setPreferredSize(new Dimension(85, 25));
      fortyHoursButton.setMargin(new Insets(0, 0, 0, 0));
      fortyHoursButton.setText("Set to 40");

      clearButton.setMargin(new Insets(0, 0, 0, 0));
      clearButton.setText("Clear");
   }

   private void newMenuItemActionPerformed(ActionEvent event) {
      dataFile = null;
      timesheetModel.clear();
      setTitle(TITLE);
   }

   private void openMenuItemActionPerformed(ActionEvent event) {
      int result;

      try {
         result = fileChooser.showOpenDialog(this);

         if (result == JFileChooser.CANCEL_OPTION) {
            return;
         }

         dataFile = fileChooser.getSelectedFile();
         setTitle(TITLE + " - " + dataFile.getName());
         repaint();

         timesheetModel.load(dataFile);
      } catch(Exception ex) {
         ExceptionDialog.show(
            this,
            "Error",
            "Cannot open " + dataFile + ".",
            ex);
      }
   }

   private void saveMenuItemActionPerformed(ActionEvent event) {
      int result;

      try {
         // Get file if not known.

         if (dataFile == null) {
            result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.CANCEL_OPTION) {
               return;
            }

            dataFile = fileChooser.getSelectedFile();
            setTitle(TITLE + " - " + dataFile.getName());
            repaint();
         }

         timesheetModel.save(dataFile);
      } catch(Exception ex) {
         ExceptionDialog.show(
            this,
            "Error",
            "Cannot save " + dataFile + ".",
            ex);
      }
   }

   private void aboutMenuItemActionPerformed(ActionEvent event) {
      StringBuffer msg = new StringBuffer();

      msg.append(TITLE);
      msg.append("\nVersion ");
      msg.append(VERSION);
      msg.append("\n\nDeveloped by Thornton Rose");
      msg.append("\nCopyright 2000");

      MessageDialog.show(this, "About", msg.toString());
   }

   private void clearButtonActionPerformed(ActionEvent event) {
      timesheetModel.clear();
   }

   private void fortyHoursButtonActionPerformed(ActionEvent event) {
      timesheetModel.load40Hours();
   }

   private void exit() {
      System.exit(0);
   }

   //------------------------------------------------------------------------------------

   public static void main(String[] args) {
      (new Timesheet()).show();
   }
}
