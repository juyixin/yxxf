package com.eazytec.bpm.common.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;

//import com.openkm.core.Config;
//import com.openkm.core.DatabaseException;
//import com.openkm.dao.MimeTypeDAO;
//import com.openkm.dao.bean.MimeType;
//import com.openkm.util.impexp.RepositoryImporter;

public class FileUtils {
	private static Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * Returns the name of the file without the extension.
	 */
	public static String getFileName(String file) {
		log.debug("getFileName({})", file);
		int idx = file.lastIndexOf(".");
		String ret = idx>=0?file.substring(0, idx):file;
		log.debug("getFileName: {}", ret);
		return ret;
	}
	
	/**
	 * Returns the filename extension.
	 */
	public static String getFileExtension(String file) {
		log.debug("getFileExtension({})", file);
		int idx = file.lastIndexOf(".");
		String ret = idx>=0?file.substring(idx+1):"";
		log.debug("getFileExtension: {}", ret);
		return ret;
	}
	
	/**
	 * Creates a temporal and unique directory
	 * 
	 * @throws IOException If something fails.
	 */
	public static File createTempDir() throws IOException {
		File tmpFile = File.createTempFile("okm", null);
		
		if (!tmpFile.delete())
			throw new IOException();
		if (!tmpFile.mkdir())
			throw new IOException();
		return tmpFile;       
	}
	
	/**
	 * Create temp file
	 */
	public static File createTempFile() throws IOException {
		return File.createTempFile("okm", ".tmp");
	}
	
	/**
	 * Create temp file with extension from mime
	 */
	public static File createTempFileFromMime(String mimeType) throws IOException {
	/*	MimeType mt = MimeTypeDAO.findByName(mimeType);
		String ext = mt.getExtensions().iterator().next();*/
		String ext=".pdf";
		return File.createTempFile("bpm", "." + ext);
	}
	
	/**
	 * Wrapper for FileUtils.deleteQuietly
	 * 
	 * @param file File or directory to be deleted.
	 */
	public static boolean deleteQuietly(File file) {
		return deleteQuietly1(file);
	}
	
