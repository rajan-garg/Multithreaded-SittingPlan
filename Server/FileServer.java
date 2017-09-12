package Server;


import java.io.*;
import java.net.*;

/**
 * Main Server class which listens on a port for client requests
 */
public class FileServer {


    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        ServerSocket servsock = new ServerSocket(Config.listenPort);

        initLocks();

        File file = new File(Config.infoFilePath);

        while (true) {

            System.out.println("Waiting...");

            Socket sock = servsock.accept();

            System.out.println("Accepted connection : " + sock);

            DataInputStream dIn = new DataInputStream(sock.getInputStream());

            byte messageType = dIn.readByte();

            if( messageType==1) {
                Client teacher = new Client(sock, dIn, 0, file);
                teacher.start();
            }
            else {
                Client teacher = new Client(sock, dIn, 1, file);
                teacher.start();
            }

        }
    }

    /**
     *  Initialize lock flags
     */
    public static void initLocks() {
        Locks.lock = new Object();
        Locks.isTeacherActive = 0;
        Locks.isStudentActive = 0;
    }

}
