package pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.pc.comm.*;

/**
 * Communicator on the PC(GUI) side
 */ 
public class GridControlCommunicator {
	
	/**
	 * Instance variables
	 */
	CommListener control; // call back reference; calls setMessage, dreawRobotPositin, drasObstacle;
	private NXTConnector connector = new NXTConnector(); // connects to NXT using bluetooth
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private Reader reader = new Reader(); // listens for incoming data from the NXT
	
	/**
	 * Constructor for Communicator on the PC side
	 * @param control - the GNC interface object
	 */
	public GridControlCommunicator(CommListener control) {
		this.control = control; // callback path
		System.out.println("GridControlCom1 built");
	}

	/**
	 * establishes a bluetooth connection to the robot ; needs the robot name
	 * 
	 * @param name
	 */
	public void connect(String name) {
		try {
			connector.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println(" conecting to " + name);

		if (connector.connectTo(name, "", NXTCommFactory.BLUETOOTH)) {
			control.setMessage("Connected to " + name);
			System.out.println(" connected !");
			dataIn = new DataInputStream(connector.getInputStream());
			dataOut = new DataOutputStream(connector.getOutputStream());

			if (dataIn == null) {
				System.out.println(" no Data  ");
			} else if (!reader.isRunning) {
				reader.start();
			}

		} else {
			System.out.println(" no connection ");
		}
	}

	/**
	 * Sends an x and a y to the robot to indicate what point it should travel
	 * to.
	 * 
	 * @param x
	 *            the x coordinate to travel to
	 * @param y
	 *            the y coordinate to travel to
	 */
	public void sendDestination(float x, float y) {
		System.out.println("Communicator sending: MOVE TO " + x + ", " + y);
		try {
			dataOut.writeInt(MessageType.MOVE.ordinal());
			dataOut.flush();
			dataOut.writeFloat(x);
			dataOut.flush();
			dataOut.writeFloat(y);
			dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends STOP message from the GUI to the NXT
	 */
	public void sendStop() {
		System.out.println("Communicator sending: STOP");
		try {
			dataOut.writeInt(MessageType.STOP.ordinal());
			dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a new pose to the NXT
	 * @param x - x coordinate of new pose
	 * @param y - y coordinate of new pose
	 * @param heading - heading of new pose
	 */
	public void sendSetPose(float x, float y, float heading) {
		System.out.println("Communicator sending: SET POSE");
		try {
			dataOut.writeInt(MessageType.SET_POSE.ordinal());
			dataOut.flush();
			dataOut.writeFloat(x);
			dataOut.flush();
			dataOut.writeFloat(y);
			dataOut.flush();
			dataOut.writeFloat(heading);
			dataOut.flush();
			System.out.println("should set pose");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Sends a FIX_POS to fix the pose to the NXT
	 */
	public void sendFix() {
		System.out.println("Communicator sending: FIX_POS");
		try {
			dataOut.writeInt(MessageType.FIX_POS.ordinal());
			dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a ping to the NXT.
	 */
	public void sendPing() {
		System.out.println(" Communicator sending: PING");
		try {
			dataOut.writeInt(MessageType.PING.ordinal());
			dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a distance to travel to the NXT.
	 * @param dist - distance to travel
	 */
	public void sendTravel(float dist) {
		System.out.println(" Communicator sending: TRAVEL");
		try {
			dataOut.writeInt(MessageType.TRAVEL.ordinal());
			dataOut.writeFloat(dist);
			dataOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends an amount to rotate to the NXT.
	 * @param angle - amount to rotate
	 */
	public void sendRotate(float angle) {
		System.out.println(" Communicator sending: ROTATE");
		try {
			dataOut.writeInt(MessageType.ROTATE.ordinal());
			dataOut.writeFloat(angle);
			dataOut.flush();
		} catch (IOException e) {
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
			System.out.println(" reader started GridControlComm1 ");
			isRunning = true;
			float x = 0;
			float y = 0;
			float heading = 0;
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
						//header = MessageType.PONG;
					}
					switch (header) {
					case POS_UPDATE:
						x = dataIn.readFloat();
						y = dataIn.readFloat();
						heading = dataIn.readFloat();
						System.out.println("  Robot position: " + x + "," + y
								+ "," + heading);
						message = x + "," + y + "," + heading;
						control.drawRobotPath((int) x, (int) y, (int) heading);
						break;
					case CRASH:
						message = " CRASHED!!!!";
						break;
					}

				} catch (IOException e) {
					System.out.println("Read Exception in GridControlComm");
					count++;
				}
				control.setMessage(message);
			}
		}
	}
}
