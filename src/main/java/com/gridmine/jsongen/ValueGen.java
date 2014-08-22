package com.gridmine.jsongen;

import java.util.Random;

import org.fluttercode.datafactory.impl.DataFactory;

import simplelatlng.LatLng;

public class ValueGen {

	DataFactory df = new DataFactory();
	Random r = new Random();


	public String invoker(String methodName,String[] args){

		switch(methodName){

			case "getInt" : return getInt(args);
			case "getEmail" : return getEmail();
			case "getName" : return getName();
			case "getLatitude" : return getLatitude();
			case "getLongitude" : return getLongitude();
			case "getFromList": return getFromList(args);
			case "getDouble": return getDouble(args);

		}


		return "";

	}


	private String getFromList(String[] args) {
		// TODO Auto-generated method stub

		return df.getItem(args);


	}
	
	
	private String getDouble(String[] args){
		
		int rangeMin = Integer.parseInt(args[0]);
		int rangeMax = Integer.parseInt(args[1]);
		
		
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return String.valueOf(randomValue);
	}


	private String getLongitude() {
		// TODO Auto-generated method stub
		return String.valueOf(LatLng.random().getLongitude());

	}


	private String getName() {
		// TODO Auto-generated method stub
		return df.getName();
	}


	public String  getInt(String[] args){

		if(args!=null & args[0].length()>0){	
			return String.valueOf(df.getNumberBetween(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
		}
		else{
			return String.valueOf(df.getNumber());
		}


	}

	public String getEmail(){
		return df.getEmailAddress();
	}

	public String getLatitude(){
		return String.valueOf(LatLng.random().getLatitude());

	}

	



}
