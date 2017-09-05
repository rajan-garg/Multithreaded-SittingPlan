import java.io.*;
import java.net.*;

class MyRunnable1 implements Runnable {

      Socket sock;
      DataInputStream dIn;

      public MyRunnable1(Socket sock, DataInputStream dIn) {
      this.sock = sock;
      this.dIn = dIn;
      }

      public void run() {

        synchronized(FileSever.lock){
            FileSever.flag = 0;
        }


        try{
          File f1 = new File("/abc.txt");
          FileWriter fileWritter = new FileWriter(f1.getName(),true);
          BufferedWriter bw = new BufferedWriter(fileWritter);
          String data = dIn.readUTF();
          bw.write(data);
          bw.close();
          System.out.println("Message A: " + data );
        }
        catch(IOException ex){
          System.out.println (ex.toString());
        }

        synchronized(FileSever.lock){
          FileSever.flag = 1;
          FileSever.lock.notify();
        }
      }
    }


    class MyRunnable2 implements Runnable {

      Socket sock;
      DataInputStream dIn;

      public MyRunnable2(Socket sock, DataInputStream dIn) {
      this.sock = sock;
      this.dIn = dIn;
      }
      
      public void run() {

        synchronized(FileServer.lock){
            while(FileSever.flag==0)
              FileSever.lock.wait();
        }

        try{
          BufferedReader in = new BufferedReader(new FileReader("/home/rajan/Downloads/abc.txt"));
          DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

          String str;
          while ((str = in.readLine()) != null) {
            dOut.writeUTF(str);
            dOut.flush();
          }
          dOut.writeUTF("");
          dOut.flush();

          byte msg = dIn.readByte();
          while(msg==1)
          {
            String path = "/home/rajan/Downloads/" + dIn.readUTF() + ".jpg";
            int i;
            FileInputStream fis = new FileInputStream (path);
            while ((i = fis.read()) > -1){
              dOut.write(i);
              dOut.flush();
            }
            dOut.flush();
            fis.close();
            msg = dIn.readByte();
          }
          dOut.close();

        }
        catch(IOException ex){
          System.out.println(ex.toString());
        }

      }
    }

public class FileServer {

  public static final Object lock = new Object();
  public static final int flag=1;

  public static void main(String[] args) throws IOException {

    ServerSocket servsock = new ServerSocket(8000);

    while (true) {
      System.out.println("Waiting...");
      Socket sock = servsock.accept();
      System.out.println("Accepted connection : " + sock);
      DataInputStream dIn = new DataInputStream(sock.getInputStream());
      byte messageType = dIn.readByte();

      if( messageType==1){
        MyRunnable1 mrt1 = new MyRunnable1(sock,dIn);
        Thread t1 = new Thread(mrt1);
        t1.start();
      }
      else{
        MyRunnable2 mrt2 = new MyRunnable2(sock,dIn);
        Thread t2 = new Thread(mrt2);
        t2.start();

      }
    }

  }

}
