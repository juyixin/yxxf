package com.licensekit;
import java.awt.*;

public class LicenseDialog extends Dialog
{
   boolean oked = true;
   
   public LicenseDialog(Frame parent)
   {
      super(parent, true);

      //{{INIT_CONTROLS
      setVisible(false);
      setLayout(new BorderLayout(0,0));
      setBackground(java.awt.Color.lightGray);
      setSize(400,360);
      bottomPanel.setLayout(new GridBagLayout());
      add("South", bottomPanel);
      okButton.setLabel("OK");
      bottomPanel.add(okButton, new GridBagConstraintsD(0,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,16,16),0,0));
      relicenseButton.setLabel("Re-license Application");
      bottomPanel.add(relicenseButton, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,0,16,16),0,0));
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
      titleLabel.setText("License");
      topPanel.add(titleLabel, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,8,16),0,0));
      titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
      midPanel.setLayout(new BorderLayout(0,0));
      add("Center", midPanel);
      midTopPanel.setLayout(new GridBagLayout());
      midPanel.add("North", midTopPanel);
      userIDTitle.setText("Serial Number:");
      userIDTitle.setAlignment(java.awt.Label.RIGHT);
      midTopPanel.add(userIDTitle, new GridBagConstraintsD(0,0,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,0,8),8,0));
      userIDTitle.setFont(new Font("Dialog", Font.BOLD, 12));
      userIDField.setEditable(false);
      userIDField.setText("cq0037");
      midTopPanel.add(userIDField, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.WEST,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,0,8),12,0));
      hostIDTitle.setText("Host ID:");
      hostIDTitle.setAlignment(java.awt.Label.RIGHT);
      midTopPanel.add(hostIDTitle, new GridBagConstraintsD(2,0,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,8,0,8),12,0));
      hostIDTitle.setFont(new Font("Dialog", Font.BOLD, 12));
      hostIDField.setEditable(false);
      hostIDField.setText("PR2315");
      midTopPanel.add(hostIDField, new GridBagConstraintsD(3,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,0,16),12,0));
      ownerTitle.setText("License Owner:");
      ownerTitle.setAlignment(java.awt.Label.RIGHT);
      midTopPanel.add(ownerTitle, new GridBagConstraintsD(0,1,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,0,8),8,0));
      ownerTitle.setFont(new Font("Dialog", Font.BOLD, 12));
      ownerField.setEditable(false);
      ownerField.setText("ACME Inc.");
      midTopPanel.add(ownerField, new GridBagConstraintsD(1,1,3,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,0,16),12,0));
      typeTitle.setText("License Type:");
      typeTitle.setAlignment(java.awt.Label.RIGHT);
      midTopPanel.add(typeTitle, new GridBagConstraintsD(0,2,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,16,8),8,0));
      typeTitle.setFont(new Font("Dialog", Font.BOLD, 12));
      typeField.setEditable(false);
      typeField.setText("Network License for 5 Users");
      midTopPanel.add(typeField, new GridBagConstraintsD(1,2,3,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(6,0,16,16),12,0));
      midMidPanel.setLayout(new GridBagLayout());
      midPanel.add("Center", midMidPanel);
      modulesTitle.setEditable(false);
      modulesTitle.setText(" Module                Licensed   Expires");
      midMidPanel.add(modulesTitle, new GridBagConstraintsD(0,0,1,1,0.0,0.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(0,16,0,16),0,0));
      modulesTitle.setBackground(java.awt.Color.gray);
      modulesTitle.setForeground(java.awt.Color.white);
      modulesTitle.setFont(new Font("MonoSpaced", Font.PLAIN, 12));
      midMidPanel.add(modulesScrollPane, new GridBagConstraintsD(0,1,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(0,16,0,16),0,0));
      modulesScrollPane.setBounds(16,23,368,117);
      modulesPanel.setLayout(new GridLayout(6,1,0,0));
      modulesScrollPane.add(modulesPanel);
      //}}

      //{{REGISTER_LISTENERS
      SymWindow aSymWindow = new SymWindow();
      this.addWindowListener(aSymWindow);
      SymAction lSymAction = new SymAction();
      relicenseButton.addActionListener(lSymAction);
      okButton.addActionListener(lSymAction);
      SymKey aSymKey = new SymKey();
      relicenseButton.addKeyListener(aSymKey);
      okButton.addKeyListener(aSymKey);
      //}}
      
      java.util.ResourceBundle b = LicenseHandler.getLicenseStrings();
      titleLabel.setText(b.getString("LICENSE_TITLE"));
      userIDTitle.setText(b.getString("LICENSE_SERIAL"));
      hostIDTitle.setText(b.getString("LICENSE_HOST_ID"));
      ownerTitle.setText(b.getString("LICENSE_OWNER"));
      typeTitle.setText(b.getString("LICENSE_TYPE"));
      okButton.setLabel(b.getString("LICENSE_OK"));
      relicenseButton.setLabel(b.getString("LICENSE_RELICENSE"));
      
      if (!LicenseHandler.isRegistrationEnabled()) {
         relicenseButton.setEnabled(false);
         bottomPanel.remove(relicenseButton);
      }
   }
   
   public LicenseDialog()
   {
      this(new Frame());
   }

   public void runModal ()
   {
      pack();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - getSize().width) / 2;
      int y = (int)((screenSize.height - getSize().height) / 2.5);
      setLocation(x, y);
      
      oked = true;
      
      setVisible(true);
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
   java.awt.Panel bottomPanel = new java.awt.Panel();
   java.awt.Button okButton = new java.awt.Button();
   java.awt.Button relicenseButton = new java.awt.Button();
   java.awt.Panel topPanel = new java.awt.Panel();
   ImagePanel imagePanel = new ImagePanel();
   java.awt.Label titleLabel = new java.awt.Label();
   java.awt.Panel midPanel = new java.awt.Panel();
   java.awt.Panel midTopPanel = new java.awt.Panel();
   java.awt.Label userIDTitle = new java.awt.Label();
   java.awt.TextField userIDField = new java.awt.TextField();
   java.awt.Label hostIDTitle = new java.awt.Label();
   java.awt.TextField hostIDField = new java.awt.TextField();
   java.awt.Label ownerTitle = new java.awt.Label();
   java.awt.TextField ownerField = new java.awt.TextField();
   java.awt.Label typeTitle = new java.awt.Label();
   java.awt.TextField typeField = new java.awt.TextField();
   java.awt.Panel midMidPanel = new java.awt.Panel();
   java.awt.TextField modulesTitle = new java.awt.TextField();
   java.awt.ScrollPane modulesScrollPane = new java.awt.ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
   java.awt.Panel modulesPanel = new java.awt.Panel();
   //}}

   class SymWindow extends java.awt.event.WindowAdapter
   {
      public void windowActivated(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == LicenseDialog.this)
            LicenseDialog_WindowActivated(event);
      }

      public void windowOpened(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == LicenseDialog.this)
            LicenseDialog_WindowOpened(event);
      }

      public void windowClosing(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == LicenseDialog.this)
            LicenseDialog_WindowClosing(event);
      }
   }
   
   void LicenseDialog_WindowClosing(java.awt.event.WindowEvent event)
   {
      setVisible(false);
   }

   class SymAction implements java.awt.event.ActionListener
   {
      public void actionPerformed(java.awt.event.ActionEvent event)
      {
         Object object = event.getSource();
         if (object == relicenseButton)
            relicenseButton_ActionPerformed(event);
         else if (object == okButton)
            okButton_ActionPerformed(event);
      }
   }

   void relicenseButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
      oked = false;
   }

   void okButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
   }
   
   public void setUserID (String aString)
   {
      userIDField.setText(aString);
   }
   
   public void setHostID (String aString)
   {
      hostIDField.setText(aString);
   }
   
   public void setOwner (String aString)
   {
      ownerField.setText(aString);
   }
   
   public void setType (String aString)
   {
      typeField.setText(aString);
   }
   
   public void setModules (String[] moduleArray, String[] licensedArray, String[] expiresArray)
   {
      int count = moduleArray.length;
      if (count > licensedArray.length)
         count = licensedArray.length;
      if (count > expiresArray.length)
         count = expiresArray.length;
      
      java.util.ResourceBundle b = LicenseHandler.getLicenseStrings();
      int pre_module_spaces = 1;
      String module_header = b.getString("COL_MODULE");
      int pre_licensed_spaces = 3;
      String licensed_header = b.getString("COL_MODE");
      int pre_expires_spaces = 3;
      String expires_header = b.getString("COL_EXPDATE");
      
      int module_width = module_header.length();
      int licensed_width = licensed_header.length();
      int i;
      
      for (i = 0; i < count; i++) {
         if (module_width < moduleArray[i].length())
            module_width = moduleArray[i].length();
         if (licensed_width < licensedArray[i].length())
            licensed_width = licensedArray[i].length();
      }
      
      String spaces = "                                                                                                        ";
      
      String string = spaces.substring(0, pre_module_spaces)
         + module_header + spaces.substring(0, module_width - module_header.length())
         + spaces.substring(0, pre_licensed_spaces)
         + licensed_header + spaces.substring(0, licensed_width - licensed_header.length())
         + spaces.substring(0, pre_expires_spaces)
         + expires_header;
      
      modulesTitle.setText(string);
      
      Font label_font = new Font("MonoSpaced", Font.PLAIN, 12);
      
      for (i = 0; i < count; i++) {
         string = spaces.substring(0, pre_module_spaces)
            + moduleArray[i] + spaces.substring(0, module_width - moduleArray[i].length())
            + spaces.substring(0, pre_licensed_spaces)
            + licensedArray[i] + spaces.substring(0, licensed_width - licensedArray[i].length())
            + spaces.substring(0, pre_expires_spaces)
            + expiresArray[i];

         java.awt.Label label = new java.awt.Label();
         label.setText(string);
         modulesPanel.add(label);
         label.setFont(label_font);
         //label.setBounds(0, 23 * i, 348, 23);
      }
   }
   
   public boolean wasOKed ()
   {
      return oked;
   }


   void LicenseDialog_WindowOpened(java.awt.event.WindowEvent event)
   {
      okButton.requestFocus();
   }

   void LicenseDialog_WindowActivated(java.awt.event.WindowEvent event)
   {
      okButton.requestFocus();
   }

   class SymKey extends java.awt.event.KeyAdapter
   {
      public void keyPressed(java.awt.event.KeyEvent event)
      {
         Object object = event.getSource();
         if (object == relicenseButton)
            relicenseButton_KeyPressed(event);
         else if (object == okButton)
            okButton_KeyPressed(event);
      }
   }

   void relicenseButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         oked = false;
         setVisible(false);
      }
   }

   void okButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         setVisible(false);
      }
   }
}
