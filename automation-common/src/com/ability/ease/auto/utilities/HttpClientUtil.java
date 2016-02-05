package com.ability.ease.auto.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class HttpClientUtil {

	public static int sendPOST(String sReferenceURI){
		int returnCode = 0;
		HttpClient client = new HttpClient();
		BufferedReader br = null;
		PostMethod postMethod = new PostMethod("http://"+sReferenceURI);
		//postMethod.addParameter(paramName, paramValue);
		try{
			returnCode = client.executeMethod(postMethod);
			if(returnCode == HttpStatus.SC_NOT_IMPLEMENTED){
				System.err.println("the post method in not implemented by this URI");
			}else{
				br = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
				//you can write code that retrieve the response message here 
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			postMethod.releaseConnection();
			if(br != null) try { br.close(); } catch (Exception fe) {}
		}
		return returnCode;
	}
	
	class MyAuthenticator extends Authenticator{
	   PasswordAuthentication authentication;
	 MyAuthenticator(String userName, String password)
	 {
	     authentication= new PasswordAuthentication(userName, password.toCharArray());
	 }
	}
	
	public static int downloadPdf(WebDriver driver, String url) throws ClientProtocolException, IOException  {
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Content-Type", "application/pdf");
	
		DefaultHttpClient client = new DefaultHttpClient();
		org.apache.http.client.CookieStore  cookiestore = client.getCookieStore();
		//final org.openqa.selenium.Cookie c = driver.manage().getCookieNamed("JSESSIONID");
		Set<Cookie> cookies = driver.manage().getCookies();
		
		for(final Cookie c:cookies){
			org.apache.http.cookie.Cookie c1 = new org.apache.http.cookie.Cookie() {
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isPersistent() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isExpired(Date arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getVersion() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getValue() {
				// TODO Auto-generated method stub
				return c.getValue();
			}
			
			@Override
			public int[] getPorts() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return c.getPath();
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return c.getName();
			}
			
			@Override
			public Date getExpiryDate() {
				// TODO Auto-generated method stub
				return c.getExpiry();
			}
			
			@Override
			public String getDomain() {
				// TODO Auto-generated method stub
				return c.getDomain();
			}
			
			@Override
			public String getCommentURL() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getComment() {
				// TODO Auto-generated method stub
				return null;
			}
		}; 
			cookiestore.addCookie(c1);
		}
		
/*		URL server = new URL(url);//works for https and not for http, i needed https in  my case.
		Authenticator.setDefault((new HttpClientUtil().new MyAuthenticator("test.customer", "test1234!@#$")));

		URLConnection connection = (URLConnection)server.openConnection();
		connection.connect();
		InputStream is = connection.getInputStream();*/
		HttpResponse response = client.execute(httpget);
	
		System.out.println("Download response: " + response.getStatusLine());

		HttpEntity entity = response.getEntity();

		InputStream inputStream = null;
		OutputStream outputStream = null;

		if (entity != null) {
			long len = entity.getContentLength();
			inputStream = entity.getContent();

			outputStream = new FileOutputStream(new File("SummaryReport123.pdf"));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
		}
		return response.getStatusLine().getStatusCode();
	}
	
	public static void downloadFile(String url) throws IOException{
		URL url1 = new URL(url);
		Authenticator.setDefault((new HttpClientUtil().new MyAuthenticator("test.customer", "test1234!@#$")));

		URLConnection connection = (URLConnection)url1.openConnection();
		connection.connect();
		InputStream in = connection.getInputStream();
		Files.copy(in, Paths.get("someFile.pdf"), StandardCopyOption.REPLACE_EXISTING);
		in.close();
	}
}
