package org.raygoza.bx.model;

import java.util.HashMap;
import java.util.Vector;

public class Reference {

	private String defintition;
	private HashMap<String, String> values= new HashMap<String, String>();
	private Vector<String> keys = new Vector<String>(); 
	
	
	public Reference() {
		
	}
	
	public void put(String key, String val) {
		values.put(key, val);
		keys.add(key);
	}
	
	public String get(String key) {
		return values.get(key);
	}
	
	public Vector<String> keys(){
		return keys;
	}
	public String getDefintition() {
		return defintition;
	}
	public void setDefintition(String defintition) {
		this.defintition = defintition;
	}
	
	
	
}
