package wtsomonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

public class WTSOStorage {	
	private String path = System.getProperty("user.dir");
	private String name = "test.txt";
	private String fqpath = path + name;
	
	
	
	JSONObject GetOutput() throws Exception {

		FileReader fin = new FileReader(fqpath);
		
		try{
			JSONTokener token = new JSONTokener(fin);
			JSONObject obj = new JSONObject(token);
			return obj;
		} catch(Exception e){
			throw e;
			
		} finally{
			fin.close();
		}
	}
	
	void WriteOutput(JSONObject StoredData) throws JSONException, IOException{

		String fqpath = System.getProperty("user.dir") + "test.txt";
		
		File WTSOFile = new File(fqpath);
		
		FileWriter fout = new FileWriter(WTSOFile);
		
		fout.write(StoredData.toString());
		fout.close();
	}
}
