package com.licensekit;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//import com.ca.dao.LookDAO;
import com.eazytec.bpm.model.LoggedUserDTO;
import com.eazytec.common.util.CalendarUtil;
import com.eazytec.common.util.CommonUtil;

@Service
public class LicenseHandler
{
	
 /*  private static LookDAO lookupMgr;
   
   public LookDAO getLookupMgr() {
	   return lookupMgr;
   }

   public void setLookupMgr(LookDAO lookupMgr) {
	   this.lookupMgr = lookupMgr;
   }*/
	
   public static int PERMISSION_NO = 0;
   public static int PERMISSION_DEMO = -1;
   public static int PERMISSION_YES = -2;
   
   public static int permissionOfModule0 = PERMISSION_NO;
   public static int permissionOfModule1 = PERMISSION_NO;
   public static int permissionOfModule2 = PERMISSION_NO;
   public static int permissionOfModule3 = PERMISSION_NO;
   public static int permissionOfModule4 = PERMISSION_NO;
   public static int permissionOfModule5 = PERMISSION_NO;
   
   private static int pdiff = -3;
   static {
      RandomGenerator rg = new RandomGenerator();
      
      PERMISSION_NO = (int)(rg.get() * 100000) + 1;
      do {
         PERMISSION_DEMO = (int)(rg.get() * 100000) + 1;
      } while (PERMISSION_DEMO == PERMISSION_NO);
      pdiff = (int)(rg.get() * 100000) + 1;
      PERMISSION_YES = PERMISSION_NO + PERMISSION_DEMO + pdiff;

      permissionOfModule0 = PERMISSION_NO;
      permissionOfModule1 = PERMISSION_NO;
      permissionOfModule2 = PERMISSION_NO;
      permissionOfModule3 = PERMISSION_NO;
      permissionOfModule4 = PERMISSION_NO;
      permissionOfModule5 = PERMISSION_NO;
   }

   // This copy of your 'official demo key' will be re-installed during
   // fall-back to demo mode. For a discussion of the recommended settings
   // for your official demo key, see the License Key Usage Hints.
   private static String demoLicenseString = "2MBG-&ZM9-eb4[-!8&&-tX{^-41wi-nWAJ-V}oL";
   
   private static String userName = null;

   private static LicenseDef licenseDef = new LicenseDef();

   private static File licenseFolder = null;
   private static String licenseSubfolder = ".l";
   private static String licenseFile = ".lf";
   private static String licenseCheckSubfolder = ".lc";
   private static String licenseCheckFile = ".lcf";

   private static boolean canRegister = true; //MADAN
   private static boolean canUseUI = true;
   private static boolean canDoDemo = true;

   private static String appName = "";
   
   private static LicenseHandlerExitDelegate exitDelegate = null;
   
   private static String moduleNames[] = { null, null, null, null, null, null };

   private static int moduleCount = 0;

   private static String mlModeString[] = { null, null, null, null };

   private static int reqVersionAtLeast = 0;

   private static int maxUsers = 0;

   private static boolean maxUsersChecked = false;

   private static int hostID = -1;

   private static boolean demoLicenseUsed = true;

   private static ResourceBundle b;
   
   private static int maxWorkstations=0;
   
   private static int maxReadingPoints=0;

   static {
      b = ResourceBundle.getBundle("LicenseStrings", Locale.getDefault());
      
      mlModeString[0] = b.getString("MODE_NO");
      mlModeString[1] = b.getString("MODE_YES");
      mlModeString[2] = b.getString("MODE_FREE");
      mlModeString[3] = b.getString("MODE_DEMO");
   }
   
   static ResourceBundle getLicenseStrings ()
   {
      return b;
   }
      
   // internal license string evaluation functions

private static boolean licenseStringIsValid (String aString)
   // test-decode aString, validating key syntax
   {
	   System.out.println("licenseStringIsValid license=====>"+aString);
      try {
         LicenseDef ld = new LicenseDef();
         ld.decode(aString);
         ld.getMaxUsers();
         ld.getHostID();
      } catch (Exception e) {
         return false;
      }        
      return true;
   }

