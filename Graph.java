package indexedPriorityQueue;

import java.io.*;
import java.util.*;

/**
 * 
 * @author Viswanadha Pratap Kondoju
 * NetID : vxk147730
 * 
 */

/**
 * Class to represent a graph
 * 
 *
 */
public class Graph implements Iterable<Graph.Vertex> {
static final int INFINITY = Integer.MAX_VALUE;
static final int Negative_INFINITY = Integer.MIN_VALUE;
    public Vertex[] V; // array of vertices
    public int N; // number of verices in the graph

    /**
     * Constructor for Graph
     * 
     * @param size
     *            : int - number of vertices
     */
    Graph(int size) {
	N = size+2;
	V = new Vertex[size+3];
	// create an array of Vertex objects
	for (int i = 0; i <= size+1; i++)
	    V[i] = new Vertex(i);
	
    }
    
    /**
     * Class that represents an arc in a Graph
     *
     */
    public class Edge {
	public Vertex From; // head vertex
	public Vertex To; // tail vertex
	public int Weight;// weight of the arc

	/**
	 * Constructor for Edge
	 * 
	 * @param u
	 *            : Vertex - The head of the arc
	 * @param v
	 *            : Vertex - The tail of the arc
	 * @param w
	 *            : int - The weight associated with the arc
	 */
	Edge(Vertex u, Vertex v, int w) {
	    From = u;
	    To = v;
	    Weight = w;
	}

	/**
	 * Method to find the other end end of the arc given a vertex reference
	 * 
	 * @param u
	 *            : Vertex
	 * @return
	 */
	public Vertex otherEnd(Vertex u) {
	    // if the vertex u is the head of the arc, then return the tail else return the head
	    if (From == u) {
		return To;
	    } else {
		return From;
	    }
	}

	/**
	 * Method to represent the edge in the form (x,y) where x is the head of
	 * the arc and y is the tail of the arc
	 */
	public String toString() {
	    return "(" + From + "," + To + ")";
	}
    }

    /**
     * Class to represent a vertex of a graph
     * 
     *
     */
    public class Vertex implements Comparable<Vertex> {
	public int name; // name of the vertex
	public boolean seen; // flag to check if the vertex has already been visited
	public Vertex parent; // parent of the vertex
	public int weight; // field for storing int attribute of vertex
	public int indexPQ;  // index of node in priotity queue
	public LinkedList<Edge> Adj,revAdj; // adjacency list
	public int indegree,revIndegree;
	public int ec,lc,slack;
	public int criticalPathNo;

	/**
	 * Constructor for the vertex
	 * 
	 * @param n
	 *            : int - name of the vertex
	 */
	Vertex(int n) {
	    name = n;
	    seen = false;
	    parent = null;
	    weight = Negative_INFINITY;
	    // A list of outgoing edges from a vertex are maintained in Adj
	    Adj = new LinkedList<Edge>(); 
	    // A list of incoming edges from a vertex are maininted in revAdj
	    revAdj = new LinkedList<Edge>();
	    indegree = 0;
	    revIndegree = 0; 
	    //ec is the earliest completion time of a task
	    ec = Negative_INFINITY;
	    //lc is the latest completion time of a task
	    lc = INFINITY;
	    //criticalPathNo = 0;
	    
	    
	}

	public void putIndex(int index) {
	    indexPQ = index;
	}

	public int getIndex() {
	    return indexPQ;
	}

	/**
	 * Method used to order vertices, based on algorithm
	 */
	public int compareTo(Vertex v) {
	    return this.weight - v.weight;
	}

	/**
	 * Method to represent a vertex by its name
	 */
	public String toString() {
	    return Integer.toString(name);
	}
	/*
	 * method to return the weight of the vertex
	 */
	public String weight()
	{
		return Integer.toString(weight);
	}
    }


    /**
     * Method to add an arc to the graph
     * 
     * @param a
     *            : int - the head of the arc
     * @param b
     *            : int - the tail of the arc
     * @param weight
     *            : int - the weight of the arc
     */
    /*
     * here we are working on a directed graph so we add an edge from a to b to a's Adj(adjacency list)
     * and a reverse edge from b to a to b's revAdj(reverse adjacency list) which is defined in the vertex
     * class and corresponding indegrees are also updated .
     * 
     */
    void addEdge(int a, int b, int weight) {
	Edge e = new Edge(V[a], V[b], weight);
	V[a].Adj.add(e);
	V[b].revAdj.add(e); // since this is a DAG 
	// used while parsing the graph in reverse topological order
	V[a].revIndegree++;
	V[b].indegree++;
	}

    /**
     * Method to create an instance of VertexIterator
     */
    public Iterator<Vertex> iterator() {
	return new VertexIterator<Vertex>(V, N);
    }

    /**
     * A Custom Iterator Class for iterating through the vertices in a graph
     * 
     *
     * @param <Vertex>
     */
    private class VertexIterator<Vertex> implements Iterator<Vertex> {
	private int nodeIndex = 0;
	private Vertex[] iterV;// array of vertices to iterate through
	private int iterN; // size of the array

	/**
	 * Constructor for VertexIterator
	 * 
	 * @param v
	 *            : Array of vertices
	 * @param n
	 *            : int - Size of the graph
	 */
	private VertexIterator(Vertex[] v, int n) {
	    nodeIndex = 0;
	    iterV = v;
	    iterN = n;
	}

