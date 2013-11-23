package pc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.FlowLayout;

/**
 * GUI to draw robot path and current location of the robot. Uses mouse to
 * enter x,y coordinates and transmits commands from the GUI to the robot.
 * @author Corey Short, Phuoc Nguyen, Khoa Tran. 
 * Reference to Glassey OffScreenGrid.java and Milestone 5 sample GUI.
 */
public class CommListener extends JFrame implements GNC {

	private JPanel contentPane;
	private JTextField nameField, xField, yField, xField2, yField2; 
	private JTextField headingField, movingField, statusField, amountField;
	private JLabel lblX, lblY, lbl2X, lbl2Y, lblAngle, lblHeading;
	private JLabel lblData, lblPose, lblMoving, lblStatus;
	private JButton stopButton, setPoseButton, gotoButton, map1Button, map2Button;
	private JButton fixButton, travelButton, rotateButton, rotateToButton, echoButton;
	private JButton connectButton, scannerRotateButton, map3Button;
	
	private GridControlCommunicator communicator = new GridControlCommunicator(this);
	private OffScreenDrawing oSGrid = new OffScreenDrawing();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CommListener frame = new CommListener();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the frame for the gui to interact with the robot for the final project.
	 * @author Short
	 */
	public CommListener() {
		setTitle("Mission Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(20, 20, 1200, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel();
		topPanel.setBounds(new Rectangle(0, 0, 240, 480));
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel connectPanel = new JPanel();
		topPanel.add(connectPanel);
		
		connectButton = new JButton("Connect");
		connectButton.setBounds(15, 5, 100, 25);
		connectButton.addActionListener(new ConnectButtonActionListener());
		connectPanel.setLayout(null);
		connectPanel.add(connectButton);

		nameField = new JTextField();
		nameField.setBounds(120, 11, 100, 20);
		nameField.setColumns(10);
		connectPanel.add(nameField);
		
		lblData = new JLabel("Data");
		lblData.setBounds(245, 0, 60, 15);
		connectPanel.add(lblData);

		lblX = new JLabel("X");
		lblX.setBounds(245, 20, 15, 15);
		connectPanel.add(lblX);

		xField = new JTextField();
		xField.setBounds(260, 20, 30, 15);
		xField.setColumns(5);
		connectPanel.add(xField);

		lblY = new JLabel("Y");
		lblY.setBounds(300, 20, 15, 15);
		connectPanel.add(lblY);

		yField = new JTextField();
		yField.setBounds(315, 20, 30, 15);
		yField.setColumns(5);
		connectPanel.add(yField);
		
		lblAngle = new JLabel("Amount");
		lblAngle.setBounds(355, 20, 50, 15);
		connectPanel.add(lblAngle);
		
		amountField = new JTextField();
		amountField.setBounds(405, 20, 30, 15);	
		amountField.setColumns(5);
		connectPanel.add(amountField);
		
		lblPose = new JLabel("Pose");
		lblPose.setBounds(490, 0, 60, 15);
		connectPanel.add(lblPose);
		
		lbl2X = new JLabel("X");
		lbl2X.setBounds(490, 20, 15, 15);
		connectPanel.add(lbl2X);
		
		xField2 = new JTextField();
		xField2.setBounds(505, 20, 30, 15);
		xField2.setColumns(5);
		connectPanel.add(xField2);
		
		lbl2Y = new JLabel("Y");
		lbl2Y.setBounds(545, 20, 15, 15);
		connectPanel.add(lbl2Y);
		
		yField2 = new JTextField();
		yField2.setBounds(560, 20, 30, 15);
		yField2.setColumns(5);
		connectPanel.add(yField2);
		
		lblHeading = new JLabel("Heading");
		lblHeading.setBounds(600, 20, 50, 15);
		connectPanel.add(lblHeading);
		
		headingField = new JTextField();
		headingField.setBounds(650, 20, 30, 15);
		headingField.setColumns(5);
		connectPanel.add(headingField);
		
		lblMoving = new JLabel("Moving");
		lblMoving.setBounds(690, 20, 50, 15);
		connectPanel.add(lblMoving);
		
		movingField = new JTextField();
		movingField.setBounds(740, 20, 30, 15);
		movingField.setColumns(5);
		connectPanel.add(movingField);
		
		JPanel enumPanel = new JPanel();
		topPanel.add(enumPanel);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(new StopButtonActionListener());
		enumPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		enumPanel.add(stopButton);
		
		gotoButton = new JButton("GO TO");
		gotoButton.addActionListener(new GoToButtonActionListener());
		enumPanel.add(gotoButton);
		
		map1Button = new JButton("map left");
		map1Button.addActionListener(new MapLeftButtonActionListener());
		enumPanel.add(map1Button);
		
		map2Button = new JButton("map right");
		map2Button.addActionListener(new MapRightButtonActionListener());
		enumPanel.add(map2Button);
		
		map3Button = new JButton("map explore");
		map3Button.addActionListener(new MapExploreButtonActionListener());
		enumPanel.add(map3Button);
		
		setPoseButton = new JButton("Set Pose");
		setPoseButton.addActionListener(new SetPoseButtonActionListener());
		enumPanel.add(setPoseButton);
		
		fixButton = new JButton("Fix");
		fixButton.addActionListener(new FixButtonActionListener());
		enumPanel.add(fixButton);
		
		travelButton = new JButton("Travel");
		travelButton.addActionListener(new TravelButtonActionListener());
		enumPanel.add(travelButton);
		
		rotateButton = new JButton("Rotate");
		rotateButton.addActionListener(new RotateButtonActionListener());
		enumPanel.add(rotateButton);
		
		rotateToButton = new JButton("Rotate To");
		rotateToButton.addActionListener(new RotateToButtonActionListener());
		enumPanel.add(rotateToButton);
		
		echoButton = new JButton("Get echo");
		echoButton.addActionListener(new EchoButtonActionListener());
		enumPanel.add(echoButton);
		
		JPanel statusPanel = new JPanel();
		topPanel.add(statusPanel);

		lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 13));
		statusPanel.add(lblStatus);