   private static boolean nameIsEmpty (String aName)
   {
      int i, len = aName.length();
      
      for (i = 0; i < len; i++)
         if (!Character.isWhitespace(aName.charAt(i)))
            return false;
      
      return true;
   }
   
   private static boolean expDateDidExpire (ExpDate ed)
   {
      Calendar now = Calendar.getInstance();
      GregorianCalendar given = new GregorianCalendar(ed.year(), ed.month() - 1, ed.day());
      
      return now.after(given);
   }
   
   // internal licenseFile/licenseCheckFile read & write functions
   // (used only if canRegister)
   
   private static Vector readLicenseStringAndUserName ()
   // read license string and user name from licenseFolder/licenseSubfolder/licenseFile,
   // verifying it via licenseFolder/licenseCheckSubfolder/licenseCheckFile
   {
      try {
         File subfolder = new File(licenseFolder, licenseSubfolder);
         File file = new File(subfolder, licenseFile);
         File check_subfolder = new File(licenseFolder, licenseCheckSubfolder);
         File check_file = new File(check_subfolder, licenseCheckFile);
         String string;
         char c;
         Vector v = new Vector(2);
         
         // read license key and user name from licenseFile

         RandomAccessFile raf = new RandomAccessFile(file, "r");

         for (string = ""; (c = raf.readChar()) != '\n'; )
            string += c;

         try {
            string = LicenseDef.unscrambleLicenseString(string);
         } catch (Exception e) {
         }
         v.addElement(string);
         
         for (string = ""; (c = raf.readChar()) != '\n'; )
            string += c;
         v.addElement(string);

         raf.close();
         
         // obtain combo-timestamp previously stored
         // in licenseCheckFile in "HostID"-format 
         
         raf = new RandomAccessFile(check_file, "r");

         for (string = ""; (c = raf.readChar()) != '\n'; )
            string += c;

         raf.close();
         
         int stored_ts = HostID.decodeHostID(string);
         
         // obtain actual combo-timestamp...
         
         int ts = (int)((HostID.fileCreationTimeOf(file) / 1000
            + HostID.fileCreationTimeOf(subfolder) / 1000) % 1023) + 1;
         
         // ...and compare timestamps - if they differ, the
         // user had copied our app to another location
         
         if (ts == stored_ts)
            return v;
         
         String[] options = { b.getString("OK") };
         AlertDialog.runModalDialog(appName,
            b.getString("CHECK_ERR"),
            AlertDialog.WARNING_MESSAGE,
            options);
         
      } catch (Exception e) {    	  
         // don't display any particular error info here -- if the license
         // infos were written properly, they will read-in properly; giving
         // any particular hints _here_ will only enhelp users in faking
         // a correct license folder/file set-up on a foreign machine
      }
   
      return null;
   }
   
   private static boolean writeLicenseStringAndUserName (String aString, String aName)
   // write aString and aName to licenseFolder/licenseSubfolder/licenseFile,
   // and a combo timestamp to licenseFolder/licenseCheckSubfolder/licenseCheckFile
   {
      try {
         File subfolder = new File(licenseFolder, licenseSubfolder);
         File file = new File(subfolder, licenseFile);
         File check_subfolder = new File(licenseFolder, licenseCheckSubfolder);
         File check_file = new File(check_subfolder, licenseCheckFile);
         
         // open licenseFolder/licenseSubfolder/licenseFile...
         
         subfolder.mkdirs();
         RandomAccessFile raf = new RandomAccessFile(file, "rw");
         
         // ...and write license key and user name, each in a line of its own
         
         try {
            aString = LicenseDef.scrambleLicenseString(aString);
         } catch (Exception e) {
         }
         raf.writeChars(aString);
         raf.writeChar('\n');
         raf.writeChars(aName);
         raf.writeChar('\n');
         raf.close();
         
         // obtain a combo-timestamp...
         
         int ts = (int)((HostID.fileCreationTimeOf(file) / 1000
            + HostID.fileCreationTimeOf(subfolder) / 1000) % 1023) + 1;
         
         // ...and store it in "HostID"-format to licenseCheckFile
         
         check_subfolder.mkdirs();
         raf = new RandomAccessFile(check_file, "rw");
         
         raf.writeChars(HostID.encodeHostID(ts));
         raf.writeChar('\n');
         raf.close();
         
         return true;
         
      } catch (Exception e) {
         Object[] args = { e.toString() };
         String[] options = { b.getString("OK") };
         AlertDialog.runModalDialog(appName,
            MessageFormat.format(b.getString("WRITE_ERR"), args),
            AlertDialog.WARNING_MESSAGE,
            options);
         
         return false;
      }
   }
   
