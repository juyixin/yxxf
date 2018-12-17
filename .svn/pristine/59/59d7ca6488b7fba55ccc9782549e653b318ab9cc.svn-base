package com.eazytec.bpm.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PathUtils {
	private static Log log = LogFactory.getLog(PathUtils.class);
	/**
	 * Get parent node.
	 */
	public static String getParent(String path) {
		log.debug("getParent({}) "+path);
		int lastSlash = path.lastIndexOf('/');
		String ret = (lastSlash > 0)?path.substring(0, lastSlash):"/";
		log.debug("getParent: {}"+ ret);
		return ret;	
	}

	/**
	 * Get node name.
	 */
	public static String getName(String path) {
		log.debug("getName({})"+ path);
		String ret = path.substring(path.lastIndexOf('/') + 1);
		log.debug("getName: {}" +ret);
		return ret;
	}
	
	/**
	 * Get path depth 
	 */
	public static int getDepth(String path) {
		return path.substring(1).split("/").length;
	}
	
	/**
	 * Get path context
	 */
	public static String getContext(String path) {
		int idx = path.indexOf('/', 1);
		return path.substring(0, idx < 0 ? path.length() : idx);
	}
	
	/**
	 * Eliminate dangerous chars in node name.
	 * TODO Keep on sync with uploader:com.openkm.applet.Util.escape(String)
	 */
	public static String escape(String name) {
		log.debug("escape({})"+ name);
		String ret = name.replace('/', ' ');
		ret = ret.replace(':', ' ');
		ret = ret.replace('[', ' ');
		ret = ret.replace(']', ' ');
		ret = ret.replace('*', ' ');
		ret = ret.replace('\'', ' ');
		ret = ret.replace('"', ' ');
		ret = ret.replace('|', ' ');
		ret = ret.trim();
		log.debug("escape: {}"+ ret);
		return ret;
	}
	
	/**
	 * Fix context definition. For example "/okm:root" -> "okm_root"
	 */
	public static String fixContext(String context) {
		return context.substring(1).replace(':', '_');
	}
}
