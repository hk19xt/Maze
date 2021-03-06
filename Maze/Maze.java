import java.util.*;
import java.io.*;

/* This class is main class. 
 * After compiling and running the program, follow the instructions by Interactions Pane. 
 1. Input a value for width.
 2. Input a value for height.
 3. Input a value for depth.
 4. Select your preference among five options: 1) Solve suboptimally, 2) Estimate optimal solution cost, 3) Solve optimally, 4) Enter new puzzle, or 5) Quit. 
 
  Name: Hyejin Kim
  Student #6823116
 
  Date: 7/13/2020  */


public class Maze {

    static int s; 
    static int total; // total = width x height x depth.
    static int start; // start point in the maze.
    static int end; // end point in the maze.
    
    static int width; 
    static int height;
    static int depth;

    static List<List<Node>> maze;
    static Scanner input;
    
    static List<Node> path; 
    static Stack<Node> path2; // To track the shortest path
    
    /* This method is for including an entirely blank line. 
      It allows for users to input blank lines or just a space. 
      @param Scanner in */
    private static String retrieveLine(Scanner in){
      
      String l = " ";
      do{
        s++;
        l = in.nextLine();
      }while(l.equals(" "));
      return l;
      
    }// retrieveLine
    
    
    public Maze() {
      
      input = new Scanner(System.in);
      boolean bool = true;
      
      while(bool){
        
        System.out.print("\nEnter width: ");
        width= input.nextInt();
        System.out.print("\nEnter height: ");
        height = input.nextInt();
        System.out.print("\nEnter depth: ");
        depth = input.nextInt();
        System.out.println("Enter maze below. Only rows of width " + width + " will be accepted. ");
        
        total = width*height*depth;
        
        maze = new ArrayList<>(total);
        
        for(int i=0;i<total;i++) {
          maze.add(new LinkedList<>());}
        
        int position = 0; 
        
        for(int i=0;i<height*depth;i++){
          
          boolean bool2 = true;
          while(bool2){ // Check the directions (U,D,N,E,S,W) and whether the path is opened or not.
            String line = retrieveLine(input);
            
            if (line.length() > width){
              System.out.println("Your input (length) is not correct. The width should be " + width + ".");}
            
            else if(!line.isEmpty()){
              bool2 = false;
              char[] word = line.toCharArray();
              
              for(int k=0;k<width;k++){ // This for loop performs each row until it hits the end of the width.
                
                if(word[k]=='E'){ // If a word in the row is "E", then it's assumed the end position.
                  end=position;
                  word[k]='O';} // In order for the path to connect to the end, it should be "O".
                
                if(word[k]=='S'){ // If a word in the row is "S", then it's assumed the start position.
                  start = position;}
                
                switch(word[k]){ // When the word is "X", "S" or "O", it follows each case.
                  case 'X': // When a word is "X" then it means a wall.
                    break;
                    
                  case ' ':
                    
                  case 'O': // When a word is "O" then it means a opened path.
                    // When a word is opened path and connected to up level.
                    if(position+(height*width)<maze.size() && position<=(height*width*(depth-1))){
                    Node x = new Node(position, 'U');
                    maze.get(position+(height*width)).add(x);}
                    // When a word is opened path and connected to down level.
                    if(position-(height*width) > 0 && position/(height*width)>=1) { 
                      Node x = new Node(position, 'D');
                      maze.get(position-(height*width)).add(x);}
                    // When a word is opened path and connected to north direction.
                    if (position+width<maze.size() && (i+1)%height!=0) {
                      Node x = new Node(position, 'N');
                      maze.get(position + width).add(x);}
                    // When a word is opened path and connected to south direction.
                    if (position-width>0 && i%height!=0) {
                      Node x = new Node(position, 'S');
                      maze.get(position - width).add(x);}
                    // When a word is opened path and connected to east direction.
                    if (k-1 >= 0 && word[k-1]!='X') {
                      Node x = new Node(position, 'E');
                      maze.get(position - 1).add(x);}
                    // When a word is opened path and connected to west direction.
                    if (k+1 < width && word[k+1]!='X') {
                      Node x = new Node(position, 'W');
                      maze.get(position + 1).add(x);}
                    break;
                                     
                  case 'S': // When a word is "S" then it means a starting point.
                    break;
                    
                  default: // When a user inserted invalid character.
                    System.out.println("Your input should be a character among 'E','X','O', or 'S'");
                    break;
                }
                position++;
              }
            }
            
          }
          
        }
        
        boolean bool3 = true;
        while (bool3) { // To perform the menu option by the user input value.
          System.out.println();
          options();
          System.out.print(":> ");
          int button = input.nextInt(); // Button by scanner
          
          switch(button){
            case 1: // Solve subobtimally. However, in this program, it is not implemented. Instead perform case 3.
              System.out.println("Not implemented. Use optimal instead. ");
              break;
              
            case 2: // Estimate optimal solution cost.
              solution();
              break;
              
            case 3: // Solve optimally.
              solution();
              break;
              
            case 4: // Enter new puzzle.
              bool3 = false;
              break;
              
            case 5: // Quit.
              bool3 = false;
              bool = false;
              break;
          }
        }
      }
      System.out.println("Goodbye!");
      input.close();
      
    }// Maze
    
