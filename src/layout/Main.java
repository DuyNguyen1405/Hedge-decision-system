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
import lib.Hedge;


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
		frame.setLayout(new BorderLayout());
		frame.add(new JLabel("Use left mouse button to pan, mouse wheel to zoom and right mouse to select"), BorderLayout.NORTH);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JPanel vertical = new JPanel(new BorderLayout());
        JToolBar rateVertical = new JToolBar(JToolBar.VERTICAL);
        JToolBar impVertical = new JToolBar(JToolBar.VERTICAL);
        rateVertical.setFloatable(false);
        rateVertical.setMargin(new Insets(10, 5, 5, 5));
        impVertical.setFloatable(false);
        impVertical.setMargin(new Insets(10, 5, 5, 5));
        btnSetup();
        controller.mapSetup();
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

        JLabel statusbar = new JLabel(" Statusbar");
        frame.add(statusbar, BorderLayout.SOUTH);
//        frame.setTitle("Hedge decision system"); 
        
        controller.getMapViewer().addPropertyChangeListener("zoom", new PropertyChangeListener()
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
	
//	public void setupTableColumn(TableColumn<Car, String> column, int i, String colName, String[][] array, Hedge hedge) {
//		column.setCellValueFactory(new PropertyValueFactory<Car, String>(colName));
//		column.setCellFactory(TextFieldTableCell.forTableColumn());
//		column.setOnEditCommit(
//            new EventHandler<CellEditEvent<Car, String>>() {
//                @Override
//                public void handle(CellEditEvent<Car, String> t) {
//                	if(checkValidHedge(t.getNewValue(), hedge) == 1 ){
//                		Car car = (Car) t.getTableView().getItems().get(t.getTablePosition().getRow());
//                		String newValue = t.getNewValue();
//                    	switch(colName) {
//                    		case "w1":  car.setW1(newValue);
//                    		case "w2":  car.setW2(newValue);
//                    		case "w3":  car.setW3(newValue);
//                    		case "w4":  car.setW4(newValue);
//                    		case "q1":  car.setQ1(newValue);
//                    		case "q2":  car.setQ2(newValue);
//                    		case "q3":  car.setQ3(newValue);
//                    		case "q4":  car.setQ4(newValue);
//                    	}
//                	}else {
//                		Alert alert = new Alert(AlertType.WARNING);
//                		alert.setTitle("Warning Dialog");
//                		alert.setHeaderText(null);
//                		alert.setContentText("Please enter a valid input");
//                		alert.showAndWait();
//                	}
//                    array[(t.getTablePosition().getRow())+1][i] = t.getNewValue();
//                }
//            }
//        );
//	}
	
	
	
//	
//	public void rateHedgeEdit() {
//		if(getRateHedge() != null && getImpHedge() != null) {
//			String[][] rateArray = rateHedge.getWords();
//			Hedge hedge = getRateHedge();
////			String[][] impArray = impHedge.getWords();
//			ObservableList<Car> data = FXCollections.observableArrayList(car1,car2,car3,car4,car5);
////			for(int i = 1; i <= 5; i++) {
////				for(int j = 1; j<5; j++) {
////					System.out.print(rateArray[i][j] + ",");
////				}
////				System.out.print("\n");
////			}
//			try {
//				Scene scene = new Scene(new Group());
//				Stage stage = new Stage();
//	            stage.setTitle("abcdas");
//				 final Label label = new Label("Rate hedge edit");
//			     label.setFont(new Font("Arial", 20));
//			     rateTable.setEditable(true);
//				TableColumn<Car, String> nameCol = new TableColumn<Car, String>("Name");
//				TableColumn<Car, String> w1Col = new TableColumn<Car, String>("W1");
//				TableColumn<Car, String> w2Col = new TableColumn<Car, String>("W2");
//				TableColumn<Car, String> w3Col = new TableColumn<Car, String>("W3");
//				TableColumn<Car, String> w4Col = new TableColumn<Car, String>("W4");
//				setupTableColumn(nameCol,0, "name",rateArray, hedge);
//				setupTableColumn(w1Col,1, "w1",rateArray, hedge);
//				setupTableColumn(w2Col,2, "w2",rateArray, hedge);
//				setupTableColumn(w3Col,3, "w3",rateArray, hedge);
//				setupTableColumn(w4Col,4, "w4",rateArray, hedge);
//				rateTable.setItems(data);
//				if(rateTable.getColumns().size() == 0) {rateTable.getColumns().addAll(nameCol,w1Col,w2Col,w3Col,w4Col);}
//		        final VBox vbox = new VBox();
//		        vbox.setSpacing(4);
////		        vbox.setPadding(new Insets(10, 0, 0, 10));
//		        vbox.getChildren().addAll(label, rateTable);
//		        vbox.setMaxSize(370,190);
////		        vbox.setMinHeight(200);
//		        ((Group) scene.getRoot()).getChildren().addAll(vbox);
//		 
//		        stage.setScene(scene);
//		        stage.show();
//			} catch(Exception e) {
//		        e.printStackTrace();
//		     }
//		} else System.out.println("Click init button first");
//	}
//	
//	public void impHedgeEdit() {
//		if(getRateHedge() != null && getImpHedge() != null) {
////			String[][] rateArray = rateHedge.getWords();
//			Hedge hedge = getImpHedge();
//			String[][] impArray = impHedge.getWords();
//			ObservableList<Car> data = FXCollections.observableArrayList(car1,car2,car3,car4,car5);
////			for(int i = 1; i <= 5; i++) {
////				for(int j = 1; j<5; j++) {
////					System.out.print(rateArray[i][j] + ",");
////				}
////				System.out.print("\n");
////			}
//			try {
//				Scene scene = new Scene(new Group());
//				Stage stage = new Stage();
//	            stage.setTitle("abcdas");
//				 final Label label = new Label("Imp hedge edit");
//			     label.setFont(new Font("Arial", 20));
//			     impTable.setEditable(true);
//				TableColumn<Car, String> nameCol = new TableColumn<Car, String>("Name");
//				TableColumn<Car, String> q1Col = new TableColumn<Car, String>("Q1");
//				TableColumn<Car, String> q2Col = new TableColumn<Car, String>("Q2");
//				TableColumn<Car, String> q3Col = new TableColumn<Car, String>("Q3");
//				TableColumn<Car, String> q4Col = new TableColumn<Car, String>("Q4");
//				setupTableColumn(nameCol,0, "name",impArray, hedge);
//				setupTableColumn(q1Col,1, "q1",impArray, hedge);
//				setupTableColumn(q2Col,2, "q2",impArray, hedge);
//				setupTableColumn(q3Col,3, "q3",impArray, hedge);
//				setupTableColumn(q4Col,4, "q4",impArray, hedge);
//				impTable.setItems(data);
//				if(impTable.getColumns().size() == 0) { impTable.getColumns().addAll(nameCol,q1Col,q2Col,q3Col,q4Col);}
//		        final VBox vbox = new VBox();
//		        vbox.setSpacing(4);
////		        vbox.setPadding(new Insets(10, 0, 0, 10));
//		        vbox.getChildren().addAll(label, impTable);
//		        vbox.setMaxSize(550,190);
////		        vbox.setMinHeight(200);
//		        vbox.setMinWidth(550);
//		        ((Group) scene.getRoot()).getChildren().addAll(vbox);
//		 
//		        stage.setScene(scene);
//		        stage.show();
//			} catch(Exception e) {
//		        e.printStackTrace();
//		     }
//		} else System.out.println("Click init button first");
//	}
//	
//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		Parent root = FXMLLoader.load(getClass().getResource("Gr.fxml"));
//		primaryStage.setTitle("abc");
//		primaryStage.setScene(new Scene(root, 640, 400));
//		primaryStage.show();
//		JXMapViewer mapViewer = new JXMapViewer();
//
//		TileFactoryInfo info = new OSMTileFactoryInfo();
//		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
//		mapViewer.setTileFactory(tileFactory);
//		tileFactory.setThreadPoolSize(8);
//		// Set the focus
//		GeoPosition frankfurt = new GeoPosition(50.11, 8.68);
//		mapViewer.setZoom(7);
//		mapViewer.setAddressLocation(frankfurt);
//		JFrame frame = new JFrame("JXMapviewer2 Example 1");
//		frame.getContentPane().add(mapViewer);
//		frame.setSize(800, 600);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
////		final JFXPanel mainJFXPanel = new JFXPanel();
////		frame.getContentPane().add(mainJFXPanel);
////		Scene scene = new Scene(root);
////		mainJFXPanel.setScene(scene);
//}
//	
//	public static void main(String[] args) throws IOException {
//		launch(args);
//	}
	
}