   // internal license & registration dialog methods
   // (used only if canUseUI)

   private static boolean runLicenseDialog ()
   // returns true if "OK" button clicked, false if "Re-license" button clicked
   {
      LicenseDialog ld = new LicenseDialog();
      
      ld.setTitle(appName);
      
      // set plain values
      
      if (demoLicenseUsed)
         ld.setUserID(b.getString("SERIAL_DEMO"));
      else
         ld.setUserID(licenseDef.getUserID());
      
      if (demoLicenseUsed)
         ld.setOwner(b.getString("OWNER_DEMO"));
      else
         ld.setOwner((userName != null) ? userName : "");
      
      if (demoLicenseUsed)
         ld.setType(b.getString("TYPE_DEMO"));
      else if (maxUsers > 0) {
         if (maxUsers == 101)
            ld.setType(b.getString("TYPE_MAX_USERS_NA"));
         else if (maxUsers == 1)
            ld.setType(b.getString("TYPE_MAX_USERS_1"));
         else {
            Object[] args = { new Integer(maxUsers) };
            ld.setType(MessageFormat.format(b.getString("TYPE_MAX_USERS_X"), args));
         }
      } else
         ld.setType(b.getString("TYPE_SINGLE"));
      
      ld.setHostID(HostID.encodeHostID(hostID));
      
      // construct and set module columns
      
      String[] module = new String[moduleCount];
      String[] licensed = new String[moduleCount];
      String[] expires = new String[moduleCount];
      
      for (int i = 0; i < moduleCount; i++) {
         module[i] = moduleNames[i];
         licensed[i] = mlModeString[licenseDef.getMode(i)];
         int edi = licenseDef.getExpDateIndex(i);
         expires[i] = (edi < 0) ? "-" : licenseDef.getExpDate(edi).toString();
      }
      
      ld.setModules(module, licensed, expires);
      
      // ok, show the stuff
   
      ld.runModal();
      
      boolean oked = ld.wasOKed();
      ld.dispose();
      return oked;
   }
   
   private static Vector runRegisterDialog ()
   // null if cancelled, else new license string
   // in Vector[0] and user name in Vector[1]
   {
      RegisterDialog rd = new RegisterDialog();
      
      rd.setTitle(appName);
      rd.setKey("");
      rd.setName("");
      
      while (true) {
         rd.runModal();
         if (rd.wasCancelled()) {
            rd.dispose();
            return null;
         }
         
         String key = "", name = "";
         boolean key_error = true;
         int host_id = -2;
         
         try {
            key = rd.getKey().substring(0, 39);
            name = rd.getName();
            
            LicenseDef ld = new LicenseDef();
            ld.decode(key);
            ld.getMaxUsers();
            host_id = ld.getHostID();
            
            key_error = false;
            
         } catch (Exception e) {
            key_error = true;
         }
         
         String err_msg = null;
         
         if (key_error) {
            err_msg = b.getString("REGISTER_KEY_ERR");
         } else if ((host_id != 0) && (host_id != hostID)) {
            String[] args = {
               HostID.encodeHostID(host_id),
               HostID.encodeHostID(hostID)
            };
            err_msg = MessageFormat.format(b.getString("HOST_ID_ERR"), args);
         } else if (nameIsEmpty(name)) {
            err_msg = b.getString("REGISTER_NAME_ERR");
         }
         
         if (err_msg == null) {
            writeLicenseStringAndUserName(key, name);
            
            Vector v = new Vector(2);
            v.addElement(key);
            v.addElement(name);
            rd.dispose();
            return v;
         }

         String[] options = { b.getString("OK") };
         AlertDialog.runModalDialog(appName,
            err_msg,
            AlertDialog.WARNING_MESSAGE,
            options);
      }        
   }
   
