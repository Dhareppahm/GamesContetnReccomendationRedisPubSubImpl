package com.bridgelabz.contentRec.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ListOfCategoriesInformation {
	Set s1 = new TreeSet();
	int flag = 0;
	/**
	 * This method is used to check category exist or not
	 * 
	 * @param String,
	 *            is the first parameter for this method contains category name
	 * @return boolean,true or false
	 */
	public boolean categoryExist(String category) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(new FileReader("/home/bridgeit/categoryList.json"));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject object = (JSONObject) obj;
		JSONArray data = (JSONArray) object.get("categories");
		for (int i = 0; i < data.size(); i++) {
			JSONObject itemObj = (JSONObject) data.get(i);
			Object oob = itemObj.get("categoryName");
			String str = (String) oob;
			s1.add(str);
		}
		if (s1.contains(category)) {
			return true;
		} else
			return false;
	}
}
