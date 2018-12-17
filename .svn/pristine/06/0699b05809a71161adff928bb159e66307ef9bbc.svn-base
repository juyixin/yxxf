package com.licensekit;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;

public class SampleApp extends Frame implements LicenseHandlerExitDelegate, ClipboardOwner
{
   private static String appName = "SampleApp";
   private static ResourceBundle appStrings = ResourceBundle.getBundle("LicenseKitAppStrings", Locale.getDefault());

   private boolean doEnableMenuItem;   // to be set before calling enableMenuItem(anItem)
   
   @Autowired
	private LicenseHandler licenseHandler;
   
   void enableMenuItem (Object aMenuItem)
   // replaco for "aMenuItem.setEnabled(this.doEnableMenuItem);"
   {
      try {
         char c0='s', c9='d', c1='e', c8='e', c2='t', c7='l', c3='E', c6='b', c4='n', c5='a';
         String s=""+c0+c1+c2+c3+c4+c5+c6+c7+c8+c9;
         Method m = aMenuItem.getClass().getMethod(s, new Class[] { boolean.class } );
         m.invoke(aMenuItem, new Object[] { new Boolean(doEnableMenuItem) } );
      }
      catch (Exception e) {
         System.exit(1);
      }
   }
   
   void openLicenseDialog ()
   {
      if (LicenseHandler.showLicenseDialog())
         reflectLicensePermissions();
   }
   
   void openRegisterDialog ()
   {
      if (LicenseHandler.showRegisterDialog()) {
         reflectLicensePermissions();
         openLicenseDialog();
      }
   }
   
   void reflectLicensePermissions ()
   {
      doEnableMenuItem = (LicenseHandler.permissionOfModule(0) == LicenseHandler.PERMISSION_YES);
      enableMenuItem(saveMenuItem);
      doEnableMenuItem = (LicenseHandler.permissionOfModule0 == LicenseHandler.PERMISSION_YES);
      enableMenuItem(saveAsMenuItem);
      doEnableMenuItem = (LicenseHandler.permissionOfModule1 != LicenseHandler.PERMISSION_NO);
      enableMenuItem(importGraphicsMenuItem);
   }

