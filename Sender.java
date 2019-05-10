
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
 * 
 * @author Pranav Patel and Duncan Wilson
 * @ID 150380910 and 150322290
 * @Purpose Sender class for Assignment 2
 *
 */
public class Sender {

	// GUI declarations
	private JFrame frame;
	private JTextField receiver_ip_input;
	private JTextField receiver_port_input;
	private JTextField sender_port_input;
	private JTextField file_name_input;
	private JTextField mds_input;
	private static JTextArea status_label;

	// UDP declaration
	private static int byte_array; // will be buffer size of messages
	private static long total_datagrams; // will be the total datagrams number for sequence
	private static long file_size; // will be file size in bytes
	private static boolean found_error; // if something went wrong
	// connection variables
	private static InetAddress receiver_ip;
	private static int receiver_port;
	private static int sender_port;
	private static int mds;
	private static String file_name;
	private static String file_path;
	private static int timeout_ms;
	private static int max_line = 2048; // byte length of the max line to send
	private static long startTime;
	private static long elapsedTime;
	private JTextField timeout_input;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sender window = new Sender();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Sender() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.ORANGE);
		frame.setBounds(100, 100, 550, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Receiver IP address");
		lblNewLabel.setBounds(10, 11, 136, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Receiver UDP port");
		lblNewLabel_1.setBounds(10, 36, 136, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Sender UDP port");
		lblNewLabel_2.setBounds(10, 61, 136, 14);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Name of (TEXT) file");
		lblNewLabel_3.setBounds(10, 111, 136, 14);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Max datagram size (bytes)");
		lblNewLabel_4.setBounds(10, 87, 159, 14);
		frame.getContentPane().add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Timeout (microseconds)");
		lblNewLabel_5.setBounds(10, 136, 141, 14);
		frame.getContentPane().add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Total transmission time (milliseconds)");
		lblNewLabel_6.setBounds(10, 161, 225, 14);
		frame.getContentPane().add(lblNewLabel_6);

		JLabel transmission_label = new JLabel("0");
		transmission_label.setBackground(Color.CYAN);
		transmission_label.setBounds(264, 161, 46, 14);
		frame.getContentPane().add(transmission_label);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 186, 136, 14);
		frame.getContentPane().add(lblStatus);

		receiver_ip_input = new JTextField();
		receiver_ip_input.setForeground(Color.BLACK);
		receiver_ip_input.setBounds(161, 5, 363, 20);
		frame.getContentPane().add(receiver_ip_input);
		receiver_ip_input.setColumns(10);

		receiver_port_input = new JTextField();
		receiver_port_input.setForeground(Color.BLACK);
		receiver_port_input.setBounds(161, 30, 363, 20);
		frame.getContentPane().add(receiver_port_input);
		receiver_port_input.setColumns(10);

		sender_port_input = new JTextField();
		sender_port_input.setForeground(Color.BLACK);
		sender_port_input.setBounds(161, 55, 363, 20);
		frame.getContentPane().add(sender_port_input);
		sender_port_input.setColumns(10);

		file_name_input = new JTextField();
		file_name_input.setText("Select file with button");
		file_name_input.setEditable(false);
		file_name_input.setForeground(Color.BLACK);
		file_name_input.setBounds(161, 105, 363, 20);
		frame.getContentPane().add(file_name_input);
		file_name_input.setColumns(10);

		mds_input = new JTextField();
		mds_input.setForeground(Color.BLACK);
		mds_input.setBounds(161, 81, 363, 20);
		frame.getContentPane().add(mds_input);
		mds_input.setColumns(10);
		
		status_label = new JTextArea();
		status_label.setBackground(Color.WHITE);
		status_label.setEditable(false);
		status_label.setBounds(10, 208, 514, 42);
		frame.getContentPane().add(status_label);

		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (receiver_ip_input.getText().equals("") || receiver_port_input.getText().equals("")
							|| sender_port_input.getText().equals("") || mds_input.getText().equals("")) {
						status_label.setText("Please enter correct inputs");
					}
					else {	
						boolean no_error = false;
						status_label.setText("Transfer in progress");
						// writing inputs into global variables
						receiver_ip = InetAddress.getByName(receiver_ip_input.getText());
						receiver_port = Integer.parseInt(receiver_port_input.getText());
						sender_port = Integer.parseInt(sender_port_input.getText());
						// file_name = file_name_input.getText();
						mds = Integer.parseInt(mds_input.getText());
						timeout_ms = Integer.parseInt(timeout_input.getText())/1000; //converted to ms
						if (receiver_port > 0 && sender_port >0 && mds > 0 && timeout_ms > 0) { // basically no input error so far
							if (file_name_input.getText().contains("Select file with button") || file_name_input.getText().contains("null")) {
								no_error = false;
							}
							else {
								no_error = true;
							}
						}
						else {
							status_label.setText("Please check number inputs and try again");
						}
						if (no_error) {
							startTime = System.nanoTime(); //starting timer
							transfer();
							status_label.setText(status_label.getText() + "\nTransfer completed successfully");
							elapsedTime = (System.nanoTime() - startTime)/1000000; // in milliseconds
							transmission_label.setText(""+elapsedTime);
						}
						else {
							status_label.setText("Please check file inputs and try again");
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
					status_label.setText("Transfer completed unsuccessfully, please check inputs and/or try again");
				}
			}
		});

		JButton btnNewButton = new JButton("File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog dialog = new FileDialog((Frame) null, "Select file to transfer");
				dialog.setMode(FileDialog.LOAD);
				dialog.setVisible(true);
				String file = dialog.getFile();
				file_name_input.setText(file + "");
				file_name = file_name_input.getText();
				file_path = "" + dialog.getDirectory();
				if (!file_name.equals("")) {
					btnTransfer.setEnabled(true);
				} else {
					btnTransfer.setEnabled(false);
				}
			}
		});

		btnTransfer.setForeground(Color.BLACK);
		btnTransfer.setBackground(Color.RED);
		btnTransfer.setBounds(435, 161, 89, 23);
		frame.getContentPane().add(btnTransfer);

		btnNewButton.setBackground(Color.RED);
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBounds(435, 132, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		timeout_input = new JTextField();
		timeout_input.setBounds(161, 133, 264, 20);
		frame.getContentPane().add(timeout_input);
		timeout_input.setColumns(10);
	
	}

	public static void transfer() throws Exception {
		int sequence = 1;
		String line;
		String packet_string;
		//mds = 64; // for testing delete after
		int count;
		boolean acknowledged;
		byte[] receive_packet = new byte[max_line];
		
		//for testing, enable inputs when completed
		//file_path = "C:/Users/prana/Documents/Winter 2019/Networking/a2/testing/";
		//file_name = "test.txt";
		//receiver_ip = InetAddress.getLocalHost();
		//receiver_port = 9999;
		
		File file = new File(file_path + "" + file_name);
		// System.out.println(file_path);
		file_size = file.length();
		total_datagrams = (file_size + mds - 1) / mds; // the reason for +mds -1 is that it will act like a rounding up
		status_label.setText("File size (bytes): " + file_size + "\tPackets size (bytes): " + mds + "\tMINIMUM packets: " + total_datagrams);

		DatagramSocket socket = new DatagramSocket(sender_port);
		socket.setSoTimeout(timeout_ms);
	
		BufferedReader reader = new BufferedReader(new FileReader(file));
		line = reader.readLine();
		
		int failed_ack = 0;
		String lines[];
		while (line != null) {
			//System.out.println(line);
			if(line.getBytes().length > mds) {
				lines = new String[(line.getBytes().length / mds) ];
				int leftSide = 0;
				int rightSide = mds;
				for(int i = 0; i < (line.getBytes().length / mds) - 1 ; i++) {
					lines[i] = line.substring(leftSide, rightSide );
					leftSide = rightSide ;
					rightSide += mds ;
					//System.out.println(lines[i]);
				}
				lines[lines.length -1] = line.substring(leftSide);
				String multiLine = lines[0];
				
				for(int i = 1; i < lines.length + 1; i++) {
					if(i < lines.length) {
						multiLine =  multiLine + " " + sequence + "," + "0" ; // adding sequence number to the end of line
					} else {
						multiLine =  multiLine + " " + sequence + "," + "1" ; // adding sequence number to the end of line
					}
					
					byte[] send_packet = new byte[mds];
					send_packet =  multiLine.getBytes();
					packet_string = new String(send_packet);
					//System.out.println(packet_string);
					//System.out.println(receiver_ip);
					
					
					DatagramPacket packet = new DatagramPacket(send_packet, send_packet.length,receiver_ip,receiver_port);
					socket.send(packet);
					
					while(true) {
						byte[] ack = new byte[mds];
						DatagramPacket ack_packet = new DatagramPacket(ack,ack.length,receiver_ip,sender_port);
						
						try {
							socket.setSoTimeout(timeout_ms);
							socket.receive(ack_packet);
							acknowledged = true;
							failed_ack = 0;
						}
						catch (SocketTimeoutException e) {
							acknowledged = false;
							failed_ack++;
							
						}
						
						if (acknowledged) {
							String s_sequence = new String(ack_packet.getData());
							//System.out.println(s_sequence + " " + sequence);
							break;
						}
						else {
							if(failed_ack > 10) {
								status_label.setText("Connection broken please check inputs and try again!\n\n\n");
								break;
							}
							socket.send(packet);
						}
					}
					if(i < lines.length) {
						multiLine = lines[i];
					}
					
					sequence++;
					
					
				}
				
				line = reader.readLine();
			}else {
				line =  line + " " + sequence + "," + "1" ; // adding sequence number to the end of line // adding sequence number to the end of line
				byte[] send_packet = new byte[mds];
				send_packet = line.getBytes();
				packet_string = new String(send_packet);
				//System.out.println(packet_string);
				//System.out.println(receiver_ip);
				
				
				DatagramPacket packet = new DatagramPacket(send_packet, send_packet.length,receiver_ip,receiver_port);
				socket.send(packet);
				
				while(true) {
					byte[] ack = new byte[mds];
					DatagramPacket ack_packet = new DatagramPacket(ack,ack.length,receiver_ip,sender_port);
					
					try {
						socket.setSoTimeout(timeout_ms);
						socket.receive(ack_packet);
						acknowledged = true;
						failed_ack = 0;
					}
					catch (SocketTimeoutException e) {
						acknowledged = false;
						failed_ack++;
						
					}
					
					if (acknowledged) {
						String s_sequence = new String(ack_packet.getData());
						//System.out.println(s_sequence + " " + sequence);
						break;
					}
					else {
						if(failed_ack > 10) {
							status_label.setText("Connection broken please check inputs and try again!\n\n\n");
				
							break;
						}
						socket.send(packet);
					}
				}
				line = reader.readLine();
				sequence++;
			}
			
		}
		// End of transmission line
		byte[] eot_packet = new byte[mds];
		eot_packet = (""+receiver_port+"" + sender_port + " " + sequence).getBytes();
		packet_string = new String(eot_packet);
		DatagramPacket packet = new DatagramPacket(eot_packet, eot_packet.length,receiver_ip,receiver_port);
		socket.send(packet);
		file = null;
		reader.close();
		socket.close();
	}
}
