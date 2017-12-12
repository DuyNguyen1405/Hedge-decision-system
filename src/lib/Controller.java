package lib;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import layout.SelectionAdapter;
import layout.SelectionPainter;
import main.Car;

import java.awt.Color;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
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
import org.jxmapviewer.viewer.WaypointPainter;

public class Controller {
	private Hashtable<Double, String> resultHash = new Hashtable<Double, String>();
	private Hedge impHedge;
	private Hedge rateHedge;
	private String[][] rateArray;
	private String[][] impArray;
	private Car car1;
	private Car car2;
	private Car car3;
	private Car car4;
	private Car car5;
	private JButton rateInfoBtn;
	private JButton rateEditBtn;
	private JButton impInfoBtn;
	private JButton impEditBtn;
	private JButton initBtn;
	private JButton runBtn;
	private JTable rateTable;
	private JTable impTable;
	private final JXMapViewer mapViewer = new JXMapViewer();
	public static String test = "test";
	public void initHedge(String fileName) throws IOException {
		String rateFileName = "src/lib/map/" + fileName + ".txt";
		String impFileName =   "src/lib/map/" + fileName + "i.txt";
//		String rateFileName = rateFilePath.replace("\\", "/");
//		String impFileName = impFilePath.replace("\\", "/");
		Hedge hedge1 = new Hedge(new String[] {"low","high"}, new String[] {"very"},new String[] {"little"},0.5,0.5,0.5, rateFileName);
		Hedge hedge2 = new Hedge(new String[] {"unimportant","important"}, new String[] {"very"},new String[] {"little"},0.65,0.35,0.4, impFileName);
		setImpHedge(hedge2);
		setRateHedge(hedge1);
		setRateArray(rateHedge.getWords());
		setImpArray(impHedge.getWords());
	    car1 = new Car(rateArray[1][0], rateArray[1][1], rateArray[1][2], rateArray[1][3], rateArray[1][4], impArray[1][1],impArray[1][2],impArray[1][3],impArray[1][4],0.0);
		car2 = new Car(rateArray[2][0], rateArray[2][1], rateArray[2][2], rateArray[2][3], rateArray[2][4], impArray[2][1],impArray[2][2],impArray[2][3],impArray[2][4],0.0);
		car3 = new Car(rateArray[3][0], rateArray[3][1], rateArray[3][2], rateArray[3][3], rateArray[3][4], impArray[3][1],impArray[3][2],impArray[3][3],impArray[3][4],0.0);
		car4 = new Car(rateArray[4][0], rateArray[4][1], rateArray[4][2], rateArray[4][3], rateArray[4][4], impArray[4][1],impArray[4][2],impArray[4][3],impArray[4][4],0.0);
		car5 = new Car(rateArray[5][0], rateArray[5][1], rateArray[5][2], rateArray[5][3], rateArray[5][4], impArray[5][1],impArray[5][2],impArray[5][3],impArray[5][4],0.0);
	}

	public void mapSetup() {
    	TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		tileFactory.setThreadPoolSize(8);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		// Setup JXMapViewer
		
		mapViewer.setTileFactory(tileFactory);

		GeoPosition bachkhoa = new GeoPosition(21.00, 105.8414101);
		GeoPosition s1 = new GeoPosition(21.20, 105.8414101);
		GeoPosition s2 = new GeoPosition(21.40, 105.8414101);
		GeoPosition s3 = new GeoPosition(21.60, 105.8414101);
		GeoPosition s4 = new GeoPosition(21.80, 105.8414101);
		GeoPosition s5 = new GeoPosition(22.00, 105.8414101);
		// Set the focus
		mapViewer.setZoom(1);
		mapViewer.setAddressLocation(bachkhoa);
	
		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);
		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
		
		 // Create waypoints from the geo-positions
		Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>(Arrays.asList(
				new MyWaypoint("S1", Color.ORANGE, s1),
				new MyWaypoint("S2", Color.CYAN, s2),
				new MyWaypoint("S3", Color.GRAY, s3),
				new MyWaypoint("S4", Color.MAGENTA, s4),
				new MyWaypoint("S5", Color.RED, s5),
				new MyWaypoint("D", Color.GREEN, bachkhoa)));

