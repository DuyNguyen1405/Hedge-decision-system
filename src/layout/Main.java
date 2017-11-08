package layout;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.Hedge;
import main.Car;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class Main{
	private Hashtable<Double, String> resultHash = new Hashtable<Double, String>();
	private Hedge impHedge;
	private Hedge rateHedge;
	private String[][] rateArray;
	private String[][] impArray;
//    private TableView<Car> impTable = new TableView<Car>();
//    private TableView<Car> rateTable = new TableView<Car>();
	private Car car1;
	private Car car2;
	private Car car3;
	private Car car4;
	private Car car5;
	@FXML Pane googleMapBox = new Pane();
	
	public Main() {
        initUI();
    }
	
	private void initUI() {
    	final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(new JLabel("Use left mouse button to pan, mouse wheel to zoom and right mouse to select"), BorderLayout.NORTH);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(10, 5, 5, 5));

        ImageIcon driveIcon = createImageIcon("computer.jpg","Java");
        ImageIcon compIcon = new ImageIcon("computer.png");
        ImageIcon printIcon = new ImageIcon("printer.png");
        JButton driveBtn = new JButton(driveIcon);
        driveBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        JButton compBtn = new JButton(compIcon);
        compBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
        JButton printBtn = new JButton(printIcon);
        printBtn.setBorder(new EmptyBorder(3, 0, 3, 0));

        vertical.add(driveBtn);
        vertical.add(compBtn);
        vertical.add(printBtn);

        frame.add(vertical, BorderLayout.WEST);
        
        TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		tileFactory.setThreadPoolSize(8);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		// Setup JXMapViewer
		final JXMapViewer mapViewer = new JXMapViewer();
		mapViewer.setTileFactory(tileFactory);

		GeoPosition bachkhoa = new GeoPosition(21.00, 105.84);

		// Set the focus
		mapViewer.setZoom(2);
		mapViewer.setAddressLocation(bachkhoa);
	
		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);
		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
		
		// Add a selection painter
		SelectionAdapter sa = new SelectionAdapter(mapViewer); 
		SelectionPainter sp = new SelectionPainter(sa); 
		mapViewer.addMouseListener(sa); 
		mapViewer.addMouseMotionListener(sa); 
		mapViewer.setOverlayPainter(sp);
		
		frame.add(mapViewer, BorderLayout.CENTER);

        JLabel statusbar = new JLabel(" Statusbar");
        frame.add(statusbar, BorderLayout.SOUTH);
        frame.setTitle("Hedge decision system"); 
