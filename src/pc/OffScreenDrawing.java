package pc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * OffScreenDrawing draws the grid for the CommListener GUI
 * @author Corey Short, Phuc Nguyen, Khoa Tran
 *
 */
public class OffScreenDrawing extends JPanel {

	/** Creates new form OffScreenDrawing */
	public OffScreenDrawing() {
		initComponents();
		setBackground(Color.black);
		System.out.println(" OffScreen Drawing constructor ");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (offScreenImage == null)
		{
			makeImage();
		}
		g.drawImage(offScreenImage, 0, 0, this);  //Writes the Image to the screen
	}

	/**
	 * Create the offScreenImage, 
	 */
	public void makeImage() {
		System.out.println("OffScreenGrid  makeImage() called");
		imageWidth = getSize().width;// size from the panel
		imageHeight = getSize().height;
		yOrigin = imageHeight - 50;
		robotPrevX = xpixel(0);
		robotPrevY = ypixel(0);
		offScreenImage = createImage(imageWidth, imageHeight);// the container can make an image
		try {Thread.sleep(500);}
		catch(Exception e){};
		System.out.print("Off Screen Grid  create image ----- " );
		System.out.println( offScreenImage == null);
		if(offScreenImage == null)
		{
			//			System.out.println("Null image" );
			offScreenImage =  createImage(imageWidth, imageHeight);
		}
		osGraphics = (Graphics2D) offScreenImage.getGraphics();
		osGraphics.setColor(getBackground());
		osGraphics.fillRect(0, 0, imageWidth, imageHeight);// erase everything
		drawGrid();
	}

