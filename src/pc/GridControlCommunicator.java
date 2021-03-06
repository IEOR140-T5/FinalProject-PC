package pc;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.*;

/**
 * Communicator on the PC(GUI) side
 * @author Corey Short, Khoa Tran
 */ 
public class GridControlCommunicator {
	
	/**
	 * Instance variables
	 */
	MissionControlGUI control; // call back reference; calls setMessage, dreawRobotPositin, drasObstacle;
	private NXTConnector connector = new NXTConnector(); // connects to NXT using bluetooth
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private Reader reader = new Reader(); // listens for incoming data from the NXT
	
	/**
	 * Constructor for Communicator on the PC side
	 * @param control - the CommListener interface object
	 */
	public GridControlCommunicator(MissionControlGUI control) {
		this.control = control; // callback path
		System.out.println("GridControlCom built");
	}

	/**
	 * establishes a bluetooth connection to the robot ; needs the robot name
	 * 
	 * @param robotName - our robot's name is t
	 */
	public void connect(String robotName) {
		try {
			connector.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}

		System.out.println(" connecting to " + robotName);

		if (connector.connectTo(robotName, "", NXTCommFactory.BLUETOOTH)) {
			control.setMessage("Connected to " + robotName);
			System.out.println(" connected!");
			dataIn = new DataInputStream(connector.getInputStream());
			dataOut = new DataOutputStream(connector.getOutputStream());

			if (dataIn == null) {
				System.out.println(" no Data  ");
			}
			else if (!reader.isRunning) {
				reader.start();
			}

		}
		else {
			System.out.println(" no connection ");
		}
	}
	
