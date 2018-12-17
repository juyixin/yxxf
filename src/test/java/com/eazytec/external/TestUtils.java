package com.eazytec.external;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {
	private static final Logger log = LoggerFactory.getLogger(TestUtils.class);

	public static final String ROOT_URL = "http://localhost:8080/external/";

	/**
	 * POST形式发送请求
	 * 
	 * @param urlconn
	 *            请求地址
	 * @param params
	 *            参数
	 * @param token
	 *            安全校验码 暂不使用
	 * @return
	 * @throws IOException
	 */
	public static String post(String urlconn, String params, String token) throws IOException {
		return invokePostOrPut(urlconn, params, token, "POST");
	}
	
	public static String put(String urlconn, String params, String token) throws IOException {
		return invokePostOrPut(urlconn, params, token, "PUT");
	}

	private static String invokePostOrPut(String urlconn, String params, String token, String type) throws IOException {
		String result = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;

		urlconn = ROOT_URL + urlconn;

		log.info("请求地址： {}", urlconn);
		log.info("请求参数：  {}", params);
		log.info("请求方式：  {}", type);

		try {
			byte[] data = params.getBytes();
			URL url = new URL(urlconn);
			urlConnection = (HttpURLConnection) url.openConnection();
			// 设置可以读取数据
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod(type);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(5000);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("token", token);
			urlConnection.connect();
			out = urlConnection.getOutputStream();
			out.write(data);
			out.flush();

			int statusCode = urlConnection.getResponseCode();
			log.info("响应状态码：{}", statusCode);
			if (statusCode != HttpURLConnection.HTTP_OK) {
				log.error("服务器错误");
			}
			in = new BufferedInputStream(urlConnection.getInputStream());
			result = getStrFromInputSteam(in);
			
			log.info("响应内容：  {}", "\n" +format(result));
			
		} catch (ConnectException e) {
			e.printStackTrace();
			log.error("连接出错，请检查您的网络");
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			log.error("服务器响应超时...");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询出错");
		} finally {
			if (out != null) {
				out.close();
			}

			if (in != null) {
				in.close();
			}

			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return format(result);
	}

	/**
	 * GET形式发送请求
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String get(String urlconn, String params, String token) throws IOException {
		return invokeGetOrDelete(urlconn, params, token, "GET");
	}
	
	public static String delete(String urlconn, String params, String token) throws IOException {
		return invokeGetOrDelete(urlconn, params, token, "DELETE");
	}


	private static String invokeGetOrDelete(String urlconn, String params, String token, String type) throws IOException {
		String result = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;

		urlconn = ROOT_URL + urlconn + "?" + params;

		log.info("请求地址： {}", urlconn);
		log.info("请求参数：  {}", params);
		log.info("请求方式：  {}", type);

		try {
			URL url = new URL(urlconn);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setRequestMethod(type);
			urlConnection.setRequestProperty("token", token);

			int statusCode = urlConnection.getResponseCode();
			log.info("响应状态码：{}", statusCode);
			if (statusCode != HttpURLConnection.HTTP_OK) {
				log.error("服务器错误");
			}

			in = urlConnection.getInputStream();
			result = getStrFromInputSteam(in);
			
			log.info("响应内容：  {}", "\n" +format(result));
		} catch (ConnectException e) {
			e.printStackTrace();
			log.error("连接出错，请检查您的网络");
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			log.error("服务器响应超时...");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询出错");
		} finally {
			if (in != null) {
				in.close();
			}

			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return format(result);
	}

	public static String getStrFromInputSteam(InputStream in) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		// 最好在将字节流转换为字符流的时候 进行转码
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = bf.readLine()) != null) {
			buffer.append(line);
		}

		return buffer.toString();
	}

	/**
	 * 格式化json字符串
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static String format(String jsonStr) {
		int level = 0;
		StringBuffer jsonForMatStr = new StringBuffer();
		for (int i = 0; i < jsonStr.length(); i++) {
			char c = jsonStr.charAt(i);
			if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
				jsonForMatStr.append(getLevelStr(level));
			}
			switch (c) {
			case '{':
			case '[':
				jsonForMatStr.append(c + "\n");
				level++;
				break;
			case ',':
				jsonForMatStr.append(c + "\n");
				break;
			case '}':
			case ']':
				jsonForMatStr.append("\n");
				level--;
				jsonForMatStr.append(getLevelStr(level));
				jsonForMatStr.append(c);
				break;
			default:
				jsonForMatStr.append(c);
				break;
			}
		}
		
		return jsonForMatStr.toString();
	}

	private static String getLevelStr(int level) {
		StringBuffer levelStr = new StringBuffer();
		for (int levelI = 0; levelI < level; levelI++) {
			levelStr.append("\t");
		}
		return levelStr.toString();
	}

	public static String getFilePost(String urlconn, InputStream input, String fileName, String token) throws IOException {

		String result = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;

		urlconn = ROOT_URL + urlconn;

		log.info("urlconn {}", urlconn);
		log.info("input {}", input);

		try {
			byte[] data = InputStreamToByte(input);
			URL url = new URL(urlconn);
			urlConnection = (HttpURLConnection) url.openConnection();
			// 设置可以读取数据
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(5000);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			urlConnection.setRequestProperty("token", token);
			urlConnection.setRequestProperty("fileName", fileName);
			urlConnection.connect();
			out = urlConnection.getOutputStream();
			out.write(data);
			out.flush();

			int statusCode = urlConnection.getResponseCode();
			log.info("response-status {}", statusCode);
			if (statusCode != HttpURLConnection.HTTP_OK) {
				log.error("服务器错误");
			}
			in = new BufferedInputStream(urlConnection.getInputStream());
			result = getStrFromInputSteam(in);
			
			log.info("响应内容：  {}", "\n" +format(result));
		} catch (ConnectException e) {
			e.printStackTrace();
			log.error("连接出错，请检查您的网络");
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			log.error("服务器响应超时...");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询出错");
		} finally {
			if (out != null) {
				out.close();
			}

			if (in != null) {
				in.close();
			}

			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return format(result);
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamToByte(InputStream in) throws IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[100];
		int count = -1;
		while ((count = in.read(data, 0, 100)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return outStream.toByteArray();
	}

	public static String buildParams(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		try {
			if(!params.isEmpty()){
				for (Map.Entry<String, String> entry : params.entrySet()) {
					stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}