//        
//        mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
//		{
//			@Override
//			public void propertyChange(PropertyChangeEvent evt)
//			{
//				updateWindowTitle(frame, mapViewer);
//			}
//		});
//		
//		mapViewer.addPropertyChangeListener("center", new PropertyChangeListener()
//		{
//			@Override
//			public void propertyChange(PropertyChangeEvent evt)
//			{
//				updateWindowTitle(frame, mapViewer);
//			}
//		});
//		
//		updateWindowTitle(frame, mapViewer);
		System.out.println(driveIcon.getIconHeight());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main ex = new Main();
        });
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
    
    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer)
	{
		double lat = mapViewer.getCenterPosition().getLatitude();
		double lon = mapViewer.getCenterPosition().getLongitude();
		int zoom = mapViewer.getZoom();
		frame.setTitle(String.format("Hedge decision system (%.2f / %.2f) - Zoom: %d", lat, lon, zoom)); 
	}
    
	public void initializationClicked() throws IOException {
		if(getImpHedge() == null || getRateHedge() == null) {
			Hedge hedge1 = new Hedge(new String[] {"low","high"}, new String[] {"very"},new String[] {"little"},0.5,0.5,0.5, "M1.txt");
			Hedge hedge2 = new Hedge(new String[] {"unimportant","important"}, new String[] {"very"},new String[] {"little"},0.65,0.35,0.4, "M1i.txt");
			setImpHedge(hedge2);
			setRateHedge(hedge1);
			String[][] rateArray = rateHedge.getWords();
			String[][] impArray = impHedge.getWords();
			System.out.println(checkValidHedge("abc",hedge1));
			System.out.println(checkValidHedge("very low",hedge1));
			System.out.println(checkValidHedge("very important", hedge2));
			System.out.println(checkValidHedge("very low",hedge2));
		    car1 = new Car(rateArray[1][0], rateArray[1][1], rateArray[1][2], rateArray[1][3], rateArray[1][4], impArray[1][1],impArray[1][2],impArray[1][3],impArray[1][4],0.0);
			car2 = new Car(rateArray[2][0], rateArray[2][1], rateArray[2][2], rateArray[2][3], rateArray[2][4], impArray[2][1],impArray[2][2],impArray[2][3],impArray[2][4],0.0);
			car3 = new Car(rateArray[3][0], rateArray[3][1], rateArray[3][2], rateArray[3][3], rateArray[3][4], impArray[3][1],impArray[3][2],impArray[3][3],impArray[3][4],0.0);
			car4 = new Car(rateArray[4][0], rateArray[4][1], rateArray[4][2], rateArray[4][3], rateArray[4][4], impArray[4][1],impArray[4][2],impArray[4][3],impArray[4][4],0.0);
			car5 = new Car(rateArray[5][0], rateArray[5][1], rateArray[5][2], rateArray[5][3], rateArray[5][4], impArray[5][1],impArray[5][2],impArray[5][3],impArray[5][4],0.0);
		} else System.out.println("Already init");
	}
	
	public void runClicked() throws IOException {
		if(getRateHedge() != null && getImpHedge() != null) {
			Hedge hedge1 = getRateHedge();
			Hedge hedge2 = getImpHedge();
			double[] result = null;
			String orderList = "";
			String order = "";
			result = hedgeDecision(hedge1,hedge2,resultHash);
			Arrays.sort(result);
			for(int i = result.length-1; i > 0; i--) {
				orderList += (resultHash.get(result[i]) + ": " + result[i]);
				orderList += "\n";
				order += resultHash.get(result[i]);
				if (i > 1) order += " > ";
			}
			JOptionPane.showMessageDialog (null, orderList + "\n" + order, "Hedge result", JOptionPane.INFORMATION_MESSAGE);
		} else System.out.println("Click initalization button first");
	}
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
//		} else System.out.println("Click initalization button first");
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
//		} else System.out.println("Click initalization button first");
//	}
//	
	public void rateInfoClicked() {
		String content = null;
		Hedge hedge = getRateHedge();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Rate Hedge Information");
		alert.setHeaderText(null);
		content = "Alpha: " + hedge.getAlpha() + 
				"\nBeta: " + hedge.getBeta() + 
				"\nTheta: " + hedge.getTheta() +
				"\nBase Hedge: " + Arrays.asList(hedge.getBaseG()).toString() +
				"\nPositive Hedge: " + Arrays.asList(hedge.getPositiveHedgeH()).toString() +
				"\nNagative Hedge: " + Arrays.asList(hedge.getNegativeHedgeH()).toString() +
				"\nHedge Sign: " + Arrays.asList(hedge.getFuzzySign()).toString() + 
				"\nHedge Fm: " + Arrays.asList(hedge.getFuzzyHashFm()).toString() +
				"\nHedge V: " + Arrays.asList(hedge.getFuzzyHashV()).toString();
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public void impInfoClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("ImpInfo");

		alert.showAndWait();
	}
	public void setupTableColumn(TableColumn<Car, String> column, int i, String colName, String[][] array, Hedge hedge) {
		column.setCellValueFactory(new PropertyValueFactory<Car, String>(colName));
		column.setCellFactory(TextFieldTableCell.forTableColumn());
		column.setOnEditCommit(
            new EventHandler<CellEditEvent<Car, String>>() {
                @Override
                public void handle(CellEditEvent<Car, String> t) {
                	if(checkValidHedge(t.getNewValue(), hedge) == 1 ){
                		Car car = (Car) t.getTableView().getItems().get(t.getTablePosition().getRow());
                		String newValue = t.getNewValue();
                    	switch(colName) {
                    		case "w1":  car.setW1(newValue);
                    		case "w2":  car.setW2(newValue);
                    		case "w3":  car.setW3(newValue);
                    		case "w4":  car.setW4(newValue);
                    		case "q1":  car.setQ1(newValue);
                    		case "q2":  car.setQ2(newValue);
                    		case "q3":  car.setQ3(newValue);
                    		case "q4":  car.setQ4(newValue);
                    	}
                	}else {
                		Alert alert = new Alert(AlertType.WARNING);
                		alert.setTitle("Warning Dialog");
                		alert.setHeaderText(null);
                		alert.setContentText("Please enter a valid input");
                		alert.showAndWait();
                	}
                    array[(t.getTablePosition().getRow())+1][i] = t.getNewValue();
                }
            }
        );
	}
	
	public int checkValidHedge(String hedgeString, Hedge hedge) {
		int valid = 1;
		String[] array = hedgeString.split(" ");
		if(!Arrays.asList(hedge.getBaseG()).contains(array[array.length-1])) {
			valid = 0;
			return valid;
		}
		for(int i = 0; i < array.length-1; i++) {
			if((!Arrays.asList(hedge.getPositiveHedgeH()).contains(array[i])) && (!Arrays.asList(hedge.getNegativeHedgeH()).contains(array[i]))) {
				valid = 0;
				break;
			}
		}
		return valid;
	}
	
	public void makeTestCase(Hedge hedge) {
		System.out.println("Car name: " + hedge.getCarName(1));
		System.out.println("Car importance: " + Arrays.toString(hedge.getCarImportance(1)));
		System.out.println("BaseG: " + Arrays.toString(hedge.getBaseG()));
		System.out.println("FmBaseG[0]: " + hedge.getFuzzyHashFm().get(hedge.getBaseG()[0]));
		System.out.println("Alpha: " + hedge.getAlpha());
		System.out.println("Beta: " + hedge.getBeta());
		
		String countSpaceTest = hedge.getPositiveHedgeH()[0] + " " + hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[0];
		System.out.println("Count space: " + hedge.countSpace(countSpaceTest));
		
		String stringTest1 = hedge.getPositiveHedgeH()[0] + " " + hedge.getBaseG()[0];
		String stringTest2 = hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[1];
		System.out.println("Fuzzy compare " + stringTest1 + " + " + stringTest2 + ": "+ hedge.fuzzyCompare(stringTest1,stringTest2));
		System.out.println("Word count: " + hedge.getWordCount());
		System.out.println("Fuzzy sign: " + hedge.getFuzzySign());
		System.out.println("FuzzyHashFM: " + hedge.getFuzzyHashFm());
		System.out.println("FuzzyHashV: " + hedge.getFuzzyHashV());
		
		String stringTest3 = hedge.getPositiveHedgeH()[0] + " " + hedge.getBaseG()[1];
		String stringTest4 = hedge.getPositiveHedgeH()[0] + " " + hedge.getNegativeHedgeH()[0] + " " + hedge.getBaseG()[0];
		hedge.calculateV(stringTest1);
		hedge.calculateV(stringTest2);
		hedge.calculateV(stringTest3);
		hedge.calculateV(stringTest4);
		
		String falseTest = "abcxzuixc";
		hedge.calculateV(falseTest);
	}
	
	public double[] hedgeDecision(Hedge hedge1, Hedge hedge2, Hashtable<Double, String> resultHash) {
		double[] result = {0,0,0,0,0,0};
		for(int i = 1; i < 6; i++) {
			String[] importanceArray1 = hedge1.getCarImportance(i);
			String[] importanceArray2 = hedge2.getCarImportance(i);
			double tmp1 = 0.0;
			double tmp2 = 0.0;
//			System.out.println(Arrays.toString(importanceArray1));
//			System.out.println(Arrays.toString(importanceArray2));
			for(int j = 0; j < 4; j++) {
				tmp1 = hedge1.getFuzzyHashV().get(importanceArray1[j]);
				tmp2 = hedge2.getFuzzyHashV().get(importanceArray2[j]);
//				System.out.println(tmp1 + " " + tmp2);
				result[i] += tmp1 * tmp2;
//				System.out.println((hedge1.getFuzzyHashV().get(importanceArray1[j]))*(hedge2.getFuzzyHashV().get(importanceArray2[j])));
//				System.out.println(hedge1.getFuzzyHashV().get(importanceArray1[j]));
//				System.out.println(hedge2.getFuzzyHashV().get(importanceArray2[j]));
				
			}
			result[i] = Math.round(result[i]*10000);
			result[i] = result[i]/10000;
//			System.out.println(result[i]);
			resultHash.put(result[i],hedge1.getCarName(i));
		}
		return result;
	}
	
	public Hashtable<Double, String> getResultHash() {
		return resultHash;
	}
	public void setResultHash(Hashtable<Double, String> resultHash) {
		this.resultHash = resultHash;
	}
	public Hedge getImpHedge() {
		return impHedge;
	}
	public void setImpHedge(Hedge impHedge) {
		this.impHedge = impHedge;
	}
	public Hedge getRateHedge() {
		return rateHedge;
	}
	public void setRateHedge(Hedge rateHedge) {
		this.rateHedge = rateHedge;
	}

	public String[][] getRateArray() {
		return this.getRateHedge().getWords();
	}

	public void setRateArray(String value, int x, int y) {
		this.rateArray[x][y] = value;
	}

	public String[][] getImpArray() {
		return this.getImpHedge().getWords();
	}

	public void setImpArray(String value, int x, int y) {
		this.impArray[x][y] = value;
	}

	public Pane getGoogleMapBox() {
		return googleMapBox;
	}
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
