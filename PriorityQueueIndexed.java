package indexedPriorityQueue;

/*
 * Author: Viswanadha Pratap Kondoju 
 * 
 */
import indexedPriorityQueue.Graph.Vertex;
import java.lang.Comparable;
import java.util.Random;

public class PriorityQueueIndexed<T extends Comparable<? super T> & PQIndex> {
 T[] queue;
 int size = 0; // size of the queue
 
 /** Build a priority queue with a given array q */
 PriorityQueueIndexed(T[] q) {
	 queue = (T[]) new Comparable[q.length + 2];
	 buildHeap(q);
	 
 }

 /** Create an empty priority queue of given maximum size */
 PriorityQueueIndexed(int n) {
 }

 /*
  * insert a element x in to the priority queue
  * by calling add()
  */
 void insert(T x) {
	 
		add(x);
	 }
 
 /*
  * isEmpty() checks whether the priority queue is empty 
  * or not
  */
 boolean isEmpty()
 {
	 if(size == 0)
		 return true;
	 return false;
 }
 /*
  * add() will add the element x to the priority
  * queue 
  */
 void add(T x) {
	 
	 int hole = size+1;
	 while( (hole/2)!=0 && queue[hole/2].compareTo(x) > 0 )
	 {
		 queue[hole] = queue[hole/2];
		 queue[hole].putIndex(hole);
		 hole = hole/2;		 
	 }
	 size++;
	 queue[hole] = x;	
	 queue[hole].putIndex(hole);
		 
 }
/*
 * remove() will return the minimum element 
 * in the priority queue by calling deleteMin()
 */
 T remove() {
	 
    return deleteMin();
 }
 
/*
 * deleteMin() will return the minimum element in the 
 * priority queue and deletes it from the priority queue
 */
 T deleteMin() {
	T rv;
	rv = queue[1];	 	 
	queue[1] = queue[size] ;
	queue[1].putIndex(1);
	size--;
	percolateDown(1);
	return rv;
 }

 /** restore heap order property after the priority of x has decreased */
void decreaseKey(T x) {
	percolateUp(x.getIndex());
 }
 /*
  * min() will return the minimum element in the 
  * priority queue without altering the priority queue
  */
 T min() { 
	 
	return queue[1];
 }

 /** Priority of element at index i of queue has decreased.  It may violate heap order.
  *  Restore heap property */
 void percolateUp(int i) {
	 
	 T x = queue[i];
	 int hole = i;
	 while( (hole/2)!=0 && queue[hole/2].compareTo(x) > 0 )
	 {
		 queue[hole] = queue[hole/2];
		 queue[hole].putIndex(hole);
		 hole = hole/2;		 
	 }
	 queue[hole] = x;
	 queue[hole].putIndex(hole);
	 
 }

 /** Create heap order for sub-heap rooted at node i.  Precondition: Heap order may be violated 
  *  at node i, but its children are roots of heaps that are fine.  Restore heap property */
 void percolateDown(int i) {
	 T temp;
	 int left = 2 * i;
	 int right  = (2 * i) + 1;
	 int child = -1;
	 /*
	  * if there are no children to the node at index i
	  * don't do anything just return
	  */
	 if(left > size)
	 {
		 return; 
	 }
	 /*
	  * If the node at index i has only left child
	  * exchange them
	  */
	 else if ( left == size && queue[left].compareTo(queue[i])< 0)    
	 {
		 temp = queue[left];
		 queue[left] = queue[i];
		 queue[i] = temp ;
		 queue[i].putIndex(i);
		 queue[left].putIndex(left);;
	 }
	 /*
	  * if the node at index i has both the children
	  * exchange the node at index i with the smaller 
	  * child
	  */
	 else
	 {                             
		 
		 if(queue[left].compareTo(queue[right]) < 0)
		 {
			 child = left;
		 }
		 else
		 {
			 child = right;
		 }
		 if(queue[i].compareTo(queue[child]) > 0)
		 {
			 temp = queue[child];
			 queue[child] = queue[i];
			 queue[i] = temp;
			 //child = left;
			 queue[i].putIndex(i);
			 queue[child].putIndex(child);
			 percolateDown(child);
			
			 
		 }
		 
	 }
 }

 /*
  * buildHeap() creates a heap.  
  * Array may violate heap order in many places. 
  */
 void buildHeap(T[] arr) {
	 
	 int i = 1;
	 	 
	 while(i<arr.length)
	 {
		 insert(arr[i]);
		 i++;
	 }
	 
 }
 
 /*
  * printPQList() will print the priority queue
  * with its index and weight 
  */
 void printPQList()
 {
	 int i = 1;
	 while(i <= size )
	 {
		System.out.print(((Vertex) queue[i]).getIndex() + "--");
		System.out.print(((Vertex) queue[i]).weight + " ");
		i++;
	 }
	 System.out.println();
 }
 
 
 public static void main(String[] args)
 {
		
 }
}
