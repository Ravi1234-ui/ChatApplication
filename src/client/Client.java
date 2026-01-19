package client;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("unused")
public class Client {
	private JFrame clientframe;
	private JTextField tf;
	private JTextPane ta;
	private JScrollPane scrollpane;
	private Socket socket;
	private String ip_address;
	private DataInputStream dis;
	private DataOutputStream dos;
	private volatile boolean running = true;

	// ------------------------- Thread Creation --------------------------
	
	Thread thread=new Thread() {
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

	// --------------------------------------------------------------------
	Client(){
		ip_address=JOptionPane.showInputDialog("Enter IP Address: ");
		if(ip_address!=null) {
			
			if(!ip_address.equals("")) {
				
				clientframe=new JFrame("Client");
				clientframe.setSize(500, 500);
				clientframe.setLocationRelativeTo(null);
				clientframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				ta=new JTextPane();
				ta.setEditable(false);
				ta.setFont(new Font("Times new roman", Font.BOLD, 20));
				
				ta.setMargin(new java.awt.Insets(10, 10, 10, 10));
				scrollpane=new JScrollPane(ta);
				clientframe.add(scrollpane);
				
				tf=new JTextField();
				tf.setFont(new Font("Times new roman", Font.ITALIC, 20));
				tf.addActionListener((e) -> {
					sendMessage(tf.getText());
					appendMessage("Me: " + tf.getText(), true);  // RIGHT SIDE
					tf.setText("");
				});
				tf.setEditable(true);
				
				tf.setPreferredSize(new Dimension(100, 50));
				clientframe.add(tf,BorderLayout.SOUTH);
				
				clientframe.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) {
				        stopConnection();
				    }
				});
				
				clientframe.setVisible(true);
				connectToServer();
				setIoStreams();
			}
		}
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
	
	public void connectToServer() {
		try {
			socket=new Socket(ip_address, 1111);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void showMessage(String message) {
		appendMessage("Client: " + message, false);  // LEFT SIDE

		recieveSound();
	}
	public void setIoStreams() {
		
		try {
			dis=new DataInputStream(socket.getInputStream());
			dos=new DataOutputStream(socket.getOutputStream());
			thread.start();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	        if (dis != null) {
	            String message = dis.readUTF();
	            appendMessage("Server: " + message, false);  // LEFT SIDE
	        }
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
