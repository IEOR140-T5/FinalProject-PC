package pc;

/**
 * The various message types that can be sent between the robot and the computer.
 */
public enum MessageType {
	GOTO, STOP, SET_POSE, FIX_POS, POS_UPDATE, 
	CRASH, ECHO, ROTATE, TRAVEL, ROTATE_TO, SCANNER_ROTATE, SEND_MAP, WALL, EXPLORE, STD_DEV,
	STD_DEV_RECIEVED, DISCONNECT, EXPLORE_RECEIVED
}
