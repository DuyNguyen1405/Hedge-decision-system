package lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolTip;

import layout.SelectionAdapter;
import layout.SelectionPainter;
import main.Car;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private Hashtable<String, String> constrainHash = new Hashtable<String, String>();
	private static BufferedReader reader;
	private Hedge impHedge;
	private Hedge rateHedge;
	private String[][] rateArray;
	private String[][] impArray;
	private String[][] posArray;
	private String[][] posInfoArray;
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
	private JComboBox comboBox = new JComboBox();
	private static JTextArea textArea = new JTextArea("Log");
	private static JTextArea routeArea = new JTextArea("Waypoint route");
	private final JXMapKit jXMapKit = new JXMapKit();
	private final JXMapViewer mapViewer = jXMapKit.getMainMap();
//	private final JXMapViewer mapViewer = new JXMapViewer();
	private static final double R = 6372.8;
	private static final String START = "D";
	private static final String END = "V";
	private static Set<MyWaypoint> waypoints = null;
	private Edge[] GRAPH = {
		      new Edge("D", "V", 70), new Edge("D", "2", 9), new Edge("2", "1", 14), new Edge("1", "V", 20),
		      new Edge("D", "M", 4), new Edge("D","3",2), new Edge("3", "M",1),
		      new Edge("D", "W", 9),
		      new Edge("D", "2", 10), new Edge("2", "Z", 1), new Edge("D","Z",20),
		      new Edge("D", "O", 10),
		      new Edge("D", "E", 5),
		      new Edge("D", "R", 6),
		      new Edge("D", "Y", 8),
		      new Edge("D", "B", 10), new Edge("B","V",15),
		      new Edge("D", "X", 1), new Edge("X","Z",1), new Edge("L", "Z", 10), new Edge("J","Z",10), 
		      new Edge("K","M",0), new Edge("D","K",0)
		   };
	private Graph g = new Graph(GRAPH);
	private List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
	public void initHedge(String fileName) throws IOException {
		String rateFileName = "src/lib/Map/" + fileName + ".txt";
		String impFileName =  "src/lib/Map/" + fileName + "i.txt";
		String posFileName =  "src/lib/Map/" + fileName + "p.txt";
		
		Hedge hedge1 = new Hedge(new String[] {"low","high"}, new String[] {"very"},new String[] {"little"},0.5,0.5,0.5, rateFileName);
		Hedge hedge2 = new Hedge(new String[] {"unimportant","important"}, new String[] {"very"},new String[] {"little"},0.65,0.35,0.4, impFileName);
		
		setImpHedge(hedge2);
		setRateHedge(hedge1);
		setRateArray(rateHedge.getWords());
		setImpArray(impHedge.getWords());
		readPosWaypoint(posFileName);
		
        for (int r = 0; r < 6; r++) {
			comboBox.addItem(posInfoArray[r][1]);
		}
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
		// Create a TileFactoryInfo for OpenStreetMap
//		TileFactoryInfo info = new OSMTileFactoryInfo();
//		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
//		tileFactory.setThreadPoolSize(8);
//
//		// Setup local file cache
//		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
//		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

		// Setup JXMapViewer
		mapViewer.setTileFactory(tileFactory);
		mapViewer.setZoom(1);
		mapViewer.setAddressLocation(new GeoPosition(21.00, 105.8414101));
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
		// Add interactions
//		MouseInputListener mia = new PanMouseInputListener(mapViewer);
//		mapViewer.addMouseListener(mia);
//		mapViewer.addMouseMotionListener(mia);
//		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
////		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
//		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
//		setWaypoint();
		textArea.append("\nSetup Map");
	}
	
	public void readPosWaypoint(String name) throws IOException {
		FileReader file = new FileReader(name);
		reader = new BufferedReader(file);
		String line = reader.readLine();;
		String[] arr = null;
		posArray = new String[18][2];
		posInfoArray = new String[18][3];
		int i = 0;
		do{
			arr = line.split(",");
			for(int j = 0; j < arr.length; j++) {
				if (j<3) {
					posInfoArray[i][j] = arr[j];
				}
				else posArray[i][j-3] = arr[j];
//				System.out.print(i + " " + j + " " + arr[j] + " ");
			}
//			System.out.println(" ");
			i++;
			line = reader.readLine();
		}while(line != null);
//		System.out.println(posArray[0][0] + " " + posInfoArray[0][0]);
		reader.close();
	}
	
	public Hashtable<String, String> setConstrainHash() {
		Hashtable<String, Integer> distHash = g.getDistanceHash();
		for(int i = 0; i<posInfoArray.length;i++) {
			if (!posInfoArray[i][2].equals("")) {
				MyWaypoint waypoint = (findWaypoint(new GeoPosition(Double.parseDouble(posArray[i][0]),Double.parseDouble(posArray[i][1]))));
				String[] constrainArray = posInfoArray[i][2].split(" ");
				for(int j = 0;j<constrainArray.length;j++) {
					if(!constrainHash.containsKey(constrainArray[j])) {
						constrainHash.put(constrainArray[j],waypoint.getLabel());
					} else {

					}
				}
			}
		}
		return constrainHash;
	}
	
