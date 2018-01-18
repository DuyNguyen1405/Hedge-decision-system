package layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import lib.Controller;
import lib.Graph;
import lib.Hedge;
import lib.RoutePainter;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;


public class Main{
	private Hashtable<Double, String> resultHash = new Hashtable<Double, String>();
//	private Hedge impHedge;
//	private Hedge rateHedge;
//	private String[][] rateArray;
//	private String[][] impArray;
//	private Car car1;
//	private Car car2;
//	private Car car3;
//	private Car car4;
//	private Car car5;
	private JButton rateInfoBtn;
	private JButton rateEditBtn;
	private JButton impInfoBtn;
	private JButton impEditBtn;
	private JButton initBtn;
	private JButton runBtn;
	private JButton routeBtn;
//	private final JXMapViewer mapViewer = new JXMapViewer();
	
	public static Controller controller = new Controller();
	
	public Main() {
        initUI();
    }
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main ex = new Main();
        });
    }
    
	private void initUI() {
    	final JFrame frame = new JFrame();
    	final JXMapViewer mapViewer = controller.getMapViewer();
		JPanel vertical = new JPanel(new BorderLayout());
        JToolBar rateVertical = new JToolBar(JToolBar.VERTICAL);
        JToolBar impVertical = new JToolBar(JToolBar.VERTICAL);
        frame.setLayout(new BorderLayout());
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        rateVertical.setFloatable(false);
        rateVertical.setMargin(new Insets(10, 5, 5, 5));
        impVertical.setFloatable(false);
        impVertical.setMargin(new Insets(10, 5, 5, 5));
        btnSetup();
        controller.mapSetup();
        addPropertyChange(frame, mapViewer);
        rateVertical.add(rateInfoBtn);
        rateVertical.add(rateEditBtn);
        rateVertical.add(runBtn);
        impVertical.add(impInfoBtn);
        impVertical.add(impEditBtn);
        impVertical.add(initBtn);
//        vertical.add(printBtn);
        
        vertical.add(rateVertical, BorderLayout.EAST);
        vertical.add(impVertical, BorderLayout.WEST);
        frame.add(vertical, BorderLayout.WEST);
		frame.add(controller.getMapViewer(), BorderLayout.CENTER);
		frame.add(routeBtn,BorderLayout.SOUTH);		
    }
    
	public void addPropertyChange(JFrame frame, JXMapViewer mapViewer) {
		mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				controller.updateWindowTitle(frame, mapViewer);
			}
		});
		
		mapViewer.addPropertyChangeListener("center", new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				controller.updateWindowTitle(frame, mapViewer);
			}
		});
		
		controller.updateWindowTitle(frame, mapViewer);
	}
	private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = Main.class.getResource(path);
        
        if (imgURL != null) {
           return new ImageIcon(imgURL, description);
        } else {            
           System.err.println("Couldn't find file: " + path);
           return null;
        }
     }
	
    public void btnSetup() {
    	ImageIcon rateInfoIcon = createImageIcon("icon/rateInfo.png","Java");
        ImageIcon rateEditIcon = createImageIcon("icon/rateEdit.png","Java");
        ImageIcon impInfoIcon = createImageIcon("icon/impInfo.png","Java");
        ImageIcon impEditIcon = createImageIcon("icon/impEdit.png","Java");
        ImageIcon initIcon = createImageIcon("icon/init.png","Java");
        ImageIcon runIcon = createImageIcon("icon/run.png","Java");
        
        rateInfoBtn = new JButton(rateInfoIcon);
        rateInfoBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        rateInfoBtn.setToolTipText("Show component value of Rate Hedge");
        rateEditBtn = new JButton(rateEditIcon);
        rateEditBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        rateEditBtn.setToolTipText("Edit rate table");
        impInfoBtn = new JButton(impInfoIcon);
        impInfoBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        impInfoBtn.setToolTipText("Show component value of Imp Hedge");
        impEditBtn = new JButton(impEditIcon);
        impEditBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        impEditBtn.setToolTipText("Edit imp table");
        initBtn = new JButton(initIcon);
        initBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        initBtn.setToolTipText("Create/Reset default value");
        runBtn = new JButton(runIcon);
        runBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        runBtn.setToolTipText("Make decision");
        routeBtn = new JButton("Draw route on map");
	  	
    	initBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					initBtnClicked();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
    	
    	runBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			runBtnClicked();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
    	rateInfoBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		rateInfoBtnClicked();
        	}
        });
    	
    	rateEditBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		rateEditBtnClicked();
        	}
        });
    	
    	impInfoBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		impInfoBtnClicked();
        	}
        });
    	
    	impEditBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					initBtnClicked();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
    	
    	routeBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		routeBtnClicked(controller.getG(), controller.getPainters(),controller.getMapViewer());
        	}
        });
    }
    
    public void routeBtnClicked(Graph g, List<Painter<JXMapViewer>> painters,JXMapViewer mapViewer) {
		g.dijkstra(controller.getStart());
		List<String> paths = g.printAllPaths();
		for(String path: paths) {
			List<GeoPosition> track = controller.createTrack(path);
			final RoutePainter routePainter = new RoutePainter(track);
			painters.add(routePainter);
			CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
			mapViewer.setOverlayPainter(painter);
		}
	}
    
	public void initBtnClicked() throws IOException {
	    GridTable frame = new GridTable();
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
//	        System.exit(0);

	      }
	    });
