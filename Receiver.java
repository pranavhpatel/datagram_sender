
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Receiver {

	private JFrame frmReceiver;
	private JTextField txtFieldReceiverPort;
	private JTextField txtFieldSenderIP;
	private JTextField txtFieldSenderPort;
	private JTextField txtFieldFileName;
	private static JTextField txtFieldSequence;

	private final ButtonGroup buttonGroup = new ButtonGroup();
	// private String senderIPAddr;
	private static JButton btnReceive;
	// private int numRecPackets;
	private static boolean reliable = true;
	private static JFileChooser fc = new JFileChooser();
	private static File file;

	// UDP declaration
	private static int byte_array; // will be buffer size of messages
	private static long total_datagrams; // will be the total datagrams number for sequence
	private static long file_size; // will be file size in bytes
	private static boolean found_error; // if something went wrong
	// connection variables
	private static InetAddress sender_ip;
	private static InetAddress receiver_ip;
	private static int receiver_port;
	private static int sender_port;
	private static int mds;
	private static String file_name;
	private static String file_path;
	private static int timeout_ms;
	private static int max_line = 2048; // byte length of the max line to receive
	private static long startTime;
	private static long elapsedTime;
	private JTextField timeout_input;
	private static JTextField txtFieldStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Receiver window = new Receiver();
					window.frmReceiver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Receiver() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		frmReceiver = new JFrame();
		frmReceiver.setTitle("Receiver");
		frmReceiver.setBounds(100, 100, 654, 400);
		frmReceiver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 204, 255));
		frmReceiver.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 50, 150, 50, 100, 100, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 30, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_2 = new GridBagConstraints();
		gbc_rigidArea_2.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_2.gridx = 3;
		gbc_rigidArea_2.gridy = 0;
		panel.add(rigidArea_2, gbc_rigidArea_2);

		JLabel lblNewLabel = new JLabel("IP Address of Sender:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_3 = new GridBagConstraints();
		gbc_rigidArea_3.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_3.gridx = 0;
		gbc_rigidArea_3.gridy = 2;
		panel.add(rigidArea_3, gbc_rigidArea_3);

		txtFieldSenderIP = new JTextField();
		GridBagConstraints gbc_txtFieldSenderIP = new GridBagConstraints();
		gbc_txtFieldSenderIP.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldSenderIP.gridwidth = 5;
		gbc_txtFieldSenderIP.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldSenderIP.gridx = 1;
		gbc_txtFieldSenderIP.gridy = 2;
		panel.add(txtFieldSenderIP, gbc_txtFieldSenderIP);
		txtFieldSenderIP.setColumns(10);

		Component rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_4 = new GridBagConstraints();
		gbc_rigidArea_4.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_4.gridx = 6;
		gbc_rigidArea_4.gridy = 2;
		panel.add(rigidArea_4, gbc_rigidArea_4);

		JLabel lblReceiverPort = new JLabel("Receiver Port:");
		GridBagConstraints gbc_lblReceiverPort = new GridBagConstraints();
		gbc_lblReceiverPort.anchor = GridBagConstraints.WEST;
		gbc_lblReceiverPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceiverPort.gridx = 1;
		gbc_lblReceiverPort.gridy = 3;
		panel.add(lblReceiverPort, gbc_lblReceiverPort);

		txtFieldReceiverPort = new JTextField();
		GridBagConstraints gbc_txtFieldReceiverPort = new GridBagConstraints();
		gbc_txtFieldReceiverPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldReceiverPort.gridwidth = 5;
		gbc_txtFieldReceiverPort.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldReceiverPort.gridx = 1;
		gbc_txtFieldReceiverPort.gridy = 4;
		panel.add(txtFieldReceiverPort, gbc_txtFieldReceiverPort);
		txtFieldReceiverPort.setColumns(10);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_1.gridx = 6;
		gbc_rigidArea_1.gridy = 4;
		panel.add(rigidArea_1, gbc_rigidArea_1);

		JLabel lblSenderPort = new JLabel("Sender Port:");
		GridBagConstraints gbc_lblSenderPort = new GridBagConstraints();
		gbc_lblSenderPort.anchor = GridBagConstraints.WEST;
		gbc_lblSenderPort.gridwidth = 2;
		gbc_lblSenderPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblSenderPort.gridx = 1;
		gbc_lblSenderPort.gridy = 5;
		panel.add(lblSenderPort, gbc_lblSenderPort);

		txtFieldSenderPort = new JTextField();
		txtFieldSenderPort.setColumns(10);
		GridBagConstraints gbc_txtFieldSenderPort = new GridBagConstraints();
		gbc_txtFieldSenderPort.gridwidth = 5;
		gbc_txtFieldSenderPort.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldSenderPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldSenderPort.gridx = 1;
		gbc_txtFieldSenderPort.gridy = 6;
		panel.add(txtFieldSenderPort, gbc_txtFieldSenderPort);

		Component rigidArea_5 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_5 = new GridBagConstraints();
		gbc_rigidArea_5.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_5.gridx = 6;
		gbc_rigidArea_5.gridy = 6;
		panel.add(rigidArea_5, gbc_rigidArea_5);

		JLabel lblFilenameToWrite = new JLabel("Filename to write transmission:");
		GridBagConstraints gbc_lblFilenameToWrite = new GridBagConstraints();
		gbc_lblFilenameToWrite.anchor = GridBagConstraints.WEST;
		gbc_lblFilenameToWrite.gridwidth = 2;
		gbc_lblFilenameToWrite.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilenameToWrite.gridx = 1;
		gbc_lblFilenameToWrite.gridy = 7;
		panel.add(lblFilenameToWrite, gbc_lblFilenameToWrite);

		txtFieldFileName = new JTextField();
		txtFieldFileName.setText("Select file with button");
		txtFieldFileName.setEditable(false);
		txtFieldFileName.setColumns(10);
		GridBagConstraints gbc_txtFieldFileName = new GridBagConstraints();
		gbc_txtFieldFileName.gridwidth = 5;
		gbc_txtFieldFileName.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldFileName.gridx = 1;
		gbc_txtFieldFileName.gridy = 8;
		panel.add(txtFieldFileName, gbc_txtFieldFileName);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 6;
		gbc_rigidArea.gridy = 8;
		panel.add(rigidArea, gbc_rigidArea);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				FileDialog dialog = new FileDialog((Frame) null, "Select file to transfer");
				dialog.setMode(FileDialog.LOAD);
				dialog.setVisible(true);
				String fileString = dialog.getFile();

				file_name = fileString;
				file_path = "" + dialog.getDirectory();
				txtFieldFileName.setText(file_path + fileString + "");
				dialog = null;

			}
		});

		btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (txtFieldSenderIP.getText().equals("") || txtFieldSenderPort.getText().equals("")
							|| txtFieldReceiverPort.getText().equals("") || txtFieldFileName.getText().equals("")) {
						txtFieldStatus.setText("Please enter correct inputs");
						btnReceive.setEnabled(true);
					} else {

						boolean no_error = false;
						txtFieldStatus.setText("Transfer in progress");
						// writing inputs into global variables

						sender_ip = InetAddress.getByName(txtFieldSenderIP.getText());
						receiver_port = Integer.parseInt(txtFieldReceiverPort.getText());
						sender_port = Integer.parseInt(txtFieldSenderPort.getText());
						// file_name = file_name_input.getText();

						if (receiver_port > 0 && sender_port > 0) { // basically no input error so far
							if (txtFieldFileName.getText().contains("Select file with button")
									|| txtFieldFileName.getText().contains("null")) {
								no_error = false;
							} else {
								no_error = true;
							}
						} else {
							txtFieldStatus.setText("Please check number inputs and try again");
						}
						if (no_error) {
							btnReceive.setEnabled(false);
							txtFieldStatus.setText("Waiting to receive");
							txtFieldSequence.setText("0");
							transfer();
							txtFieldStatus.setText("\nTransfer completed successfully");
							btnReceive.setEnabled(true);

						} else {
							txtFieldStatus.setText("Please check file inputs and try again");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnReceive = new GridBagConstraints();
		gbc_btnReceive.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnReceive.insets = new Insets(0, 0, 5, 5);
		gbc_btnReceive.gridx = 4;
		gbc_btnReceive.gridy = 9;
		panel.add(btnReceive, gbc_btnReceive);
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.gridx = 5;
		gbc_btnBrowse.gridy = 9;
		panel.add(btnBrowse, gbc_btnBrowse);

		JLabel lblStatus = new JLabel("Status:");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.gridwidth = 4;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 10;
		panel.add(lblStatus, gbc_lblStatus);

		Component rigidArea_8 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_8 = new GridBagConstraints();
		gbc_rigidArea_8.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_8.gridx = 0;
		gbc_rigidArea_8.gridy = 11;
		panel.add(rigidArea_8, gbc_rigidArea_8);

		txtFieldStatus = new JTextField();
		txtFieldStatus.setText("Waiting on input!");
		txtFieldStatus.setEditable(false);
		txtFieldStatus.setColumns(10);
		txtFieldStatus.setBackground(SystemColor.menu);
		GridBagConstraints gbc_txtFieldStatus = new GridBagConstraints();
		gbc_txtFieldStatus.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldStatus.gridx = 1;
		gbc_txtFieldStatus.gridy = 11;
		panel.add(txtFieldStatus, gbc_txtFieldStatus);

		JRadioButton rdbtnNewRadioButton = new JRadioButton("Reliable");
		rdbtnNewRadioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				reliable = true;
			}
		});
		rdbtnNewRadioButton.setSelected(true);
		buttonGroup.add(rdbtnNewRadioButton);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 4;
		gbc_rdbtnNewRadioButton.gridy = 11;
		panel.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);

		JRadioButton rdbtnUnreliable = new JRadioButton("Unreliable");
		rdbtnUnreliable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				reliable = false;
			}
		});
		buttonGroup.add(rdbtnUnreliable);
		GridBagConstraints gbc_rdbtnUnreliable = new GridBagConstraints();
		gbc_rdbtnUnreliable.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnUnreliable.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnUnreliable.gridx = 5;
		gbc_rdbtnUnreliable.gridy = 11;
		panel.add(rdbtnUnreliable, gbc_rdbtnUnreliable);

		JLabel lblNumber = new JLabel("Number of received in order packets:");
		GridBagConstraints gbc_lblNumber = new GridBagConstraints();
		gbc_lblNumber.anchor = GridBagConstraints.WEST;
		gbc_lblNumber.gridwidth = 4;
		gbc_lblNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumber.gridx = 1;
		gbc_lblNumber.gridy = 12;
		panel.add(lblNumber, gbc_lblNumber);

		Component rigidArea_6 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_6 = new GridBagConstraints();
		gbc_rigidArea_6.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_6.gridx = 0;
		gbc_rigidArea_6.gridy = 13;
		panel.add(rigidArea_6, gbc_rigidArea_6);

		txtFieldSequence = new JTextField();
		txtFieldSequence.setText("0");
		txtFieldSequence.setBackground(SystemColor.control);
		txtFieldSequence.setEditable(false);
		GridBagConstraints gbc_txtFieldSequence = new GridBagConstraints();
		gbc_txtFieldSequence.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFieldSequence.insets = new Insets(0, 0, 5, 5);
		gbc_txtFieldSequence.gridx = 1;
		gbc_txtFieldSequence.gridy = 13;
		panel.add(txtFieldSequence, gbc_txtFieldSequence);
		txtFieldSequence.setColumns(10);

		Component rigidArea_7 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_7 = new GridBagConstraints();
		gbc_rigidArea_7.anchor = GridBagConstraints.WEST;
		gbc_rigidArea_7.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_7.gridx = 2;
		gbc_rigidArea_7.gridy = 13;
		panel.add(rigidArea_7, gbc_rigidArea_7);
	}

	public static void transfer() throws Exception {

		//InetAddress ia = InetAddress.getLocalHost();

		DatagramSocket socket = new DatagramSocket(receiver_port);
		// int counter = 0;

		file = new File(file_path + "" + file_name);

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		// String line;
		// String packet_string;
		int packetsWrote = 0;
		int count = 0;
		// boolean acknowledged;
		// int lastSequence;
		boolean run = true;
		boolean drop = false;
		String buffer = "";
		txtFieldStatus.setText("Waiting for transfer!");
		while (run) {
			count++;
			if (reliable == false) {
				// System.out.println(count);
				if (count % 10 == 0) {
					drop = true; // dont receive packet
				} else {
					drop = false;
				}
			}

			byte[] receive_packet = new byte[max_line];

			// BREAKING IT DOWN
			DatagramPacket packet = new DatagramPacket(receive_packet, receive_packet.length);

			// GOT PACKET
			socket.receive(packet);
			writer.flush();
			if (drop == false) {
				// CONVERTED PACKET AND DID STRING STUFF

				String s = new String(packet.getData()); // message with sequence number
				String header = s.substring(s.lastIndexOf(" ") + 1); // extracts sequence number from message
				String actual_message = s.substring(0, s.lastIndexOf(" ")).trim(); // removes sequence number from
				
				int flagBit = 0;
				String sequence = "";
				if (!actual_message.equals("" + receiver_port + "" + sender_port)) {
					String headerArr[] = header.trim().split(",");
					flagBit = Integer.parseInt(headerArr[1]);
					sequence = headerArr[0];
				}

				// message
				packetsWrote++;

				// CHECKING IF LAST LINE THEN SAVE FILE AND ASK USER TO DO IT AGAIN
				if (actual_message.equals("" + receiver_port + "" + sender_port)) {
					// run = false;
					txtFieldStatus.setText("Transmission successful! Ready to transmit again!");
					break;
				} else {

					txtFieldSequence.setText(Integer.toString(packetsWrote));
					if (flagBit == 1) {
						// System.out.println(buffer + actual_message);
						writer.write(buffer + actual_message, 0, buffer.length() + actual_message.length());
						writer.newLine();
						// SENDING ACK
						byte[] b2 = new byte[max_line];
						b2 = sequence.getBytes();
						// System.out.println(actual_message);
						DatagramPacket packet1 = new DatagramPacket(b2, b2.length, sender_ip, sender_port);
						// ACK DOESNT MATTER WHAT IS SENT
						socket.send(packet1);
						buffer = "";
					} else {

						buffer = buffer + actual_message;
						// System.out.println(buffer );
						byte[] b2 = new byte[max_line];
						b2 = sequence.getBytes();
						// System.out.println(actual_message);
						DatagramPacket packet1 = new DatagramPacket(b2, b2.length, sender_ip, sender_port);
						// ACK DOESNT MATTER WHAT IS SENT
						socket.send(packet1);

					}

				}
			}
		}
		writer.close();
		socket.close();
		txtFieldStatus.setText("Transmission complete, please check file");
		fc = null;
		file = null;
		
	}

}