//	public Hashtable<String, String> setConstrainHash() {
//		Hashtable<String, Integer> distHash = g.getDistanceHash();
//		for(int i = 0; i<posInfoArray.length;i++) {
//			for (MyWaypoint p : waypoints) {
//			      if((p.getName().equals(posInfoArray[i][0])) && (!posInfoArray[i][2].equals(""))) {
//			    	  String[] constrainArray = posInfoArray[i][2].split(" ");
//			    	  for(int j = 0; j < constrainArray.length; j++) {
//			    		  if(!constrainHash.containsKey(constrainArray[j])) {
//			    			  System.out.println(constrainArray[j] + " " + p.getLabel());
//			    			  constrainHash.put(constrainArray[j],p.getLabel());
//			    		  } else {
////			    			  System.out.println(constrainArray[j] + " " + p.getLabel());
//			    		  }
//			    	  }
////			    	  constrainHash.put(posInfoArray[i][2],p.getLabel());
//			      }
//			   }
//		}
//		return constrainHash;
//	}
	
	public void setWaypoint() {
		GeoPosition bachkhoa = new GeoPosition(Double.parseDouble(posArray[0][0]), Double.parseDouble(posArray[0][1]));
		GeoPosition s1 = new GeoPosition(Double.parseDouble(posArray[1][0]), Double.parseDouble(posArray[1][1]));
		GeoPosition s2 = new GeoPosition(Double.parseDouble(posArray[2][0]), Double.parseDouble(posArray[2][1]));
		GeoPosition s3 = new GeoPosition(Double.parseDouble(posArray[3][0]), Double.parseDouble(posArray[3][1]));
		GeoPosition s4 = new GeoPosition(Double.parseDouble(posArray[4][0]), Double.parseDouble(posArray[4][1]));
		GeoPosition s5 = new GeoPosition(Double.parseDouble(posArray[5][0]), Double.parseDouble(posArray[5][1]));
		GeoPosition t1 = new GeoPosition(Double.parseDouble(posArray[6][0]), Double.parseDouble(posArray[6][1]));
		GeoPosition t2 = new GeoPosition(Double.parseDouble(posArray[7][0]), Double.parseDouble(posArray[7][1]));
		GeoPosition t3 = new GeoPosition(Double.parseDouble(posArray[8][0]), Double.parseDouble(posArray[8][1]));
		GeoPosition s12 = new GeoPosition(Double.parseDouble(posArray[9][0]), Double.parseDouble(posArray[9][1]));
		GeoPosition s13 = new GeoPosition(Double.parseDouble(posArray[10][0]), Double.parseDouble(posArray[10][1]));
		GeoPosition s22 = new GeoPosition(Double.parseDouble(posArray[11][0]), Double.parseDouble(posArray[11][1]));
		GeoPosition t4 = new GeoPosition(Double.parseDouble(posArray[12][0]), Double.parseDouble(posArray[12][1]));
		GeoPosition t5 = new GeoPosition(Double.parseDouble(posArray[13][0]), Double.parseDouble(posArray[13][1]));
		GeoPosition t6 = new GeoPosition(Double.parseDouble(posArray[14][0]), Double.parseDouble(posArray[14][1]));
		GeoPosition t7 = new GeoPosition(Double.parseDouble(posArray[15][0]), Double.parseDouble(posArray[15][1]));
		GeoPosition t8 = new GeoPosition(Double.parseDouble(posArray[16][0]), Double.parseDouble(posArray[16][1]));
		GeoPosition t9 = new GeoPosition(Double.parseDouble(posArray[17][0]), Double.parseDouble(posArray[17][1]));
		
		MyWaypoint destination = new MyWaypoint("D", Color.ORANGE, posInfoArray[0][0], bachkhoa);
		MyWaypoint w1 = new MyWaypoint("W", Color.CYAN, posInfoArray[1][0], s1);
		MyWaypoint w2 = new MyWaypoint("M", Color.GRAY, posInfoArray[2][0], s2);
		MyWaypoint w3 = new MyWaypoint("Z", Color.MAGENTA, posInfoArray[3][0], s3);
		MyWaypoint w4 = new MyWaypoint("O", Color.GREEN, posInfoArray[4][0], s4);
		MyWaypoint w5 = new MyWaypoint("V", Color.YELLOW, posInfoArray[5][0], s5);
		MyWaypoint wt1 = new MyWaypoint("1", Color.WHITE, posInfoArray[6][0], t1);
		MyWaypoint wt2 = new MyWaypoint("2", Color.WHITE, posInfoArray[7][0], t2);
		MyWaypoint wt3 = new MyWaypoint("3", Color.WHITE, posInfoArray[8][0], t3);
		MyWaypoint w12 = new MyWaypoint("E", Color.CYAN, posInfoArray[9][0], s12);
		MyWaypoint w13 = new MyWaypoint("R", Color.CYAN, posInfoArray[10][0], s13);
		MyWaypoint w22 = new MyWaypoint("Y", Color.GRAY, posInfoArray[11][0], s22);
		MyWaypoint wt4 = new MyWaypoint("B", Color.BLACK, posInfoArray[12][0], t4);
		MyWaypoint wt5 = new MyWaypoint("X", Color.BLACK, posInfoArray[13][0], t5);
		MyWaypoint wt6 = new MyWaypoint("L", Color.BLACK, posInfoArray[14][0], t6);
		MyWaypoint wt7 = new MyWaypoint("K", Color.BLACK, posInfoArray[15][0], t7);
		MyWaypoint wt8 = new MyWaypoint("J", Color.BLACK, posInfoArray[16][0], t8);
		MyWaypoint wt9 = new MyWaypoint("H", Color.BLACK, posInfoArray[17][0], t9);
		
		mapViewer.setAddressLocation(bachkhoa);
		waypoints = new HashSet<MyWaypoint>(Arrays.asList(destination,w1, w2, w3, w4, w5,wt1,wt2,wt3,w12,w13,w22,wt4,wt5,wt6,wt7,wt8,wt9));
		// Create a waypoint painter that takes all the waypoints
		final WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
		waypointPainter.setWaypoints(waypoints);
		waypointPainter.setRenderer(new FancyWaypointRenderer());
		
		// Create a compound painter that uses both the route-painter and the waypoint-painter
		final List<Painter<JXMapViewer>> nodePainters = new ArrayList<Painter<JXMapViewer>>();
		nodePainters.add(waypointPainter);
		painters = nodePainters;
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(nodePainters);
		mapViewer.setOverlayPainter(painter);
		textArea.append("\nSet waypoint position");
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
	
	public void changeDistance(MyWaypoint waypoint1, int dist) {
		int size = GRAPH.length;
		for(int i = 0;i<size;i++) {
			if(GRAPH[i].v1.equals("D") && GRAPH[i].v2.equals(waypoint1.getLabel())) {
//				System.out.println(GRAPH[i].v2 + " " + GRAPH[i].dist);
				GRAPH[i].dist = dist;
			}
		}
		g = new Graph(GRAPH);
	}
	
	public static MyWaypoint findWaypoint(String label) {
		MyWaypoint wayPoint = new MyWaypoint(null, null, null,null);
		for (MyWaypoint p : waypoints) {
	      if(p.getLabel().equals(label)) {
	     	 wayPoint = p;
	      }
	   }
		return wayPoint;
	}
	
	public static MyWaypoint findWaypoint(GeoPosition pos) {
		MyWaypoint wayPoint = new MyWaypoint(null, null, null,null);
		for (MyWaypoint p : waypoints) {
	      if(p.getPosition().equals(pos)) {
	    	  wayPoint = p;
	      }
	   }
		return wayPoint;
	}
	
	public static GeoPosition findWaypointPos(String label) {
		GeoPosition position = null;
		for (MyWaypoint p : waypoints) {
	      if(p.getLabel().equals(label)) {
	     	 position = p.getPosition();
	      }
	   }
		return position;
	}
	
	public List<String> removeSameType(List<String> paths,Hashtable<Double, String> resultHash) {
		List<MyWaypoint> waypointList = new ArrayList<MyWaypoint>();
		List<String> newPath = new ArrayList<String>();
		Hashtable<String, Integer> distHash = g.getDistanceHash();
		Hashtable<String, Integer> dupHash = new Hashtable<String, Integer>();
		MyWaypoint dupPoint = new MyWaypoint(null, null, null,null);
		int i = 0;
		waypointList.addAll(getWaypoints());
		for(i = 0; i < waypointList.size(); i++) {
			MyWaypoint point = waypointList.get(i);
			if(!dupHash.containsKey(point.getName())) {
				dupHash.put(point.getName(), 1);
			} else {
				int count = dupHash.get(point.getName());
				dupHash.remove(point.getName());
				dupHash.put(point.getName(), count+1);
			}
		}
		Object[] dupKey = dupHash.keySet().toArray();
		for(i = 0; i < dupKey.length; i++) {
			if(dupHash.get(dupKey[i]) > 1) textArea.append("\nThere are " + dupHash.get(dupKey[i]) + " of " + dupKey[i] + " nearby ");
		}
		System.out.println(dupHash);
		for(i = 0; i < waypointList.size(); i++) {
//			System.out.println(waypointList.get(i).getLabel() + " " + waypointList.get(i).getName());
			for(int j = i+1; j < waypointList.size(); j++) {
//				System.out.println(waypointList.get(i).getName() + " " + waypointList.get(j).getName() + " " + i + " " + j);
				if((waypointList.get(i).getName().equals(waypointList.get(j).getName())) && (i != j)) {
//					System.out.println(waypointList.get(i).getName() + " " + waypointList.get(j).getName() + " " + i + " " + j);
					int distI = distHash.get(waypointList.get(i).getLabel());
					int distJ = distHash.get(waypointList.get(j).getLabel());
					if (distI < distJ) {
						dupPoint = waypointList.get(j);
//						waypointList.remove(j);
					} else {
						dupPoint = waypointList.get(i);
//						waypointList.remove(i);
					}
//					System.out.println(dupPoint.getName());

					//					System.out.println("name: " + waypointList.get(i).getLabel() + " dist: " + distHash.get(waypointList.get(i).getLabel()));
//					waypoints.remove(dupPoint);
					for(int p = 0; p < paths.size(); p++) {
						if (paths.get(p).indexOf(dupPoint.getLabel()) == 0) {
							textArea.append("\n" + dupPoint.getName() + " waypoint " + dupPoint.getLabel() + " is too far from the destination");
							paths.remove(p);
						}
					}
				}
			}
		}
		
		for(i = 0; i < paths.size(); i++) {
			String route = paths.get(i);
			Object[] constrain = constrainHash.keySet().toArray();
			if (paths.get(i).matches("\\s+\\d(.*)") || paths.get(i).matches("\\d(.*)") || findWaypoint(paths.get(i).substring(0, 1)).getName().matches("t++(.*)")) {
				paths.remove(i);
				i--;
			}
		}
		newPath = paths;
		
		return newPath;
	}
	
	public void getHash() {
		String newLabel = new String();
		textArea.append("\n\nWaypoints appear in multiple routes. Priority will be decided by Hedge value");
		for (MyWaypoint p : waypoints) {
			final JToolTip tooltip = new JToolTip();
			String text = "";
			Hashtable<String, Double> visited = p.getVisited(); 
			for (String key : visited.keySet()) {
				text = text + key + ": " + visited.get(key) + " ";  
			}
			if (!text.equals("")) textArea.append("\n" + p.getLabel() + ": " + text);
//	        tooltip.setTipText(text);
//	        tooltip.setComponent(jXMapKit.getMainMap());
//	        jXMapKit.getMainMap().add(tooltip);
//	        
//	        jXMapKit.getMainMap().addMouseMotionListener(new MouseMotionListener() {
//	            @Override
//	            public void mouseDragged(MouseEvent e) { 
//	                // ignore
//	            }
//
//	            @Override
//	            public void mouseMoved(MouseEvent e)
//	            {
//	                JXMapViewer map = jXMapKit.getMainMap();
//
//	                // convert to world bitmap
//	                Point2D worldPos = map.getTileFactory().geoToPixel(p.getPosition(), map.getZoom());
//
//	                // convert to screen
//	                Rectangle rect = map.getViewportBounds();
//	                int sx = (int) worldPos.getX() - rect.x;
//	                int sy = (int) worldPos.getY() - rect.y;
//	                Point screenPos = new Point(sx, sy);
//
//	                // check if near the mouse
//	                if (screenPos.distance(e.getPoint()) < 20)
//	                {
//	                    screenPos.x -= tooltip.getWidth() / 2;
//
//	                    tooltip.setLocation(screenPos);
//	                    tooltip.setVisible(true);
//	                }
//	                else
//	                {
//	                    tooltip.setVisible(false);
//	                }
//	            }
//	        });
	   }
	}
	
	public static List<GeoPosition> createTrack(String route, Hashtable<Double, String> resultHash){
//		System.out.println(route);
//		System.out.println(resultHash);
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		GeoPosition position = null;
		Hashtable<String, Double> visited = new Hashtable<String, Double>(); 
		String[] routeArray = route.split(" ");
		MyWaypoint point = null;
		MyWaypoint from = findWaypoint(routeArray[0]);
		for(int i = 0; i < routeArray.length;i++) {
			position = findWaypointPos(routeArray[i]);
			point = findWaypoint(routeArray[i]);
			if (routeArray[i].matches("\\d+") && routeArray[0].matches("[a-zA-Z]+")){
//				System.out.print(routeArray[i] + " " + (routeArray[i].matches("\\d+") && routeArray[0].matches("\\S+")) + " ");
//				System.out.println(from.getName() + ": " + getKeyFromValue(resultHash,from.getName()));
				point.getVisited().put(from.getName(), getKeyFromValue(resultHash,from.getName()));
			}
			track.add(position);
//			System.out.print(findWaypoint(routeArray[i]).getVisited() + " ");
		}
		return track;
	}
	
	public static double getKeyFromValue(Hashtable<Double, String> hm, String value) {
	    double result = 0;
		for (double o : hm.keySet()) {
	      if (hm.get(o).equals(value)) {
	        result = o;
	      }
	    }
		return result;
	}
	
	public static String getKey(Hashtable<String, String> constrainHash, String value) {
		String result = "";
		for (String s: constrainHash.keySet()) {
			if(constrainHash.get(s).equals(value)) {
				result += s + " ";
			}
		}
		result = result.substring(0, result.length()-1);
		return result;
	}
	
	public void setBothConstrainHash() {
		constrainHash = setConstrainHash();
		g.setConstrainHash(constrainHash,textArea);
//		System.out.println(constrainHash);
		textArea.append("\nConstrain: On");
//		System.out.println(constrainHash + " " + g.getConstrainHash());
	}
	
	public void testDraw() {
		List<GeoPosition> track = createTrack("E D",resultHash);
		RoutePainter routePainter = new RoutePainter(track);
		painters.add(routePainter);
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}
	
	public void testDraw2() {
		List<GeoPosition> track = createTrack("O D",resultHash);
		RoutePainter routePainter = new RoutePainter(track);
		painters.add(routePainter);
		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}
    public void drawRoute(Hashtable<Double, String> resultHash) {
    	String path = new String();
    	textArea.append("\n\nGet shortest route for waypoints");
//    	g.setConstrainHash(constrainHash,textArea);
		g.dijkstra(START);
		List<String> paths = g.printAllPaths();
		paths = removeSameType(paths, resultHash);
		SortedSet<Double> keys = new TreeSet<Double>(Collections.reverseOrder());
		keys.addAll(resultHash.keySet());
		textArea.append("\n\nAll route:");
		routeArea.append("\n");
		int j =1;
		for (Double key : keys) { 
		   String value = resultHash.get(key);
		   
			for(int i = 1; i< paths.size(); i++) {
				path = paths.get(i);
				if(findWaypoint(paths.get(i).substring(0, 1)).getName().equals(value)) {
					List<GeoPosition> track = createTrack(path,resultHash);
					new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {
					            	final RoutePainter routePainter = new RoutePainter(track);
									painters.add(routePainter);
									CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
									mapViewer.setOverlayPainter(painter);
					            }
					        }, 
					        2000*j
					);
					textArea.append("\n" + findWaypoint(path.substring(0, 1)).getName() + ": "+ path);
					String routeApp = "\n" + findWaypoint(path.substring(0, 1)).getName() + ": "+ path;
					if (constrainHash.contains(path.substring(0, 1))) {
//						System.out.println(getKey(constrainHash, path.substring(0, 1)));
						routeApp += " (Blocked by " + getKey(constrainHash, path.substring(0, 1)) + ")";
					}
					routeArea.append(routeApp);
				}
			}
			j = j + 1;
		}
