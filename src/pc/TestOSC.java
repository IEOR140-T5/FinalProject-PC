import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

//import javax.swing.*;

public class TestOSC extends JFrame {
	public OffScreenDrawing canvas;

	private final int WIDTH = 400;
	private final int HEIGHT = 600;
	private JTextField xField = new JTextField(4);
	private JTextField yField = new JTextField(4);
	boolean _newData;

	public TestOSC() {
		System.out.println("construct Test");
		setTitle(" off screen draw Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		JPanel cpane = (JPanel) getContentPane();
		canvas = new OffScreenDrawing();
		JPanel panel = new JPanel();
		JButton go = new JButton("set Robot Position ");
		go.addActionListener(new ButtonListener());
		panel.add(new JLabel("X"));
		panel.add(xField);
		panel.add(new JLabel("Y"));
		panel.add(yField);
		panel.add(go);
		// cpane.add(panel,BorderLayout.NORTH);
		// cpane.add(canvas,BorderLayout.CENTER);
		add(panel, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);

		// System.out.println(" TestOCS makes image ");
		// canvas.makeImage();//can only be done when screen actually displays
		setVisible(true);

		Updater artist = new Updater();
		artist.start();
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			_newData = true;
			// int x,y;
			// x = Integer.parseInt(xField.getText());
			// y = Integer.parseInt(yField.getText());
			// canvas.drawRobotPath(x,y);
			// Tools.pause(2000);
			// System.out.println("x y  "+x+" "+y);
			// canvas.drawObstacle(4,4);
			System.out.println(" done with action");
		}
	}

	class Updater extends Thread {
		public void run() {
			System.out.println(" updater started ");
			boolean more = true;
			while (more) {
				if (_newData) {
					for (int i = 0; i < 5; i++) {
						canvas.drawObstacle(i, i);
						System.out.println(" updater " + i);
					}

				}
				_newData = false;
				Thread.yield();
			}
		}
	}

	// public void go()
	// {
	// System.out.println( "go" );
	// // canvas.drawRobotPath(0,1);
	// for(int i = 1; i < 3; i++)
	//
	// {
	// _x = i;
	// _y = i;
	// _newData = true;
	// Tools.pause(500);
	// }
	// }

	public static void main(String[] args) {
		TestOSC t = new TestOSC();
		Tools.pause(100);
		// t.go();
	}
}
