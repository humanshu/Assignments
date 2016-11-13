package com.ws.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.testng.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTest {

	public static HttpResponse callArticleService(String json, String parameter) throws ClientProtocolException, IOException  {
		String wsURL = "http://localhost:8080/articleservice/article/"+parameter;
		HttpPut request = new HttpPut(wsURL); 
	    StringEntity entity = new StringEntity(json);
	    entity.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    request.addHeader("Accept","application/json");
	    request.setEntity(entity);
	    HttpResponse response = null;
	    HttpClient httpclient = HttpClientBuilder.create().build();
	    try {
	        response = httpclient.execute(request);
	        return response;
	    } catch (SocketException se) {
	        throw se;
	    }
	}
	
	public static String getArticleServiceResponse(String type,String parameter) throws IOException {
		
		String wsURL = "http://localhost:8080/articleservice/"+type+"/"+parameter;
		URL object = new URL(wsURL);
		HttpURLConnection connection = (HttpURLConnection) object.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();	
		while ((inputLine = inputReader.readLine()) != null) {
			response.append(inputLine);
		}
		
		inputReader.close();
		return response.toString();
	}
	
	
	@Test
	public void putMethodTest() throws ClientProtocolException, IOException {
		
		String sentJson1  = "{\"weight\": 10000.0, \"type\": \"Spring Transactional Mechanisms\",\"parent_id\": 10 }";
		HttpResponse receivedResponse1 = callArticleService(sentJson1, "11");
		Assert.assertTrue(receivedResponse1.toString().contains("200"));
		
		String sentJson2  = "{\"weight\": 5000.0, \"type\": \"Spring Transactional Mechanisms\" }";
		HttpResponse receivedResponse2 = callArticleService(sentJson2, "10");
		Assert.assertTrue(receivedResponse2.toString().contains("200"));	
		
		String sentJson3  = "{}";
		HttpResponse receivedResponse3 = callArticleService(sentJson3, "111");
		Assert.assertTrue(receivedResponse3.toString().contains("200"));
		
	}

	@Test
	public void getArticleByIDTest() throws IOException  {
		
		String expectedJson = "{\"article_id\":11, \"weight\": 10000.0, \"type\": \"Spring Transactional Mechanisms\",\"parent_id\": 10 }";
	    String receivedJson = getArticleServiceResponse("article","11");
	    Assert.assertTrue(receivedJson.replace(" ", "").equals(expectedJson.replace(" ", "")));
	}
	
	
	@Test
	public void getArticleIdsByTypeTest() throws IOException {
		
		String expectedJson = "[10,11]";
	    String receivedJson = getArticleServiceResponse("type","Spring%20Transactional%20Mechanisms");
	    Assert.assertTrue(receivedJson.equals(expectedJson));
	}
	
	@Test
	public void getSumArticleWeightTest() throws IOException {
		
		String expectedJson1 = "{\"sum\":15000.0}";
	    String receivedJson1 = getArticleServiceResponse("sum","10");
	    Assert.assertTrue(receivedJson1.equals(expectedJson1));
				
		String expectedJson2 = "{\"sum\":10000.0}";
	    String receivedJson2 = getArticleServiceResponse("sum","11");
	    Assert.assertTrue(receivedJson2.equals(expectedJson2));
	    
		String expectedJson3 = "{\"sum\":-9999.999}";
	    String receivedJson3 = getArticleServiceResponse("sum","111");
	    Assert.assertTrue(receivedJson3.equals(expectedJson3));
	}
	

	
}
