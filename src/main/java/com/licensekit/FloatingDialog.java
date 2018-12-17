package com.licensekit;
import java.awt.*;
import java.util.*;

public class FloatingDialog extends Dialog implements FloatingLicenseAddHostDelegate
{
   java.awt.Panel topPanel;
   ImagePanel imagePanel;
   java.awt.Label titleLabel;
   java.awt.Panel midPanel;
   java.awt.TextArea hostsTextArea;
   java.awt.Panel bottomPanel;
   WrappingLabel msgLabel;
   java.awt.Button demoButton;
   java.awt.Button quitButton;

   Frame parent;
   boolean cancelled = true;
   boolean firstHostname = true;
   boolean showDemoButton = false;
   
   
   public FloatingDialog()
   {
      this(new Frame(), false);
   }
   
   public FloatingDialog(Frame aFrame, boolean modal)
   {
      super(aFrame, modal);
      parent = aFrame;
      
      topPanel = new java.awt.Panel();
      imagePanel = new ImagePanel();
      titleLabel = new java.awt.Label();
      midPanel = new java.awt.Panel();
      hostsTextArea = new java.awt.TextArea("",1,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
      if (modal) {
         bottomPanel = new java.awt.Panel();
         msgLabel = new WrappingLabel();
         demoButton = new java.awt.Button();
         quitButton = new java.awt.Button();
         showDemoButton = LicenseHandler.isDemoModeEnabled();
      }
      
      setVisible(false);
      setLayout(new BorderLayout(0,0));
      setBackground(java.awt.Color.lightGray);
      setSize(400, (modal) ? 230 : 160);
      
      topPanel.setLayout(new GridBagLayout());
      add("North", topPanel);
      try {
         imagePanel.setImageURL(getClass().getResource("appIcon.gif"));
         imagePanel.setStyle(ImagePanel.IMAGE_NORMAL);
         imagePanel.setLayout(null);
         imagePanel.setSize(32,32);
      } catch(Exception e) {
      }
      topPanel.add(imagePanel, new GridBagConstraintsD(0,0,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.NONE,new Insets(0,16,0,0),0,0));
      titleLabel.setText("Checking Network...");
      titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
      topPanel.add(titleLabel, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,8,16),0,0));

      midPanel.setLayout(new GridBagLayout());
      add("Center", midPanel);
      hostsTextArea.setEditable(false);
      hostsTextArea.setSize(368,66);
      hostsTextArea.setFont(new Font("Dialog", Font.PLAIN, 12));
      midPanel.add(hostsTextArea, new GridBagConstraintsD(0,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(8,16,16,16),0,32));
      
      if (modal) {
         bottomPanel.setLayout(new GridBagLayout());
         add("South", bottomPanel);
         msgLabel.setEnabled(false);
         bottomPanel.add(msgLabel, new GridBagConstraintsD(0,0,2,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(0,20,8,12),0,0));
         msgLabel.setFont(new Font("Dialog", Font.BOLD, 12));
         msgLabel.setSize(368,20);
         if (showDemoButton) {
            demoButton.setLabel("Demo");
            bottomPanel.add(demoButton, new GridBagConstraintsD(0,1,1,1,1.0,0.0,java.awt.GridBagConstraints.EAST,java.awt.GridBagConstraints.NONE,new Insets(8,0,8,2),12,0));
            demoButton.setBackground(java.awt.Color.lightGray);
            quitButton.setLabel("Quit");
            bottomPanel.add(quitButton, new GridBagConstraintsD(1,1,1,1,1.0,0.0,java.awt.GridBagConstraints.WEST,java.awt.GridBagConstraints.NONE,new Insets(8,2,8,0),12,0));
            quitButton.setBackground(java.awt.Color.lightGray);
         } else {
            quitButton.setLabel("Quit");
            bottomPanel.add(quitButton, new GridBagConstraintsD(0,1,2,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.NONE,new Insets(8,2,8,0),12,0));
            quitButton.setBackground(java.awt.Color.lightGray);
         }
      }

      SymWindow aSymWindow = new SymWindow();
      this.addWindowListener(aSymWindow);
      SymKey aSymKey = new SymKey();
      this.addKeyListener(aSymKey);
      hostsTextArea.addKeyListener(aSymKey);
      if (modal) {
         SymAction lSymAction = new SymAction();
         if (showDemoButton)
            demoButton.addActionListener(lSymAction);
         quitButton.addActionListener(lSymAction);
         if (showDemoButton)
            demoButton.addKeyListener(aSymKey);
         quitButton.addKeyListener(aSymKey);
         SymFocus aSymFocus = new SymFocus();
         hostsTextArea.addFocusListener(aSymFocus);
      }
      
