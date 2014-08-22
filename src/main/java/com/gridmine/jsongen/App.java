package com.gridmine.jsongen;

import com.google.gson.Gson;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int repeat = Integer.parseInt(args[1]);

		Gson gson = new Gson();		
		
		String filePath = args[0];
		JsonGen app = new JsonGen(filePath);
		
		for(int i= 0;i < repeat; i++){
			System.out.println(app.generate());
		}
		
		

	}

}
