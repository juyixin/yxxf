package com.licensekit;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Label;

public class WrappingLabel extends Canvas
{
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTERED = 1;
    public static final int ALIGN_RIGHT = 2;
    
    public WrappingLabel()
    {
        this("");
    }

    public WrappingLabel(String s)
    {
        this(s, WrappingLabel.ALIGN_LEFT);
    }

    public WrappingLabel(String s, int a)
    {
         setText(s);
         setAlignStyle(a);
    }

    public int getAlignStyle()
    {
        return align;
    }

    public void setAlignStyle(int newAlignStyle)
    {
      if (align != newAlignStyle) {
         align = newAlignStyle;
         repaint();
      }
    }

    public String getText()
    {
        return text;
    }

    public void setText(String newText)
    {
      text = newText;
      repaint();
    }

    /**
     * Moves and/or resizes this component.
     * This is a standard Java AWT method which gets called to move and/or
     * resize this component. Components that are in containers with layout
     * managers should not call this method, but rely on the layout manager
     * instead.
     *
     * It is overridden here to re-wrap the text as necessary.
     *
     * @param x horizontal position in the parent's coordinate space
     * @param y vertical position in the parent's coordinate space
     * @param width the new width
     * @param height the new height
     */
/* public synchronized void reshape(int x, int y, int width, int height) {
       super.reshape(x, y, width, height);
       invalidate();
       validate();
       repaint();
   }*/

    /**
     * Resizes this component.
     * This is a standard Java AWT method which gets called to
     * resize this component. Components that are in containers with layout
     * managers should not call this method, but rely on the layout manager
     * instead.
     *
     * It is overridden here to re-wrap the text as necessary.
     *
     * @param width the new width, in pixels
     * @param height the new height, in pixels
     */
/* public synchronized void resize(int width, int height) {
       super.resize(width, height);
       invalidate();
       validate();
       repaint();
   }*/

   /**
    * Paints this component using the given graphics context.
     * This is a standard Java AWT method which typically gets called
     * by the AWT to handle painting this component. It paints this component
     * using the given graphics context. The graphics context clipping region
     * is set to the bounding rectangle of this component and its [0,0]
     * coordinate is this component's top-left corner.
     *
     * @param g the graphics context used for painting
     * @see java.awt.Component#repaint
     * @see java.awt.Component#update
    */
    public void paint(Graphics g)
    {
      if (text != null)
      {
         int x, y;
         int boundx, boundy;
         Dimension d;
         int fromIndex = 0;
         int pos = 0;
         int bestpos;
         String largestString;
         String s;

         // Set up some class variables
         fm = getToolkit().getFontMetrics(getFont());
         if (fm == null)
            return;
         baseline = fm.getMaxAscent();

         // Get the maximum height and width of the current control
         d = getSize();
         boundx = d.width;
         boundy = d.height;

         // X and Y represent the coordinates of the upper left portion
         // of the next text line.
         x = 0;
         y = 0;

         // While we haven't passed the bottom of the label and we
         // haven't run past the end of the string...
         while ((y + fm.getHeight()) <= boundy && fromIndex != -1)
         {
            // Automatically skip any spaces at the beginning of the line
            while (fromIndex < text.length() && text.charAt(fromIndex) == ' ')
            {
               ++fromIndex;
               // If we hit the end of line while skipping spaces, we're done.
               if (fromIndex >= text.length()) break;
            }

            // fromIndex represents the beginning of the line
            pos = fromIndex;
            bestpos = -1;
            largestString = null;

            while (pos >= fromIndex)
            {
               pos = text.indexOf(' ', pos);

               // Couldn't find another space?
               if (pos == -1)
               {
                  s = text.substring(fromIndex);
               }
               else
               {
                  s = text.substring(fromIndex, pos);
               }

               // If the string fits, keep track of it.
               if (fm.stringWidth(s) < boundx)
               {
                  largestString = s;
                  bestpos = pos;

                  // If we've hit the end of the string, use it.
                  if (pos == -1) break;
               }
               else
               {
                   break;
               }

               ++pos;
            }

            if (largestString == null)
            {
               // Couldn't wrap at a space, so find the largest line
               // that fits and print that.  Note that this will be
               // slightly off -- the width of a string will not necessarily
               // be the sum of the width of its characters, due to kerning.
               int totalWidth = 0;
               int oneCharWidth = 0;

               pos = fromIndex;

               while (pos < text.length())
               {
                  oneCharWidth = fm.charWidth(text.charAt(pos));
                  if ((totalWidth + oneCharWidth) >= boundx) break;
                  totalWidth += oneCharWidth;
                  ++pos;
               }

               drawAlignedString(g, text.substring(fromIndex, pos), x, y, boundx);
               fromIndex = pos;
            }
            else
            {
               drawAlignedString(g, largestString, x, y, boundx);

               fromIndex = bestpos;
            }

            y += fm.getHeight();
         }

         // We're done with the font metrics...
         fm = null;
         
      }
    }

