package layout;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * @version 1.0 11/09/98
 */
public class GridTable extends JFrame {

  public GridTable() {
    super("JButtonTable Example");

    DefaultTableModel dm = new DefaultTableModel();
    dm.setDataVector(new Object[][] { { "M1", "Wildfire" },
        { "M2", "Tornado" },{ "M3", "Tsunami" },{ "M4", "Earthquake" },{ "M5", "Flood" }}, new Object[] { "Map", "Description" });

    JTable table = new JTable(dm);
    table.getColumn("Map").setCellRenderer(new ButtonRenderer());
    table.getColumn("Map").setCellEditor(
        new ButtonEditor(new JCheckBox()));
    JScrollPane scroll = new JScrollPane(table);
    getContentPane().add(scroll);
    setSize(500, 200);
    setVisible(true);
  }

  public static void main(String[] args) {
  
//   
//    test frame = new test();
//    frame.addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        System.exit(0);
//      }
//    });
  }
}
/**
 * @version 1.0 11/09/98
 */



/**
 * @version 1.0 11/09/98
 */