   public SampleApp() throws Exception
   {
      LicenseHandler.setExitDelegate(this);
      LicenseHandler.setAppName(appName);
      LicenseHandler.setModuleNames(null);
      LicenseHandler.setRequiredVersion(0);
      
      //LicenseHandler.setUIEnabled(false);
      //LicenseHandler.setDemoModeEnabled(false);
   
      licenseHandler.checkLicenseInFolder(new File(System.getProperty("user.dir"), ".license"));
      //LicenseHandler.checkLicenseString("2|BG-&^M}-eb{T-8$&x-4X8@-y|NJ-JWA$-0|oA");
      //LicenseHandler.checkLicenseStringAndUserName("T$jG-hUp7-eIP[-8h&w-@XGF-wkw|-JYAr-V0em", "ACME Sales Dept.");

      reflectLicensePermissions();
         
      //{{INIT_CONTROLS
      setLayout(new GridLayout(1,1,0,0));
      setSize(640,480);
      setVisible(false);
      textArea.setEditable(false);
      textArea.setFont(new Font("MonoSpaced", Font.PLAIN, 12));
      textArea.setText(appStrings.getString("INTRO_TEXT"));
      add(textArea);
      setTitle("SampleApp");
      //}}
      
      //{{INIT_MENUS
      menu1.setLabel(appStrings.getString("FILE"));
      menu1.add(newMenuItem);
      newMenuItem.setLabel(appStrings.getString("NEW"));
      newMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_N,false));
      menu1.add(openMenuItem);
      openMenuItem.setLabel(appStrings.getString("OPEN"));
      openMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_O,false));
      menu1.add(saveMenuItem);
      saveMenuItem.setLabel(appStrings.getString("SAVE"));
      saveMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_S,false));
      menu1.add(saveAsMenuItem);
      saveAsMenuItem.setLabel(appStrings.getString("SAVE_AS"));
      menu1.add(new java.awt.MenuItem("-"));
      menu1.add(printMenuItem);
      printMenuItem.setLabel(appStrings.getString("PRINT"));
      printMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_P,false));
      menu1.add(new java.awt.MenuItem("-"));
      menu1.add(licenseMenuItem);
      licenseMenuItem.setLabel(appStrings.getString("LICENSE"));
      menu1.add(new java.awt.MenuItem("-"));
      menu1.add(exitMenuItem);
      exitMenuItem.setLabel(appStrings.getString("EXIT"));
      mainMenuBar.add(menu1);
      
      menu2.setLabel(appStrings.getString("EDIT"));
      menu2.add(cutMenuItem);
      cutMenuItem.setLabel(appStrings.getString("CUT"));
      cutMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_X,false));
      menu2.add(copyMenuItem);
      copyMenuItem.setLabel(appStrings.getString("COPY"));
      copyMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_C,false));
      menu2.add(pasteMenuItem);
      pasteMenuItem.setLabel(appStrings.getString("PASTE"));
      pasteMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_V,false));
      menu2.add(new java.awt.MenuItem("-"));
      menu2.add(importGraphicsMenuItem);
      importGraphicsMenuItem.setLabel(appStrings.getString("IMPORT_GRAPHICS"));
      importGraphicsMenuItem.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_G,false));
      mainMenuBar.add(menu2);
      
      menu3.setLabel(appStrings.getString("HELP"));
      menu3.add(aboutMenuItem);
      aboutMenuItem.setLabel(appStrings.getString("ABOUT"));
      mainMenuBar.add(menu3);
      //$$ mainMenuBar.move(0,312);
      setMenuBar(mainMenuBar);
      //}}
      
      //{{REGISTER_LISTENERS
      SymAction lSymAction = new SymAction();
      aboutMenuItem.addActionListener(lSymAction);
      exitMenuItem.addActionListener(lSymAction);
      SymWindow aSymWindow = new SymWindow();
      this.addWindowListener(aSymWindow);
      licenseMenuItem.addActionListener(lSymAction);
      importGraphicsMenuItem.addActionListener(lSymAction);
      saveMenuItem.addActionListener(lSymAction);
      saveAsMenuItem.addActionListener(lSymAction);
      printMenuItem.addActionListener(lSymAction);
      newMenuItem.addActionListener(lSymAction);
      openMenuItem.addActionListener(lSymAction);
      cutMenuItem.addActionListener(lSymAction);
      copyMenuItem.addActionListener(lSymAction);
      pasteMenuItem.addActionListener(lSymAction);
      //}}

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - getSize().width) / 2;
      int y = (int)((screenSize.height - getSize().height) / 2.5);
      setLocation(x, y);
      
      setVisible(true);
   }

   public void exitApplication()
   {
      try {
         setVisible(false);    // hide the Frame
         dispose();            // free the system resources
         System.exit(0);       // close the application
      } catch (Exception e) {
      }
      System.exit(1);
   }
   
   static public void main(String args[])
   {
      try {
         new SampleApp();
      }
      catch (Throwable t) {
         System.err.println(t);
         t.printStackTrace();
         System.exit(1);
      }
   }
   
   public void addNotify()
   {
      // Record the size of the window prior to calling parents addNotify.
      Dimension d = getSize();
      
      super.addNotify();
   
      if (fComponentsAdjusted)
         return;
   
      // Adjust components according to the insets
      setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
      Component components[] = getComponents();
      for (int i = 0; i < components.length; i++)
      {
         Point p = components[i].getLocation();
         p.translate(getInsets().left, getInsets().top);
         components[i].setLocation(p);
      }
      fComponentsAdjusted = true;
   }
   
   // Used for addNotify check.
   boolean fComponentsAdjusted = false;
   
   //{{DECLARE_CONTROLS
   java.awt.TextArea textArea = new java.awt.TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
   //}}
   
   //{{DECLARE_MENUS
   java.awt.MenuBar mainMenuBar = new java.awt.MenuBar();
   java.awt.Menu menu1 = new java.awt.Menu();
   java.awt.MenuItem newMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem openMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem saveMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem saveAsMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem printMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem licenseMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem exitMenuItem = new java.awt.MenuItem();
   java.awt.Menu menu2 = new java.awt.Menu();
   java.awt.MenuItem cutMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem copyMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem pasteMenuItem = new java.awt.MenuItem();
   java.awt.MenuItem importGraphicsMenuItem = new java.awt.MenuItem();
   java.awt.Menu menu3 = new java.awt.Menu();
   java.awt.MenuItem aboutMenuItem = new java.awt.MenuItem();
   //}}
   
   class SymAction implements java.awt.event.ActionListener
   {
      public void actionPerformed(java.awt.event.ActionEvent event)
      {
         Object object = event.getSource();
         if (object == aboutMenuItem)
            aboutMenuItem_ActionPerformed(event);
         else if (object == exitMenuItem)
            exitMenuItem_ActionPerformed(event);
         else if (object == licenseMenuItem)
            licenseMenuItem_ActionPerformed(event);
         else if (object == importGraphicsMenuItem)
            importGraphicsMenuItem_ActionPerformed(event);
         else if (object == saveMenuItem)
            saveMenuItem_ActionPerformed(event);
         else if (object == saveAsMenuItem)
            saveAsMenuItem_ActionPerformed(event);
         else if (object == printMenuItem)
            printMenuItem_ActionPerformed(event);
         else if (object == newMenuItem)
            newMenuItem_ActionPerformed(event);
         else if (object == openMenuItem)
            openMenuItem_ActionPerformed(event);
         else if (object == cutMenuItem)
            cutMenuItem_ActionPerformed(event);
         else if (object == copyMenuItem)
            copyMenuItem_ActionPerformed(event);
         else if (object == pasteMenuItem)
            pasteMenuItem_ActionPerformed(event);
      }
   }

   void aboutMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      try {
         (new SampleAppAboutDialog(this, true)).setVisible(true);
      } catch (Exception e) {
      }
   }

   void exitMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      exitApplication();
   }

   class SymWindow extends java.awt.event.WindowAdapter
   {
      public void windowClosing(java.awt.event.WindowEvent event)
      {
         Object object = event.getSource();
         if (object == SampleApp.this)
            SampleApp_WindowClosing(event);
      }
   }

   void SampleApp_WindowClosing(java.awt.event.WindowEvent event)
   {
      exitApplication();
   }

   void newMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_NEW_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   void openMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_OPEN_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   void saveMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_SAVE_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   void saveAsMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_SAVE_AS_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   void printMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      if (LicenseHandler.permissionOfModule0 == LicenseHandler.PERMISSION_DEMO) {
         String[] options = { appStrings.getString("OK"), appStrings.getString("CANCEL") };
         if (AlertDialog.runModalDialog(appName,
            appStrings.getString("DO_DEMO_PRINT_MSG"),
            AlertDialog.INFORMATION_MESSAGE,
            options) != 0)
            return;
      }
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_PRINT_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   void licenseMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      this.openLicenseDialog();
   }

   void importGraphicsMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      if (LicenseHandler.permissionOfModule(1) == LicenseHandler.PERMISSION_DEMO) {
         String[] options = { appStrings.getString("OK"), appStrings.getString("CANCEL") };
         if (AlertDialog.runModalDialog(appName,
            appStrings.getString("DO_DEMO_IMPORT_GRAPHICS_MSG"),
            AlertDialog.INFORMATION_MESSAGE,
            options) != 0)
            return;
      }
      String[] options = { appStrings.getString("OK") };
      AlertDialog.runModalDialog(appName,
         appStrings.getString("DO_IMPORT_GRAPHICS_MSG"),
         AlertDialog.INFORMATION_MESSAGE,
         options);
   }

   // clipboard stuff

   void cutMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      try {
         String s = textArea.getSelectedText();
         StringSelection contents = new StringSelection(s);
         getToolkit().getSystemClipboard().setContents(contents, this);
         
//       textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
      }
      catch (Exception e) {
      }
   }

   void copyMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      try {
         String s = textArea.getSelectedText();
         StringSelection contents = new StringSelection(s);
         getToolkit().getSystemClipboard().setContents(contents, this);
      }
      catch (Exception e) {
      }
   }

   void pasteMenuItem_ActionPerformed(java.awt.event.ActionEvent event)
   {
      try {
//       Transferable t = getToolkit().getSystemClipboard().getContents(this);
//       if ((t != null) && t.isDataFlavorSupported(DataFlavor.stringFlavor))
//          textArea.replaceRange((String)t.getTransferData(DataFlavor.stringFlavor),
//             textArea.getSelectionStart(), textArea.getSelectionEnd());        
      }
      catch (Exception e) {
      }
   }

   public void lostOwnership (Clipboard clipboard, Transferable contents)
   {
   }

}