	/**
	 * Method to check if there is any vertex left in the iteration
	 * Overrides the default hasNext() method of Iterator Class
	 */
	public boolean hasNext() {
	    return nodeIndex != iterN;
	}

	/**
	 * Method to return the next Vertex object in the iteration
	 * Overrides the default next() method of Iterator Class
	 */
	public Vertex next() {
	    nodeIndex++;
	    return iterV[nodeIndex];
	}

	/**
	 * Throws an error if a vertex is attempted to be removed
	 */
	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    public static void main(String[] args) throws FileNotFoundException {
	}

    static Graph readGraph(Scanner in) {
	// read the graph related parameters
    String firstLine = in.nextLine();
    int noOfVertices,noOfEdges;
    if(firstLine.startsWith("#"))
    {
    	noOfVertices = in.nextInt(); // number of vertices in the graph
    	noOfEdges = in.nextInt(); // number of edges in the graph
    	
    }
    else
    {
    	String[] graphInput = firstLine.split("-");
    	noOfVertices = Integer.parseInt(graphInput[0]); 
    	noOfEdges = Integer.parseInt(graphInput[1]); 
    }
    Graph g = new Graph(noOfVertices);
    //maintaining two dummy nodes one at the start and one at the end
	g.V[0].weight = 0;
	g.V[noOfVertices+1].weight = 0;
	for( int j = 1 ; j < noOfVertices+1 ; j++)
	{
		g.V[j].weight = in.nextInt();
	}
	for (int i = 0; i < noOfEdges; i++) {
	    int u = in.nextInt();
	    int v = in.nextInt();
	    g.addEdge(u, v, 0);
	}
	/*
	 * add an edge from the dummy node named 0 to any edge with indegree 0
	 * add an edge from the dummy node named n+1 to any edge with revIndegree 0
	 * where n is the given size of the graph
	 */
	for(int i = 0; i < g.N ; i++)
	{
		if(g.V[i].indegree == 0 && g.V[i].name != 0 && g.V[i].name != g.N - 1 )
		{
			g.addEdge(0,i,0);
		}
		if(g.V[i].revIndegree == 0 && g.V[i].name != 0 && g.V[i].name != g.N - 1 )
		{
			g.addEdge(i, noOfVertices+1, 0);
		}
	}
	in.close();
	return g;
    }
    /**
     * Method to initialize a graph
     *  1) Sets the parent of every vertex as null
     *  2) Sets the seen attribute of every vertex as false 
     *  3) Sets the distance of every vertex as infinite
     * 
     * @param g
     *            : Graph - The reference to the graph
     */
    void initialize() {
	for (Vertex u : this) {
	    u.seen = false;
	    u.parent = null;
	    u.weight = INFINITY;
	}
    }

    /**
     * Method to print the graph
     * 
     * @param g
     *            : Graph - The reference to the graph
     */
    void printGraph() {
	for(int i = 0; i < this.N ;i++)	
	{
	    System.out.print(this.V[i] + ": ");
	    for(Edge e: this.V[i].Adj) {
		System.out.print(e);
	    }
	    System.out.println();
	}
    }
    
    /*
     * prints the weights of the vertices in the graph
     */
    void printGraphWeights() {
    	for(int i = 0; i < this.N ;i++){
    	    System.out.println(this.V[i] + ": " + this.V[i].weight);
    	    }
        }
    /*
     * computes the topological order of the vertices of 
     * the given graph and return the same. To find the topological order
     * used the indegree method in which we maintain a Queue of vertices with
     * indegree 0. Initially we enque all the vertices with indegree 0 into the Queue
     * then for each vertex in Queue will reduce the indegree of each adjacent vertex
     * and if its indegree becomes 0 we will add that vertex to the Queue. We do this till
     * the Queue becomes empty. We also maintain the count of number of times a node is 
     * processed . If the number of times a node is processed is greater than or equal to 
     * the no of vertices then the graph is not a DAG..
     * 
     */
    Vertex[] topologicalOrder()
    {
    	Vertex[] Top = new Vertex[this.N];
    	Queue<Vertex> Q = new LinkedList<Vertex>(); // empty queue of vertices
    	
    	//Add all the vertices with indegree 0 to the Queue
    	for(int i = 0; i < this.N ; i++)
    	{
    		if(this.V[i].indegree == 0)
    		{
    			Q.add(this.V[i]);
    		}
    	}
    	int count = 0 ; // no of processed nodes
    	Vertex currentVertex,otherEndOfCurrentVertex;
    	while(!Q.isEmpty())
    	{
    		currentVertex = Q.remove();
    		Top[count++] = currentVertex;
    		for(Edge e : currentVertex.Adj)
    		{
    			otherEndOfCurrentVertex = e.otherEnd(currentVertex);
    			otherEndOfCurrentVertex.indegree--;
    			if(otherEndOfCurrentVertex.indegree == 0)
    			{
    				Q.add(otherEndOfCurrentVertex);
    			}
    		}
    		
    	}
    	if(count != this.N)
    	{
    		System.out.println("Not a DAG"); // We can raise an exception here
    	}
    	return Top;
    }
}
