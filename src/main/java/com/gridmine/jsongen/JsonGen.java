package com.gridmine.jsongen;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.google.gson.Gson;

public class JsonGen {
	

	String schemaFilepath;
	Map<String,String> schemaMap = new HashMap<String,String>();
	Gson gson = new Gson();
	ValueGen vg = new ValueGen();
	
	public JsonGen(String schemaFilepath){
		this.schemaFilepath = schemaFilepath;
		setUp();
	}
	
	private void setUp(){
		
		String schema = "";
		
		try {
			schema = readFile(schemaFilepath,Charset.defaultCharset());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// create a map of the schema
				
				ObjectMapper mapper = new ObjectMapper();
				try {
					schemaMap = mapper.readValue(schema, new TypeReference<HashMap<String,Object>>(){});
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
		
	}
	
	public String generate(){
		
			Map<String,String> jsonMap = new HashMap<String,String>();
			try{
				for (Entry<String,String> e: schemaMap.entrySet()){
					String fullCall = e.getValue();
					String methodName = fullCall.substring(0, fullCall.indexOf("("));
					String paramString = fullCall.substring(fullCall.indexOf("(") +1, fullCall.indexOf(")"));
					String[] methodArgs = paramString.split(",");
					jsonMap.put(e.getKey(), vg.invoker(methodName, methodArgs));
				}
			}
				catch (Exception e) {
					e.printStackTrace();
				}
			
				return gson.toJson(jsonMap);
			
		}
		
				
	
	private String readFile(String path, Charset encoding) throws IOException {
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

	
	
	

}
