package com.licensekit;
import java.awt.*;

public class RegisterDialog extends Dialog
{
   boolean cancelled = true;
   
   public RegisterDialog(Frame parent)
   {
      super(parent, true);

      //{{INIT_CONTROLS
      setVisible(false);
      setLayout(new BorderLayout(0,0));
      setBackground(java.awt.Color.lightGray);
      setSize(486,188);
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
      titleLabel.setText("Registration");
      topPanel.add(titleLabel, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,8,16),0,0));
      titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
      midPanel.setLayout(new GridBagLayout());
      add("Center", midPanel);
      keyLabel.setText("License String:");
      keyLabel.setAlignment(java.awt.Label.RIGHT);
      midPanel.add(keyLabel, new GridBagConstraintsD(0,0,1,1,0.0,1.0,java.awt.GridBagConstraints.EAST,java.awt.GridBagConstraints.NONE,new Insets(0,0,0,8),4,0));
      keyField.setText("XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX");
      midPanel.add(keyField, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(0,0,0,16),12,0));
      keyField.setFont(new Font("DialogInput", Font.PLAIN, 12));
      nameLabel.setText("Name or Organisation:");
      nameLabel.setAlignment(java.awt.Label.RIGHT);
      midPanel.add(nameLabel, new GridBagConstraintsD(0,1,1,1,0.0,1.0,java.awt.GridBagConstraints.EAST,java.awt.GridBagConstraints.NONE,new Insets(4,0,0,8),4,0));
      nameField.setText("ACME Inc.");
      midPanel.add(nameField, new GridBagConstraintsD(1,1,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.HORIZONTAL,new Insets(4,0,0,16),12,0));
      nameField.setFont(new Font("DialogInput", Font.PLAIN, 12));
      bottomPanel.setLayout(new GridBagLayout());
      add("South", bottomPanel);
      registerButton.setLabel("Register Application");
      bottomPanel.add(registerButton, new GridBagConstraintsD(0,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,16,16,16),0,0));
      registerButton.setBackground(java.awt.Color.lightGray);
      cancelButton.setLabel("Cancel");
      bottomPanel.add(cancelButton, new GridBagConstraintsD(1,0,1,1,1.0,1.0,java.awt.GridBagConstraints.CENTER,java.awt.GridBagConstraints.BOTH,new Insets(16,0,16,16),0,0));
      cancelButton.setBackground(java.awt.Color.lightGray);
      //}}

      //{{REGISTER_LISTENERS
      SymWindow aSymWindow = new SymWindow();
      this.addWindowListener(aSymWindow);
      SymFocus aSymFocus = new SymFocus();
      keyField.addFocusListener(aSymFocus);
      nameField.addFocusListener(aSymFocus);
      SymAction lSymAction = new SymAction();
      registerButton.addActionListener(lSymAction);
      cancelButton.addActionListener(lSymAction);
      SymKey aSymKey = new SymKey();
      registerButton.addKeyListener(aSymKey);
      cancelButton.addKeyListener(aSymKey);
      this.addKeyListener(aSymKey);
      keyField.addKeyListener(aSymKey);
      nameField.addKeyListener(aSymKey);
      //}}
      
      java.util.ResourceBundle b = LicenseHandler.getLicenseStrings();
      titleLabel.setText(b.getString("REGISTER_TITLE"));
      keyLabel.setText(b.getString("REGISTER_KEY"));
      nameLabel.setText(b.getString("REGISTER_NAME"));
      registerButton.setLabel(b.getString("REGISTER_REGISTER"));
      cancelButton.setLabel(b.getString("REGISTER_CANCEL"));
   }
   
   public RegisterDialog()
   {
      this(new Frame());
   }

   public void runModal ()
   {
      String key = getKey();
      setKey("XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX");
      pack();
      setKey(key);
      
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - getSize().width) / 2;
      int y = (int)((screenSize.height - getSize().height) / 2.5);
      setLocation(x, y);
      
      cancelled = true;
      
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
   java.awt.Panel topPanel = new java.awt.Panel();
   ImagePanel imagePanel = new ImagePanel();
   java.awt.Label titleLabel = new java.awt.Label();
   java.awt.Panel midPanel = new java.awt.Panel();
   java.awt.Label keyLabel = new java.awt.Label();
   java.awt.TextField keyField = new java.awt.TextField();
   java.awt.Label nameLabel = new java.awt.Label();
   java.awt.TextField nameField = new java.awt.TextField();
   java.awt.Panel bottomPanel = new java.awt.Panel();
   java.awt.Button registerButton = new java.awt.Button();
   java.awt.Button cancelButton = new java.awt.Button();
   //}}

   class SymWindow extends java.awt.event.WindowAdapter
   {
      public void windowOpened(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == RegisterDialog.this)
            RegisterDialog_WindowOpened(event);
      }

      public void windowActivated(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == RegisterDialog.this)
            RegisterDialog_WindowActivated(event);
      }

      public void windowClosing(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == RegisterDialog.this)
            RegisterDialog_WindowClosing(event);
      }
   }
   
   void RegisterDialog_WindowClosing(java.awt.event.WindowEvent event)
   {
      setVisible(false);
   }

   void RegisterDialog_WindowActivated(java.awt.event.WindowEvent event)
   {
      keyField.requestFocus();
   }

   void RegisterDialog_WindowOpened(java.awt.event.WindowEvent event)
   {
      keyField.requestFocus();
   }

   class SymFocus extends java.awt.event.FocusAdapter
   {
      public void focusLost(java.awt.event.FocusEvent event)
      {
         Object object = event.getSource();
         if (object == keyField)
            keyField_FocusLost(event);
         else if (object == nameField)
            nameField_FocusLost(event);
      }

      public void focusGained(java.awt.event.FocusEvent event)
      {
         Object object = event.getSource();
         if (object == keyField)
            keyField_FocusGained(event);
         else if (object == nameField)
            nameField_FocusGained(event);
      }
   }

   void keyField_FocusGained(java.awt.event.FocusEvent event)
   {
      keyField.selectAll();
   }

   void nameField_FocusGained(java.awt.event.FocusEvent event)
   {
      nameField.selectAll();
   }

   void keyField_FocusLost(java.awt.event.FocusEvent event)
   {
      keyField.select(0,0);
   }

   void nameField_FocusLost(java.awt.event.FocusEvent event)
   {
      nameField.select(0,0);
   }
   
   public String getKey ()
   {
      return keyField.getText();
   }
   
   public void setKey (String aString)
   {
      keyField.setText(aString);
   }
   
   public String getName ()
   {
      return nameField.getText();
   }
   
   public void setName (String aString)
   {
      nameField.setText(aString);
   }
   
   public boolean wasCancelled ()
   {
      return cancelled;
   }
   
   // action handling

   class SymAction implements java.awt.event.ActionListener
   {
      public void actionPerformed(java.awt.event.ActionEvent event)
      {
         Object object = event.getSource();
         if (object == registerButton)
            registerButton_ActionPerformed(event);
         else if (object == cancelButton)
            cancelButton_ActionPerformed(event);
      }
   }

   void registerButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      cancelled = false;
      setVisible(false);
   }

   void cancelButton_ActionPerformed(java.awt.event.ActionEvent event)
   {
      setVisible(false);
   }
   
   // <Return> and <Esc> key handling

   class SymKey extends java.awt.event.KeyAdapter
   {
      public void keyPressed(java.awt.event.KeyEvent event)
      {
         Object object = event.getSource();
         if (object == registerButton)
            registerButton_KeyPressed(event);
         else if (object == cancelButton)
            cancelButton_KeyPressed(event);
         else if (object == keyField)
            keyField_KeyPressed(event);
         else if (object == nameField)
            nameField_KeyPressed(event);
      }
   }

   void registerButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         cancelled = false;
         setVisible(false);
      }
   }

   void cancelButton_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         setVisible(false);
      }
   }

   void keyField_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         nameField.requestFocus();
      }
   }

   void nameField_KeyPressed(java.awt.event.KeyEvent event)
   {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
         setVisible(false);
         
      } else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         registerButton.requestFocus();
      }
   }
}