   // floating functions
   
   private static boolean checkFloatingPermission (int aMaxUsersValue)
   {
      int users = -1;
      String err_msg = "unknown error";
      
      FloatingDialog fd = null;
      
      if (canUseUI) {
         fd = new FloatingDialog();
         //fd.setTitle(appName);
        // fd.runNonModal();
      }
      
      try {
         users = FloatingLicense.getCurrentUsers(appName, aMaxUsersValue, fd);
      } catch (Exception e) {
         users = -1;
         err_msg = e.toString();
      }
      if ((users < 0) || (users >= aMaxUsersValue)) {
         String msg;
         boolean quit;
         
         if (fd != null) {
            if (users < 0) {
               Object[] args = { err_msg };
               msg = MessageFormat.format(b.getString("NETWORK_ERR"), args);
            } else if (aMaxUsersValue == 1) {
               msg = b.getString("NETWORK_MAX_USERS_1");
            } else {
               Object[] args = { new Integer(aMaxUsersValue) };
               msg = MessageFormat.format(b.getString("NETWORK_MAX_USERS_X"), args);
            }
            Set<LoggedUserDTO> details=CommonUtil.getLoggedUserDetail();
            throw new BadCredentialsException(details+"   "+msg);
          /*  fd.runModal(msg);
            quit = fd.wasCancelled();
            fd.setVisible(false);
            fd.dispose();*/
         } else
            quit = true;
         
         if (quit) {
            try {
               if (exitDelegate != null)
                  exitDelegate.exitApplication();
            } catch (Exception e) {
            }
            //System.exit(1);
            throw new NumberFormatException("Limited users are allowed to access.Please Contact Administrator");
         } else
            return false;
      }
   
      if (fd != null) {
         fd.setVisible(false);
         fd.dispose();
      }
      return true;
   }
   
   private static void checkWorkstationPermission(int aMaxValue){
	  int workstations = 10;//lookupMgr.getNoOfWorkstations();
	  if ((workstations < 0) || (workstations >= aMaxValue)) {
		  String  msg = MessageFormat.format(b.getString("WORKSTATION_ERR"), aMaxValue);
		  throw new BadCredentialsException(msg);
		  
	  }
   }

   private static void checkReadingPointPermission(int aMaxValue){
		  int readingPoints = 20;//lookupMgr.getNoOfReadingPoint();
		  if ((readingPoints < 0) || (readingPoints >= aMaxValue)) {
			  String  msg = MessageFormat.format(b.getString("READINGPOINT_ERR"), aMaxValue);
			  throw new BadCredentialsException(msg);
		  }
	   }
   // start-up functions
   
   public static boolean isRegistrationEnabled ()
   {
      return canRegister;
   }
   
   public static void setUIEnabled (boolean whether)
   {
      canUseUI = whether;
   }
   
   public static boolean isUIEnabled ()
   {
      return canUseUI;
   }
   
   public static void setDemoModeEnabled (boolean whether)
   {
      canDoDemo = whether;
   }
   
   public static boolean isDemoModeEnabled ()
   {
      return canDoDemo;
   }
   
   public static void setAppName (String aString)
   {
      if (aString != null)
         appName = aString;
   }
   
   public static String getAppName ()
   {
      return appName;
   }
   
   public static File getLicenseFolder ()
   {
      return licenseFolder;
   }
   
   public static void setExitDelegate (LicenseHandlerExitDelegate anExitDelegate)
   {
      exitDelegate = anExitDelegate;
   }
   
   public static LicenseHandlerExitDelegate getExitDelegate ()
   {
      return exitDelegate;
   }

   public static void setRequiredVersion (int reqVersion)
   {
      reqVersionAtLeast = reqVersion;
   }
   
