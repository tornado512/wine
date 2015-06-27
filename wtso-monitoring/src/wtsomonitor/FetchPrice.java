package wtsomonitor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FetchPrice {

	//Containers for WTSO response
	
	private String varietal_limit, _varietal, _country, _alcohol_content, _unit_size, _vintage, _grape, _region;
	private double p, price_limit, _price;
	private boolean flag = false;
	private final String TARGET = "https://api.import.io/store/data/182d2498-839b-47c4-a719-ac514667d0e5/_query?input/webpage/url=http%3A%2F%2Fwtso.com%2F&_user=c7cc483e-0a43-4123-941e-40dfb391b080&_apikey=c7cc483e-0a43-4123-941e-40dfb391b080%3AUeA1Yf4BikAGU4yiUXvRl%2BMsMyVuvuxTJlH4iKtLRM3uZ5zTRdg68o%2Bjp%2BoxJg5U%2FiAyIqTI2VefOjUsAZ2W7Q%3D%3D";
	private final LinkedList<String> JSONFIELDS = new LinkedList<String> (Arrays.asList("country", "alcohol_content", "unit_size", "vintage", "grape", "region"));
	
	public FetchPrice(double price, String varietal){
		this.price_limit = price;
		this.varietal_limit = varietal;
	}
	
	void process () throws Exception{
		
		GetWebData(TARGET);
		
		if(flag == true && p < price_limit && varietal_limit.equalsIgnoreCase(_varietal)){
			System.out.println("Email invoked");
			
			String username = "notificationsAKD";
			String password = "Airplane3";
			String recipientEmail = "tornado512@gmail.com";
			String title = "WTSO Notification";
			String message = "Current WTSO listing\n\n"
					+ "Country: " + _country
					+ "\n Alcohol Content: " + _alcohol_content
					+ "\n Unit Size: " + _unit_size
					+ "\n Vintage: " + _vintage
					+ "\n Grape: " + _grape
					+ "\n Region: " + _region
					+ "\n Price: " + p
					+ "\n \n www.wtso.com";
					
			SendEmail.Send(username, password, recipientEmail, title, message);
			System.out.println("SendEmail invoked");
		}
	}
	
	void GetWebData(String target) throws Exception{
		//Create client,get, response and execute GET Request
		CloseableHttpClient httpClient = HttpClients.createDefault(); 
		HttpGet httpGet = new HttpGet(target);
		CloseableHttpResponse WTSOresponse = httpClient.execute(httpGet);
		
		try{
			OutputWriter(WTSOresponse, httpClient);
			} catch (Exception e){
				throw e;
			} finally{
				WTSOresponse.close();
				httpGet.releaseConnection();
				httpClient.close();
				
				System.out.println("Connections closed");
			}
	}
	
	void OutputWriter(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws Exception{
		HttpEntity httpEntity = httpResponse.getEntity();
		
		if(!(httpResponse.getStatusLine().getStatusCode()==200)){
			String badResponse = "Bad resposne :: Status Code :: " + httpResponse.getStatusLine().getStatusCode() ;
			System.out.println("Unable to retrieve web data.  " + badResponse);
		}
		else{
			
				LinkedList<String> WTSOData = new LinkedList<String>();
				LinkedList<String> OldData = new LinkedList<String>();
				
				JSONTokener jsonTokener  = new JSONTokener(httpEntity.getContent());
				JSONObject jsonObject = new JSONObject(jsonTokener);
				JSONArray jsonArray = jsonObject.getJSONArray("results");
				jsonObject = jsonArray.getJSONObject(0);
				
				WTSOStorage DataStorage = new WTSOStorage();
				JSONObject StoredData = DataStorage.GetOutput();
				
				Iterator<String> i = JSONFIELDS.iterator(); 
				while (i.hasNext()){
					String key = i.next().toString();
					WTSOData.add(jsonObject.getString(key));
					OldData.add(StoredData.getString(key));
				}
				
				p = jsonObject.getDouble("price");
				WTSOData.add(String.valueOf(p));
				
				_price = StoredData.getDouble("price");
				OldData.add(String.valueOf(_price));
				
				System.out.println(WTSOData);
				
				if (WTSOData.equals(OldData)){
					System.out.println("Duplicate data.");
				} else{
					flag = true;
					System.out.println("New wine found");
					DataStorage.WriteOutput(jsonObject);
					_varietal = jsonObject.getString("varietal");
					_country = jsonObject.getString("country");
					_alcohol_content = jsonObject.getString("alcohol_content");
					_unit_size = jsonObject.getString("unit_size");
					_vintage = jsonObject.getString("vintage");
					_grape = jsonObject.getString("grape");
					_region = jsonObject.getString("region");
					
				}
			}//else
		}//outputwriter
}//FetchPrice
	
