package Server;


import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;

/**
 * A thread class to handle the client requests from teacher and students
 */
public class Client extends Thread {

    private Socket socket;
    private DataInputStream dIn;
    private Integer flag;
    private File file;

    /**
     *
     * @param socket client socket connected to the server.
     * @param dIn input datastream for the connected client socket
     * @param flag to find the type of client
     * @param file database file
     */
    public Client(Socket socket, DataInputStream dIn, Integer flag, File file) {
        this.socket = socket;
        this.dIn = dIn;
        this.flag = flag;
        this.file = file;
        System.out.println("Client()");
    }

    /**
     * Default run method for thread class which calls method based on client type
     */
    @Override
    public void run() {
        try {
            boolean result = (this.flag == 1) ? runTeacher() : runStudent();
        } catch (Exception e) {
            System.out.println("Exception: Client.run() " + e.getMessage());
        }
    }

    /**
     *
     * @return boolean value true
     * @throws IOException
     */
    private boolean runTeacher() throws IOException {

        System.out.println("Teacher(): isTeacherActive, isStudentActive = " + Locks.isTeacherActive + ", " + Locks.isStudentActive);

        synchronized(Locks.lock) {
            while(Locks.isStudentActive == 1) {
                try{
                    System.out.println("Teacher.wait()");
                    Locks.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception: Teacher.run() " + e.getMessage());
                }
            }
        }

        Locks.isTeacherActive = 1;

        try {
            System.out.println("teacher.run()");

            // send files
            File[] files = new File(Config.imagesFolder).listFiles();

            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);

            System.out.println("Teacher: file.length = " + files.length);

            dos.writeInt(files.length);

            for(File file : files)	{
                dos.writeLong((Long) file.length());
                dos.writeUTF(file.getName());
                System.out.println("Teacher: file.name = " + file.getName());
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int theByte = 0;
                while((theByte = bis.read()) != -1)
                    bos.write(theByte);
                bis.close();
            }

            // send roll number and seat number
            List<String> lines = Files.readAllLines(Paths.get(Config.infoFilePath), Charset.forName("UTF-8"));
            dos.writeLong(lines.size());
            for(String line: lines) {
                String[] tokens = line.split("\\s+");
                if(tokens.length >= 2) {
                    dos.writeUTF(tokens[0]);
                    dos.writeUTF(tokens[1]);
                }
            }

            dos.close();

        } catch (Exception e) {
            System.out.println("Exception: Teacher.run() " + e.getMessage());
        }

        synchronized(Locks.lock) {
            Locks.isTeacherActive = 0;
            Locks.lock.notify();
        }

        socket.close();

        return true;
    }

    /**
     *
     * @return boolean value true
     * @throws IOException
     */
    private boolean runStudent()  throws IOException {

        System.out.println("Student(): isTeacherActive, isStudentActive = " + Locks.isTeacherActive + ", " + Locks.isStudentActive);

        synchronized(Locks.lock) {
            while(Locks.isTeacherActive == 1) {
                try{
                    Locks.lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception: Student.run() " + e.getMessage());
                }
            }
        }


        Locks.isStudentActive = 1;

        try {
            System.out.println("Student.run()");
            synchronized(file) {
                String data = dIn.readUTF();
                FileWriter fileWritter = new FileWriter(file.getName(), true);
                BufferedWriter bw = new BufferedWriter(fileWritter);
                bw.write(data);
                bw.close();
                System.out.println("Student: " + data);
            }
        } catch (IOException e) {
            System.out.println("Exception: Student.run() " + e.getMessage());
        }

        synchronized(Locks.lock) {
            Locks.isStudentActive = 0;
            Locks.lock.notify();
        }

        socket.close();

        return true;
    }

}