   public static void setModuleNames (String[] arrayOfModuleNames)
   {
      if (arrayOfModuleNames != null) {
         moduleCount = (arrayOfModuleNames.length <= 6) ? arrayOfModuleNames.length : 6;
            
         for (int i = 0; i < moduleCount; i++)
            moduleNames[i] = new String(arrayOfModuleNames[i]);
         
      } else {
         int i;
         
         for (i = 0; i < 6; i++) {
            try {
               String name = b.getString("MODULE_" + i);
               if (name.length() > 0)
                  moduleNames[i] = name;
               else
                  break;
            } catch (Exception e) {
               break;
            }
         }
         
         moduleCount = i;
      }
   }
   
   public void checkLicenseInFolder (File aFolder) throws Exception
   {
      checkLicense(aFolder, null, null);
   }
   
   public static boolean checkLicenseString (String aLicenseString) throws Exception
   {
      return checkLicense(null, aLicenseString, null);
   }
   
   public void checkLicenseStringAndUserName (String aLicenseString, String aUserName) throws Exception
   {
      checkLicense(null, aLicenseString, aUserName);
   }
   
   private static boolean checkLicense (File aFolder, String aLicenseString, String aUserName) throws Exception
   {
	   System.out.println("license cjeck needed================");
      boolean do_demo = false;   // true if demoLicenseString to be used
      Vector v;
      String string = null;
      String name = null;

      licenseFolder = aFolder;
      canRegister = (licenseFolder != null);
      
      hostID = HostID.obtainHostID(HostID.BY_DATE_OF_JAVA_HOME);
      // evaluate given license string, ...
      System.out.println("checkLicense metod===="+aLicenseString);
      if (aLicenseString != null) {
         if (licenseStringIsValid(aLicenseString)) {
            string = aLicenseString;
            name = aUserName; // may be null
         } else
            do_demo = true;
      
      } else {
         //System.out.println("licenece string is null.....");
         // ... or else read and syntax-check the contents of licenseFile
         
         if ((v = readLicenseStringAndUserName()) == null){
        	// licenseFile and/or licenseCheckFile don't exist or don't match
             do_demo = true;
             //System.out.println("do demo true.....");
         }
            

         else {
            string = (String)v.elementAt(0);
            name = (String)v.elementAt(1);
            if (!licenseStringIsValid(string) || nameIsEmpty(name)) {
               // licenseFile exists but contains garbage
               if ((v = runRegisterDialog()) == null)
                  // registration cancelled by user
                  do_demo = true;
               else {
                  // all ok, licenseFile contains a valid license
                  string = (String)v.elementAt(0);
                  name = (String)v.elementAt(1);
               }
            }
         }
      }
      
      // either 'string' is valid now, or 'demoLicenseString' is to be used
      System.out.println("license are coming here========"+do_demo);
      if (!do_demo) {
         // obtain PRO settings from pre-validated 'string'
         try {
            int host_id = -2;
            LicenseDef ld = new LicenseDef();
            
            ld.decode(string);
            maxUsers = ld.getMaxUsers();
            //System.out.println("max users...."+maxUsers);
            host_id = ld.getHostID();
            //System.out.println("host id desired........."+host_id);
            System.out.println("the max users===="+maxUsers);
            
         /*   maxWorkstations = ld.getVersion();
            maxReadingPoints= ld.getSerialNumber();*/
           
            /*if(maxWorkstations != 0){
            	checkWorkstationPermission(maxWorkstations);
            }
            if(maxReadingPoints != 0){
            	checkReadingPointPermission(maxReadingPoints);
            }*/
            if (maxUsers != 0) {
               // this is a floating license
               
               if (maxUsers != 101) {
                  if (checkFloatingPermission(maxUsers))
                     maxUsersChecked = true;
                  else
                     do_demo = true;
               }
               
            } else {
               /*** The Host ID has already been verified once - during key evaluation
                *** when running the RegisterDialog. If our app were copied to another
                *** machine, the copy-protection system would automatically invalidate
                *** the license, enforcing the user to re-run the RegisterDialog.
                *** Thus, it is not necessary to re-check the Host ID once the license
                *** is successfully installed.
                *** If you do want to, un-comment the following code.
                ***/
               System.out.println("comes to check the host id=========="+hostID);
               if ((host_id != 0) && (host_id != hostID)) {
            	   String[] args = {
                           HostID.encodeHostID(host_id),
                           HostID.encodeHostID(hostID)
                        };
            	   throw new BadCredentialsException( MessageFormat.format(b.getString("HOST_ID_ERR"), args));
                  // this a non-floating, Host ID based license, which doesn't fit
                  /*String[] args = {
                     HostID.encodeHostID(host_id),
                     HostID.encodeHostID(hostID)
                  };
                 // return false;

                  String[] options = { b.getString("DEMO"), b.getString("QUIT") };
                  // COMMENTED BY MADAN........
                  if (AlertDialog.runModalDialog(appName,
                     MessageFormat.format(b.getString("HOST_ID_ERR"), args),
                     AlertDialog.WARNING_MESSAGE,
                     options) != 0) {
                     try {
                        if (exitDelegate != null)
                           exitDelegate.exitApplication();
                     } catch (Exception e) {
                     }
                     System.exit(1); // MADAN........
                  }

               do_demo = true;*/
               }
            }
            GregorianCalendar expDate = LicenseHandler.getLeastExpiredDate(string);
	   		int days = CalendarUtil.getDaysFromNow(expDate);
	   		if(days < 0){
	   			 throw new NumberFormatException("license is expired"); 
	   		}
           // }
            System.out.println("at the end======="+do_demo);
         } catch (Exception e) {
        	 
           // prakash do_demo = true;
        	 throw e;
         }        
      }
      
      // decode either 'string' or 'demoLicenseString' to official 'licenseDef'
      
      /*userName = (do_demo || (name == null)) ? null : new String(name);
      licenseDef.decode((do_demo) ? demoLicenseString : string);
      demoLicenseUsed = do_demo;
System.out.println("in lice andler============="+userName+" license ddef==========="+licenseDef+" demoLiensed======"+demoLicenseUsed);
      */// check whether 'licenseDef' allows for permissionOfModule(0)
      
            
      // notify user in case we're in demo mode 
      // COMMENTED By MADAN............
     /* if (permissionOfModule(0) == PERMISSION_DEMO) {
         String[] options = (canRegister) ? new String[] { b.getString("DEMO_DEMO"), b.getString("DEMO_REGISTER") } : new String[] { b.getString("DEMO_DEMO") };
         if (AlertDialog.runModalDialog(appName,
            b.getString("DEMO_MSG"),
            AlertDialog.INFORMATION_MESSAGE,
            options) == 1)
            showRegisterDialog();
      }*/
      
      return true;//checkBasicPermission();
   }
   
