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
import javax.swing.JTextArea;

import lib.Controller;
import lib.Graph;
import lib.Hedge;
import lib.MyWaypoint;
import lib.RoutePainter;
import lib.SmartScroller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;

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
	private JButton posBtn;
	private JButton setPosBtn;
	private JButton mapSetupBtn;
	private JButton resetHashBtn;
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
		JPanel log = new JPanel(new BorderLayout());
        JToolBar rateVertical = new JToolBar(JToolBar.VERTICAL);
        JToolBar impVertical = new JToolBar(JToolBar.VERTICAL);
       
        JTextArea textArea = controller.getTextArea();
        JTextArea routeArea = controller.getRouteArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        JScrollPane scrollPane2 = new JScrollPane(routeArea);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret(); 
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);    
        new SmartScroller( scrollPane );
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.append("\n\nInit UI");
        textArea.setCaretPosition(textArea.getDocument().getLength() - 1);
        routeArea.setColumns(10);
        routeArea.setLineWrap(true);
        routeArea.setRows(10);
        routeArea.setWrapStyleWord(true);
        routeArea.setEditable(false);
        routeArea.setLineWrap(true);
        routeArea.setWrapStyleWord(true);
        frame.setLayout(new BorderLayout());
		frame.setSize(1200, 600);
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
        rateVertical.add(setPosBtn);
        rateVertical.add(resetHashBtn);
        impVertical.add(impInfoBtn);
        impVertical.add(impEditBtn);
        impVertical.add(initBtn);
        impVertical.add(posBtn);
        impVertical.add(mapSetupBtn);
//        vertical.add(printBtn);
        
        vertical.add(rateVertical, BorderLayout.EAST);
        vertical.add(impVertical, BorderLayout.WEST);
        
//        log.add(textArea, BorderLayout.WEST);
        log.add(scrollPane, BorderLayout.EAST);
        log.add(scrollPane2, BorderLayout.SOUTH);
        frame.add(vertical, BorderLayout.WEST);
		frame.add(controller.getMapViewer(), BorderLayout.CENTER);
		frame.add(routeBtn,BorderLayout.SOUTH);		
		frame.add(log, BorderLayout.EAST);
		textArea.append("\nConstrain: Off");
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
        ImageIcon posIcon = createImageIcon("icon/posInfo.png","Java");
        ImageIcon setPosIcon = createImageIcon("icon/posEdit.png","Java");
        ImageIcon mapSetupIcon = createImageIcon("icon/posSet.png","Java");
        ImageIcon resetHashIcon = createImageIcon("icon/posSet.png","Java");
        
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
        posBtn = new JButton(posIcon);
        posBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        posBtn.setToolTipText("Node position");
        setPosBtn = new JButton(setPosIcon);
        setPosBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        setPosBtn.setToolTipText("Set node position");
        mapSetupBtn = new JButton(mapSetupIcon);
        mapSetupBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        mapSetupBtn.setToolTipText("Reset node postion");
        resetHashBtn = new JButton(resetHashIcon);
        resetHashBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        resetHashBtn.setToolTipText("Reset hash");
        
        controller.getTextArea().append("\nSetup Button");
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
        		impEditBtnClicked();
        	}
        });
    	
    	routeBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		routeBtnClicked();
        	}
        });
    	
    	posBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		posBtnClicked();
        	}
        });
    	
    	setPosBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setPosBtnClicked();
        	}
        });
    	
    	mapSetupBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		resetBtnClicked();
        	}
        });
    	
    	resetHashBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		controller.setBothConstrainHash();
        	}
        });
    }
    
