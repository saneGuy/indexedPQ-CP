package indexedPriorityQueue;
/*
 * Author: Viswanadha Pratap Kondoju
 * NetID:  vxk147730
 */
import indexedPriorityQueue.Graph.*;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CriticalPaths {
    static final int Infinity = Integer.MAX_VALUE;
    static final int Negative_Infinity = Integer.MIN_VALUE;
    static ArrayList<Vertex> CP = new ArrayList<Vertex>();
    static Queue<Vertex> Q = new LinkedList<Vertex>();
    static Graph g;
    private int phase = 0;
    private long startTime, endTime, elapsedTime;
    
    
    /*
     * The output will contain multiple critical paths. 
     * Thus storing all the paths with nodes having slack 0
     * and sum of the duration of nodes in each path which 
     * can be compared to the critical path distance and 
     * print only the path that matches this value. Essentially
     * we are maintaining a list of paths and each path is a list
     * of vertices and sum of the duration of those vertices in the
     * path
     */
  static class CriticalPathInfo
  {
  	int distance;
  	int numberOfCriticalPaths;
  	List<Path> ListOfPaths;
      public static class Path
      {
      	List<Vertex> path;
      	int pathSum;
      	Path(List<Vertex> p, int sum)
      	{
      		path = new LinkedList<>(p);
      		pathSum = sum;
      	}
      }
                          
      CriticalPathInfo()
      {
      	ListOfPaths = new LinkedList<>();
      	distance = 0;
      }
                          
      void addNewPath(List<Vertex> path, int sum)
      {
      	ListOfPaths.add(new Path(path, sum));
      }
  }             

  /*
   * DAGCriticalPaths will take graph and Topological order of the
   * graph as input and computes the EC(earliest completion time),
   * LC(Latest completion time), slack time(LC-EC)
   */
    int  DAGCriticalPaths(Graph g,Vertex[] Top)
    {
	   Vertex v;
	   //computing the indegree of the vertices
	   for(int i = 0; i < g.N ; i++)
	    {
   			for(Edge e : g.V[i].Adj)
   			{
   				v = e.otherEnd(g.V[i]);
   				v.indegree++;
   			}
	    }
	   //computing the ECs of the nodes
	   for(int i = 0; i < Top.length; i++)
	   {
		   if(Top[i].indegree == 0)
		   {
			   Top[i].ec = Top[i].weight;
		   }
		   for(Edge e : Top[i].Adj)
		   {
			   v = e.otherEnd(Top[i]);
			   if(v.ec < Top[i].ec + v.weight)
			   {
				   v.ec = Top[i].ec + v.weight;
			   }
		   }
	   }
	   // computing the LCs of the nodes 
	   int noOfNodesInCP = -1;
	   for(int i = Top.length - 1 ; i >= 0 ; i --)
	   {
		   if(i == Top.length - 1)
		   {
			  Top[i].lc = Top[i].ec;
		   }
		   for(Edge e : Top[i].revAdj)
		   {
			  v = e.otherEnd(Top[i]);
			  if(v.lc > (Top[i].lc - Top[i].weight))
			  {
				  v.lc = Top[i].lc - Top[i].weight;
			      if(v.lc == v.ec)
			      {
			    	  noOfNodesInCP++;
			      }
			   }
		   }
	   }
	   
	   CriticalPathInfo out = new CriticalPathInfo();
	   // we will compute the critical paths and print them
	   computeCP(g.V[0], new LinkedList<>(), out, 0);
	   //printing the output as required
	   System.out.println("Critical Path Length : " + Top[g.N - 1].ec);
	   System.out.println("Output: ");
	   System.out.println(Top[g.N - 1].ec+ " " +noOfNodesInCP + " " + out.numberOfCriticalPaths);
	   int count = 0;  
	   for(CriticalPathInfo.Path p : out.ListOfPaths )
        {
           if(p.pathSum == Top[g.N - 1].ec)
        	{
        	   count++;
        	   p.path.remove(p.path.size() - 1);
        	   System.out.println( count + ":" + p.path);
        	}
        }
	  return 0;
    }
   /*
    * computeCP will compute the number of critical paths ,critical paths
    *  and the sum of durations of the tasks in each path. Essentially
    *  we maintain a list of paths and each path is a list of vertices.
    *  We also maintain the sum of the durations of the vertices of a path 
    *  in the path itself. If there is multiple same path sums then we have 
    *  multiple critical paths.
    */
   
   public void computeCP(Vertex v, List<Vertex> path, CriticalPathInfo out, int sum)
   {
   	if(v.equals(g.V[g.N - 1]))
   	{
   		if(sum > out.distance)
   		{
   			out.distance = sum;
   			out.numberOfCriticalPaths=1;
   		} 
   		else if(sum == out.distance)
   		{
   			out.numberOfCriticalPaths++;
   		}
   		out.addNewPath(path, sum);
   	} 
   	else
   	{
   		for(Edge e : v.Adj)
   		{
   			Vertex u = e.otherEnd(v);
   			if(u.lc - u.ec == 0)
   			{
   				path.add(u);
   				computeCP(u, path, out, sum+u.weight);
   				path.remove(u);
   			}
   		}
       }
   }
    /*
     * Driver function that reads a graph from the input
     * file and calls PrimMST() on the graph to create 
     * the minimum spanning tree and prints the weight 
     * of the minimum spanning tree.
     */

    public static void main(String[] args) throws FileNotFoundException 
    {
    	Scanner in;
    	if (args.length > 0) {
    	    File inputFile = new File(args[0]);
    	    in = new Scanner(inputFile);
    	} else {
    	    in = new Scanner(System.in);
    	}
	g = Graph.readGraph(in);
	Vertex[] Top;
	CriticalPaths cp = new CriticalPaths();
	// we get a topological order of the vertices of the graph
	cp.timer();
	Top = g.topologicalOrder();
	cp.DAGCriticalPaths(g,Top);
	cp.timer();
	// printing the output tasks their EC,LC and SLACKS as required
	System.out.println("Task" + "       " + " EC " + "         " + " LC " + "         " + " Slack ") ;
    for(int i = 1; i< g.N - 1 ; i++)
	{
		System.out.println(g.V[i].name + "           " + g.V[i].ec + "           " + g.V[i].lc + "           " + (g.V[i].lc - g.V[i].ec)) ;
	}
	}
    
    public void timer()
    {
        if(phase == 0) {
	    startTime = System.currentTimeMillis();
	    phase = 1;
	} else {
	    endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;
            System.out.println("Time: " + elapsedTime + " msec.");
            memory();
            phase = 0;
        }
    }

    public void memory() {
        long memAvailable = Runtime.getRuntime().totalMemory();
        long memUsed = memAvailable - Runtime.getRuntime().freeMemory();
        System.out.println("Memory: " + memUsed/1000000 + " MB / " + memAvailable/1000000 + " MB.");
    }


  }

