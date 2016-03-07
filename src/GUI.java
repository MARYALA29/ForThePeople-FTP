import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private static JComboBox<String> serverFileList;
	private static JButton download;
	private static JButton send; 

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
		JTextField ipAddress = new JTextField(30);
		JTextField port = new JTextField(4);

		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		pane.add(headerLabel);
		pane.add(ipAddressLabel);
		pane.add(ipAddress);
		pane.add(portLabel);
		pane.add(port);
		pane.add(connectButton);
		pane.add(Box.createHorizontalStrut(10));
		
		send = new JButton("1. Send File");
		pane.add(send);
		pane.add(Box.createHorizontalStrut(10));
		get = new JButton("2. GetFileList") ; 
		pane.add(get) ; 
		fileList = new JMenuBar() ;
		fileList.setLayout(new GridLayout(1,4));
		String[] items = {} ; 
		serverFileList = new JComboBox<>(items) ; 
		
		download = new JButton("Download") ; 
		fileList.add(serverFileList) ; 
		fileList.add(download) ; 
		pane.add(fileList) ; 


	}

}