	/**
	 *draws the grid with labels; draw robot at 0,0
	 */
	public void drawGrid() {
		int xmin = -240;
		int xmax = 240;
		int xSpacing = 30;
		int ymax = 238;
		int ySpacing = 30;
		int count = 0;
		osGraphics.setColor(Color.green); // Set the line color
		for (int y = 0; y <= ymax; y += ySpacing)
		{
			if (count == 1) {
				y = 12;
				osGraphics.drawLine(xpixel(xmin), ypixel(y), xpixel(xmax), ypixel(y));
				osGraphics.setColor(Color.green);
			}
			if (count == 8) {
				osGraphics.setColor(Color.red);
				BasicStroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT,
										BasicStroke .JOIN_BEVEL, 0, new float[]{9}, 0);
				osGraphics.setStroke(dashed);
				osGraphics.drawLine(xpixel(xmin), ypixel(y+16), xpixel(xmax), ypixel(y+16));
				osGraphics.setColor(Color.green);
				osGraphics.setStroke(new BasicStroke());
				
			}
			osGraphics.drawLine(xpixel(xmin), ypixel(y), xpixel(xmax), ypixel(y));//horizontal lines
			count++;
		}
		count = 0;
		for (int x = xmin; x <= xmax; x += xSpacing)
		{
			osGraphics.drawLine(xpixel(x), ypixel(0), xpixel(x), ypixel(ymax));// vertical lines
		}
		osGraphics.setColor(Color.white); //set number color 	
		for (int y = 0; y <= ymax; y += ySpacing) // number the  y axis
		{
			if (count == 1) {
				y = 12;
				osGraphics.drawString(y + "", xpixel(-251f), ypixel(y) + 4);
			}
			if (count == 8) {
				osGraphics.drawString(y+16 + "", xpixel(-251f), ypixel(y+16) + 4);
			}
			osGraphics.drawString(y + "", xpixel(-251f), ypixel(y) + 4);
			count++;
		}
		for (int x = xmin; x <= xmax; x +=  xSpacing) // number the x axis
		{
			osGraphics.drawString(x + "", xpixel(x) - 4, ypixel(-8f));
		}
		//drawRobotPath(0, 0, 0);

}

	/**
	 *clear the screen and draw a new grid
	 */
	public void clear() {
		System.out.println(" clear called ");
		osGraphics.setColor(getBackground());
		osGraphics.fillRect(0, 0, imageWidth, imageHeight);// clear the image
		drawGrid();
		repaint();
	}

	/**
	 *Obstacles shown as magenta dot
	 */
	public void drawBomb(int x, int y) {
		x = xpixel(x); // coordinates of intersection
		y = ypixel(y);
		osGraphics.setColor(Color.yellow);
		osGraphics.fillOval(x, y, 6, 6);//bounding rectangle is 10 x 10
		repaint();
	}

	public void drawDest(int x, int y) {
		x = xpixel(x); // coordinates of intersection
		y = ypixel(y);
		osGraphics.setColor(Color.blue);
		osGraphics.fillOval(x - 3, y - 3, 6, 6);//bounding rectangle is 10 x 10
		repaint();
	}

	/**
	 *blue line connects current robot position to last position if adjacent to current position
	 */
	public void drawRobotPath(int xx, int yy, int heading) {
		int x = xpixel(xx); // coordinates of intersection
		int y = ypixel(yy);
		
		drawGrid(); // for redrawing grid lines
		osGraphics.setColor(Color.blue);
		drawPose(robotPrevX, robotPrevY, robotPrevHeading, Color.black); // erases old pose
		repaint();
		drawPose(x, y, heading, Color.orange); // draws a new pose
		
		if (isRobotPathCalled) {
			osGraphics.drawLine(robotPrevX, robotPrevY, x, y); 
		}
		robotPrevX = x;
		robotPrevY = y;
		isRobotPathCalled = true;
		robotPrevHeading = heading;
		repaint();
		
	}
	
	/**
	 * clear the old robot position
	 */
	private void clearSpot(int x, int y, Color c) {
		System.out.println("clear spot ");
		if(osGraphics == null)System.out.println("null osGraphics");
		osGraphics.setColor(Color.white);
		osGraphics.fillOval(x - 3, y - 3, 6, 6);
		osGraphics.setColor(c);
	}

	/**
	 * draws the pose of the robot as a triangle on the GUI.
	 * @param x
	 * @param y
	 * @param heading
	 * @param c
	 */
	public void drawPose(int x, int y, int heading, Color c) {
		poseTriangle = new Polygon();
		int newX;
		int newY;
		int radius;
		robotPrevHeading = heading;
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				radius = 10;
			}
			else {
				radius = 6;
			}
			newX = x + (int) (radius * Math.cos(Math.toRadians(heading + (120 * i))));
			newY = y - (int) (radius * Math.sin(Math.toRadians(heading + (120 * i))));

			poseTriangle.addPoint(newX, newY);
			System.out.println("Point " + i + ": (" + newX + "," + newY + ")");
		}
		osGraphics.setColor(c);
		osGraphics.drawPolygon(poseTriangle);
	}
	
	/**
	 * draws a wall for the map left and right methods.
	 * @param a
	 * @return
	 */
	public void drawWall(int xx, int yy) {
		int x = xpixel(xx);
		int y = ypixel(yy);
		osGraphics.setColor(Color.magenta);
		osGraphics.fillOval(x, y, 6, 6);
		/*if (isDrawWallCalled) {
			osGraphics.setColor(Color.magenta);
			osGraphics.drawLine(wallPrevX, wallPrevY, x, y);
			isDrawWallCalled = false;
		}
		wallPrevX = x;
		wallPrevY = y;
		isDrawWallCalled = true;*/
		repaint();
	}
	
	public int abs(int a) {
		return (a < 0 ? (-a) : (a));
	}

	/**
	 *convert grid coordinates to pixels
	 */
	private int xpixel(float x) {
		return xOrigin + (int) (x * gridSpacing);
	}

	private int gridX(int xpix) {
		float x = (xpix - xOrigin)/(1.0f*gridSpacing);
		return Math.round(x);
	}
	private int ypixel(float y) {
		return yOrigin - (int) (y * gridSpacing);
	}
	private int gridY(int ypix) {
		float y = (yOrigin - ypix)/(1.0f*gridSpacing);
		return Math.round(y);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}
		});
		
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);
	
		clearButton = new JButton("Clear");
		this.add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			clear();
			}
		});
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Translates a click on the screen to a selection of destination in the text fields.
	 * 
	 * @param evt
	 */
	private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
	{
		clearSpot(xpixel(destXo), ypixel(destYo), Color.green);

		destXo = gridX(evt.getX());
		destYo = gridY(evt.getY());

		textX.setText(destXo + "");
		textY.setText(destYo + "");
		osGraphics.setColor(Color.yellow);
		drawDest(destXo, destYo);
	}//GEN-LAST:event_formMouseClicked

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JButton clearButton;
	// End of variables declaration//GEN-END:variables
	/**
	 *The robot path is drawn and updated on this object. <br>
	 *created by makeImage which is called by paint(); guarantees image always exists before used; 
	 */
	Image offScreenImage;
	/**
	 *width of the dawing area;set by makeImage,used by clearImage
	 */
	int imageWidth;
	/**
	 *height of the dawing are; set by  makeImage,used by clearImage
	 */
	int imageHeight;
	/** 
	 *the graphics context of the image; set by makeImage, used by all methods that draw on the image
	 */
	private Graphics2D osGraphics;
	/**
	 * y origin in pixels
	 */
	public int yOrigin;
	/**
	 * line spacing in  pixels
	 */
	public final int gridSpacing = 2;
	/**
	 * origin in pixels from corner of drawing area
	 */
	public final int xOrigin = 550;
	/**
	 *robot position ; used by checkContinuity, drawRobotPath
	 */
	private int robotPrevX = xpixel(0);
	/**
	 * robot position; used by checkContinuity, drawRobotPath
	 */
	private int robotPrevY = ypixel(0);
	private int robotPrevHeading = 0;
	
	private int destXo = xpixel(0);
	private int destYo = ypixel(0);
	
	private int wallPrevX;
	private int wallPrevY;
	
	private Polygon poseTriangle = new Polygon();
	
	public JTextField textX;
	public JTextField textY;
	
	public boolean isRobotPathCalled;
	public boolean isDrawWallCalled;
	
	
	
}
