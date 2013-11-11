package pc;
import java.awt.BorderLayout;
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
public class CommListener extends JFrame {

	private JPanel contentPane;
	private JTextField nameField, xTextField, yTextField, xTextField2, yTextField2; 
	private JTextField headingField, movingField, statusField, amountField;
	private JLabel lblX, lblY, lbl2X, lbl2Y, lblAmount, lblHeading;
	private JLabel lblData, lblPose, lblMoving, lblStatus;
	private JButton stopButton, setPoseButton, gotoButton, map1Button, map2Button;
	private JButton fixButton, travelButton, rotateButton, rotateToButton, pingButton;
	private JButton connectButton;
	
	/**
	 * provides communications services: sends and receives NXT data
	 */
	
	//private Message communicator = new Message(this);
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
	 * Create the frame.
	 * @author Short
	 */
	public CommListener() {
		setTitle("Mission Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel();
		topPanel.setBounds(new Rectangle(0, 0, 200, 50));
		contentPane.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel connectPanel = new JPanel();
		topPanel.add(connectPanel);
		
		connectButton = new JButton("connect to");
		connectButton.setBounds(15, 5, 95, 25);
		connectButton.addActionListener(new BtnConnectActionListener());
		connectPanel.setLayout(null);
		connectPanel.add(connectButton);

		nameField = new JTextField();
		nameField.setBounds(120, 11, 100, 15);
		nameField.setColumns(10);
		connectPanel.add(nameField);
		
		lblData = new JLabel("Data");
		lblData.setBounds(245, 0, 30, 15);
		connectPanel.add(lblData);

		lblX = new JLabel("X");
		lblX.setBounds(245, 20, 15, 15);
		connectPanel.add(lblX);

		xTextField = new JTextField();
		xTextField.setBounds(260, 20, 30, 15);
		xTextField.setColumns(5);
		connectPanel.add(xTextField);

		lblY = new JLabel("Y");
		lblY.setBounds(300, 20, 15, 15);
		connectPanel.add(lblY);

		yTextField = new JTextField();
		yTextField.setBounds(315, 20, 30, 15);
		yTextField.setColumns(5);
		connectPanel.add(yTextField);
		
		lblAmount = new JLabel("Amount");
		lblAmount.setBounds(360, 20, 50, 15);
		connectPanel.add(lblAmount);
		
		amountField = new JTextField();
		amountField.setBounds(410, 20, 30, 15);	
		amountField.setColumns(5);
		connectPanel.add(amountField);
		
		lblPose = new JLabel("Pose");
		lblPose.setBounds(460, 0, 30, 15);
		connectPanel.add(lblPose);
		
		lbl2X = new JLabel("X");
		lbl2X.setBounds(460, 20, 15, 15);
		connectPanel.add(lbl2X);
		
		xTextField2 = new JTextField();
		xTextField2.setBounds(475, 20, 30, 15);
		xTextField2.setColumns(5);
		connectPanel.add(xTextField2);
		
		lbl2Y = new JLabel("Y");
		lbl2Y.setBounds(515, 20, 15, 15);
		connectPanel.add(lbl2Y);
		
		yTextField2 = new JTextField();
		yTextField2.setBounds(530, 20, 30, 15);
		yTextField2.setColumns(5);
		connectPanel.add(yTextField2);
		
		lblHeading = new JLabel("Heading");
		lblHeading.setBounds(570, 20, 50, 15);
		connectPanel.add(lblHeading);
		
		headingField = new JTextField();
		headingField.setBounds(620, 20, 30, 15);
		headingField.setColumns(5);
		connectPanel.add(headingField);
		
		lblMoving = new JLabel("Moving");
		lblMoving.setBounds(660, 20, 50, 15);
		connectPanel.add(lblMoving);
		
		movingField = new JTextField();
		movingField.setBounds(710, 20, 20, 15);
		movingField.setColumns(5);
		connectPanel.add(movingField);
		
		JPanel enumPanel = new JPanel();
		topPanel.add(enumPanel);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(new SendButtonActionListener());
		enumPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 7));
		enumPanel.add(stopButton);

		setPoseButton = new JButton("Set Pose");
		//setPoseButton.addActionListner(new SetPoseButtonActionLIstner());
		enumPanel.add(setPoseButton);
		
		gotoButton = new JButton("GO TO");
		//gotoButton.addActionListner(new GoToButtonActionListener());
		enumPanel.add(gotoButton);
		
		map1Button = new JButton("<map");
		//map1Button.addActionListener(new Map1ButtonActionListner());
		enumPanel.add(map1Button);
		
		map2Button = new JButton("map>");
		//map2Button.addActionListener(new Map2ButtonActionListener());
		enumPanel.add(map2Button);
		
		fixButton = new JButton("Fix");
		//fixButton.addActionListener(new FixButtonActionListener());
		enumPanel.add(fixButton);
		
		travelButton = new JButton("Travel");
		//travelButton.addActionListener(new TravelButtonActionListener());
		enumPanel.add(travelButton);
		
		rotateButton = new JButton("Rotate");
		//rotateButton.addActionListener(new RotateButtonActionListener());
		enumPanel.add(rotateButton);
		
		rotateToButton = new JButton("Rotate To");
		//rotateToButton.addActionListener(new RotateToButtonActionListener());
		enumPanel.add(rotateButton);
		
		pingButton = new JButton("Ping");
		//pingButton.addActionListener(new PingButtonActionListener());
		enumPanel.add(pingButton);
		
		JPanel statusPanel = new JPanel();
		topPanel.add(statusPanel);

		lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 13));
		statusPanel.add(lblStatus);

		statusField = new JTextField();
		statusPanel.add(statusField);
		statusField.setColumns(35);

		contentPane.add(oSGrid, BorderLayout.CENTER);

		oSGrid.textX = this.xTextField;
		oSGrid.textY = this.yTextField;		
		
	}

	private class BtnConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			//communicator.connect(name);
			System.out.println("Connect to " + name);
		}
	}
	
	// set pose might look something like this.
	private class SendButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("send button");
			int x = 0;
			int y = 0;
			try {
				x = Integer.parseInt(xTextField.getText());
				System.out.println(" get x " + x);
			}
			catch (Exception e) {
				setInfo("Problem with X field");
				return;
			}
			try {
				y = Integer.parseInt(yTextField.getText());
				System.out.println(" get y " + y);
			}
			catch (Exception e) {
				setInfo("Problem with Y field");
				return;
			}
			//communicator.send(x, y);
			System.out.println("send " + x + " " + y);
			repaint();
		}
	}

	public void setInfo(String message) {
		statusField.setText(message);
	}

	public void incomingMessage(int header, int x, int y) {
		if (header == 0) {
			oSGrid.drawRobotPath(x, y);
			System.out.println("Drawing robot path to " + x + " " + y);
		}
		if (header == 1) {
			oSGrid.drawObstacle(x, y);
			System.out.println("Drawing obstacle path to " + x + " " + y);
		}
		System.out.println("HAHAHAHAHA YOU FOOL");
	}

	
	public void setMessage(String message) {
		statusField.setText(message);
	}


	public void drawRobotPath(int x, int y) {
		incomingMessage(0, x, y);
	}


	public void drawObstacle(int x, int y) {
		incomingMessage(1, x, y);
	}
}

