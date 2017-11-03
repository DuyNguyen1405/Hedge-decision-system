package test;

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

public class test extends JFrame {
//    public test() {
//        initUI();
//    }

//    private void initUI() {
//    	final JFrame frame = new JFrame();
//		frame.setLayout(new BorderLayout());
//		frame.add(new JLabel("Use left mouse button to pan, mouse wheel to zoom and right mouse to select"), BorderLayout.NORTH);
//		frame.setSize(800, 600);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//
//        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
//        vertical.setFloatable(false);
//        vertical.setMargin(new Insets(10, 5, 5, 5));
//
//        ImageIcon driveIcon = createImageIcon("computer.jpg","Java");
//        ImageIcon compIcon = new ImageIcon("computer.png");
//        ImageIcon printIcon = new ImageIcon("printer.png");
//        JButton driveBtn = new JButton(driveIcon);
//        driveBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
//        JButton compBtn = new JButton(compIcon);
//        compBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
//        JButton printBtn = new JButton(printIcon);
//        printBtn.setBorder(new EmptyBorder(3, 0, 3, 0));
//
//        vertical.add(driveBtn);
//        vertical.add(compBtn);
//        vertical.add(printBtn);
//
//        frame.add(vertical, BorderLayout.WEST);
//        
//        TileFactoryInfo info = new OSMTileFactoryInfo();
//		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
//		tileFactory.setThreadPoolSize(8);
//
//		// Setup local file cache
//		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
//		LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);
//
//		// Setup JXMapViewer
//		final JXMapViewer mapViewer = new JXMapViewer();
//		mapViewer.setTileFactory(tileFactory);
//
//		GeoPosition frankfurt = new GeoPosition(50.11, 8.68);
//
//		// Set the focus
//		mapViewer.setZoom(7);
//		mapViewer.setAddressLocation(frankfurt);
//	
//		// Add interactions
//		MouseInputListener mia = new PanMouseInputListener(mapViewer);
//		mapViewer.addMouseListener(mia);
//		mapViewer.addMouseMotionListener(mia);
//		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
//		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
//		mapViewer.addKeyListener(new PanKeyListener(mapViewer));
//		
//		// Add a selection painter
//		SelectionAdapter sa = new SelectionAdapter(mapViewer); 
//		SelectionPainter sp = new SelectionPainter(sa); 
//		mapViewer.addMouseListener(sa); 
//		mapViewer.addMouseMotionListener(sa); 
//		mapViewer.setOverlayPainter(sp);
//		
//		frame.add(mapViewer, BorderLayout.CENTER);
//
//        JLabel statusbar = new JLabel(" Statusbar");
//        frame.add(statusbar, BorderLayout.SOUTH);
//
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
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            test ex = new test();
//        });
//    }
//    
//    private static ImageIcon createImageIcon(String path, String description) {
//        java.net.URL imgURL = Sample1.class.getResource(path);
//        
//        if (imgURL != null) {
//           return new ImageIcon(imgURL, description);
//        } else {            
//           System.err.println("Couldn't find file: " + path);
//           return null;
//        }
//     }
//    
//    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer)
//	{
//		double lat = mapViewer.getCenterPosition().getLatitude();
//		double lon = mapViewer.getCenterPosition().getLongitude();
//		int zoom = mapViewer.getZoom();
//		frame.setTitle(String.format("JXMapviewer2 Example 3 (%.2f / %.2f) - Zoom: %d", lat, lon, zoom)); 
//	}
	
}