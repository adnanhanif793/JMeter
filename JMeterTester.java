package org.poc.test;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

import okhttp3.Headers;

import java.net.URI;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class JMeterTester implements EventHandler 
{

	public List<String> respList = new ArrayList<String>();

	public void onOpen() throws Exception
	{
		info("The connection has been opened");
	}

	public void onClosed() throws Exception
	{
		info("The connection has been closed");
	}

	public void onMessage(String Event, MessageEvent messageEvent) throws Exception
	{
		respList.add(messageEvent.getData());
	}

	public void onComment(String comment) throws Exception
	{
		info(comment);
	}

	public void onError(Throwable t){
		info("Error "+t);
	}
	
	public void info(String str)
	{
		System.out.println(str);
	}

	public List<String> getList()
	{
		return respList;
	}
	
	public static void main(String args[]) throws Exception
	{
		
		String uri = "https://dewdrops-eproc-qcvw.zycus.net/api/a/dd/batch/";
		
			
		String saas_tokenId = "0d944dbe-e154-4ce4-a985-a9c82b16def7";
		String batch_token_id = "e8387e00-718b-11e9-a21b-37ecb030611c-14.143.127.118,%2010.120.0.8";
		
		String url = uri+ batch_token_id;
		String cookie = "SAAS_COMMON_BASE_TOKEN_ID="+saas_tokenId;

		JMeterTester eH = new JMeterTester();

		//String responseList = "";
		
		EventSource.Builder builder = new EventSource.Builder(eH, URI.create(url));
		builder.method("GET");
		
		Headers.Builder headerBuilder = new Headers.Builder();
		headerBuilder.add("Cookie", cookie);
		Headers headers = headerBuilder.build();
		builder.headers(headers);
		
		EventSource eventSource = builder.build();
		
		eventSource.setReconnectionTimeMs(10000);
		eventSource.start();
		TimeUnit.SECONDS.sleep(10);
		eventSource.close();

		for(String respRecord : eH.respList)
		{
//			JsonReader jsonReader = Json.createReader(new StringReader(respRecord));
//			JsonObject jsonObject = jsonReader.readObject();
//			JsonValue title = jsonObject.getValue(args[3]);
//			JsonValue changeType = jsonObject.getValue(args[4]);
//			responseList = responseList + changeType.toString()+" : "+title.toString()+"\n"; '
			
			System.out.println("***********************************");
			System.out.println(respRecord);
		}
		System.out.println("***********************************");
	}
}
	