    /* This method is menu options for users. */
    private void options() {
      
      System.out.println("1. Solve subobtimally");
      System.out.println("2. Estimate optimal solution cost");
      System.out.println("3. Solve optimally");
      System.out.println("4. Enter new puzzle");
      System.out.println("5. Quit");
      
    }// options
    
    /* This method is solution for finding optimal path. */
    private void solution(){
      
      System.out.println("Finding Solution...");
      
      path = new LinkedList<Node>(); // To connect the opened paths (characters which are "O" in the rows)
      boolean wasVisited[] = new boolean[total]; // To check already visited characters.

      Queue<Node> queue = new LinkedList<>(); // To add the chracters which are connected each other.
      
      Node initial = new Node(start, ' ');
      queue.add(initial);
      path.add(initial);
      wasVisited[initial.from] = true; // Check to true because it was visited. 
      boolean isPath = false; // Initialize to false until the path is found through while loop below.
      
      while (!queue.isEmpty()) { // To find whether there exits a path or not.
        
        Node top = queue.remove();
        if (top.from == end) { // When the top position in the queue met the end position. 
                               // (The end position was already numbered in the for loop in the Maze above.)
          isPath = true;} // This means the start and end position are connected.
        
        for (Node n : maze.get(top.from)) { // To add the nodes to the queue until the collection is null.
          if (!wasVisited[n.from]) {
            
            wasVisited[n.from] = true; // Check to true when the character is already checked.
            n.path = top; // To track the path
            path.add(n); // To add the node to the path linked list.
            queue.add(n); 
          }
          
        }
      }
      
      if (!isPath) { // When there does not exist any path.
        
        System.out.print("Exit not reachable.");
        System.out.println();} 
      
      else {
        int cost = pathCost(path); // To calculate the length of the shortest path.
        
        System.out.println("Optimal Path Cost: " + cost);
        System.out.println();
        System.out.println("Optimal Path: ");
        
        direction(); // To show the actual path all directions.
        System.out.println();
      }
      
    }// solution
    
    /* This method is direction, showing the character "N", "E", "W", "S", "U" or "D".*/
    public void direction() {
      // If there exist a path, then pop the character of the nodes until the path is null.
      while (!path2.empty()) {
        
        System.out.print(path2.pop().to + "  ");
        
      }
    }// direction
    
    /* This method is to calculate the shortest path. 
     * @param List<Node> path */
    public int pathCost(List<Node> path) {
      
      path2 = new Stack<>(); // To add the shortest path in the stack, adding nodes and removing the top nodes.
      int lastPoint = 0;
      for(int u=0; u<path.size();u++) {
        if (path.get(u).from == end) { // When the path met the end position.
          lastPoint = u;
        }
        
      }
      
      Node shortestPath = path.get(lastPoint); 
      
      path2.push(shortestPath);
      adding(shortestPath);
      
      int cost = path2.size();
      return cost-1; 
    }// pathCost
    
    /* This method is tracking the path list of the nodes, adding every node connected to the end.
     * @param Node node */
    public void adding(Node node) {
      
      if (node.path != null) { // Recursively add all nodes connected to a stack.
        
        path2.push(node.path);
        adding(node.path);
      }
    }// adding
    
   
    
    public static void main(String[] args) throws java.io.IOException  {
      Maze m = new Maze();
    }// main
}// Maze