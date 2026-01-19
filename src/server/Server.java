package server;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Server {
	private JFrame serverframe;
	private JTextField tf;
	private JTextPane ta;

	private JScrollPane scrollpane;
	private ServerSocket serverSocket;
	private Socket socket;
	private InetAddress inet_address;
	private DataInputStream dis;
	private DataOutputStream dos;
	private volatile boolean running = true;


	// ------------------------- Thread Creation --------------------------

	Thread thread = new Thread() {
		public void run() {
			while (running) {
			    readMessage();
			}

		}
	};
	
	public void stopConnection() {
	    try {
	        running = false;

	        if (dis != null) dis.close();
	        if (dos != null) dos.close();
	        if (socket != null) socket.close();

	        thread.interrupt();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// ----------------------- Thread creation end ------------------------
	Server() {
		serverframe = new JFrame("Server");
		serverframe.setSize(500, 500);

		serverframe.setLocationRelativeTo(null);
		serverframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ta = new JTextPane();
		ta.setEditable(false);
		ta.setFont(new Font("Times new roman", Font.BOLD, 20));
		ta.setMargin(new java.awt.Insets(10, 10, 10, 10));
//
//		ta.setWrapStyleWord(true);
//		ta.setLineWrap(true);
		scrollpane = new JScrollPane(ta);
		serverframe.add(scrollpane);

		tf = new JTextField();
		ta.setFont(new Font("Times new roman", Font.ITALIC, 20));
		tf.addActionListener((e) -> {
			sendMessage(tf.getText());
			appendMessage("Me: " + tf.getText(), true);  // RIGHT SIDE
			tf.setText("");
		});
		tf.setEditable(false);
		tf.setPreferredSize(new Dimension(100, 50));
		serverframe.add(tf, BorderLayout.SOUTH);
		
		serverframe.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        stopConnection();
		    }
		});

		
		serverframe.setVisible(true);
		
	}
	
	public void appendMessage(String msg, boolean isRight) {
	    try {
	        StyledDocument doc = ta.getStyledDocument();
	        SimpleAttributeSet attr = new SimpleAttributeSet();

	        if (isRight) {
	            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
	        } else {
	            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
	        }

	        StyleConstants.setFontSize(attr, 16);
	        StyleConstants.setSpaceAbove(attr, 10);
	        StyleConstants.setSpaceBelow(attr, 10);

	        doc.setParagraphAttributes(doc.getLength(), 1, attr, false);
	        doc.insertString(doc.getLength(), msg + "\n", attr);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void waitingForClient() {
		try {
			serverSocket = new ServerSocket(1111);
			String ip_address = getIPAddress();
			appendMessage("To connect server provide this IP address: " + ip_address + "\n", true);  //  SIDE
			socket = serverSocket.accept();

			ta.setText("Client found");
			
			appendMessage("\n-----------------------------------------\n ", false);  // LEFT SIDE

			tf.setEditable(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIPAddress() {
		String ip_address = "";
		try {
			inet_address = InetAddress.getLocalHost();
			ip_address = inet_address.getHostAddress();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip_address;
	}

	public void setIoStreams() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		thread.start();
	}

	public void showMessage(String message) {
		appendMessage("Client: " + message, false);  // LEFT SIDE

		recieveSound();
	}

	public void sendMessage(String message) {
		try {
			dos.writeUTF(message);
			sendSound();
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void readMessage() {
		try {
			String message = dis.readUTF();
			showMessage(message);  // LEFT SIDE

		} catch (Exception e) {
			stopConnection();
		}
	}
	public void recieveSound() {
	    try {
	        File soundFile = new File("src/sounds/receive.wav"); // correct path
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void sendSound() {
		try {
			File soundFile = new File("src/sounds/send.wav"); // correct path
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
