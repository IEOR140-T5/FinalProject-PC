package pc;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JButton;


/**
 To avoid maintaing a data record of robot movements, the path is drawn and recorded on the off screen image. <br>
 the paint() method of an Image is does not clear it (unlike the GUI objects)<br> 
 <br>displays updated image on command <br>
 Image coordinates are in pixels ; public method parameters use grid coordinates <br>
 Uses  Image for drawing updating the robot path.
  by Karthik, modified by R. Glassey Sept 2007  
  for mars explorer
*/

public class OffScreenDrawing extends JPanel implements ActionListener
{
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
   private int y0;
/**
* line spacing in  pixels
*/
   private final int gridSpacing = 50;
/**
* origin in pixels from corner of drawing area
*/
   private final int origin = 50;
/**
  *robot position ; used by checkContinuity, drawRobotPath
  */  
   private int robotPrevX = xpixel(0);
/**
 * robot position; used by checkContinuity, drawRobotPath
 */
   private int  robotPrevY = ypixel(0); 

/**
 * node status - true if blocked; set by drawObstacle, used by drawRobotPath
 */
   private boolean block = false;
/**
 *simple constructor
 */
   OffScreenDrawing() 
   {			
      setBackground(Color.white); 
      System.out.println(" OffScreen Drawing constructor " );
      JButton clear = new JButton("Clear");
      add( clear);		
      clear.addActionListener(this);
   }
   public void init(){ makeImage(); }// executes after constructor
/**
 * Create the offScreenImage, or make a new one if applet size has changed.
*/				   
   public void makeImage() 
   {	
		imageWidth = getSize().width;// size from the panel
		imageHeight = getSize().height;
		y0= imageHeight - origin;
		robotPrevX = xpixel(0);
		robotPrevY = ypixel(0);
		offScreenImage = createImage(imageWidth, imageHeight);// the container can make an image
		osGraphics = (Graphics2D) offScreenImage.getGraphics(); 
		osGraphics.setColor(getBackground()); 
		osGraphics.fillRect(0, 0, imageWidth, imageHeight);// erase everything
		drawGrid();
   }

	public void actionPerformed(ActionEvent e) { clear(); }
/**
 *clear the screen and draw a new grid
 */  
	public void clear()
	{
		osGraphics.setColor(getBackground());
		osGraphics.fillRect(0, 0, imageWidth, imageHeight);// clear the image
		drawGrid();
		repaint();
	}	
   
/**
 *Copy off screen canvas to the screen; g is the graphics context of the panel.
 **/
   public void paintComponent(Graphics g) 
   {
	   	super.paintComponent(g);
	   	if(offScreenImage == null) makeImage();
		g.drawImage(offScreenImage, 0, 0, this);  //Writes the Image to the screen
   }
/**
 *draws the grid with labels; draw robot at 0,0
 */
   public void drawGrid()
	{ 
		int xmax = 5;
		int ymax = 7;
		osGraphics.setColor(Color.green); // Set the line color
		for(int y = 0; y <= ymax; y ++)
			 osGraphics.drawLine(xpixel(0),ypixel(y),xpixel(xmax),ypixel(y));//horizontal lines
		for (int x=0; x<=xmax; x++ )
			osGraphics.drawLine(xpixel(x),ypixel(0),xpixel(x),ypixel(ymax));// vertical lines
		osGraphics.setColor(Color.black); //set number color 	
	   	for(int y=0; y<=ymax;y++) // number the  y axis
		osGraphics.drawString(y+"", xpixel(-0.5f),ypixel(y)); 
		for (int x=0; x<=xmax; x++ ) // number the x axis
		osGraphics.drawString(x+"", xpixel(x),ypixel(-0.5f));
		drawRobotPath(0,0);

   } 

/**
 *Obstacles shown as magenta dot
 */ 	
   public void drawObstacle(int x, int y) 
   {
		x = xpixel(x); // coordinates of intersection
  		y = ypixel(y);
  		block=true;
		osGraphics.setColor(Color.magenta); 
		osGraphics.fillOval(x-5,y-5,10,10);//bounding rectangle is 10 x 10
		repaint(100);
   }
/**
 *blue line connects current robot position to last position if adjacent to current position
 */  
  public void drawRobotPath(int xx, int yy) 
  {     
		int x = xpixel(xx); // coordinates of intersection
  		int y = ypixel(yy);		
		if(checkContinuity(x, y)) 
		{
	  		osGraphics.setColor(Color.blue);
	  		osGraphics.drawLine(robotPrevX,robotPrevY,x,y);
		} 
  	   	if(block)block=false;
  	   	else	clearRobotOval(robotPrevX, robotPrevY);
  	   	osGraphics.setColor(Color.blue);
	  	osGraphics.fillOval(x-5,y-5,10,10);  //show robot position
	  	robotPrevX = x;
	  	robotPrevY = y;
	  	repaint(100);
  }
  /**
 *convert grid coordinates to pixels
 */    
  private  int xpixel(float x)
  {
  	 return  origin+(int)(x*gridSpacing);
  }
  private int ypixel(float y)
  {
   return y0-(int)(y*gridSpacing);
  }

 /**
  * clear the old robot position, arg pixels
  */ 
   private void clearRobotOval(int x, int y)
	{
  	   osGraphics.setColor(Color.white); 
	   osGraphics.fillOval(x-5,y-5,10,10);
	   osGraphics.setColor(Color.blue); 
	   osGraphics.drawLine(x-5,y,x+5,y);
	   osGraphics.drawLine(x,y-5,x,y+5);
  }
 /**
  *see of robot has moved to adjacent node - used by drawRobotPath
  */ 
  private boolean checkContinuity(int x, int y)
   {
	  	if( (abs(x-robotPrevX)==0 && abs(y-robotPrevY)==gridSpacing) ||
	 			(abs(x-robotPrevX)==gridSpacing && abs(y-robotPrevY)==0) || 
	 			(abs(x-robotPrevX)==0 && abs(y-robotPrevY)==0))
	  		return true;
	  	else 
	  		return false;
  }
  
  public int abs(int a) {
  	return (a<0 ? (-a) : (a));
  }
                     
} 