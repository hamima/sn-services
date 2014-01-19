package ir.mod.tavana.toranj.services.communication;

import java.util.Scanner;

public class PrintShortestPath {

	private int lastCost=0;
	private int[] path= new int[20];
        private int from_;
        private int to_;

    
	
	public PrintShortestPath(){
		return;
	}

    public int[] getPath() {
        return path;
    }
	
	/*
	 * PrintShortestPath 
	 * It uses DistanceVector to output
	 */
	public PrintShortestPath(int[][] matrix, DistanceVector dv) {
		
		int Max = matrix.length; // Get size of the entrance matrix
		
		Node[][] node = dv.getNode();
		
		dv.readMatrix(matrix);
		dv.runAlgorithm(node, matrix);
		
		// PRINTING
		System.out.println("----SHORTEST PATH FROM ? to ? ----");
		Scanner sc = new Scanner(System.in);
		System.out.println("the router source :");
		int from = sc.nextInt();
		Scanner sc2 = new Scanner(System.in);
		System.out.println("the destination router :");
		int to = sc2.nextInt();
		System.out.println("The shortest path from " + from + " to " + to + " is:" );
		
		// EXECUTE THE ALGORITHM
		FindShortestPath2(from, to, node, matrix,0);
		
	}

    public PrintShortestPath(int[][] matrix, DistanceVector dv,int from_, int to_) {
       
        int Max = matrix.length; // Get size of the entrance matrix

		Node[][] node = dv.getNode();

		dv.readMatrix(matrix);
		dv.runAlgorithm(node, matrix);

        FindShortestPath2(from_, to_, node, matrix,0);
    }





	/*
	 * Algorithm to Find the Shortest Path
	 */
	public void FindShortestPath(int from, int to, Node[][] node, int[][] matrix){
		

		int theRow = from-1;
		int theCol = to-1;
		int temp;

		if (node[theRow][theCol].getIndex() == theCol ){
			System.out.println(theRow+1 + " - " + (theCol+1) );
			temp = lastCost + node[theRow][theCol].getCost();
			lastCost = temp;

			System.out.println("The total cost is: " + lastCost );

		}

		else{
			System.out.print((theRow+1) + " - " + (node[theRow][theCol].getIndex()+1) + " - ");
			temp = lastCost + node[theRow][theCol].getCost();
			lastCost = temp;
			int newRow = node[theRow][to-1].getIndex()+1;

			FindShortestPath(newRow, to, node, matrix);

		}

        }

               public void FindShortestPath2(int from, int to, Node[][] node, int[][] matrix, int pathIndex){
		
                
		int theRow = from-1;
		int theCol = to-1;
		int temp;

		if (node[theRow][theCol].getIndex() == theCol ){
                     if (pathIndex==0){
                      path[pathIndex]=theRow+1;
                      pathIndex++;
                    }
                    
			System.out.println(theRow+1 + " - " + (theCol+1) );
			temp = lastCost + node[theRow][theCol].getCost();
			lastCost = temp;
                        path[pathIndex]=theCol+1;


		}

		else{
                    if (pathIndex==0){
                      path[pathIndex]=theRow+1;
                      pathIndex++;
                    }
                      path[pathIndex]=node[theRow][theCol].getIndex()+1;
                      pathIndex++;
			System.out.print((theRow+1) + " - " + (node[theRow][theCol].getIndex()+1) + " - ");
			temp = lastCost + node[theRow][theCol].getCost();
			lastCost = temp;
			int newRow = node[theRow][to-1].getIndex()+1;
                        
                        
			FindShortestPath2(newRow, to, node, matrix, pathIndex);

		}
               }



	
}
