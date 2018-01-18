package lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import layout.SelectionAdapter;
import layout.SelectionPainter;
import main.Car;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
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
	private final JXMapKit jXMapKit = new JXMapKit();
	private final JXMapViewer mapViewer = jXMapKit.getMainMap();
	private static final double R = 6372.8;
	private static final String START = "D";
	private static final String END = "V";
	private static Set<MyWaypoint> waypoints = null;
	private final Edge[] GRAPH = {
		      new Edge("D", "V", 70), new Edge("D", "2", 9), new Edge("2", "1", 14), new Edge("1", "V", 20),
		      new Edge("D", "M", 10), new Edge("D","3",6), new Edge("3", "M",1),
		      new Edge("D", "W", 10),
		      new Edge("D", "Z", 10),
		      new Edge("D", "O", 10)
		   };
	private final Graph g = new Graph(GRAPH);
	private final List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
	public void initHedge(String fileName) throws IOException {
		String rateFileName = "src/lib/Map/" + fileName + ".txt";
		String impFileName =   "src/lib/Map/" + fileName + "i.txt";
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
    	TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		tileFactory.setThreadPoolSize(8);

		// Setup local file cache
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		// Setup JXMapViewer
		
		jXMapKit.setTileFactory(tileFactory);

		GeoPosition bachkhoa = new GeoPosition(21.00, 105.8414101);
		GeoPosition s1 = new GeoPosition(21.04, 105.7814101);
		GeoPosition s2 = new GeoPosition(21.00, 105.8214101);
		GeoPosition s3 = new GeoPosition(21.01, 105.8614101);
		GeoPosition s4 = new GeoPosition(21.02, 105.8414101);
		GeoPosition s5 = new GeoPosition(20.981, 105.841120);
		GeoPosition t1 = new GeoPosition(20.9931, 105.841120);
		GeoPosition t2 = new GeoPosition(20.9959, 105.842750);
		GeoPosition t3 = new GeoPosition(21.0016, 105.825);
		
		MyWaypoint destination = new MyWaypoint("D", Color.ORANGE, bachkhoa);
		MyWaypoint w1 = new MyWaypoint("W", Color.CYAN, s1);
		MyWaypoint w2 = new MyWaypoint("M", Color.GRAY, s2);
		MyWaypoint w3 = new MyWaypoint("Z", Color.MAGENTA, s3);
		MyWaypoint w4 = new MyWaypoint("O", Color.GREEN, s4);
		MyWaypoint w5 = new MyWaypoint("V", Color.YELLOW, s5);
		MyWaypoint wt1 = new MyWaypoint("1", Color.WHITE, t1);
		MyWaypoint wt2 = new MyWaypoint("2", Color.WHITE, t2);
		MyWaypoint wt3 = new MyWaypoint("3", Color.WHITE, t3);
		
		mapViewer.setZoom(1);
		mapViewer.setAddressLocation(bachkhoa);
		// Add interactions
		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);
		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
//		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
		
		waypoints = new HashSet<MyWaypoint>(Arrays.asList(destination,w1, w2, w3, w4, w5,wt1,wt2,wt3));
		// Create a waypoint painter that takes all the waypoints
		final WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
		waypointPainter.setWaypoints(waypoints);
		waypointPainter.setRenderer(new FancyWaypointRenderer());
		
		// Create a compound painter that uses both the route-painter and the waypoint-painter
		
		painters.add(waypointPainter);
		
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}
	
	public static double distance(GeoPosition pos1, GeoPosition pos2) {
		double lon1 = pos1.getLongitude();
		double lon2 = pos2.getLongitude();
		double lat1 = pos1.getLatitude();
		double lat2 = pos2.getLatitude();
		double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);
	    lat1 = Math.toRadians(lat1);
	    lat2 = Math.toRadians(lat2);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    return R * c;
	}
	
	public static MyWaypoint findWaypoint(String label) {
		MyWaypoint wayPoint = new MyWaypoint(null, null, null);
		for (MyWaypoint p : waypoints) {
	      if(p.getLabel().equals(label)) {
	     	 wayPoint = p;
	      }
	   }
		return wayPoint;
	}
	
	public static GeoPosition findWaypointPos(String label) {
		GeoPosition position = null;
		for (MyWaypoint p : waypoints) {
	      if(p.getLabel().equals(label)) {
	//     	 System.out.println(p.getPosition());
	     	 position = p.getPosition();
//	     	 p.setLabel(findWaypoint(START).getLabel());
	      }
	   }
		return position;
	}
	
	public static List<GeoPosition> createTrack(String route){
		System.out.println(route);
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		GeoPosition position = null;
		String[] routeArray = route.split(" ");
		for(int i = 0; i < routeArray.length;i++) {
			position = findWaypointPos(routeArray[i]);
			track.add(position);
			System.out.println(position);
		}
		return track;
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

	public static Set<MyWaypoint> getWaypoints() {
		return waypoints;
	}

	public static void setWaypoints(Set<MyWaypoint> waypoints) {
		Controller.waypoints = waypoints;
	}

	public JXMapKit getjXMapKit() {
		return jXMapKit;
	}

	public static String getStart() {
		return START;
	}

	public static String getEnd() {
		return END;
	}

	public Graph getG() {
		return g;
	}

	public List<Painter<JXMapViewer>> getPainters() {
		return painters;
	}
}
