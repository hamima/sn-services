package ir.mod.tavana.toranj.services.communication;

import java.util.Scanner;


public class DistanceVector
{
	int Max; // Size of the matrix
	final static int INFINITE = 1000;
	private Node[][] node;

    public Node[][] getNode() {
        return node;
    }

	
	public static int row(int[][] matrix) 
		{ return matrix.length;
		}
	public static int col(int[][] matrix) 
		{ return matrix.length;
		}



        public void initializeNode(){
            node=new Node[Max][Max];
            for( int i=0; i<Max; i++){
			for( int j=0; j<Max; j++){
				node[i][j] = new Node(0, 0);
			}
		}

        }
	/*
	 * General running steps
	 */
	public DistanceVector(int[][] matrix){
		Max = matrix.length; // Get size of the entrance matrix
		
		initializeNode();
		
		readMatrix(matrix);
		System.out.println("Input table : ");
		PrintMatrix(matrix); //------------print the input table (To Do : synchronize with IHM)
		runAlgorithm(node, matrix);
		System.out.println("Final table : ");
		PrintMatrix(matrix); //print the final table 
		
		
		//System.out.println("----SHORTEST PATH FROM ? to ? ----");
		//Scanner sc = new Scanner(System.in);
		//System.out.println("the router source :");
		//int from = sc.nextInt();
		//Scanner sc2 = new Scanner(System.in);
		//System.out.println("the destination router :");
		//int to = sc2.nextInt();
		//System.out.println("The shortest path from " + from + " to " + to + " is:" );
		
		//PrintShortestPath(from, to, node, matrix); // --------  PrintShortestPath ---------
		
		return;		
	}
	
	/*
	 * Reads the table and assigns an index and a cost to each node. 
	 * - cost are taken through the entrance table
	 * it looks after the '-1'
	 * - index are ranged over columns 
	 * 0 1 2 ...
	 * 0 1 2 ...
 	 * 0 1 2 ...
	 */
	public void readMatrix(int[][] matrix){
				
		for (int r = 0; r<row(matrix); r++)
		{
			for (int c = 0; c<col(matrix); c++)
			{
				if (matrix[r][c]>0)
				{
					node[r][c].setIndex(c);
					node[r][c].setCost((int)matrix[r][c]);
				}
				else
				{
					if (matrix[r][c]==0)
					{
						node[r][c].setIndex(r);
						node[r][c].setCost(0);
					}
					else
					{
						node[r][c].setIndex(INFINITE);
						node[r][c].setCost(INFINITE);
					}
				}
			}
		}
		
	}

	/* 
	 * Print the final (and intermediate) Matrix
	 */
	public void PrintMatrix(int[][] matrix){
		
		for (int i = 0; i<row(matrix); i++)
		{
			System.out.printf("\t%c", i+'1');
		}
		System.out.print("\n----------------------------------------------------------------------\n");
    
		for (int r = 0; r<row(matrix); r++)
		{
			System.out.printf("%c\t", r+'1');
			for (int c = 0; c<col(matrix); c++)
			{
				if(node[r][c].getIndex()!= INFINITE )
				{
					System.out.printf("(%c,%d)\t", node[r][c].getIndex()+'1', node[r][c].getCost());
				}
				else
				{
					System.out.print("(-,-)\t");
				}
			}
			System.out.print("\n");
		}		
	}
public int[][] matrixForIhm(int[][] matrix, Node[][] node){
    int[][] result = matrix;
		for (int r = 0; r<row(matrix); r++)
		{

			for (int c = 0; c<col(matrix); c++)
			{
				if(node[r][c].getIndex()!= INFINITE )
				{
					result[r][c]=node[r][c].getCost();
				}
				else
				{
					result[r][c]=-1;
				}
                        }
		}
    return result;
}
	
	/*
	 * repeat "Algorithm" over rows and matrix columns
	 */
	public void runAlgorithm(Node [][] node, int [][] matrix){
		
		boolean findNew;
		do
		{
			findNew = false;
			for (int r = 0; r<row(matrix); r++)
			{
				for (int c = 0; c<col(matrix); c++)
				{
					if (Algorithm(matrix, r, c, node))
					{
						//System.out.printf("New elements : matrix[%d][%d]\n", r, c); //------ IF YOU WANT TO SEE THE CHANGING ELEMENTS IN THE TABLE
						findNew = true;
					}
				}
				//PrintMatrix(matrix, node); // ---------------------------------- IF YOU WANT TO SEE THE RUNNING STEPS OF THE ALGORITHM
				//System.out.println("boolean findNew "+findNew);
			}
		}
		while (findNew);
			
		}
		
	
	
