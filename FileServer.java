import java.io.*;
import java.net.*;

public class FileServer {

  public static void main(String[] args) throws IOException {

        File f1 = new File("/abc.txt");

        ServerSocket servsock = new ServerSocket(8000);
        while (true) {
          System.out.println("Waiting...");

          Socket sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          DataInputStream dIn = new DataInputStream(sock.getInputStream());
          byte messageType = dIn.readByte();

          if( messageType==1){
            FileWriter fileWritter = new FileWriter(f1.getName(),true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            String data = dIn.readUTF();
            bw.write(data);
            bw.close();
            f1.close();
            System.out.println("Message A: " + data );
          }
          else
          {
            
            BufferedReader in = new BufferedReader(new FileReader("/home/rajan/Downloads/abc.txt"));
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

            String str;
            while ((str = in.readLine()) != null) {
              dOut.writeUTF(str);
              dOut.flush();
            }

            byte msg = dIn.readByte();
            while(msg)
            {
              String path = "/home/rajan/Downloads/" + dIn.readUTF() + ".jpg";
              int i;
              FileInputStream fis = new FileInputStream (path);
              while ((i = fis.read()) > -1)
              dOut.write(i);

              fis.close();
            }
            
            dOut.close();
          }

          sock.close();
        }
      }

    }
