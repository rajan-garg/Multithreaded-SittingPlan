package Server;


import java.io.*;
import java.net.*;


public class Teacher extends Thread {

	private Socket socket;
	private DataInputStream dIn;

	public Teacher(Socket socket, DataInputStream dIn) {
		this.socket = socket;
		this.dIn = dIn;
        System.out.println("Teacher()");
	}

    @Override
	public void run() {

		System.out.println("teacher.run()");
		
		BufferedReader in = new BufferedReader(new FileReader("/home/abhinavp/Desktop/Multithreaded-SittingPlan/serverdata.txt"));
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

		String str;
		while ((str = in.readLine()) != null) {
		  dOut.writeUTF(str);
		  dOut.flush();
		}

		byte msg = dIn.readByte();
		while(msg != 0) {
		  String path = "/home/abhinavp/Desktop/Multithreaded-SittingPlan/" + dIn.readUTF() + ".jpg";
		  int i;
		  FileInputStream fis = new FileInputStream (path);
		  while ((i = fis.read()) > -1)
		  dOut.write(i); 
		  fis.close();
		}

		dOut.close();


	}

}