   private static boolean checkBasicPermission ()
   {
      /*while (permissionOfModule(0) == PERMISSION_NO) {
    	//  return false; // MADAN..........   
         if (!canUseUI) {
            try {
               if (exitDelegate != null)
                  exitDelegate.exitApplication();
            } catch (Exception e) {
            }
           System.exit(1);
           return false;
         }
         
         String r = b.getString("BASIC_REGISTER");
         String d = b.getString("BASIC_DEMO");
         String q = b.getString("BASIC_QUIT");
         String[] options;
         if (canRegister && canDoDemo)
            options = new String[] { r, d, q };
         else if (canRegister)
            options = new String[] { r, q };
         else if (canDoDemo)
            options = new String[] { d, q };
         else
        	options = new String[] { q };
//               
         int result = AlertDialog.runModalDialog(appName,
            b.getString("BASIC_ERR"),
            AlertDialog.WARNING_MESSAGE,
            options);
         
         if ((result >= 0) && (result < options.length)) {
            
            if (options[result].equals(r)) {
               showRegisterDialog();
               continue;
            }
            
            if (options[result].equals(d)) {
               if (canRegister) {
                  try {
                     File subfolder = new File(licenseFolder, licenseSubfolder);
                     File file = new File(subfolder, licenseFile);
                     File check_subfolder = new File(licenseFolder, licenseCheckSubfolder);
                     File check_file = new File(check_subfolder, licenseCheckFile);
                     file.delete();
                     check_file.delete();
                     subfolder.delete();
                     check_subfolder.delete();
                  } catch (Exception e) {
                  }
               }
               userName = null;
               licenseDef.decode(demoLicenseString);
               demoLicenseUsed = true;
               continue;
            }
         }
               
         try {
            if (exitDelegate != null)
               exitDelegate.exitApplication();
         } catch (Exception e) {
         }
         System.exit(1);
      }*/
      
      permissionOfModule0 = permissionOfModule(0);
      permissionOfModule1 = permissionOfModule(1);
      permissionOfModule2 = permissionOfModule(2);
      permissionOfModule3 = permissionOfModule(3);
      permissionOfModule4 = permissionOfModule(4);
      permissionOfModule5 = permissionOfModule(5);
      System.out.println("the check sthe permissions==");
      return true;
   }
   