		// Create a waypoint painter that takes all the waypoints
		WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
		waypointPainter.setWaypoints(waypoints);
		waypointPainter.setRenderer(new FancyWaypointRenderer());
		
		mapViewer.setOverlayPainter(waypointPainter);
	}
	
    public static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer)
	{
		double lat = mapViewer.getCenterPosition().getLatitude();
		double lon = mapViewer.getCenterPosition().getLongitude();
		int zoom = mapViewer.getZoom();
		frame.setTitle(String.format("Hedge decision system (%.2f / %.2f) - Zoom: %d", lat, lon, zoom)); 
//		frame.setTitle(String.format("Hedge decision system")); 
	}
    
	public String getInfoContent(Hedge hedge) {
		String content = null;
		content = "Alpha: " + hedge.getAlpha() + 
				"\nBeta: " + hedge.getBeta() + 
				"\nTheta: " + hedge.getTheta() +
				"\nBase Hedge: " + Arrays.asList(hedge.getBaseG()).toString() +
				"\nPositive Hedge: " + Arrays.asList(hedge.getPositiveHedgeH()).toString() +
				"\nNagative Hedge: " + Arrays.asList(hedge.getNegativeHedgeH()).toString() +
				"\nHedge Sign: " + Arrays.asList(hedge.getFuzzySign()).toString() + 
				"\nHedge Fm: " + Arrays.asList(hedge.getFuzzyHashFm()).toString() +
				"\nHedge V: " + Arrays.asList(hedge.getFuzzyHashV()).toString();
		return content;
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
		return rateArray;
	}
	
		
	public void setRateArray(String[][] rateArray) {
		this.rateArray = rateArray;
	}

	public void setRateArrayValue(Object object, int x, int y) {
		this.rateArray[x][y] = (String) object;
	}

	public String[][] getImpArray() {
		return impArray;
	}

	public void setImpArray(String[][] impArray) {
		this.impArray = impArray;
	}

	public Car getCar1() {
		return car1;
	}

	public void setCar1(Car car1) {
		this.car1 = car1;
	}

	public Car getCar2() {
		return car2;
	}

	public void setCar2(Car car2) {
		this.car2 = car2;
	}

	public Car getCar3() {
		return car3;
	}

	public void setCar3(Car car3) {
		this.car3 = car3;
	}

	public Car getCar4() {
		return car4;
	}

	public void setCar4(Car car4) {
		this.car4 = car4;
	}

	public Car getCar5() {
		return car5;
	}

	public void setCar5(Car car5) {
		this.car5 = car5;
	}

	public JButton getRateInfoBtn() {
		return rateInfoBtn;
	}

	public void setRateInfoBtn(JButton rateInfoBtn) {
		this.rateInfoBtn = rateInfoBtn;
	}

	public JButton getRateEditBtn() {
		return rateEditBtn;
	}

	public void setRateEditBtn(JButton rateEditBtn) {
		this.rateEditBtn = rateEditBtn;
	}

	public JButton getImpInfoBtn() {
		return impInfoBtn;
	}

	public void setImpInfoBtn(JButton impInfoBtn) {
		this.impInfoBtn = impInfoBtn;
	}

	public JButton getImpEditBtn() {
		return impEditBtn;
	}

	public void setImpEditBtn(JButton impEditBtn) {
		this.impEditBtn = impEditBtn;
	}

	public JButton getInitBtn() {
		return initBtn;
	}

	public void setInitBtn(JButton initBtn) {
		this.initBtn = initBtn;
	}

	public JButton getRunBtn() {
		return runBtn;
	}

	public void setRunBtn(JButton runBtn) {
		this.runBtn = runBtn;
	}

	public JXMapViewer getMapViewer() {
		return mapViewer;
	}

	public JTable getRateTable() {
		return rateTable;
	}

	public void setRateTable(JTable rateTable) {
		this.rateTable = rateTable;
	}

	public JTable getImpTable() {
		return impTable;
	}

	public void setImpTable(JTable impTable) {
		this.impTable = impTable;
	}
}