    /**
     * This helper method draws a string aligned the requested way.
     * @param g the graphics context used for painting
     * @param s the string to draw
     * @param x the point to start drawing from, x coordinate
     * @param y the point to start drawing from, y coordinate
     * @param width the width of the area to draw in, in pixels
     */
    protected void drawAlignedString(Graphics g, String s, int x, int y, int width)
    {
        int drawx;
        int drawy;

        drawx = x;
        drawy = y + baseline;

        if (align != WrappingLabel.ALIGN_LEFT)
        {
            int sw;

            sw = fm.stringWidth(s);

            if (align == WrappingLabel.ALIGN_CENTERED)
            {
                drawx += (width - sw) / 2;
            }
            else if (align == WrappingLabel.ALIGN_RIGHT)
            {
                drawx = drawx + width - sw;
            }
        }

        g.drawString(s, drawx, drawy);
    }

    /**
     * The text string being displayed.
     */
    protected String text;

    /**
     * The current text alignment.
     * One of ALIGN_LEFT, ALIGN_CENTERED, or ALIGN_RIGHT.
     * @see symantec.itools.awt.AlignStyle
     * @see symantec.itools.awt.AlignStyle#ALIGN_LEFT
     * @see symantec.itools.awt.AlignStyle#ALIGN_CENTERED
     * @see symantec.itools.awt.AlignStyle#ALIGN_RIGHT
     */
    protected int align;

    /**
     * The maximum ascent of the font used to display text.
     *
     */
    protected int baseline;

    /**
     * The metrics of the font used to display text.
     */
    transient protected FontMetrics fm;
    
    

    public boolean textFits(int aWidth, int aHeight)
    {
      if (text != null)
      {
         int x, y;
         int boundx, boundy;
         //Dimension d;
         int fromIndex = 0;
         int pos = 0;
         int bestpos;
         String largestString;
         String s;

         // Set up some class variables
         fm = getToolkit().getFontMetrics(getFont());
         if (fm == null)   // error case
            return true;   // stop any while (!textFits())... loops
         baseline = fm.getMaxAscent();

         // Get the maximum height and width of the current control
         //d = getSize();
         boundx = aWidth;
         boundy = aHeight;

         // X and Y represent the coordinates of the upper left portion
         // of the next text line.
         x = 0;
         y = 0;

         // While we haven't passed the bottom of the label and we
         // haven't run past the end of the string...
         while ((y + fm.getHeight()) <= boundy && fromIndex != -1)
         {
            // Automatically skip any spaces at the beginning of the line
            while (fromIndex < text.length() && text.charAt(fromIndex) == ' ')
            {
               ++fromIndex;
               // If we hit the end of line while skipping spaces, we're done.
               if (fromIndex >= text.length()) break;
            }

            // fromIndex represents the beginning of the line
            pos = fromIndex;
            bestpos = -1;
            largestString = null;

            while (pos >= fromIndex)
            {
               pos = text.indexOf(' ', pos);

               // Couldn't find another space?
               if (pos == -1)
               {
                  s = text.substring(fromIndex);
               }
               else
               {
                  s = text.substring(fromIndex, pos);
               }

               // If the string fits, keep track of it.
               if (fm.stringWidth(s) < boundx)
               {
                  largestString = s;
                  bestpos = pos;

                  // If we've hit the end of the string, use it.
                  if (pos == -1) break;
               }
               else
               {
                   break;
               }

               ++pos;
            }

            if (largestString == null)
            {
               // Couldn't wrap at a space, so find the largest line
               // that fits and print that.  Note that this will be
               // slightly off -- the width of a string will not necessarily
               // be the sum of the width of its characters, due to kerning.
               int totalWidth = 0;
               int oneCharWidth = 0;

               pos = fromIndex;

               while (pos < text.length())
               {
                  oneCharWidth = fm.charWidth(text.charAt(pos));
                  if ((totalWidth + oneCharWidth) >= boundx) break;
                  totalWidth += oneCharWidth;
                  ++pos;
               }

               //drawAlignedString(g, text.substring(fromIndex, pos), x, y, boundx);
               fromIndex = pos;
            }
            else
            {
               //drawAlignedString(g, largestString, x, y, boundx);

               fromIndex = bestpos;
            }

            y += fm.getHeight();
         }

         // We're done with the font metrics...
         fm = null;
         
         return (fromIndex == -1);
      }
      return true;
    }
}

