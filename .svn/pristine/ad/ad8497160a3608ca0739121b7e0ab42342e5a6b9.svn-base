package com.licensekit;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;

import org.springframework.security.authentication.BadCredentialsException;

public class AlertDialog extends Dialog
{
   public static final int PLAIN_MESSAGE = 0;
   public static final int QUESTION_MESSAGE = 1;
   public static final int INFORMATION_MESSAGE = 2;
   public static final int WARNING_MESSAGE = 3;
   public static final int ERROR_MESSAGE = 4;
   
   int selectedOption = -1;
   boolean setInitialFocus = true;

   public AlertDialog(Frame parent)
   {
      super(parent, true);

      //{{INIT_CONTROLS
      setVisible(false);
      setLayout(new BorderLayout(0,0));
      setSize(360,120);
      midPanel.setLayout(new GridBagLayout());
      add("Center", midPanel);
      try {
         imagePanel.setImageURL(getClass().getResource("appIcon.gif"));
         imagePanel.setStyle(ImagePanel.IMAGE_NORMAL);
         imagePanel.setLayout(null);
         imagePanel.setSize(32,32);
      } catch(Exception e) {
      }
      midPanel.add(imagePanel, new GridBagConstraintsD(0,0,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.NONE,new Insets(16,16,0,0),0,0));
      midPanel.add(msgLabel, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,0,16),0,0));
      msgLabel.setSize(280,16);
      bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,8,16));
      add("South", bottomPanel);
      button0.setLabel("button");
      bottomPanel.add(button0);
      button0.setBackground(java.awt.Color.lightGray);
      button1.setLabel("button");
      bottomPanel.add(button1);
      button1.setBackground(java.awt.Color.lightGray);
      button2.setLabel("button");
      bottomPanel.add(button2);
      button2.setBackground(java.awt.Color.lightGray);
      button3.setLabel("button");
      bottomPanel.add(button3);
      button3.setBackground(java.awt.Color.lightGray);
      button4.setLabel("button");
      bottomPanel.add(button4);
      button4.setBackground(java.awt.Color.lightGray);
      //}}

      //{{REGISTER_LISTENERS
      SymWindow aSymWindow = new SymWindow();
      this.addWindowListener(aSymWindow);
      SymAction lSymAction = new SymAction();
      button1.addActionListener(lSymAction);
      button2.addActionListener(lSymAction);
      button3.addActionListener(lSymAction);
      button0.addActionListener(lSymAction);
      button4.addActionListener(lSymAction);
      SymKey aSymKey = new SymKey();
      this.addKeyListener(aSymKey);
      button0.addKeyListener(aSymKey);
      button1.addKeyListener(aSymKey);
      button2.addKeyListener(aSymKey);
      button3.addKeyListener(aSymKey);
      button4.addKeyListener(aSymKey);
      //}}
   }
   
   public AlertDialog()
   {
      this(new Frame());
   }
   
   public int runModal (String title, String message, int messageType, String[] options)
   {
      setTitle(title);
      if(messageType == 3){
    	  throw new BadCredentialsException(message);
      }
      StringBuffer no_newlines = new StringBuffer(message);
      int i, len = no_newlines.length();
         
      for (i = 0; i < len; i++)
         if (no_newlines.charAt(i) == '\n')
            no_newlines.setCharAt(i, ' ');
         
      msgLabel.setText(no_newlines.toString());

      selectedOption = -1;
      
      int count = (options != null) ? options.length : 0;
      
      if (count > 0) {
         button0.setLabel(options[0]);
         setInitialFocus = true;
      } else {
         bottomPanel.remove(button0);
         setInitialFocus = false;
      }
      if (count > 1)
         button1.setLabel(options[1]);
      else
         bottomPanel.remove(button1);
      if (count > 2)
         button2.setLabel(options[2]);
      else
         bottomPanel.remove(button2);
      if (count > 3)
         button3.setLabel(options[3]);
      else
         bottomPanel.remove(button3);
      if (count > 4)
         button4.setLabel(options[4]);
      else
         bottomPanel.remove(button4);
      
      pack();
      
      Dimension msg_size = msgLabel.getSize();
      int width_add = 0, height_add = 0;
      while (!msgLabel.textFits(msg_size.width + width_add, msg_size.height + height_add)) {
         width_add += 4;
         height_add += 2;
      }
      
      Dimension size = getSize();
      setSize(size.width + width_add, size.height + height_add);
      msgLabel.setSize(msg_size.width + width_add, msg_size.height + height_add);
      
      pack();
      
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - getSize().width) / 2;
      int y = (int)((screenSize.height - getSize().height) / 2.25);
      setLocation(x, y);
      
      java.awt.Toolkit.getDefaultToolkit().beep();
      setVisible(true);
      
      return selectedOption;
   }

   public static int runModalDialog (String title, String message, int messageType, String[] options)
   {
	  return (new AlertDialog()).runModal(title, message, messageType, options);
   }
   
   public void addNotify()
   {
       // Record the size of the window prior to calling parents addNotify.
       Dimension d = getSize();

      super.addNotify();

      if (fComponentsAdjusted)
         return;

      // Adjust components according to the insets
      Insets insets = getInsets();
      setSize(insets.left + insets.right + d.width, insets.top + insets.bottom + d.height);
      Component components[] = getComponents();
      for (int i = 0; i < components.length; i++)
      {
         Point p = components[i].getLocation();
         p.translate(insets.left, insets.top);
         components[i].setLocation(p);
      }
      fComponentsAdjusted = true;
   }

    // Used for addNotify check.
   boolean fComponentsAdjusted = false;

   //{{DECLARE_CONTROLS
   java.awt.Panel midPanel = new java.awt.Panel();
   ImagePanel imagePanel = new ImagePanel();
   WrappingLabel msgLabel = new WrappingLabel();
   java.awt.Panel bottomPanel = new java.awt.Panel();
   java.awt.Button button0 = new java.awt.Button();
   java.awt.Button button1 = new java.awt.Button();
   java.awt.Button button2 = new java.awt.Button();
   java.awt.Button button3 = new java.awt.Button();
   java.awt.Button button4 = new java.awt.Button();
   //}}

   class SymWindow extends java.awt.event.WindowAdapter
   {
      public void windowActivated(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == AlertDialog.this)
            AlertDialog_WindowActivated(event);
      }

      public void windowOpened(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == AlertDialog.this)
            AlertDialog_WindowOpened(event);
      }

      public void windowClosing(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == AlertDialog.this)
            AlertDialog_WindowClosing(event);
      }
   }
   
   void AlertDialog_WindowClosing(java.awt.event.WindowEvent event)
   {
      setVisible(false);
   }

   void AlertDialog_WindowOpened(java.awt.event.WindowEvent event)
   {
      if (setInitialFocus) {
         button0.requestFocus();
         setInitialFocus = false;
      }
   }

   void AlertDialog_WindowActivated(java.awt.event.WindowEvent event)
   {
      if (setInitialFocus) {
         button0.requestFocus();
         setInitialFocus = false;
      }
   }

   class SymAction implements java.awt.event.ActionListener
   {
      public void actionPerformed(java.awt.event.ActionEvent event)
      {
         Object object = event.getSource();
         if (object == button0)
            button0_ActionPerformed(event);
         else if (object == button1)
            button1_ActionPerformed(event);
         else if (object == button2)
            button2_ActionPerformed(event);
         else if (object == button3)
            button3_ActionPerformed(event);
         else if (object == button4)
            button4_ActionPerformed(event);
      }
   }

   void button0_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      selectedOption = 0;
   }

   void button1_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      selectedOption = 1;
   }

   void button2_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      selectedOption = 2;
   }

   void button3_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      selectedOption = 3;
   }

   void button4_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      selectedOption = 4;
   }

   class SymKey extends java.awt.event.KeyAdapter
   {
      public void keyPressed(java.awt.event.KeyEvent event)
      {
         Object object = event.getSource();
         if (object == AlertDialog.this)
            AlertDialog_KeyPressed(event);
         else if (object == button0)
            button0_KeyPressed(event);
         else if (object == button1)
            button1_KeyPressed(event);
         else if (object == button2)
            button2_KeyPressed(event);
         else if (object == button3)
            button3_KeyPressed(event);
         else if (object == button4)
            button4_KeyPressed(event);
      }
   }

   void AlertDialog_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
      }
   }

   void button0_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         selectedOption = 0;
         setVisible(false);
      }
   }

   void button1_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         selectedOption = 1;
         setVisible(false);
      }
   }

   void button2_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         selectedOption = 2;
         setVisible(false);
      }
   }

   void button3_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         selectedOption = 3;
         setVisible(false);
      }
   }

   void button4_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         selectedOption = 4;
         setVisible(false);
      }
   }
}