      java.util.ResourceBundle b = LicenseHandler.getLicenseStrings();
      titleLabel.setText(b.getString("NETWORK_TITLE"));
      if (modal) {
         if (showDemoButton)
            demoButton.setLabel(b.getString("NETWORK_DEMO"));
         quitButton.setLabel(b.getString("NETWORK_QUIT"));
      }
   }
   
   public void runNonModal ()
   {
      pack();
      
      Dimension size = getSize();
      if (size.width < 400)
         setSize(size.width = 400, size.height);
      
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - size.width) / 2;
      int y = (screenSize.height - size.height) / 3;
      setLocation(x, y);
      
      hostsTextArea.setText("");
      firstHostname = true;
      
      setVisible(true);
   }
   
   private void interruptNonModal()
   {
      ResourceBundle b = LicenseHandler.getLicenseStrings();
      
      String[] options = {
         b.getString("NETWORK_NM_QUIT"),
         b.getString("NETWORK_NM_CONTINUE")
      };
      if (AlertDialog.runModalDialog(LicenseHandler.getAppName(),
         b.getString("NETWORK_NM_MSG"),
         AlertDialog.WARNING_MESSAGE,
         options) == 0) {
         try {
            LicenseHandlerExitDelegate ed = LicenseHandler.getExitDelegate();
            if (ed != null)
               ed.exitApplication();
         } catch (Exception e) {
         }
         System.exit(1);
      }
   }
   
   public void addHost (String aHostname)
   {
      if (firstHostname)
         firstHostname = false;
      else
         hostsTextArea.append(" ...");
      
      hostsTextArea.append(aHostname);
      
      show();
   }

   public void runModal (String msgText)
   {
      FloatingDialog mfd = new FloatingDialog(parent, true);
      
      mfd.setTitle(getTitle());
      mfd.setLocation(getLocation());
      
      cancelled = mfd.cancelled = true;

      mfd.hostsTextArea.append(hostsTextArea.getText());
      mfd.msgLabel.setText(msgText);
      
      mfd.pack();
      
      Dimension msg_size = mfd.msgLabel.getSize();
      int height_add = 0;
      while (!mfd.msgLabel.textFits(msg_size.width, msg_size.height + height_add))
         height_add += 2;
      
      Dimension size = mfd.getSize();
      mfd.setSize(size.width, size.height + height_add);
      mfd.msgLabel.setSize(msg_size.width, msg_size.height + height_add);
      
      mfd.pack();
      size = mfd.getSize();
      if (size.width < 400)
         mfd.setSize(size.width = 400, size.height);
      
      setVisible(false);
      java.awt.Toolkit.getDefaultToolkit().beep();
      mfd.setVisible(true);
      
      cancelled = mfd.cancelled;
      
      mfd.setVisible(false);
      mfd.dispose();
   }
   
   public boolean wasCancelled ()
   {
      return cancelled;
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


   class SymWindow extends java.awt.event.WindowAdapter
   {
      public void windowClosing(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == FloatingDialog.this)
            FloatingDialog_WindowClosing(event);
      }
   }
   
   void FloatingDialog_WindowClosing(java.awt.event.WindowEvent event)
   {
      if (isModal())
         setVisible(false);
      else
         interruptNonModal();
   }

   class SymFocus extends java.awt.event.FocusAdapter
   {
      public void focusGained(java.awt.event.FocusEvent event)
      {
         Object object = event.getSource();
         if (object == hostsTextArea)
            hostsTextArea_FocusGained(event);
      }
   }

   void hostsTextArea_FocusGained(java.awt.event.FocusEvent event)
   {
      if (showDemoButton)
         demoButton.requestFocus();
      else if (quitButton != null)
         quitButton.requestFocus();
   }

   class SymAction implements java.awt.event.ActionListener
   {
      public void actionPerformed(java.awt.event.ActionEvent event)
      {
         Object object = event.getSource();
         if (object == demoButton)
            demoButton_ActionPerformed(event);
         else if (object == quitButton)
            quitButton_ActionPerformed(event);
      }
   }

   void demoButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      cancelled = false;
   }

   void quitButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
   }

   class SymKey extends java.awt.event.KeyAdapter
   {
      public void keyPressed(java.awt.event.KeyEvent event)
      {
         Object object = event.getSource();
         if (object == demoButton)
            demoButton_KeyPressed(event);
         else if (object == quitButton)
            quitButton_KeyPressed(event);
         else if (object == hostsTextArea)
            hostsTextArea_KeyPressed(event);
      }
   }

   void demoButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         cancelled = false;
         setVisible(false);
      }
   }

   void quitButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         setVisible(false);
      }
   }

   void hostsTextArea_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         if (isModal())
            setVisible(false);
         else
            interruptNonModal();
      }
   }
}
