import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GUI {
	public static JFrame frame;
	public static Container pane;
	public static JMenuBar menuBar;
	public static JLabel headerLabel;
	public static JButton get ;
	public static JMenuBar fileList ;
	public static JComboBox<String> serverFileList;
	public static JButton download;
	public static JButton send;
	public static Container myfileList;
	public static JComboBox<String> myServerFileList;
	public static  JTextField ipAddress;
	public static  JTextField port;
	public static JButton sendFile; 

	public static void main(String[] args) {
		showGUI();

		frame.setVisible(true);
		Server server = new Server();

	}

	private static void showGUI() {
		// TODO Auto-generated method stub
		frame = new JFrame("ForThePeople-FTP");
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = new Container();
		pane = frame.getContentPane();
		pane.setLayout(new GridLayout(20, 1));

		menuBar = new JMenuBar();
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel conneectTo = new JLabel("Connect to:   ");
		menuBar.add(conneectTo);
		pane.add(menuBar);

		headerLabel = new JLabel("", JLabel.CENTER);

		JLabel ipAddressLabel = new JLabel("ipAddress: ");
		JLabel portLabel = new JLabel("Port: ");
		ipAddress = new JTextField(30);
		port = new JTextField(4);

		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client client = new Client() ; 
				try {
					client.startClient("connect");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		pane.add(headerLabel);
		pane.add(ipAddressLabel);
		pane.add(ipAddress);
		pane.add(portLabel);
		pane.add(port);
		pane.add(connectButton);
		pane.add(Box.createHorizontalStrut(10));
		
		
		sendFile = new JButton("1. Send File");
		pane.add(sendFile);
		myfileList = new JMenuBar() ;
		myfileList.setLayout(new GridLayout(1,4));
		myServerFileList = new JComboBox<>() ;
		sendFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		    	Process proc;
				try {
					proc = Runtime.getRuntime().exec("/bin/bash -c ls\n");
			        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream())) ; 
			        String line = "" ; int count = 0 ; 
			        while ((line = reader.readLine()) != null){
			        	myServerFileList.addItem(line);
			        	System.out.println(count+". "+line);
			        	count++;
			        }
			        int i;
			        for(i=0;i<count;i++){
			        }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

			}		
		});

		send = new JButton("Send");
		myfileList.add(myServerFileList) ; 
		myfileList.add(send) ; 
		pane.add(myfileList) ;
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client client = new Client() ; 
				try {
					client.startClient("send");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		pane.add(Box.createHorizontalStrut(10));

		
		
		
		get = new JButton("2. GetFileList") ; 
		pane.add(get) ; 
		fileList = new JMenuBar() ;
		fileList.setLayout(new GridLayout(1,4));
		serverFileList = new JComboBox<>() ; 
		get.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Client client = new Client() ; 
				try {
					client.startClient("list");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
		});
		
		download = new JButton("Download") ; 
		fileList.add(serverFileList) ; 
		fileList.add(download) ; 
		pane.add(fileList) ; 
		download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Client client = new Client() ; 
				try {
					client.startClient("get");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


	}

}