		statusField = new JTextField();
		statusPanel.add(statusField);
		statusField.setColumns(40);

		contentPane.add(oSGrid, BorderLayout.CENTER);

		oSGrid.textX = this.xField;
		oSGrid.textY = this.yField;		
	}
	
	/**
	 * ActionListener that pairs the PC and NXT together.
	 * @author Short
	 *
	 */
	private class ConnectButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String name = nameField.getText();
			System.out.println("* Trying to connect to " + name);
			communicator.connect(name);
		}
	}
	
	/**
	 * ActionListener that sends the coordinates for the NXT to goto.
	 * @author Short
	 *
	 */
	private class GoToButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Send button pressed.");
			sendMove();
		}
	}
	
	/**
	 * ActionListener that sets a pose on the GUI for the nxt.
	 * @author Short
	 *
	 */
	private class SetPoseButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Set pose button pressed.");
			sendSetPose();
		}
	}
	
	/**
	 * ActionListener that gets the echo distance to an object.
	 * @author Short
	 *
	 */
	private class EchoButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Echo button pressed.");
			sendEcho();
		}
	}
	
	/**
	 * ActionListener that sends the command for the NXT to stop any of its action's.
	 * @author Short
	 *
	 */
	private class StopButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Stop button pressed.");
			sendStop();
		}
	}
	
	/**
	 * ActionListener that tells the NXT to travel in a straight line a given distance.
	 * @author Short
	 *
	 */
	private class TravelButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Travel button pressed.");
			sendTravel();
		}
	}
	
	/**
	 * ActionListener that rotates the NXT by a given amount.
	 * @author Short
	 *
	 */
	private class RotateButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Rotate button pressed.");
			sendRotate();
		}
	}
	
	/**
	 * ActionListener that rotates the NXT to a specified angle.
	 * @author Short
	 *
	 */
	private class RotateToButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Rotate To button pressed.");
			sendRotateTo();
		}
	}
	
	/**
	 * ActionListener that fixes the position of the NXT on the GUI.
	 * @author Short
	 *
	 */
	private class FixButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Fix button pressed.");
			sendFix();
		}
	}
	
	/**
	 * ActionListener that rotates scanner 90 degrees and draws a map of the
	 * wall detected on GUI.
	 */
	private class MapLeftButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Map left button pressed.");
			sendMapLeft();
		}
	}
	
	/**
	 * ActionListener that rotates scanner -90 degrees and draws a map of the 
	 * wall detected on GUI.
	 */
	private class MapRightButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Map right button pressed.");
			sendMapRight();
		}
	}
	/**
	 * ActionListener that rotates scanner from 90 to -90 and draws a 
	 * map of the wall detected on the GUI.
	 */
	private class MapExploreButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Map explore button pressed.");
			sendMapExplore();
		}
	}
	
	
	
	/**
	 * Sends a ping to the communicator to get an echo distance.
	 */
	public void sendEcho() {
		float angle = 0;
		
		try {
			angle = Float.parseFloat(amountField.getText());
			System.out.println(" get angle " + angle);
		} catch (Exception e) {
			setMessage("Problem with Angle Field");
			return;
		}
		communicator.sendEcho(angle);
		repaint();
	}
	
	/**
	 * sends the destination to move to the communicator.
	 */
	public void sendMove() {
		float x = 0;
		float y = 0;

		try {
			x = Float.parseFloat(xField.getText());
			System.out.println(" get x " + x);
		} catch (Exception e) {
			setMessage("Problem with X field");
			return;
		}

		try {
			y = Float.parseFloat(yField.getText());
			System.out.println(" get y " + y);
		} catch (Exception e) {
			setMessage("Problem  with Y field");
			return;
		}

		communicator.sendDestination(x, y);
		repaint();
	}

	/**
	 * sends the pose to the communicator. sends the x and y coordinates and the heading.
	 */
	public void sendSetPose() {
		float x = 0;
		float y = 0;
		float heading = 0;
		
		try {
			x = Float.parseFloat(xField2.getText());
			System.out.println(" get x " + x);
		} catch (Exception e) {
			setMessage("Problem with X field");
			return;
		}

		try {
			y = Float.parseFloat(yField2.getText());
			System.out.println(" get y " + y);
		} catch (Exception e) {
			setMessage("Problem  with Y field");
			return;
		}
		
		try {
			heading = Float.parseFloat(headingField.getText());
			System.out.println(" get heading " + heading);
		} catch (Exception e) {
			setMessage("Problem  with Heading field");
			return;
		}
		
		communicator.sendSetPose(x, y, heading);
		repaint();
	}
	
	/**
	 * sends the stop command message to the communicator.
	 */
	public void sendStop() {
		communicator.sendStop();
		repaint();
	}
	
	/**
	 * sends a fixed position to the communicator.
	 */
	public void sendFix() {
		communicator.sendFix();
		repaint();
	}
	
	/**
	 * sends the communicator the map left message
	 */
	public void sendMapLeft() {
		float x = 0;
		float y = 0;

		try {
			x = Float.parseFloat(xField.getText());
			System.out.println(" get x " + x);
		} catch (Exception e) {
			setMessage("Problem with X field");
			return;
		}

		try {
			y = Float.parseFloat(yField.getText());
			System.out.println(" get y " + y);
		} catch (Exception e) {
			setMessage("Problem  with Y field");
			return;
		}
		
		communicator.sendMapLeft(x, y, 90f);
		repaint();
	}
	
	/**
	 * sends the communicator the map right message
	 */
	public void sendMapRight() {
		float x = 0;
		float y = 0;

		try {
			x = Float.parseFloat(xField.getText());
			System.out.println(" get x " + x);
		} catch (Exception e) {
			setMessage("Problem with X field");
			return;
		}

		try {
			y = Float.parseFloat(yField.getText());
			System.out.println(" get y " + y);
		} catch (Exception e) {
			setMessage("Problem  with Y field");
			return;
		}
		communicator.sendMapRight(x, y, -90f);
		repaint();
	}
	
	/**
	 * sends the communicator the map explore message
	 */
	public void sendMapExplore() {
		communicator.sendMapExplore();
		repaint();
	}
	
	/**
	 * sends the communicator the distance to travel.
	 */
	public void sendTravel() {
		float dist = 0;
		
		try {
			dist = Float.parseFloat(amountField.getText());
			System.out.println(" get dist " + dist);
		} catch (Exception e) {
			setMessage("Problem with travel field");
			return;
		}
		
		communicator.sendTravel(dist);
		repaint();
	}
	
	/**
	 * sends the communicator the angle to rotate.
	 */
	public void sendRotate() {
		float angle = 0;
		
		try {
			angle = Float.parseFloat(amountField.getText());
			System.out.println(" get angle " + angle);
		} catch (Exception e) {
			setMessage("Problem with Angle Field");
			return;
		}
		
		communicator.sendRotate(angle);
		repaint();
	}
	
	/**
	 * sends the communicator the angle to rotate to.
	 */
	public void sendRotateTo() {
		float angle = 0;
		
		try {
			angle = Float.parseFloat(amountField.getText());
			System.out.println(" get angle " + angle);
		} catch (Exception e) {
			setMessage("Problem with Angle Field");
		}
		
		//communicator.sendRotateTo(angle);
		repaint();
	}
	
	public void setInfo(String message) {
		statusField.setText(message);
	}

	public void setMessage(String message) {
		statusField.setText(message);
	}

	public void drawRobotPath(int x, int y, int heading) {
		oSGrid.drawRobotPath(x, y, heading);
	}

	public void drawObstacle(int x, int y) {
		oSGrid.drawBomb(x, y);
	}
	public void drawWall(int x, int y) {
		oSGrid.drawWall(x, y);
	}
	
}