   // query methods
   
   public static int permissionOfModule (int moduleNumber)
   {
      try {
         if ((moduleNumber < 0) || (moduleNumber >= moduleCount))
            return PERMISSION_NO;
            
         if ((licenseDef == null) || (licenseDef.getVersion() < reqVersionAtLeast))
            return PERMISSION_NO;
            
         int i = licenseDef.getExpDateIndex(moduleNumber);
         if (i >= 0)
            if (expDateDidExpire(licenseDef.getExpDate(i)))
               return PERMISSION_NO;

         if (PERMISSION_YES != PERMISSION_NO + PERMISSION_DEMO + pdiff)
            return PERMISSION_NO;
            
         switch (licenseDef.getMode(moduleNumber)) {
            case LicenseDef.ML_YES:
            case LicenseDef.ML_FREE:
               return PERMISSION_YES;
            case LicenseDef.ML_DEMO:
               if (canDoDemo)
                  return PERMISSION_DEMO;
            default:
               return PERMISSION_NO;
         }
      } catch (Throwable t) {
      }
      return PERMISSION_NO;
   }
      
   public static boolean showLicenseDialog ()
   // returns true if license permissions have changed
   {
      if (!canUseUI)
         return false;
         
      boolean changed = false;

      while (!runLicenseDialog()) { // i.e. do re-license application
         if (!showRegisterDialog())
            break;
         changed = true;
      }

      return changed;
   }

   public static boolean showRegisterDialog ()
   // returns true if license permissions have changed
   {
      if (!canUseUI || !canRegister)
         return false;
         
      try {
         Vector v = runRegisterDialog();
         if (v == null)
            return false;
            
         licenseDef.decode((String)v.elementAt(0));   // verified in runRegisterDialog()
         userName = (String)v.elementAt(1);           // verified in runRegisterDialog()
            
         maxUsers = licenseDef.getMaxUsers();
         demoLicenseUsed = false;
                     
         if ((maxUsers != 0) && (maxUsers != 101) && !maxUsersChecked) {
            if (checkFloatingPermission(maxUsers))
               maxUsersChecked = true;
            else {
               userName = null;
               licenseDef.decode(demoLicenseString);
               demoLicenseUsed = true;
            }
         }

         checkBasicPermission();
         return true;
         
      } catch (Exception e) {
         return false;
      }
   }
   
   /**
    * Gets the expiration date of the first module, module 0
    * @param aString the license key string
    * @return expiration date
    * @throws Exception if license key cannot be decoded
    * @author madan added for need
    */
   public static GregorianCalendar getLeastExpiredDate (String aString) throws Exception
   // test-decode aString, validating key syntax
   {
      try {
         LicenseDef ld = new LicenseDef();
         ld.decode(aString);
         ExpDate ed = ld.getExpDate(0);
         GregorianCalendar given = new GregorianCalendar(ed.year(), ed.month() - 1, ed.day());
         return given;
      } catch (Exception e) {
         throw new Exception("Cannot decode license string:"+aString, e);
      }        
      
   }

}