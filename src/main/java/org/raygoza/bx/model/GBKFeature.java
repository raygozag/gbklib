package org.raygoza.bx.model;

import java.util.HashMap;
import java.util.Vector;

public class GBKFeature implements Comparable<GBKFeature> {

	HashMap<String, String> properties;
	String feat_type;
	String strand="+";
	int start;
	int end;
	Vector<GbkLocation> locations;
	Vector<String> keys;
	
	public GBKFeature(String type) {
		properties= new HashMap<String, String>();
		feat_type=type;
		keys = new Vector<String>();
		locations = new Vector<GbkLocation>();
	}
	
	public void put(String key,String value) {
		properties.put(key, value);
		if(!keys.contains(key)) {
			keys.add(key);
		}
	}
	
	public String get(String key) {
		return properties.get(key);
	}
	
	public Vector<String> getKeys(){
		return keys;
	}
	
	public String getType() {
		return feat_type;
	}
	
	public void setStrand(String str) {
		strand =str;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getStrand() {
		return strand;
	}


	
	public void remove(String key) {
		properties.remove(key);
		keys.remove(key);
	}
	
	@Override
	public int compareTo(GBKFeature o) {
		if(o.start < this.start) return 1;
		if(o.start > this.start) return -1;
		if(o.start==this.start && o.end==this.end) return 0;
		
		return 0;
	}

	public void addLocation(GbkLocation loc) {
		locations.add(loc);
	}
	
	public Vector<GbkLocation> locations(){
		return locations;
	}
	
	
}
