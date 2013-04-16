package org.raygoza.bx.model;

import java.util.HashMap;
import java.util.Vector;

public class GBKFeature implements Comparable<GBKFeature> {

	HashMap<String, String> properties;
	String feat_type;
	String strand="+";
	int start;
	int end;
	boolean extends_left=false;
	boolean extends_right=false;
	Vector<String> keys;
	
	public GBKFeature(String type) {
		properties= new HashMap<String, String>();
		feat_type=type;
		keys = new Vector<String>();
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

	public boolean isExtends_left() {
		return extends_left;
	}

	public void setExtends_left(boolean extends_left) {
		this.extends_left = extends_left;
	}

	public boolean isExtends_right() {
		return extends_right;
	}

	public void setExtends_right(boolean extends_right) {
		this.extends_right = extends_right;
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
	
}
