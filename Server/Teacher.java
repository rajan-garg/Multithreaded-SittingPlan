package Server;


import java.io.*;
import java.net.*;


public class Teacher extends Thread {

	private Socket socket;
	private DataInputStream dIn;
	private Integer flag;
	private File file;

	public Teacher(Socket socket, DataInputStream dIn, Integer flag, File file) {
		this.socket = socket;
		this.dIn = dIn;
		this.flag = flag;
		this.file = file;
		System.out.println("Client()");
	}

	@Override
	public void run() {
		try {
			if(flag == 1) {
				System.out.println("in teacher" + Locks.context);

				synchronized(Locks.lock) {
					while(Locks.context == 1) {
						try{
							Locks.lock.wait();
						} catch (InterruptedException e) {
							System.out.println("Exception: teacher.run() " + e.getMessage());
						}
					}
				}			

				try {
					System.out.println("teacher.run()");
					
					BufferedReader in = new BufferedReader(new FileReader("/home/abhinavp/Desktop/Multithreaded-SittingPlan/incomingdata.txt"));
					DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

					String str;
					while ((str = in.readLine()) != null) {
						dOut.writeUTF(str);
						dOut.flush();
					}
					dOut.writeUTF("");
					dOut.flush();

					byte msg = dIn.readByte();

					while(msg != 0) {
						String path = "/home/abhinavp/Desktop/Multithreaded-SittingPlan/" + dIn.readUTF() + ".jpg";
						int i;
						FileInputStream fis = new FileInputStream (path);
						while ((i = fis.read()) > -1) {
							dOut.write(i); 
							dOut.flush();
						}
						fis.close();
						msg = dIn.readByte();
					}

					dOut.close();
				} catch (Exception e) {
					System.out.println("Exception: teacher.run() " + e.getMessage());
				}

				Locks.context = 1;
				synchronized(Locks.lock) {
					Locks.lock.notify();
				}

			} else {

				System.out.println("in student" + Locks.context);

				synchronized(Locks.lock) {
					while(Locks.context == 0) {
						try{
							Locks.lock.wait();
						} catch (InterruptedException e) {
							System.out.println("Exception: teacher.run() " + e.getMessage());
						}
					}
				}

				try {
					System.out.println("student.run()");
					synchronized(file) {
						String data = dIn.readUTF();
						FileWriter fileWritter = new FileWriter(file.getName(), true);
						BufferedWriter bw = new BufferedWriter(fileWritter);
						bw.write(data);
						bw.close();
						System.out.println("Message A: " + data + " FileName " + file.getName());
					}
					// PrintWriter writer = new PrintWriter("/home/abhinavp/Desktop/Multithreaded-SittingPlan/incomingdata.txt", "UTF-8");
				 //    writer.println(data);
				 //    writer.close();
				} catch (IOException e) {
					System.out.println("Exception: teacher.run() " + e.getMessage());
				}
				Locks.context = 0;
				synchronized(Locks.lock) {
					Locks.lock.notify();
				}
				//System.out.println("end student" + Locks.context);
			}
			socket.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}

}