package pc;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * OffScreenDrawing.java manages drawing of grid and robot path on an Image 
 * which is displayed when repaint() is called. 
 * @author Roger Glassey
 * @Edit Phuoc Nguyen, Khoa Tran, Corey Short
 */
public class OffScreenDrawing extends JPanel implements ActionListener {
	
	/**
	 * The robot path is drawn and updated on this object. <br>
	 * created by makeImage which is called by paint(); guarantees image always
	 * exists before used;
	 */
	Image offScreenImage;
	Color color = Color.white;
	private Graphics2D osGraphics;      // the graphics context of the image. set by makeImage,
	   							        // used by all methods that draw on the image
	public final int gridSpacing = 85;  // line spacing in pixels
	public final int xOrigin = 50;      // x origin in pixels from corner of drawing area
	public int yOrigin; 			    // y origin in pixels
	int x, y, nearestX, nearestY;
	//private int destXo = xpixel(0);
	//private int destYo = ypixel(0);
	private int robotPrevX = xpixel(0); // robot position. used by checkContinuity, drawRobotPath
	private int robotPrevY = ypixel(0); // robot position. used by checkContinuity, drawRobotPath
	int imageWidth;					    // width of the drawing area.
								        // set by makeImage, used by clearImage
	int imageHeight; 				    // height of the drawing area.
									    // set by makeImage, used by clearImage
	private boolean block = false;      // node status - true if blocked.
									    // set by drawObstacle, used by drawRobotPath
	boolean line = true;
	private JButton clearButton;
	public JTextField textX, textY;
   
   /**
    *simple constructor
    */
	OffScreenDrawing() {
		setBackground(Color.white); 
		System.out.println("OffScreen Drawing constructor");
		clearButton = new JButton("Clear");
		add(clearButton);		
		clearButton.addActionListener(this);
    }
   
