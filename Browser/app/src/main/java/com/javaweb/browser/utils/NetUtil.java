package com.javaweb.browser.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil {


	/**
	 * 判断字符串是否为URL
	 * @param url
	 * @return true:是URL、false:不是URL
	 */
	public static boolean isHttpUrl(String url) {
		boolean isurl = false;
		String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
				+ "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

		Pattern pat = Pattern.compile(regex.trim());//比对
		Matcher mat = pat.matcher(url.trim());
		isurl = mat.matches();//判断是否匹配
		if (isurl) {
			isurl = true;
		}
		return isurl;
	}


	public static String getFileName(String url) {
		String filename = "";
		boolean isok = false;
		// 从UrlConnection中获取文件名称
		try {
			URL myURL = new URL(url);

			URLConnection conn = myURL.openConnection();
			if (conn == null) {
				return null;
			}
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return null;
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return null;
			}
			// Log.i("test", "getContentType:" + conn.getContentType() + ",Url:"
			// + conn.getURL().toString());
			for (String skey : key) {
				List<String> values = hf.get(skey);
				for (String value : values) {
					String result;
					try {
						result = new String(value.getBytes("ISO-8859-1"), "GBK");
						int location = result.indexOf("filename");
						if (location >= 0) {
							result = result.substring(location
									+ "filename".length());
							filename = result
									.substring(result.indexOf("=") + 1);
							isok = true;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}// ISO-8859-1 UTF-8 gb2312
				}
				if (isok) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 从路径中获取
		if (filename == null || "".equals(filename)) {
			filename = url.substring(url.lastIndexOf("/") + 1);
		}
		return filename;

	}


}
