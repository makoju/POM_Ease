package com.ability.ease.auto.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

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

}
