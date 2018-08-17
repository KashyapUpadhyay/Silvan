package com.divum.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.divum.callback.DimmerLightCallback;
import com.divum.callback.SensorCallback;
import com.divum.callback.UpdateProileCallback;
import com.divum.callback.VDBCallback;
import com.divum.callback.VDPCallback;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

public class BaseParser {

	String TAG="BaseParser";
	String url="";
	String response="";
	private String errorString="";
	static int mTimeOut = 20000;

	String payload;
	private boolean isHide=false;

	private VDBCallback vdbCallback;
	private VDPCallback vdpCallback;
	private UpdateProileCallback profileCallback;
	private DimmerLightCallback dimmerCallback;
	private SensorCallback sensorCallback;

	public BaseParser(String url){
		this.url=url;
	}
	public BaseParser(String url,String payload){
		this.url=url;
		this.payload=payload;
	}

	public BaseParser(String url, boolean _isHide) {
		this.url=url;
		isHide=false;

	}


	public JSONObject getJsonObject(){
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		try {
			String resp="";
			errorString="";
			try{
				resp=getJSONFeedFromServer(url,mTimeOut);//doHttpGet(url);//
				//CustomLog.d(TAG, "getJSONFeedFromServer55");
				resp=resp.replace("&apos;", "'");
				//CustomLog.d(TAG, "getJSONFeedFromServer66");
				if(resp.trim().equals("")){
					resp="{}";
				}

				JSONObject jsonObject = new JSONObject(resp);
				//CustomLog.d(TAG, "getJSONFeedFromServer77");
				return jsonObject;
			}catch (JSONException e) {
				CustomLog.e("JSONObject", "JSONException="+e);
				e.printStackTrace();
				errorString=""+e;
			}catch (Exception e) {
				CustomLog.e(TAG, "Exception="+ e);
				errorString=""+e;

			}
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return null;
	}

	public String getException(){
		return errorString;
	}

	public JSONObject getAuthJsonObject(){
		CustomLog.e(TAG, "url->"+url);
		try {
			String resp="";
			try{
				resp=doHttpGet(url);
				//CustomLog.d(TAG, "getJSONFeedFromServer55");
				resp=resp.replace("&apos;", "'");
				//CustomLog.d(TAG, "getJSONFeedFromServer66");
				JSONObject jsonObject = new JSONObject(resp);
				//CustomLog.d(TAG, "getJSONFeedFromServer77");
				return jsonObject;
			}catch (JSONException e) {
				CustomLog.e("JSONObject", ""+e);
				e.printStackTrace();
			}catch (Exception e) {
				CustomLog.e(TAG, ""+ e);
			}
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return null;
	}

	public String getAuthResponse(){
		CustomLog.e(TAG, "url->"+url);

		String resp="";
		try{
			resp=doHttpGet(url);
		}catch (Exception e) {
			CustomLog.e(TAG, "" + e);
		}
		return resp;
	}

	public String getResponse(){
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}

		String resp="";
		errorString="";
		try{
			resp=getJSONFeedFromServer(url, mTimeOut);
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			errorString=""+e;
		}
		return resp;
	}

	public void getResponseVDBCallback(int timeout, Context context,Intent intent) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		vdbCallback = (VDBCallback)context;
		try{
			if(vdbCallback!=null){
				vdbCallback.VDBResponse(getJSONFeedFromServer(url,timeout),intent);
			}
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			vdbCallback.VDBException(""+e);
		}

	}