	public void init() { // executes after constructor
		makeImage();
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				formMouseClicked(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				paneMouseMoved(e);
			}
		});
		
    }
	
	private void formMouseClicked(MouseEvent e) {
		System.out.println("Mouse click event " + e.getX() + "," + e.getY());
		osGraphics.fillOval(x - 3, y - 3, 10, 10);
		findNearestCoordinate(x-3, y-3);
		repaint();		
	}

	private void paneMouseMoved(MouseEvent e) {
		System.out.println("Mouse moved event " + e.getX() + "," + e.getY());
		Point p = e.getPoint();
		x = (int) p.getX();
		y = (int) p.getY();
	}

	public void actionPerformed(ActionEvent e) {
		clear();
	}
	
	/**
	 * Finding nearest coordinates based on Mouse Click. 
	 * The result will be sent back to XField and YField
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	private void findNearestCoordinate(int x, int y) {
		double tempVal = 0.0;
		int thresholdX = 50;
		int thresholdY = 415;
		int nearestX = 0, nearestY = 0;
		
		nearestX = (x - thresholdX) / 50;
		tempVal = (x - thresholdX) % 50;
		System.out.println(tempVal);
		if (tempVal > 24.0)
			nearestX += 1;
		
		nearestY =  (thresholdY - y) / 50;
		tempVal =  (thresholdY - y) % 50;
		System.out.println(tempVal);
		if (tempVal > 24.0)
			nearestY += 1;
		
		if ((nearestX < 6) && (nearestY < 8) && (nearestX > -1) && (nearestX > -1)) {
			updateNearestCoordinate(nearestX, nearestY);
		}
		else {
			System.out.println("Out of Bounds!" + nearestX + " " + nearestY);
			updateErrorCoordinates();
		}
	}
	
	/**
	 * When the mouse click is out of bounds, update the Error to XField and YField
	 */
	private void updateErrorCoordinates() {
		textX.setText("Error");
		textY.setText("Error");
	}
	
	/**
	 * Update the X,Y coordinate to XField and YField
	 * @param x
	 * @param y
	 */
	private void updateNearestCoordinate(int x, int y){
		textX.setText(Integer.toString(x));
		textY.setText(Integer.toString(y));
	}
   
   /**
    * Create the offScreenImage, or make a new one if applet size has changed.
    */				   
   public void makeImage() {	
	   	System.out.println("OffScreenDrawing makeImage() called.");
		imageWidth = getSize().width;// size from the panel
		imageHeight = getSize().height;
		yOrigin= imageHeight - xOrigin;
		robotPrevX = xpixel(0);
		robotPrevY = ypixel(0);
		offScreenImage = createImage(imageWidth, imageHeight); // the container can make
															   // an image
		osGraphics = (Graphics2D) offScreenImage.getGraphics(); 
		osGraphics.setColor(getBackground()); 
		osGraphics.fillRect(0, 0, imageWidth, imageHeight);    // erase everything
		drawGrid();
   }
	
	/**
	 *clear the screen and draw a new grid
	 */  
	public void clear() {
		System.out.println("Clear buttton pressed.");
		osGraphics.setColor(getBackground());
		osGraphics.fillRect(0, 0, imageWidth, imageHeight); // clear the image
		drawGrid();
		repaint();
	}	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	   	if(offScreenImage == null)
	   		makeImage();
		g.drawImage(offScreenImage, 0, 0, this);  // writes the Image to the screen
   }
	
	/**
	 *draws the grid with labels; draw robot at 0,0
	 */
	public void drawGrid() { 
		int xmax = 8;
		int ymax = 4;
		int numX = -4;
		osGraphics.setColor(Color.green);	    // Set the line color for the graph
		for (int y = 0; y <= ymax; y++)
			 osGraphics.drawLine(xpixel(0),ypixel(y),xpixel(xmax),ypixel(y)); //horizontal
																			  //lines
		for (int x = 0; x <= xmax; x++)
			osGraphics.drawLine(xpixel(x),ypixel(0),xpixel(x),ypixel(ymax)); // vertical
																			 // lines
		osGraphics.setColor(Color.black);	    // set number color 	
		for(int y = 0; y <= ymax; y++)		    // number the  y axis
			osGraphics.drawString(y * 60 + "", xpixel(-0.3f), ypixel(y)); 
		for (int x = 0; x <= xmax; x++) {		// number the x axis
			osGraphics.drawString(numX * 60 + "", xpixel(x), ypixel(-0.2f));
			numX++;
		}
		drawRobotPath(0,0);
	}

	/**
	 *Obstacles shown as magenta dot
	 */
	public void drawObstacle(int x, int y) {
		x = xpixel(x); // coordinates of intersection
  		y = ypixel(y);
  		block = true;
		osGraphics.setColor(Color.magenta); 
		osGraphics.fillOval(x-5,y-5,10,10); // bounding rectangle is 10 x 10
		repaint(100);
    }
   
	/**
	 * blue line connects current robot position to last position if adjacent
	 * to current position
	 */
	public void drawRobotPath(int xx, int yy) {     
		int x = xpixel(xx); // coordinates of intersection
		int y = ypixel(yy);		
		if (checkContinuity(x, y)) {
			osGraphics.setColor(Color.blue);
			osGraphics.drawLine(robotPrevX,robotPrevY,x,y);
		}
		if (block) 
			block = false;
		else
			clearRobotOval(robotPrevX, robotPrevY);
		osGraphics.setColor(Color.blue);
		osGraphics.fillOval(x-5,y-5,10,10);  // show robot position
	  	robotPrevX = x;
	  	robotPrevY = y;
	  	repaint(100);
	}
	
	/**
	 *convert grid coordinates to pixels
	 */    
	private  int xpixel(float x) {
		return xOrigin + (int) (x * gridSpacing);
	}
	
	/*private int gridX(int xpix) {
		float x = (xpix - xOrigin) / (1.0f * gridSpacing);
		return Math.round(x);
	}*/
	
	private int ypixel(float y) {
		return yOrigin - (int) (y * gridSpacing);
	}
	
	/*private int gridY(int ypix) {
		float y = (yOrigin - ypix) / (1.0f * gridSpacing);
		return Math.round(y);
	}*/

	/**
     * clear the old robot position, arg pixels
     */ 
	private void clearRobotOval(int x, int y) {
		System.out.println("Clear robot oval called.");
  	   	osGraphics.setColor(Color.white); 
  	   	osGraphics.fillOval(x-5,y-5,10,10);
  	   	osGraphics.setColor(Color.blue); 
  	   	osGraphics.drawLine(x-5,y,x+5,y);
  	   	osGraphics.drawLine(x,y-5,x,y+5);
	}
	
	/**
	 *see of robot has moved to adjacent node - used by drawRobotPath
	 */ 
	private boolean checkContinuity(int x, int y) {
		if ((abs(x - robotPrevX) == 0 && abs(y - robotPrevY) == gridSpacing)
				|| (abs(x - robotPrevX) == gridSpacing && abs(y - robotPrevY) == 0)
				|| (abs(x - robotPrevX) == 0 && abs(y - robotPrevY) == 0)) {
			return true;
		} else {
			return false;
		}
	}
  
  	public int abs(int a) {
  		return (a<0 ? (-a) : (a));	
  	}
  	
                     
} 