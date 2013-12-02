package pc;

/**
 * The various message types that can be sent between the robot and the computer.
 */
public enum MessageType {
	MOVE, STOP, SET_POSE, FIX_POS, POS_UPDATE, 
	CRASH, ECHO, ROTATE, TRAVEL, ROTATE_TO, SCANNER_ROTATE, SEND_MAP, WALL, EXPLORE, STDDEV, WALLDETECTED
}