//        if(controller.getImpHedge() == null || controller.getRateHedge() == null) {
//			JOptionPane.showMessageDialog (null, "Create default data", null, JOptionPane.INFORMATION_MESSAGE);
//		} else JOptionPane.showMessageDialog (null, "Reset default data", null, JOptionPane.INFORMATION_MESSAGE);
//		try {
//			controller.initHedge(fileName);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
	
	public void rateEditBtnClicked() {
		if(controller.getRateHedge() != null && controller.getImpHedge() != null) {
			String[][] rowData = new String[6][5];
			for (int r = 0; r < 6; r++) {
				rowData[r] = controller.getRateArray()[r].clone();
			}
			final String columnNames[] = rowData[0];
			List<String[]> l = new ArrayList<String[]>(Arrays.asList(rowData));
			l.remove(0);
			rowData = l.toArray(new String[][] {});
			final JTable table = new JTable(rowData, columnNames);
			JScrollPane scrollPane = new JScrollPane(table);

			table.getModel().addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					int x = e.getFirstRow();
					int y = e.getColumn();
					String oldValue = new String(controller.getRateArray()[x + 1][y]);
					if (controller.checkValidHedge((String) table.getValueAt(x, y), controller.getRateHedge()) == 1) {
						System.out.println(controller.getRateArray()[x + 1][y]);
					} else {
						table.setValueAt(oldValue, x, y);
						JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JFrame frame = new JFrame("Rate hedge edit");
			frame.add(scrollPane, BorderLayout.CENTER);
			frame.setSize(700, 150);
			frame.setVisible(true);
		} else JOptionPane.showMessageDialog (null, "Click init button first", null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void runBtnClicked() throws IOException {
		if(controller.getRateHedge() != null && controller.getImpHedge() != null) {
			Hedge hedge1 = controller.getRateHedge();
			Hedge hedge2 = controller.getImpHedge();
			double[] result = null;
			String orderList = "";
			String order = "";
			result = controller.hedgeDecision(hedge1,hedge2,resultHash);
			Arrays.sort(result);
			for(int i = result.length-1; i > 0; i--) {
				orderList += (resultHash.get(result[i]) + ": " + result[i]);
				orderList += "\n";
				order += resultHash.get(result[i]);
				if (i > 1) order += " > ";
			}
			JOptionPane.showMessageDialog (null, orderList + "\n" + order, "Hedge result", JOptionPane.INFORMATION_MESSAGE);
		} else JOptionPane.showMessageDialog (null, "Click init button first", null, JOptionPane.INFORMATION_MESSAGE);
	}

	public void rateInfoBtnClicked() {
		if(controller.getRateHedge() != null) {
			String content = null;
			Hedge hedge = controller.getRateHedge();
			content = controller.getInfoContent(hedge);
			JOptionPane.showMessageDialog (null, content, "Rate hedge info", JOptionPane.INFORMATION_MESSAGE);
		} else JOptionPane.showMessageDialog (null, "Click init button first", null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void impInfoBtnClicked() {
		String content = null;
		Hedge hedge = controller.getImpHedge();
		content = controller.getInfoContent(hedge);
		JOptionPane.showMessageDialog (null, content, "Imp hedge info", JOptionPane.INFORMATION_MESSAGE);
	}	
}