	public void getResponseVDPCallback(int timeout, Context context, String vdbType,Intent intent) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		vdpCallback = (VDPCallback)context;
		try{
			if(vdpCallback!=null){
				vdpCallback.VDPResponse(getAuthResponse(),intent);
				//vdpCallback.VDPResponse(getJSONFeedFromServer(url,timeout));			
			}
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			vdpCallback.VDPException("" + e, vdbType);
		}

	}

	public void getResponseProfileCallback(int timeout, Context context) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		profileCallback = (UpdateProileCallback)context;
		try{
			if(profileCallback!=null){
				profileCallback.ProfileUpdateResponse(getJSONFeedFromServer(url, timeout));
			}
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			profileCallback.ProfileUpdateException("" + e);
		}
	}

	public void getResponseDimmerCallback(int timeout, Context context) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		dimmerCallback = (DimmerLightCallback)context;
		try{
			dimmerCallback.DimmerStatusResponse(getJSONFeedFromServer(url,timeout));			
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			dimmerCallback.DimmerStatusException("" + e);
		}

	}
	
	public void getResponseSensorCallback(int timeout, Context context) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		sensorCallback = (SensorCallback)context;
		try{
			sensorCallback.SensorStatusResponse(getJSONFeedFromServer(url, timeout));
		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			sensorCallback.SensorStatusException("" + e);
		}

		
	}

	public String getTimeOutResponse(int timeout) {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}

		String resp="";
		errorString="";
		try{

			resp=getJSONFeedFromServer(url,timeout);

		}catch (Exception e) {
			CustomLog.e(TAG, "getResponse="+ e);
			errorString=""+e;
		}
		return resp;
	}



	public String getJsonResponse(){
		return response;

	}

	public JSONArray getJsonArray(){
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		try {
			String resp="";
			try{
				resp=getJSONFeedFromServer(url,mTimeOut);//doHttpGet(url);//
				//	CustomLog.d(TAG, "getJSONFeedFromServer55");
				resp=resp.replace("&apos;", "'");

				//	CustomLog.d(TAG, "getJSONFeedFromServer66");
				JSONArray jsonArray = new JSONArray(resp);
				CustomLog.d(TAG, "getJSONFeedFromServer77");
				return jsonArray;
			}catch (JSONException e) {
				CustomLog.e("JSONObject", ""+e);
				e.printStackTrace();
			}catch (Exception e) {
				CustomLog.e(TAG, ""+ e);
			}
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			CustomLog.e(TAG, ""+ e1);
			e1.printStackTrace();
		}		
		return null;
	}

	public JSONArray getAuthJsonArray(){
		CustomLog.e(TAG, "url->"+url);
		try {
			String resp="";
			try{
				resp=doHttpGet(url);
				//CustomLog.d(TAG, "getJSONFeedFromServer55");
				resp=resp.replace("&apos;", "'");

				//CustomLog.d(TAG, "getJSONFeedFromServer66");
				JSONArray jsonArray = new JSONArray(resp);
				//CustomLog.d(TAG, "getJSONFeedFromServer77");
				return jsonArray;
			}catch (JSONException e) {
				CustomLog.e("JSONObject", ""+e);
				e.printStackTrace();
			}catch (Exception e) {
				CustomLog.e(TAG, ""+ e);
			}
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			CustomLog.e(TAG, ""+ e1);
			e1.printStackTrace();
		}		
		return null;
	}

	public synchronized String getJSONFeedFromServer(String inURL,int timeOut) throws Exception
	{
		//	CustomLog.d(TAG, "getJSONFeedFromServer11");
		HttpUriRequest ConnectionURI = new HttpGet(inURL);
		ConnectionURI.addHeader("Accept-Encoding","gzip");


		HttpParams HttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(HttpParams, timeOut);
		HttpConnectionParams.setSoTimeout(HttpParams, timeOut);

		//	CustomLog.d(TAG, "getJSONFeedFromServer22");

		HttpClient HTTPClient = new DefaultHttpClient(HttpParams);
		HttpResponse HTTPResponse = HTTPClient.execute(ConnectionURI);



		InputStream InpStream = HTTPResponse.getEntity().getContent();
		Header Encoding = HTTPResponse.getFirstHeader("Content-Encoding");

		//	CustomLog.d(TAG, "getJSONFeedFromServer33");


		//Check if the server response is GZip encrypted
		if(Encoding != null && Encoding.getValue().equalsIgnoreCase("gzip"))
			InpStream = new GZIPInputStream(InpStream);

		StringBuilder StrResp = new StringBuilder();

		final char[] cBuffer = new char[0x10000];
		Reader InReader = new InputStreamReader(InpStream);
		int iRead;
		do {
			iRead = InReader.read(cBuffer, 0, cBuffer.length);
			if (iRead > 0) {
				StrResp.append(cBuffer, 0, iRead);
			}
		} while (iRead >= 0);
		InpStream.close();


		response=StrResp.toString();
		CustomLog.camera(TAG, "mood:"+response);
		//System.out.println("sensor baseparser response:" + response);
		return response;
	}


	public String doHttpGet(String url) throws ClientProtocolException, IOException {
		String resp=null;
		try{
			HttpClient httpclient = new DefaultHttpClient();

			HttpGet httpget = new HttpGet(url); 
			//			httpget.setHeader("Content-Type","application/json");
			//			httpget.setHeader("Accept","application/json");

			httpget.setURI(new URI(url));

			UsernamePasswordCredentials credentials =
					new UsernamePasswordCredentials("admin", "admin");
			BasicScheme scheme = new BasicScheme();
			Header authorizationHeader = scheme.authenticate(credentials, httpget);
			httpget.addHeader(authorizationHeader);

			HttpResponse response;
			response = httpclient.execute(httpget);
			//Log.i("Response Status ",response.getStatusLine().getStatusCode()+"");



			HttpEntity entity = response.getEntity();
			if (entity != null) {
				resp=convertResponseToString(response, entity);	
			}
		}catch (Exception e) {
			// TODO: handle exception
			CustomLog.e(TAG, ""+ e);
		}
		//System.out.println("vdpresponse::"+response);
		response=resp;
		return resp;
	}

	private String convertResponseToString(HttpResponse response, HttpEntity entity) {
		String resp="";
		try{
			InputStream inputStream = entity.getContent();
			if(response.containsHeader("Content-Encoding")&&response.getLastHeader("Content-Encoding").toString().contains("gzip")){
				//Log.e(TAG, "GZipped data");
				GZIPInputStream  gzip = new GZIPInputStream (inputStream);
				StringBuffer  szBuffer = new StringBuffer ();
				byte  tByte [] = new byte [1024];
				while (true)
				{
					int  iLength = gzip.read (tByte, 0, 1024); 
					if (iLength < 0)
						break;
					szBuffer.append (new String (tByte, 0, iLength));
				}
				resp=szBuffer.toString();
			}else{

				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
				//Log.e(TAG, "Normal data");
				StringBuffer responseString = new StringBuffer();
				String temp = null;
				while((temp = in.readLine()) != null){
					responseString.append(temp);
				}
				resp=responseString.toString();
				//System.out.println("resp->"+resp);
			}	
		}catch (Exception e) {
			CustomLog.e(TAG, "Error while convertResponseToString "+e);
		}
		return resp;
	}


	public String getPOSTResponse(){
		CustomLog.latest(TAG, "url->"+url);

		String resp="";
		errorString="";
		try{
			resp=postMethod(url,payload);
		}catch (Exception e) {
			CustomLog.latest(TAG, ""+ e);
			errorString=""+e;
		}
		return resp;
	}

	public String postMethod(String url, String payload) throws Exception{
		String response="";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		ResponseHandler <String> resonseHandler = new BasicResponseHandler();
		HttpPost postMethod = new HttpPost(url);
		postMethod.setHeader("Content-Type", "application/json");
		CustomLog.e("mood","url-post--postUrl-----"+url+"----");
		CustomLog.latest("post----payload---"+payload+"---");
		postMethod.setEntity(new ByteArrayEntity(payload.getBytes("UTF8")));
		response = httpClient.execute(postMethod,resonseHandler);

		CustomLog.latest("post----response---"+response+"---");

		return response;
	}

	public String postHTTP(String url, String payload) throws IOException, JSONException{
		//String response;
		HttpPost httpPost = new HttpPost(url);
		CustomLog.e("mood","post url----------------------" + url+"\n"+payload);
		//httpPost.addHeader("Accept-Encoding","gzip");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		CustomLog.parser("post url-----------11");

		dos.writeBytes(payload); 
		dos.flush(); 
		dos.close();
		CustomLog.parser("post url-----------22");

		ByteArrayInputStream content = new ByteArrayInputStream(baos.toByteArray());
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(content);

		CustomLog.parser("post url-----------33");

		httpPost.setEntity(entity);
		CustomLog.parser("post url-----------33 11");


		HttpParams HttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(HttpParams, mTimeOut);
		HttpConnectionParams.setSoTimeout(HttpParams, mTimeOut);

		HttpClient httpClient = new DefaultHttpClient(HttpParams);
		CustomLog.parser("post url-----------33 22"+httpPost);

		HttpResponse response = httpClient.execute(httpPost);

		CustomLog.parser("post url-----------44"+response);

		InputStream InpStream = response.getEntity().getContent();
		//	Header Encoding = response.getFirstHeader("Content-Encoding");

		//Check if the server response is GZip encrypted
		/*if(Encoding != null && Encoding.getValue().equalsIgnoreCase("gzip"))
			InpStream = new GZIPInputStream(InpStream);*/

		CustomLog.parser("post url-----------55");

		StringBuilder StrResp = new StringBuilder();
		try
		{
			//int length =InpStream
			//final char[] cBuffer = new char[0x10000];
			BufferedReader InReader =new BufferedReader(new InputStreamReader(InpStream));
			/*int iRead;
			do {
				iRead = InReader.read(cBuffer, 0, cBuffer.length);
				if (iRead > 0) {
					StrResp.append(cBuffer, 0, iRead);
				}
			} while (iRead >= 0);*/
			String line = null;

			// Read Server Response
			while((line = InReader.readLine()) != null)
			{
				// Append server response in string
				StrResp.append(line + "\n");
			}

			InpStream.close();
			InReader.close();
		}
		catch (Error inErr) 
		{
			CustomLog.e("Error:", "" + inErr);
			InpStream.close();
			inErr.printStackTrace();
		}

		CustomLog.parser("post reponse----------------------" + StrResp.toString());

		return StrResp.toString();
	}


	public JSONObject getMoodJsonObject() {
		if(!isHide){
			CustomLog.e(TAG, "url->"+url);
		}
		try {
			String resp="";
			errorString="";
			try{
				resp=getJSONFeedFromServer(url,mTimeOut);//doHttpGet(url);//
				//CustomLog.d(TAG, "getJSONFeedFromServer55");
				resp=resp.replace("&apos;", "'");
				//CustomLog.d(TAG, "getJSONFeedFromServer66");
				if(resp.trim().equals("")){
					resp="{}";
				}

				JSONObject jsonObject = new JSONObject(resp);
				//CustomLog.d(TAG, "getJSONFeedFromServer77");
				return jsonObject;
			}catch (JSONException e) {
				CustomLog.e("JSONObject", "JSONException=" + e);
				e.printStackTrace();
				response="";
				JSONObject jsonObject = new JSONObject("");
				return  jsonObject;

			}catch (Exception e) {
				CustomLog.e(TAG, "Exception="+ e);
				errorString=""+e;

			}
		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
