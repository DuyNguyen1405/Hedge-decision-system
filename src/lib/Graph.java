package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.JTextArea;



public class Graph {
	   private Hashtable<String, Integer> distanceHash = new Hashtable<String, Integer>();
	   private Hashtable<String, String> constrainHash = new Hashtable<String, String>();
	   private static JTextArea tArea;
	   private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
	   public Graph(Edge[] edges) {
	      graph = new HashMap<>(edges.length);
	 
	      //one pass to find all vertices
	      for (Edge e : edges) {
	         if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
	         if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
	      }
	 
	      //another pass to set neighbouring vertices
	      for (Edge e : edges) {
	         graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
	         graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); 
	      }
	   }
	 
	   /** Runs dijkstra using a specified source vertex */ 
	   public void dijkstra(String startName) {
	      if (!graph.containsKey(startName)) {
	         System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
	         return;
	      }
	      final Vertex source = graph.get(startName);
	      NavigableSet<Vertex> q = new TreeSet<>();
	 
	      // set-up vertices
	      for (Vertex v : graph.values()) {
	         v.previous = v == source ? source : null;
	         v.dist = v == source ? 0 : Integer.MAX_VALUE;
	         q.add(v);
	      }
	 
	      dijkstra(q);
	   }
	 
	   /** Implementation of dijkstra's algorithm using a binary heap. */
	   private void dijkstra(final NavigableSet<Vertex> q) {  
		  Vertex u, v;
	      while (!q.isEmpty()) {
	         u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
	         if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable
	 
	         //look at distances to each neighbour
	         for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
	            v = a.getKey(); //the neighbour in this iteration
//	            System.out.println(v + " " + u + " " + a);
	            int alternateDist = 0;
	            if(constrainHash.size() >0) {
	            	Object[] constrain = constrainHash.keySet().toArray();
		            for(int j = 0; j < constrain.length; j++) {
		            	if (v.name.equals(constrain[j]) && (v.dist != 10000)) {
//		            		System.out.println(v + " " + constrain[j]);
		            		tArea.append("\nSet constrain " + v.name + " for waypoint " + constrainHash.get(constrain[j]));
			            	a.setValue(10000);
//			            	graph.remove("B");
			            }
					}
	            }
//	            else alternateDist = u.dist + a.getValue();
	            alternateDist = u.dist + a.getValue();
//	            System.out.println(a.getValue());
	            if (alternateDist < v.dist) { // shorter path to neighbour found
	               q.remove(v);
	               v.dist = alternateDist;
	               v.previous = u;
	               q.add(v);
//	               System.out.println(q);
	            } 
	         }
	      }
//	      System.out.println(graph);
	   }
	 
	   /** Prints a path from the source to the specified vertex */
	   public String printPath(String endName) {
//	      if (!graph.containsKey(endName)) {
//	         System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
//	         return;
//	      }
	      //graph.get(endName).printPath();
//	      System.out.println(graph.get(endName).printPath());
	      return graph.get(endName).printPath();
	   }
	   /** Prints the path from the source to every vertex (output order is not guaranteed) */
	   public List<String> printAllPaths() {
		   List<String> paths = new ArrayList<String>();
		   for (Vertex v : graph.values()) {
			   paths.add(new StringBuilder(v.printPath()).reverse().toString());
			   distanceHash.put(v.name, v.dist);
//			   paths.add(v.printPath());
		   }
		   return paths;   
	   }

	public Hashtable<String, Integer> getDistanceHash() {
		return distanceHash;
	}

	public void setDistanceHash(Hashtable<String, Integer> distanceHash) {
		this.distanceHash = distanceHash;
	}

	public Hashtable<String, String> getConstrainHash() {
		return constrainHash;
	}

	public void setConstrainHash(Hashtable<String, String> constrainHash, JTextArea textArea) {
		this.constrainHash = constrainHash;
		tArea = textArea;
	}
	
	public void setConstrainHash(Hashtable<String, String> constrainHash) {
		this.constrainHash = constrainHash;
	}
	
	public Map<String, Vertex> getGraph() {
		return graph;
	}
}