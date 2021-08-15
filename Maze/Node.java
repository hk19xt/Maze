import java.util.*;
import java.io.*;

/* This is node class. 
 
  Name: Hyejin Kim
  Student #6823116
 
  Date: 7/13/2020  */

public class Node{
  
  public int from;
  public char to;
  public Node path; // To keep track path for each linked list 
  
  public Node(int f, char t){
    from = f;
    to = t;
  }
}// Node