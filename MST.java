package indexedPriorityQueue;

/*
 * Author:  Viswanadha Pratap Kondoju
 * NetID: vxk147730
 */


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Comparable;

public class MST {
    static final int Infinity = Integer.MAX_VALUE;

    static int PrimMST(Graph g)
    {
	MST mst = new MST();
	Integer[] st = new Integer[g.N];
	int wmst = 0;	
	Graph.Vertex src = g.V[1];
	src.name = 1;	
	src.weight = 0;	
	/*
	 * create the minimum priority queue with the 
	 * vertices of the graph
	 */
	PriorityQueueIndexed<Graph.Vertex> PQ = new PriorityQueueIndexed(g.V);			
	Graph.Vertex temp,v;
	int i = 0;	
	while(!PQ.isEmpty())
	{
		
		temp = PQ.deleteMin();
		temp.seen = true;
		wmst = wmst+temp.weight;
		st[i] = temp.name; i++;				
		for( Graph.Edge e : temp.Adj)
		{
			v = e.otherEnd(temp);			
			if( v.seen == false && e.Weight < v.weight)
			{
				v.weight = e.Weight;
				v.parent = temp;
				PQ.decreaseKey(v);
			}
		}
	}
		
	return wmst;
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
	 
	Graph g = Graph.readGraph(in);
	System.out.println(PrimMST(g));
    }
}