//		for(String path: paths) {
//			List<GeoPosition> track = createTrack(path,resultHash);
//			final RoutePainter routePainter = new RoutePainter(track);
//			textArea.append("\n" + findWaypoint(path.substring(0, 1)).getName() + ": "+ path);
//			String routeApp = "\n" + findWaypoint(path.substring(0, 1)).getName() + ": "+ path;
//			if (constrainHash.contains(path.substring(0, 1))) {
////				System.out.println(getKey(constrainHash, path.substring(0, 1)));
//				routeApp += " (Blocked by " + getKey(constrainHash, path.substring(0, 1)) + ")";
//			}
//			routeArea.append(routeApp);
//			painters.add(routePainter);
//			CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
//			mapViewer.setOverlayPainter(painter);
//		}
		getHash();
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
			if (!resultHash.containsValue(hedge1.getCarName(i))){
				resultHash.put(result[i],hedge1.getCarName(i));
			} 
			if ((resultHash.containsValue(hedge1.getCarName(i))) && (result[i] != getKeyFromValue(resultHash, hedge1.getCarName(i)))){
				resultHash.remove(getKeyFromValue(resultHash, hedge1.getCarName(i)));
				resultHash.put(result[i], hedge1.getCarName(i));
			}
//			System.out.println(resultHash);
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

	public void setImpArrayValue(Object object, int x, int y) {
		this.impArray[x][y] = (String) object;
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

	public String[][] getPosArray() {
		return posArray;
	}

	public void setPosArray(String[][] posArray) {
		this.posArray = posArray;
	}
	
	public void setPosArrayValue(Object object, int x, int y) {
		this.posArray[x][y] = (String) object;
	}
	
	public String[][] getPosInfoArray() {
		return posInfoArray;
	}

	public void setPosInfoArray(String[][] posInfoArray) {
		this.posInfoArray = posInfoArray;
	}
	
	public void setPosInfoArrayValue(Object object, int x, int y) {
		this.posInfoArray[x][y] = (String) object;
	}

	public JComboBox getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox comboBox) {
		this.comboBox = comboBox;
	}

	public Edge[] getGRAPH() {
		return GRAPH;
	}

	public void setG(Graph g) {
		this.g = g;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public Hashtable<String, String> getConstrainHash() {
		return constrainHash;
	}

	public void setConstrainHash(Hashtable<String, String> constrainHash) {
		this.constrainHash = constrainHash;
	}
	
	public static JTextArea getRouteArea() {
		return routeArea;
	}

	public static void setRouteArea(JTextArea routeArea) {
		Controller.routeArea = routeArea;
	}
}
