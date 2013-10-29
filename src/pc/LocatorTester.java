package pc;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * For testing your Locator. Assumes you Locator implements the following public
 * methods : <br>
 * setX()<br>
 * setY(),<br>
 * setHeading(),<br>
 * setEchoDistance(),<br>
 * getX((),V getY(), <br>
 * getHeading() <br>
 * and has float[2] beaconBearing array.<br>
 * The Fix button calculates and displays the position and heading of the robot
 * from the beacon bearings and the exho distance.<br>
 * To find out what these data should be, enter the coordinates of your robot
 * and its heading, click on Scan.<> This simulates the operation of your
 * scanner. It sets the values in the locator beaconBearing array, and echo
 * distance. If you locator code is is correct, if you any set of observation
 * data, click on Scan then Fix, the robot location should change very little.
 * 
 * @author R0gerGlassey
 * 
 */

public class LocatorTester extends JFrame {

	JButton fixB = new JButton("fix");
	JButton scanB = new JButton("scan ");
	JTextField xField = new JTextField("0.0", 6);
	JTextField yField = new JTextField("0.0", 6);
	JTextField headingF = new JTextField("0.0", 6);
	JTextField bField[] = new JTextField[2];
	JTextField dField = new JTextField("0.0", 6);

	JPanel textPanel = new JPanel();
	JPanel beaconPanel = new JPanel();
	Drawing drawPanel = new Drawing();
	Locator myLocator = new Locator();

	public LocatorTester() {
		setTitle("Locator test - 4 beacons");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int FRAME_WIDTH = 600;
		int FRAME_HEIGHT = 400;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLayout(new BorderLayout());
		buildGui();
		setVisible(true);
	}

	public void buildGui() {
		// setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		textPanel.setLayout(new GridLayout(2, 6));
		textPanel.add(new JLabel("       x :"));
		textPanel.add(xField);
		textPanel.add(new JLabel("       y:"));
		textPanel.add(yField);
		textPanel.add(new JLabel("  heading: "));
		textPanel.add(headingF);
		textPanel.add(new JLabel(""));
		// textPanel.add(new JLabel(""));
		for (int i = 0; i < 2; i++) {
			textPanel.add(new JLabel("    Beacon" + i));
			bField[i] = new JTextField("0.0", 6);
			textPanel.add(bField[i]);
		}
		textPanel.add(new JLabel("    EchoDist"));
		textPanel.add(dField);
		ButtonPanel buttonPanel = new ButtonPanel();
		add(textPanel);
		add(buttonPanel);
		add(drawPanel);
		// textPanel.add(buttonPanel);
		add(textPanel, BorderLayout.NORTH);

		add(drawPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}

	private class ButtonPanel extends JPanel implements ActionListener {
		ButtonPanel() {
			add(scanB);
			scanB.addActionListener(this);
			add(fixB);
			fixB.addActionListener(this);

			System.out.println(" button panel built ");
		}

		public void actionPerformed(ActionEvent event) {
			float x = Float.parseFloat(xField.getText());
			float y = Float.parseFloat(yField.getText());
			myLocator.setHeading(Float.parseFloat(headingF.getText()));
			myLocator.setX(x);
			myLocator.setY(y);
			for (int i = 0; i < 2; i++) {
				myLocator.beaconBearing[i] = Float.parseFloat(bField[i]
						.getText());
			}
			myLocator.echoDistance = (Float.parseFloat(dField.getText()));
			if (event.getSource() == scanB) {
				// System.out.println(" scan button ");
				myLocator.scanBeacons();
				for (int i = 0; i < 2; i++) {
					bField[i].setText("" + myLocator.beaconBearing[i]);
				}
				dField.setText("" + myLocator.getY());
			}
			if (event.getSource() == fixB) {
				myLocator.fixPosition();
				xField.setText("" + myLocator.getX());
				yField.setText("" + myLocator.getY());
				headingF.setText("" + myLocator.getHeading());
			}
			// if(event.getSource()==setB)
			// {
			// myLocator.beacon[0].setVals(0,y);
			// }

			drawPanel.repaint();
		}
	}

	private class Drawing extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int x0 = 300;
			int y0 = 180;
			int x[] = { 0, 0 };
			int y[] = { 0, 0, };
			for (int i = 0; i < 2; i++) {
				x[i] = x0 + (int) (10 * myLocator.beacon[i].getX());
				y[i] = (int) (y0 - 10 * myLocator.beacon[i].getY());
				g.drawString("*", x[i] - 1, 7 + y[i]);
				g.drawLine(x[i] - 150, y[i], x[i] + 150, y[i]);
				// System.out.println(x[i]+" "+y[i]);
			}
			int px = x0 + (int) (10 * myLocator.position.getX());
			int py = (int) (y0 - 10 * myLocator.position.getY());
			g.drawString("*", px - 1, 7 + py);
			g.drawLine(x[1], y[1], x[0], y[0]);
			// System.out.println("px  "+ px + " " + py);
			// System.out.println("pos  "+ myLocator.position.getX() + " " +
			// myLocator.position.getY());
			// g.drawLine(x[1],y[1],x[2],y[2]);
			// g.drawLine(x[2],y[2],x[3],y[3]);
			// g.drawLine(x[0],y[0],x[3],y[3]);
			for (int i = 0; i < 2; i++)
				g.drawLine(px, py, x[i], y[i]);

			// g.drawLine(px,py,x[1+myLocator.offset],y[1+myLocator.offset]);
			// System.out.println("draw offset "+myLocator.offset);
		}
	}

	public static void main(String[] args) {
		LocatorTester frame = new LocatorTester();
		frame.setVisible(true);
	}
}
