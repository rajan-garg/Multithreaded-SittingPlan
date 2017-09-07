package Server;


import java.io.*;
import java.net.*;


public class FileServer {

  public static void main(String[] args) throws IOException { 

        ServerSocket servsock = new ServerSocket(8000);

        Locks.context = 1;

        File f1 = new File("/home/abhinavp/Desktop/Multithreaded-SittingPlan/incomingdata.txt");
        
        while (true) {
          System.out.println("Waiting...");

          Socket sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          DataInputStream dIn = new DataInputStream(sock.getInputStream());
          byte messageType = dIn.readByte();


          if( messageType==1) {
            Teacher teacher = new Teacher(sock, dIn, 0, f1);
            teacher.start();
          }
          else {
            Teacher teacher = new Teacher(sock, dIn, 1, f1);
            teacher.start();
          }

        }
      }

    }
