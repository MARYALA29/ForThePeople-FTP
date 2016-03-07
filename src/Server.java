import java.net.*;
import java.io.*;

public class Server extends GUI {
	private static ServerSocket soc;

	public static void biginServer() {
		try {
			soc = new ServerSocket(5217);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("FTP Server Started on Port Number 5217");
		while (true) {
			System.out.println("Waiting for Connection ...");
			try {
				new transferfile(soc.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}

class transferfile extends Thread {
	Socket ClientSoc;

	DataInputStream din;
	DataOutputStream dout;

	transferfile(Socket soc) {
		try {
			ClientSoc = soc;
			din = new DataInputStream(ClientSoc.getInputStream());
			dout = new DataOutputStream(ClientSoc.getOutputStream());
			System.out.println("FTP Client Connected ...");
			start();

		} catch (Exception ex) {
		}
	}

	void SendList() throws Exception {
		Process proc = Runtime.getRuntime().exec("/bin/bash -c ls\n");
		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = "";
		String result = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			result += line;
			result += "!";

		}
		result += "\n";
		// Multiplying the number by 2 and forming the return message
		String returnMessage = result;

		dout.writeUTF(returnMessage);

	}

	void SendFile() throws Exception {

		String filename = din.readUTF();
		File f = new File(filename);
		if (!f.exists()) {
			dout.writeUTF("File Not Found");
			return;
		} else {
			dout.writeUTF("READY");
			FileInputStream fin = new FileInputStream(f);
			int ch;
			do {
				ch = fin.read();
				dout.writeUTF(String.valueOf(ch));
			} while (ch != -1);
			fin.close();
			dout.writeUTF("File Receive Successfully");
		}
		System.out.println("Sent the File Succesfully");
	}

	void ReceiveFile() throws Exception {
		String filename = din.readUTF();
		if (filename.compareTo("File not found") == 0) {
			return;
		}
		File f = new File(filename);
		FileOutputStream fout = new FileOutputStream(f);
		int ch;
		String temp;
		do {
			temp = din.readUTF();
			ch = Integer.parseInt(temp);
			if (ch != -1) {
				fout.write(ch);
			}
		} while (ch != -1);
		fout.close();
		dout.writeUTF("File Send Successfully");
		System.out.println("Succeffully received");

	}

	public void run() {

		try {
			System.out.println("Waiting for Command ...");
			String Command = din.readUTF();
			if (Command.compareTo("GET") == 0) {
				System.out.println("\tGET Command Received ...");
				SendFile();
			}
			if (Command.compareTo("LIST") == 0) {
				System.out.println("\tLIST Command Received ...");
				SendList();
			} else if (Command.compareTo("SEND") == 0) {
				System.out.println("\tSEND Command Receiced ...");
				ReceiveFile();
			} else if (Command.compareTo("DISCONNECT") == 0) {
				System.out.println("\tDisconnect Command Received ...");
				System.exit(1);
			}
		} catch (Exception ex) {
		}
	}
}