	/**
	 * Deletes a file, never throwing an exception. If file is a directory,
	 * delete it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, can be {@code null}
	 * @return {@code true} if the file or directory was deleted, otherwise
	 *         {@code false}
	 * 
	 * @since 1.4
	 */
	public static boolean deleteQuietly1(final File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				org.apache.commons.io.FileUtils.cleanDirectory(file);
			}
		} catch (final Exception ignored) {
		}

		try {
			return file.delete();
		} catch (final Exception ignored) {
			return false;
		}
	}
	
	/**
	 * Delete directory if empty
	 */
	public static void deleteEmpty(File file) {
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			}
		}
	}
	
	/**
	 * Count files and directories from a selected directory.
	 */
	public static int countFiles(File dir) {
		File[] found = dir.listFiles();
		int ret = 0;
		
		if (found != null) {
			for (int i = 0; i < found.length; i++) {
				if (found[i].isDirectory()) {
					ret += countFiles(found[i]);
				}
				
				ret++;
			}
		}
		
		return ret;
	}
	/**
	 * Count files and directories from a selected directory.
	 * This version exclude .okm files
	 *//*
	public static int countImportFiles(File dir) {
		File[] found = dir.listFiles(new RepositoryImporter.NoVersionFilenameFilter());
		int ret = 0;
		
		if (found != null) {
			for (int i = 0; i < found.length; i++) {
				//log.info("File: {}", found[i].getPath());
				
				if (found[i].isDirectory()) {
					ret += countImportFiles(found[i]);
				}
				
				// NAND
				if (!(found[i].isFile() && found[i].getName().toLowerCase().endsWith(Config.EXPORT_METADATA_EXT))) {
					ret++;
				}
			}
		}
		
		return ret;
	}*/
	
	/**
	 * Copy InputStream to File.
	 */
	public static void copy(InputStream input, File output) throws IOException {
		FileOutputStream fos = new FileOutputStream(output);
		IOUtils.copy(input, fos);
		fos.flush();
		fos.close();
	}
	
	/**
	 * Copy File to OutputStream
	 */
	public static void copy(File input, OutputStream output) throws IOException {
		FileInputStream fis = new FileInputStream(input);
		IOUtils.copy(fis, output);
		fis.close();
	}
	
	/**
	 * Copy File to File
	 */
	public static void copy(File input, File output) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(input, output);
	}
	
	public static void writeFile(String path,String content) throws BpmException{
		try{			
			File file = new File(path);
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		}catch(Exception e){
			throw new EazyBpmException("Problem while writing the file"+path+e.getMessage());			
		}
	}	
	
	public static File copyAndPasteFile(String sourcePath,String destinationPath) throws BpmException{
		try {
			File source = new File(sourcePath);
			File destination = new File(destinationPath);
			if(!destination.exists()) {
				destination.createNewFile();
			}
			org.apache.commons.io.FileUtils.copyFile(source, destination);
			return destination;
		} catch (IOException e) {
			throw new EazyBpmException("Error while copying the file from "+sourcePath+" to "+destinationPath+e.getMessage());
		}
	}
	
	/**
	 * For compress folder as zip in the same path and delete the original folder 
	 * Means only compressed folder only there
	 */
	
    public static void zip(String directoryPath, String zipPath)  throws IOException{
    	File directory = new File(directoryPath);
    	File zipfile = new File(zipPath);
	    OutputStream out = new FileOutputStream(zipfile);
    	Closeable res = out;
        try {
	       URI base = directory.toURI();
	       Deque<File> queue = new LinkedList<File>();
	       queue.push(directory);

           ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
           
           while (!queue.isEmpty()) {
              directory = queue.pop();
              for (File kid : directory.listFiles()) {
	              String name = base.relativize(kid.toURI()).getPath();
	              if (kid.isDirectory()) {
	                queue.push(kid);
	                name = name.endsWith("/") ? name : name + "/";
	                zout.putNextEntry(new ZipEntry(name));
	              } else {
	                zout.putNextEntry(new ZipEntry(name));
	                copy(kid, zout);
	                zout.closeEntry();
	              }
	              kid.delete();
              }
           }
           directory.delete();
        } finally {
	        res.close();
	      }
      }
    
    public static void unzip(String zipPath, String directoryPath) throws BpmException {
    	
    	File zipfile = new File(zipPath);
    	File directory = new File(directoryPath);
        try {
	        ZipFile zfile = new ZipFile(zipfile);
	        Enumeration<? extends ZipEntry> entries = zfile.entries();
	        while (entries.hasMoreElements()) {
	            ZipEntry entry = entries.nextElement();
	            File file = new File(directory, entry.getName());
	            if (entry.isDirectory()) {
	               file.mkdirs();
	            } else {
		            file.getParentFile().mkdirs();
		            InputStream in = zfile.getInputStream(entry);
		            copy(in, file);
	            }
	        }
        }catch (IOException e) {
			throw new EazyBpmException("Error while uncompressing the file from "+zipPath+" to "+directoryPath+e.getMessage());
		} 
    }
    
    /**
	 * method to save file and get path of file
	 *
	 * @author mathi
	 * @param file
	 * @param uploadDir
	 * @throws IOException
	 */
	public static String uploadFile(CommonsMultipartFile file, String uploadDir)throws IOException{
		 if (file!=null){
			 String fileName = file.getOriginalFilename();
			 fileName = fileName.replace(" ", "_");
			 fileName = URLEncoder.encode(fileName, "UTF-8");
			 fileName = URLDecoder.decode(fileName, "ISO8859_1");
			 
             File dirPath = new File(uploadDir);

             if (!dirPath.exists()) {
                 dirPath.mkdirs();
             }
             
             InputStream stream = file.getInputStream();
             OutputStream bos = new FileOutputStream(uploadDir + fileName);
             int bytesRead;
             byte[] buffer = new byte[8192];

             while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                 bos.write(buffer, 0, bytesRead);
             }

             bos.close();
             stream.close();
             return uploadDir + fileName;
         }else{
         	 return null;
         }
	}
}
