package Server;


import java.io.*;
import java.net.*;


public class FileServer {

  private static void listenToStudent(File f1, DataInputStream dIn) throws IOException{
    String data = dIn.readUTF();
    FileWriter fileWritter = new FileWriter(f1.getName(), true);
    BufferedWriter bw = new BufferedWriter(fileWritter);
    bw.write(data);
    bw.close();
    System.out.println("Message A: " + data );
  }

  public static void main(String[] args) throws IOException { 

        File f1 = new File("/home/abhinavp/Desktop/Multithreaded-SittingPlan/incomingdata.txt");

        ServerSocket servsock = new ServerSocket(8000);
        while (true) {
          System.out.println("Waiting...");

          Socket sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          DataInputStream dIn = new DataInputStream(sock.getInputStream());
          byte messageType = dIn.readByte();

          if( messageType==1) 
            listenToStudent(f1, dIn);
          else {

            Teacher teacher = new Teacher(sock, dIn);
            teacher.start();

            // BufferedReader in = new BufferedReader(new FileReader("/home/abhinavp/Desktop/Multithreaded-SittingPlan/serverdata.txt"));
            // DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

            // String str;
            // while ((str = in.readLine()) != null) {
            //   dOut.writeUTF(str);
            //   dOut.flush();
            // }

            // byte msg = dIn.readByte();
            // while(msg) {
            //   String path = "/home/abhinavp/Desktop/Multithreaded-SittingPlan" + dIn.readUTF() + ".jpg";
            //   int i;
            //   FileInputStream fis = new FileInputStream (path);
            //   while ((i = fis.read()) > -1)
            //   dOut.write(i); 
            //   fis.close();
            // }

            // dOut.close();


          }

          sock.close();
        }
      }

    }
