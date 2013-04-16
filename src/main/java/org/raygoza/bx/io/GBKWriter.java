package org.raygoza.bx.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;
import org.raygoza.bx.model.Reference;

public class GBKWriter {

	
	public void writeGbk(GbkModel model,String filename) throws Exception{
		
		BufferedWriter wr = new BufferedWriter(new FileWriter(filename));
		
		HashMap<String, String> props = model.getProperties();
		
		Iterator<String> keyss = model.getKeys().iterator(); 
		
		while(keyss.hasNext()) {
			String k= keyss.next();
			
			if(!k.equals("REFERENCES") && !k.equals("SOURCE")) {
				wr.write(insertPeriodically(StringUtils.rightPad(k, 12) + props.get(k),"\n            ",80)+"\n");
				continue;
			}
			
			if(k.equals("REFERENCES")) {
				Vector<Reference> references = model.getReferences();
				
				for(Reference ref : references) {
					wr.write(insertPeriodically(StringUtils.rightPad("REFERENCE", 12) + ref.getDefintition(),"\n            ",80)+"\n");
					Vector<String> keys = ref.keys();
					for(String key: keys) {
						wr.write(insertPeriodically(StringUtils.rightPad("  "+key, 12) + ref.get(key),"\n            ",80)+"\n");
					}
				}
				
			}
			
		}
		
		
		
		
		wr.write(StringUtils.rightPad("FEATURES", 21)+"Location/Qualifiers\n");
		
		Vector<GBKFeature> features = model.getFeatures();
		
		for(GBKFeature feature: features) {
			
			String prefix="";
			String suffix="";
			String start_prefix="";
			String end_suffix="";
			if(feature.isExtends_left()) {
				start_prefix="<";
			}
			
			if(feature.isExtends_right()) {
				end_suffix=">";
			}
			
			if(feature.getStrand().equals("-")) {
				prefix= "complement(";
				suffix=")";
			}
			wr.write("     "+StringUtils.rightPad(feature.getType(),16) +prefix+start_prefix+feature.getStart()+".."+feature.getEnd()+end_suffix+suffix+"\n");
			Vector<String> keys = feature.getKeys();
			
			for(String key: keys) {
				String value = "                     "+formatProperty("/"+key+"=\""+feature.get(key)+"\"");
				
				
				wr.write(value+"\n");
			}
			
		}
		
		
		wr.write("ORIGIN\n");
		
	//	wr.write(formatSequence(model.getSequence())+"\n\n//");
		
		
		wr.close();
	}
	
	
	private static String formatSequence(String seq)  throws Exception{
		String tmp = insertPeriodically(insertPeriodically(seq," ",10),"\n",66).replace(" \n", "\n");
		String res= "";
		BufferedReader rd  = new BufferedReader(new StringReader(tmp));
		
		String line ="";
		int i=1;
		while(true) {
			line= rd.readLine();
			if(line==null) break;
			res+= StringUtils.leftPad(Integer.toString(i), 9)+" "+line.toLowerCase()+"\n";
			i+=60;
			
		}
		
		
		return res;
	}
	
	private String formatProperty(String str) {
		
		if(str.length()<61) return str;

		String res = insertPeriodically(str, "\n                     ", 60);
		
		return res;
	}
	
	public static String insertPeriodically(
		    String text, String insert, int period)
		{
		    StringBuilder builder = new StringBuilder(
		         text.length() + insert.length() * (text.length()/period)+1);

		    int index = 0;
		    String prefix = "";
		    while (index < text.length())
		    {
		        // Don't put the insert in the very first iteration.
		        // This is easier than appending it *after* each substring
		        builder.append(prefix);
		        prefix = insert;
		        builder.append(text.substring(index, 
		            Math.min(index + period, text.length())));
		        index += period;
		    }
		    return builder.toString();
		}
	
	
	
}
