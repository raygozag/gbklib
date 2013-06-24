package org.raygoza.bx.utils;

import org.apache.commons.lang.StringUtils;

public class GbkStringUtils {

	public String wrapAsciiPropertyValue(String value, boolean quote,int padding, int length) {
		
		String res="";
		
		String[] tokens = value.trim().split(" ");
		String line = StringUtils.repeat(" ", padding);
		for(int i=0; i < tokens.length;i++) {
			if((line+" "+tokens[i]).length() <= length) {
				line+=" "+tokens[i];
			}else {
				res+= line+"\n";
				line = StringUtils.repeat(" ", padding)+tokens[i];
			}
		}
		if(!line.trim().equals("")) {
			res+="\n"+line;
		}
		
		return res;
	}
	
	
}