//    public void routeBtnClicked(Graph g, List<Painter<JXMapViewer>> painters,JXMapViewer mapViewer) {
//		g.dijkstra(controller.getStart());
//		List<String> paths = g.printAllPaths();
//		paths = controller.removeSameType(paths);
//		for(String path: paths) {
//			List<GeoPosition> track = controller.createTrack(path,resultHash);
//			final RoutePainter routePainter = new RoutePainter(track);
//			painters.add(routePainter);
//			CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
//			mapViewer.setOverlayPainter(painter);
//		}
//		controller.getHash();
//	}
    
    public void resetBtnClicked() {
    	List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		controller.getMapViewer().setOverlayPainter(painter);
		controller.setConstrainHash(new Hashtable<String, String>());
		controller.getRouteArea().setText("Waypoint route");
		controller.getTextArea().append("\n---------------------------------------------------\nReset map and waypoints position\nConstrain: Off\n");
    }
    
    public void routeBtnClicked() {
//    	if(resultHash.size() > 0 ) {
    		if((controller.getWaypoints().size() != 0)){
    			controller.drawRoute(resultHash);
    		} else JOptionPane.showMessageDialog (null, "Set node position first", null, JOptionPane.INFORMATION_MESSAGE);
//    	} else JOptionPane.showMessageDialog (null, "Calculate Hedge priority first", null, JOptionPane.INFORMATION_MESSAGE);
	}
    
	public void initBtnClicked() throws IOException {
//	    GridTable frame = new GridTable();
//	    frame.addWindowListener(new WindowAdapter() {
//	      public void windowClosing(WindowEvent e) {
////	        System.exit(0);
//
//	      }
//	    });
        if(controller.getImpHedge() == null || controller.getRateHedge() == null) {
//			JOptionPane.showMessageDialog (null, "Create default data", null, JOptionPane.INFORMATION_MESSAGE);
		} else JOptionPane.showMessageDialog (null, "Reset default data", null, JOptionPane.INFORMATION_MESSAGE);
		try {
			controller.initHedge("M1");
			controller.getTextArea().append("\nInit data");
			controller.setWaypoint();
//			controller.setConstrainHash();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		controller.setWaypoint();
//		controller.setConstrainHash();
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
						controller.setRateArrayValue(table.getValueAt(x, y),x+1,y);
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
	
	public void impEditBtnClicked() {
		if(controller.getRateHedge() != null && controller.getImpHedge() != null) {
			String[][] rowData = new String[6][5];
			String[][] data = controller.getImpArray();
			for (int r = 0; r < 6; r++) {
				rowData[r] = data[r].clone();
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
					String oldValue = new String(controller.getImpArray()[x + 1][y]);
					if (controller.checkValidHedge((String) table.getValueAt(x, y), controller.getImpHedge()) == 1) {
						controller.setImpArrayValue(table.getValueAt(x, y),x+1,y);
					} else {
						table.setValueAt(oldValue, x, y);
						JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JFrame frame = new JFrame("Imp hedge edit");
			frame.add(scrollPane, BorderLayout.CENTER);
			frame.setSize(700, 150);
			frame.setVisible(true);
		} else JOptionPane.showMessageDialog (null, "Click init button first", null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void posBtnClicked() {
		JFrame frame = new JFrame("aaaaaaaaaaaaaaaa");
		frame.setSize(500,300);
		
        String[][] data = controller.getPosInfoArray();
        String[][] rowData = new String[12][2];
        for (int r = 0; r < 12; r++) {
			rowData[r] = data[r].clone();
		}
        Object[] columnNames = new Object[] { "Map", "Position" };
        
        System.out.println(columnNames[0]);
        List<String[]> l = new ArrayList<String[]>(Arrays.asList(rowData));
        for(int i = 0; i<l.size();i++) {
        	if (l.get(i)[0].matches("t++(.*)")) {
        		l.remove(i);
        		i--;
        	}
        }
        rowData = l.toArray(new String[][] {});
        JTable table = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int x = e.getFirstRow();
				int y = e.getColumn();
				String oldPos1 = controller.getPosArray()[x][0];
				String oldPos2 = controller.getPosArray()[x][1];
				MyWaypoint wayPoint1 = controller.findWaypoint(new GeoPosition(Double.parseDouble(oldPos1),Double.parseDouble(oldPos2)));
				controller.getTextArea().append("\nChange position for waypoint: " + wayPoint1.getLabel());
				controller.setPosInfoArrayValue(table.getValueAt(x, y),x,y);
				switch((String)table.getValueAt(x, y)){
					case "1": 
						controller.setPosArrayValue("21.04",x,0);
						controller.setPosArrayValue("105.7814101",x,1);
						controller.changeDistance(wayPoint1, 9);
						break;
					case "2":
						controller.setPosArrayValue("21.00",x,0);
						controller.setPosArrayValue("105.8214101",x,1);
						controller.changeDistance(wayPoint1, 4);
						break;
					case "3":
						controller.setPosArrayValue("21.01", x, 0);
						controller.setPosArrayValue("105.8614101", x, 1);
						controller.changeDistance(wayPoint1, 20);
						break;
				}
				if(controller.getConstrainHash().size() != 0) {
					resetBtnClicked();
					setPosBtnClicked();
				}
			}
		});
        JComboBox comboBox = controller.getComboBox();
        
        TableColumn sportColumn = table.getColumnModel().getColumn(1);
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
 
        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
        table.setOpaque(true); //content panes must be opaque
        frame.setContentPane(table);
        frame.pack();
        frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(500, 250);
        frame.setVisible(true);
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
			controller.getTextArea().append("\n\nHedge decision value: \n" + orderList + "\n" + order);
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
		if(controller.getImpHedge() != null) {
			String content = null;
			Hedge hedge = controller.getImpHedge();
			content = controller.getInfoContent(hedge);
			JOptionPane.showMessageDialog (null, content, "Imp hedge info", JOptionPane.INFORMATION_MESSAGE);
		} else JOptionPane.showMessageDialog (null, "Click init button first", null, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void setPosBtnClicked() {
		if((controller.getWaypoints() != null) && (controller.getPosArray() != null)) {
			controller.setWaypoint();
//			controller.setConstrainHash();
		} else JOptionPane.showMessageDialog (null, "Init data first", null, JOptionPane.INFORMATION_MESSAGE);
	}
}
