package com.licensekit;

import java.io.*;

public class HostID
{
   public static String encodeHostID (int aHostID)
   // converts 10-bit Host ID value to AA9999 format
   {
      if ((aHostID < 1) || (aHostID > 1023))
         return "AA0000";           // mark as invalid
         
      int lo4 = aHostID & 15;
      int hi4 = (aHostID >>> 6) & 15;
      int lo3 = aHostID & 7;
      int lm3 = (aHostID >>> 3) & 7;
      int hm3 = (aHostID >>> 4) & 7;
      int hi3 = (aHostID >>> 7) & 7;
         
      String string = "";
      
      string += (char)('A' + lo4);
      string += (char)('Z' - hi4);
      string += (char)('9' - lo3);
      string += (char)('0' + lm3);
      string += (char)('0' + hm3);
      string += (char)('9' - hi3);

      return string;
   }
   
   public static int decodeHostID (String aString) throws NumberFormatException
   // converts string in AA9999 format to 10-bit Host ID value
   // returns 1..1023 if successful, throws an exception if string is malformed
   {
      int lo4, hi4, lo3, lm3, hm3, hi3, val;
   
      if (aString.length() != 6)
         throw new NumberFormatException("illegal HostID");

      lo4 = aString.charAt(0) - 'A';
      hi4 = 'Z' - aString.charAt(1);
      lo3 = '9' - aString.charAt(2);
      lm3 = aString.charAt(3) - '0';
      hm3 = aString.charAt(4) - '0';
      hi3 = '9' - aString.charAt(5);
      
      val = lo3 + (lm3 << 3) + (hi4 << 6);

      if ((lo4 != (val & 15))
         || (hm3 != ((val >>> 4) & 7))
         || (hi3 != ((val >>> 7) & 7)))
         throw new NumberFormatException("illegal HostID");
      
      return val;
   }
   
   public final static int BY_DATE_OF_JAVA_HOME = 0;
   public final static int BY_PATH_OF_JAVA_HOME = 1;
   public final static int BY_DATE_OF_USER_DIR = 2;
   public final static int BY_PATH_OF_USER_DIR = 3;
   public final static int BY_DATE_OF_USER_HOME = 4;
   public final static int BY_PATH_OF_USER_HOME = 5;
   public final static int BY_AUTOCREATION_IN_LICENSE_FOLDER = 6;
   
   private static String hostIDSubfolder = ".lh";
   private static String hostIDFile = ".lhf";

   public static int obtainHostID (int how)
   // returns 1..1023 if successful, or -1 on error
   {
      try {
         switch (how) {
            case BY_DATE_OF_JAVA_HOME: {
               File file = new File(System.getProperty("java.home"));
               return (int)((fileCreationTimeOf(file) / 1000) % 1023) + 1;
            }
            case BY_PATH_OF_JAVA_HOME: {
               int[] values = HashString.hashString(System.getProperty("java.home"));
               return (int)((values[0] + values[1] + values[2]) % 1023) + 1;
            }
            case BY_DATE_OF_USER_DIR: {
               File file = new File(System.getProperty("user.dir"));
               return (int)((fileCreationTimeOf(file) / 1000) % 1023) + 1;
            }
            case BY_PATH_OF_USER_DIR: {
               int[] values = HashString.hashString(System.getProperty("user.dir"));
               return (int)((values[0] + values[1] + values[2]) % 1023) + 1;
            }
            case BY_DATE_OF_USER_HOME: {
               File file = new File(System.getProperty("user.home"));
               return (int)((fileCreationTimeOf(file) / 1000) % 1023) + 1;
            }
            case BY_PATH_OF_USER_HOME: {
               int[] values = HashString.hashString(System.getProperty("user.home"));
               return (int)((values[0] + values[1] + values[2]) % 1023) + 1;
            }
            case BY_AUTOCREATION_IN_LICENSE_FOLDER: {
               File subfolder = new File(LicenseHandler.getLicenseFolder(), hostIDSubfolder);
               File file = new File(subfolder, hostIDFile);
                  
               // if licenseFolder/subfolder/file doesn't exist yet, create it...
               
               if (!file.exists()) {
                  subfolder.mkdirs();
                  RandomAccessFile raf = new RandomAccessFile(file, "rw");
                  
                  // ...and store dummy contents to
                  
                  int ts = (int)((System.currentTimeMillis() / 1000) % 1023) + 1;
                  
                  raf.writeChars(HostID.encodeHostID(ts));
                  raf.writeChar('\n');
                  raf.close();
               }
               
               // return combo timestamp
               
               int ts = (int)((fileCreationTimeOf(file) / 1000
                  + fileCreationTimeOf(subfolder) / 1000) % 1023) + 1;
               
               return ts;
            }
         }
      } catch (Exception e) {
      }
      return -1;
   }

   public static long fileCreationTimeOf (File aPath) throws IOException
   {
      long timeval = 0;
      try {
         timeval = aPath.lastModified();
      } catch (Exception e) {
         timeval = 0;
      }
      if (timeval < 900000000000L)
         throw new IOException();
      return timeval;
   }
}
