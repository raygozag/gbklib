package org.raygoza.bx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.io.GBKWriter;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;

public class UpdateGbk {

	public static void main(String[] args)  throws Exception {
		
		
		BufferedReader rd = new BufferedReader(new FileReader(args[0]));
		
		String line="";
		rd.readLine();
		
		GBKReader reader = new GBKReader();
		
		GbkModel MAP = reader.readGbk(args[1]);
		String key = "";
		while(true) {
			line = rd.readLine();
			if(line==null) break;
			if(line.trim().equals("")) continue;
			
			String[] values = line.trim().split("\t");
			
			if(values.length==3) {
				
				Hashtable<String, String> properties = new Hashtable<String, String>();
				properties.put("type", values[2]);
				properties.put("start",values[0]);
				properties.put("end", values[1]);
				while(true) {
					rd.mark(1000);
					line = rd.readLine();
					if(line==null) break;
					line = line.replace("\t\t\t","");
					String[] vals = line.trim().split("\t");
					
					if(vals.length==2) {						
						properties.put(vals[0], vals[1]);		
					}else {
						rd.reset();
						break;
					}
					
				}
				
				
				
				
				GBKFeature feat=null;
				if(properties.get("type").equals("gene") || properties.get("type").equals("tRNA") || properties.get("type").equals("rRNA")) {
					feat = MAP.getFeature(properties.get("locus_tag"),properties.get("type"));
					if(feat==null) {
						//System.out.println(properties.get("locus_tag") +"  \"" +properties.get("type")+"\"");
						feat = new GBKFeature("gene");
						MAP.addFeature(feat);
					}
				}else if(properties.get("type").equals("CDS")) {
					if(properties.get("protein_id")==null) {
						System.out.println(properties.get("start"));
					}
					String protein_id = properties.get("protein_id").replace("gnl|KapurPSU|", "");
					feat = MAP.getFeature(protein_id,"CDS");
					if(feat==null) {
						//System.out.println(properties.get("locus_tag") +"  \"" +properties.get("type")+"\"");
						feat = new GBKFeature("CDS");
						
						
						MAP.addFeature(feat);
					}
					properties.put("locus_tag", protein_id);
					feat.remove("note");
					feat.remove("gene");
					if(!feat.getKeys().contains("translation")) {
						properties.put("translation", "");
					}
				}
				
				if(feat==null) {
					System.out.println(properties.get("locus_tag") +"  \"" +properties.get("type")+"\"");
					
					
				}
				
				
				int start =Integer.parseInt(properties.get("start"));
				int end = Integer.parseInt(properties.get("end"));
				if(end < start) {
					feat.setStrand("-");
					feat.setStart(end);
					feat.setEnd(start);
				}else {
					feat.setStrand("+");
					feat.setStart(start);
					feat.setEnd(end);
				}
				
				properties.remove("type");
				properties.remove("start");
				properties.remove("end");
				Iterator<String> it = properties.keySet().iterator();
				
				while(it.hasNext()) {
					String key2 = it.next();
					if(key2.equals("db_xref")) continue;
					feat.put(key2.trim().replace(" ", "_"), properties.get(key2).trim());
				}
			}		
		}
		
		
		BufferedReader rd2 = new BufferedReader(new FileReader(args[3]),2*1024*1024);
		
		rd2.readLine();
		
		String seq = IOUtils.toString(rd2).replace("\n", "");
		
		MAP.setSequence(seq);
		
		System.out.println(seq.length());
		
		GBKWriter wr = new GBKWriter();
		
		wr.writeGbk(MAP, args[2]);
		
	}
	
	
}