	/**
	 * Sends a message to disconnect the robot from the GUI and close the open streams.
	 */
	public void sendDisconnect() {
		System.out.println("Communicator sending: DISCONNECT");
		try {
			dataOut.writeInt(MessageType.DISCONNECT.ordinal());
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the GOTO message to the robot.
	 * Sends an x and a y to the robot to indicate what point it should travel
	 * to.
	 * @param x    the x coordinate to travel to
	 * @param y    the y coordinate to travel to
	 */
	public void sendGoto(float x, float y) {
		System.out.println("Communicator sending: GOTO " + x + ", " + y);
		try {
			dataOut.writeInt(MessageType.GOTO.ordinal());
			dataOut.flush();
			writeXAndYAndFlush(x, y);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the STOP message from the GUI to the NXT
	 */
	public void sendStop() {
		System.out.println("Communicator sending: STOP ");
		try {
			dataOut.writeInt(MessageType.STOP.ordinal());
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the GRAB BOMB message from the GUI to the NXT
	 */
	public void sendGrabBomb() {
		System.out.println("Communicator sending: Grab Bomb ");
		try {
			dataOut.writeInt(MessageType.GRAB_BOMB.ordinal());
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the SET_POSE message to the robot.
	 * @param x - x coordinate of new pose
	 * @param y - y coordinate of new pose
	 * @param heading - heading of new pose
	 */
	public void sendSetPose(float x, float y, float heading) {
		System.out.println("Communicator sending: SET POSE");
		try {
			dataOut.writeInt(MessageType.SET_POSE.ordinal());
			dataOut.flush();
			writeXAndYAndFlush(x, y);
			writeHeadingAndFlush(heading);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Sends the FIX_POS message to the robot.
	 * Sends a FIX_POS to fix the pose to the NXT
	 */
	public void sendFix() {
		System.out.println("Communicator sending: FIX_POS");
		try {
			dataOut.writeInt(MessageType.FIX_POS.ordinal());
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the ECHO message to the robot.
	 * Sends a ping to the NXT.
	 */
	public void sendEcho(float angle) {
		System.out.println(" Communicator sending: ECHO");
		try {
			dataOut.writeInt(MessageType.ECHO.ordinal());
			dataOut.flush();
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the TRAVEL message to the robot.
	 * Sends a distance to travel to the NXT.
	 * @param dist - distance to travel
	 */
	public void sendTravel(float dist) {
		System.out.println(" Communicator sending: TRAVEL");
		try {
			dataOut.writeInt(MessageType.TRAVEL.ordinal());
			dataOut.flush();
			writeAngleOrDistAndFlush(dist);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the ROTATE message to the robot.
	 * Sends an amount to rotate to the NXT.
	 * @param angle - amount to rotate
	 */
	public void sendRotate(float angle) {
		System.out.println(" Communicator sending: ROTATE");
		try {
			dataOut.writeInt(MessageType.ROTATE.ordinal());
			dataOut.flush();
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRotateTo(float angle) {
		System.out.println("Communicator sending: ROTATE TO");
		try {
			dataOut.writeInt(MessageType.ROTATE_TO.ordinal());
			dataOut.flush();
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends the SEND_MAP message to the robot.
	 * Sends an x and y as a travel distance to goto. 
	 */
	public void sendMapLeft(float x, float y, float angle) {
		System.out.println("Communicator sending: MAP LEFT TO " + x + ", " + y);
		try {
			dataOut.writeInt(MessageType.SEND_MAP.ordinal());
			dataOut.flush();
			writeXAndYAndFlush(x, y);
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the SEND_MAP message to the robot. 
	 * Sends an x and y as a travel distance to goto. 
	 */
	public void sendMapRight(float x, float y, float angle) {
		System.out.println("Communicator sending: MAP RIGHT TO " + x + ", " + y);
		try {
			dataOut.writeInt(MessageType.SEND_MAP.ordinal());
			dataOut.flush();
			writeXAndYAndFlush(x, y);
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sends the EXPLORE message to the robot. 
	 */
	public void sendMapExplore(float angle) {
		System.out.println("Communicator sending: MAP EXPLORE");
		try {
			dataOut.writeInt(MessageType.EXPLORE.ordinal());
			dataOut.flush();
			writeAngleOrDistAndFlush(angle);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * reads the data input stream, and calls DrawRobotPath() and DrawObstacle()
	 * uses OffScreenDrawing, dataIn
	 * 
	 * @author Roger Glassey
	 */
	class Reader extends Thread {

		int count = 0;
		boolean isRunning = false;

		/**
		 * Runs the reader and takes in readings that the robot sends. The
		 * robot's communications will contain three pieces of information in
		 * order:
		 * 
		 * 1) a header that indicates the type of message sent. 0 for the
		 * robot's position, 1 for an obstacle's position 2) the x coordinate of
		 * that position 3) the y coordinate of that position s
		 */
		public void run() {
			System.out.println(" reader started GridControlComm ");
			isRunning = true;
			float x = 0;
			float y = 0;
			float heading = 0;
			float sDevX = 0;
			float sDevY = 0;
			float sDevHeading = 0;
			String message = "";
			while (isRunning) {
				try {
					int index = dataIn.readInt();
					MessageType header = null;
					try {
						header = MessageType.values()[index];
						System.out.println("Message received - "
								+ header.toString());
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Header out of bounds, retrying.");
						header = MessageType.STOP;
					}
					switch (header) {
					case POS_UPDATE:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						heading = dataIn.readFloat();
						System.out.println("  Robot position: " + x + "," + y + "," + heading);
						message = "Current robot position is:\n" + 
											"x: " + x + ", y: " + y + ", h: " + heading;
						control.drawRobotPath((int) x, (int) y, (int) heading);
						control.updateCoordList(message);
						control.updateXAndYDataFields(x, y);
						break;
					case CRASH:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						System.out.println("Crashed!!");
						message = "CRASHED!! Oh No! at:\n" + 
											"x: " + x + ", y: " + y + ", h: " + heading;
						control.drawObstacle((int) x, (int) y);
						control.updateCoordList(message);
						break;
					case WALL:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						System.out.println("Begin scanning for wall at: " + x + "," + y);
						message = "Mapping:\n" + "x: " + x + ", y: " + y + ", h: " + heading;
						control.drawWall((int) x, (int) y, Color.magenta);
						control.updateCoordList(message);
						break;
					case EXPLORE_RECEIVED:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						System.out.println("Begin scanning for wall at: " + x + "," + y);
						message = "Mapping:\n" + "x: " + x + ", y: " + y + ", h: " + heading;
						control.drawWall((int) x, (int) y, Color.yellow);
						control.updateCoordList(message);
						break;
					case STD_DEV:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						heading = dataIn.readFloat();
						sDevX = dataIn.readFloat();
						sDevY = dataIn.readFloat();
						sDevHeading = dataIn.readFloat();
						System.out.println("Standard deviation is: " + "x: " + sDevX + ", y: " + sDevY + ", h: " + sDevHeading);
						message = "Standard deviation is:\n" + "x: " + sDevX + ", y: " + sDevY + ", h:" + sDevHeading;
						control.drawStdDev((int) x, (int) y, (int) sDevX, (int) sDevY);
						control.updateCoordList(message);
					case GRAB_BOMB:
						
						break;
					case ECHO:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						System.out.println("Begin scanning for wall at: " + x + "," + y);
						message = "Mapping:\n" + "x: " + x + ", y: " + y + ", h: " + heading;
						control.drawWall((int) x, (int) y, Color.cyan);
						control.updateCoordList(message);
						break;
					default:
						break;
					}
				} 
				catch (IOException e) {
					System.out.println("Read Exception in GridControlComm");
					count++;
				}
				control.setMessage(message);
			}
		}
	}
	
	private void writeXAndYAndFlush(float x, float y) {
		try {
			dataOut.writeFloat(x);
			dataOut.flush();
			dataOut.writeFloat(y);
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeAngleOrDistAndFlush(float angle) {
		try {
			dataOut.writeFloat(angle);
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeHeadingAndFlush(float heading) {
		try {
			dataOut.writeFloat(heading);
			dataOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
