package com.licensekit;

import java.awt.Panel;
import java.net.URL;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Graphics;
import java.awt.Dimension;

/**
 * The ImagePanel component is similar to a regular panel except that it
 * displays an image within the panel.
 * The image to use is specified with a URL.
 */
public class ImagePanel extends java.awt.Panel
{
    /**
     * A constant indicating the image is to be tiled in this panel.
     */
    public static final int IMAGE_TILED = 0;
    /**
     * A constant indicating the image is to be centered in this panel.
     */
    public static final int IMAGE_CENTERED = 1;
    /**
     * A constant indicating the image is to be scaled to fit this panel.
     */
    public static final int IMAGE_SCALED_TO_FIT = 2;
    /**
     * A constant indicating the image is to be drawn from the upper left corner of the panel.
     */
    public static final int IMAGE_NORMAL = 3;

    /**
     * Constructs a default ImagePanel. By default the image will be tiled.
     */
   public ImagePanel()
   {
       super.setLayout(null);
      imageURL = null;
      image = null;
      imageStyle = IMAGE_TILED;
   }

   // Properties

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
      Dimension dim = getSize();
      if (image != null)
      {

         int imageWidth = image.getWidth(this);
         int imageHeight = image.getHeight(this);

         switch(imageStyle)
         {
            default:
            case IMAGE_TILED:
            {
               //Calculate number of images that should be drawn horizontally
               int numHImages = dim.width / imageWidth;

               //Don't forget remainders
               if (dim.width % imageWidth != 0)
                  numHImages++;

               //Calculate number of images that should be drawn vertically
               int numVImages = dim.height / imageHeight;

               //Don't forget remainders
               if (dim.height % imageHeight != 0)
                  numVImages++;

               int h;
               int v = 0;
               for (int vCount = 0;vCount < numVImages;vCount++)
               {
                  h = 0;
                  for (int hCount = 0;hCount < numHImages;hCount++)
                  {
                     g.drawImage(image, h, v, imageWidth, imageHeight, this);

                     //Increment to next column
                     h += imageWidth;
                  }

                  //Increment to next row
                  v += imageHeight;
               }

               break;
            }

            case IMAGE_CENTERED:
            {
               g.drawImage
                  (image,
                   (dim.width - imageWidth) / 2,
                   (dim.height - imageHeight) / 2,
                   imageWidth,
                   imageHeight,
                   this);

               break;
            }

            case IMAGE_SCALED_TO_FIT:
            {
               g.drawImage(image, 0, 0, dim.width, dim.height, this);

               break;
            }

            case IMAGE_NORMAL:
            {
               g.drawImage(image, 0, 0, this);

               break;
            }
         }//switch
      }
      else
      {
          g.clearRect(0, 0, dim.width, dim.height);
      }
      super.paint(g);
   }

    /**
     * Sets the URL of the image to display in this panel.
     * @param url the URL of the image to display
     * @see #getImageURL
     */
   public void setImageURL (URL url)
   {
      imageURL = url;
      if (imageURL != null) {
         if(image != null) {
            image.flush();
            image = null;
         }
         image = getToolkit().getImage(imageURL);
         if (image != null) {
            MediaTracker mt = new MediaTracker(this);
            try {
               mt.addImage(image, 0);
               mt.waitForAll();
            } catch (InterruptedException ie) {
            }
         }
      } else {
         if(image != null) {
            image.flush();
            image = null;
         }
      }

      repaint();
   }

    /**
     * Returns the URL of the image being displayed in this panel.
     * @see #setImageURL
     */
   public URL getImageURL()
   {
      return imageURL;
   }

    /**
     * Sets the new panel image style.
     * @param newStyle the new panel image style, one of
     * IMAGE_TILED, IMAGE_CENTERED, or IMAGE_SCALED_TO_FIT
     * @see #getStyle
     * @see #IMAGE_TILED
     * @see #IMAGE_CENTERED
     * @see #IMAGE_SCALED_TO_FIT
     * @see #IMAGE_NORMAL
     */
   public void setStyle(int newStyle)
   {
      if (newStyle != imageStyle) {
         imageStyle = newStyle;
         repaint();
      }
   }

    /**
     * Gets the current panel image style.
     * @return the current panel image style, one of
     * IMAGE_TILED, IMAGE_CENTERED, or IMAGE_SCALED_TO_FIT
     * @see #setStyle
     * @see #IMAGE_TILED
     * @see #IMAGE_CENTERED
     * @see #IMAGE_SCALED_TO_FIT
     * @see #IMAGE_NORMAL
     */
   public int getStyle()
   {
      return imageStyle;
   }

   // Methods

   //???RKM??? When beans come around we need to make certain that this is not returned as a property
    /**
     * Returns the image being displayed in this panel.
     */
   public Image getImage()
   {
      return image;
   }


   /**
    * The style that the image will be displayed in.
    */
   protected int imageStyle;
   
   // Private members

   transient private Image image;
   private URL imageURL;
}

