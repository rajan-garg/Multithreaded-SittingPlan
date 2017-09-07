package Server;


import java.io.*;
import java.net.*;

public class Locks {

  public static Object lock = new Object();
  public static int context;

}