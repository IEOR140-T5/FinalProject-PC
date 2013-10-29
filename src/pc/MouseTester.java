import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
 * Creates a MouseFrame
 * Simple illustration of detecting and reacting to mouse events.
 */
public class MouseTester
{

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      MouseFrame frame = new MouseFrame();
      frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }

}
/**
 * Extends JFrame.  contains only a single panel,  this is Not an inner class, just defined in the same file
 * as the tester.   
 */
class MouseFrame extends JFrame
{
   public MouseFrame()
   {
      setTitle("Mouse Test");
      setSize(300, 200);
      MousePanel panel = new MousePanel();
      add(panel);    // note - in Jave6, you do not to mess with the  content pane of a frame. 
   }
}
/**
* This class generates  mouse events within its area, <br>
* and draws a small circle at the location of the event.
 */
class MousePanel extends JPanel
{
   // where the event occurred
   int x;
   int y;
   Color color;
   MousePanel()
   {
      // adds a mouse event listener
      addMouseListener(new MouseHandler());   
   }
  public void paint(Graphics g)
  {
     
     g.setColor(color);
     g.fillOval(x,y,10,10);
  }
  /**
   * The MouseAdapter class implements several event handling methods
   * This sub-class over rides three of then.
   */
  private class MouseHandler extends MouseAdapter
  {
     public void mousePressed(MouseEvent event)
     {       
        Point p = event.getPoint();
        x = (int) p.getX();
        y = (int) p.getY();
        color = Color.BLUE;
        repaint(); 
        System.out.println(" mouse pressed "+x+" "+y);
     }
     public void mouseClicked(MouseEvent event)
     {
        Point p = event.getPoint();
        x = (int) p.getX();
        y = (int) p.getY();
        color = Color.MAGENTA;
        repaint(); 
        System.out.println("  click "+x+" "+y);
     }
     public void mouseReleased(MouseEvent event)
     {
        Point p = event.getPoint();
        x = (int) p.getX();
        y = (int) p.getY();
        color = color.RED;
        repaint(); 
        System.out.println(" release "+x+" "+y);
     }
  }
}