	/*
	 * This is the implementation of the Distance Vector Algorithm 
	 * - it calculates a new cost = node[r][i].getCost()+ node[theRow][c].getCost()
	 * - find the minimum 
	 * - update the matrix
	 * It returns true if we find a new minimal cost
	 * Then "Algorithm" runs through "doAlgorithm"
	 */
	public boolean Algorithm(int[][] matrix, int r, int c, Node[][] node){
		{
			boolean findNew = false;
			int oldCost = node[r][c].getCost();
			int newCost; //, theCol;
			int theRow;
			for (int i = 0; i<col(matrix); i++)
			{
				//have a path
				newCost = node[r][i].getCost();
				
				theRow = r;
				while ((theRow = node[theRow][i].getIndex())!= INFINITE)
				{
					//newCost += nodeMatrix[theRow][i].getCost();
					if (i == theRow)
					{
						newCost = node[r][i].getCost()+node[theRow][c].getCost();
						if (newCost<oldCost)
						{
							node[r][c].setCost(newCost);
							oldCost = newCost;
							node[r][c].setIndex(node[r][i].getIndex());
							Update(r, c, i, node);
							findNew = true;
						}
						break;
					}
				}
			}
			return findNew;
		}
		
	}

	/*
	 * UPDATE the elements of Matrix with newCost found through "Algorithm"
	 */
	public void Update(int r, int c, int first, Node[][] node){
		
		int newCost;
		int theRow;
		newCost = node[r][c].getCost();
		theRow = r;
		while ((theRow = node[theRow][first].getIndex())!= first)
		{
			if (newCost-node[r][theRow].getCost()>0)
			{
				if (node[theRow][c].getCost() > newCost-node[r][theRow].getCost())
				{
					node[theRow][c].setIndex(node[theRow][first].getIndex());
					node[theRow][c].setCost(newCost-node[r][theRow].getCost());
				}
			}
		}	
	}
	
	
	
	/*
	 * PRINT the Shortest Path from a source to a destination router
	 */
	
	public void PrintShortestPath(int from, int to, Node[][] node, int[][] matrix ){
		
		for (int i=0; i<matrix.length;i++){
			for (int j=0; j<matrix.length; j++){
				node[i][j].setCost(matrix[i][j]);
			}
		}
		
		int theRow = from-1;
		int theCol = to-1;
		int lastCost = 0;
		int temp;
		
		if (node[theRow][to].getIndex() == theCol ){
			System.out.println(theRow+1 + " - " + (theCol+1) );
			temp = lastCost + node[theRow][theCol].getCost();
			lastCost = temp;
			
		}
		
		else{
			System.out.print((theRow+1) + " - " + (node[theRow][theCol].getIndex()+1) + " - ");
			int newRow = node[theRow][to-1].getIndex()+1;
			
			PrintShortestPath(newRow, to, node, matrix);
			
		}
		
		System.out.println("The total cost is: " + lastCost );
		
		
	}
	
	
	
/*
 * ------- MAIN	----------
 */
public static void main(String[] args) {
	
	// LOAD THE TESTFILE
	// MATRIX SIZE ALREADY IMPLEMENTED ? 
		
	//TEST MATRIX
	/*
	int [][] matrix = {
	{0, 2, 5, 1, -1},
	{2, 0, 8, 7, 9},
	{5, 8, 0, -1, 4},
	{1, 7, -1, 0, 2},
	{-1, 9, 4, 2, 0} 
	};
	*/
	
	/*
	int [][] matrix = {
	{0,2,7},
	{2,0,1},
	{7,1,0}
	};
	*/
	
	//Other test Matrix
	int [][] matrix = {      
			{0, 2, 5, 1, -1, -1},
			{2, 0, 3, 2, -1, -1},
			{5, 3, 0, 3, 1, 5},
			{1, 2, 3, 0, 1, -1},
			{-1, -1, 1, 1, 0, 2},
			{-1, -1, 5, -1, 2, 0},
			};
	
		
	//int N = matrix.length;
	
	new DistanceVector(matrix);
	
	}
}