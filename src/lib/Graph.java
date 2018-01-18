package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;



public class Graph {
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
	 
	            final int alternateDist = u.dist + a.getValue();
	            if (alternateDist < v.dist) { // shorter path to neighbour found
	               q.remove(v);
	               v.dist = alternateDist;
	               v.previous = u;
	               q.add(v);
	            } 
	         }
	      }
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
			   if(!v.name.contains("T")) {
				   paths.add(new StringBuilder(v.printPath()).reverse().toString());
			   }
//			   paths.add(v.printPath());
		   }
		   return paths;   
	   }
	}