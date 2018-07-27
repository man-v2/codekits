package org.codekit.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

public class HttpClientUtil {

	private static final String defualtCharset = "utf-8";

	public static String doPost(String url, Map params) {
		return doPost(url, params, null);
	}

	public static String doPost(String url, Map params, String charset) {
		if (StringUtils.isEmpty(charset))
			charset = defualtCharset;
		StringBuffer response = new StringBuffer();
		// 构造HttpClient的实例
		HttpClient client = new HttpClient();
		// 创建Post法的实例
		PostMethod method = new PostMethod(url);
		// 使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"utf-8");
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		// 设置Http Post数据
		if (params != null) {
			// 填入各个域的值
			NameValuePair[] data = null;

			Set sets = params.keySet();
			Object[] arr = sets.toArray();
			int mxsets = sets.size();
			if (mxsets > 0) {
				data = new NameValuePair[mxsets];
			}
			for (int i = 0; i < mxsets; i++) {
				String key = (String) arr[i];
				String val = (String) params.get(key);
				data[i] = new NameValuePair(key, val);
			}
			// 将值放入postMethod中
			method.setRequestBody(data);
		}
		try {
			// 执行getMethod
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (HttpException e) {
			e.printStackTrace();
			System.out.println("Http错误原因：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO错误原因：" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, Map params) {
		return doGet(url, params, null);
	}

	public static String doGet(String url, Map params, String charset) {
		if (StringUtils.isEmpty(charset))
			charset = defualtCharset;
		StringBuffer response = new StringBuffer();
		// 构造HttpClient的实例
		HttpClient client = new HttpClient();
		// 创建Get法的实例
		GetMethod method = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"utf-8");
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		// 设置Http Get数据
		if (params != null) {
			// 填入各个域的值
			NameValuePair[] data = null;

			Set sets = params.keySet();
			Object[] arr = sets.toArray();
			int mxsets = sets.size();
			if (mxsets > 0) {
				data = new NameValuePair[mxsets];
			}
			for (int i = 0; i < mxsets; i++) {
				String key = (String) arr[i];
				String val = (String) params.get(key);
				data[i] = new NameValuePair(key, val);
			}
			// 将表单的值放入getMethod中
			method.setQueryString(data);
		}
		try {
			// 执行getMethod
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (HttpException e) {
			e.printStackTrace();
			System.out.println("Http错误原因：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO错误原因：" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	public static String getURLSource(String url, Map params) {
		String source = "";
		HttpClient client = new HttpClient();
		// 使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
		HttpMethod method = new GetMethod(url);
		try {
			// 设置Http Get数据
			if (params != null) {
				// 填入各个域的值
				NameValuePair[] data = null;

				Set sets = params.keySet();
				Object[] arr = sets.toArray();
				int mxsets = sets.size();
				if (mxsets > 0) {
					data = new NameValuePair[mxsets];
				}
				for (int i = 0; i < mxsets; i++) {
					String key = (String) arr[i];
					String val = (String) params.get(key);
					data[i] = new NameValuePair(key, val);
				}
				// 将表单的值放入getMethod中
				method.setQueryString(data);
			}
			client.executeMethod(method);
			source = method.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
			System.out.println("Http错误原因：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO错误原因：" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return source;
	}

	public static String doPost(String url, InputStream is, String suffix,
			String charset) {
		if (StringUtils.isEmpty(charset))
			charset = defualtCharset;
		StringBuffer response = new StringBuffer();
		// 构造HttpClient的实例
		HttpClient client = new HttpClient();
		// 创建Post法的实例
		PostMethod method = new PostMethod(url);

		// 使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				charset);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 应该这样写
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		try {
			while ((len = is.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, len);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = baos.toByteArray();
		Part[] parts = { new FilePart("filedata", new ByteArrayPartSource(
				suffix, buffer)) };
		MultipartRequestEntity mre = new MultipartRequestEntity(parts,
				method.getParams());
		method.setRequestEntity(mre);
		try {
			// 执行getMethod
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (HttpException e) {
			e.printStackTrace();
			System.out.println("Http错误原因：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO错误原因：" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
}
