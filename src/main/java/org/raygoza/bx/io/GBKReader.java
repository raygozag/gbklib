package org.raygoza.bx.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;
import org.raygoza.bx.model.KeyValue;
import org.raygoza.bx.model.Reference;
import org.raygoza.bx.model.Source;

public class GBKReader {

	
	Vector<String> feat_types= new Vector<String>();
	
	
	
	public GbkModel readGbk(String filename) throws Exception{
		
		GbkModel model = new GbkModel();
		
		BufferedReader rd = new BufferedReader(new FileReader(filename),32*1024*1024);
		GBKFeature feature =null;
		String line="";
		String state="header";
		while(true) {
			line= rd.readLine();
			if(line==null) break;
			
			
			if(line.startsWith("FEATURES")) {
				state="feat";
				continue;
			}
			
			if(line.startsWith("BASE COUNT")){
				state="seq";
				continue;
			}
			
			if(line.trim().startsWith("ORIGIN")) {
				state="seq";
				continue;
			}
			
			if(state.equals("header")) {
				
				
				
				int idx = line.indexOf(" ");
				if(idx<0) continue;
			
				String key = line.substring(0,12).trim();
				
				if(isCaps(key) && line.startsWith(key)) {
					
					if(key.equals("SOURCE")) {
						Source src = getSource(rd, line);
						model.setSource(src);
						model.addKey("SOURCE");
						continue;
					}
					
					if(key.equals("REFERENCE")) {
						Reference ref = readReference(rd,line);
						model.addReference(ref);
						if(!model.getKeys().contains("REFERENCES")) {
							model.addKey("REFERENCES");
						}
						continue;
					}
					
					
					KeyValue kv = getValue(rd, line);
					model.put(kv.getKey(), kv.getValue());
					model.addKey(kv.getKey());
					
					
				}
				
			
			}
			
			if(state.equals("feat")) {
				
				while(true) {
					
					GBKFeature feat= initFeature(line);
					model.addFeature(feat);
					
					rd.mark(1000);
					
					line= rd.readLine();
					
					while(line.trim().startsWith("/")) {
						
						KeyValue kv = readFeatureProperty(rd,line);
						feat.put(kv.getKey(), kv.getValue());
						
						rd.mark(1000);
						line = rd.readLine();
					}
				
					rd.reset();
					//state="none";
					
					
					
					if(line.trim().startsWith("ORIGIN") || line.startsWith("BASE COUNT")) {
						rd.reset();
						break;
					}
					rd.mark(1000);
					line= rd.readLine();
					
				}
				
			
				
				
			}
			
			if(state.equals("seq")) {
				String seq = IOUtils.toString(rd);
				
				seq=seq.replaceAll("[0-9]+", "").replace(" ", "").replace("\n", "").replace("//", "");
				model.setSequence(seq);
			}
			
			
		}
		
		
		
		rd.close();
		model.fixHash();
		model.sortFeatures();
		return model;
		
	}
	
	private Reference readReference(BufferedReader rd,String line) throws Exception {
		Reference ref = new Reference();
		
		KeyValue kv = getValue(rd, line);
		rd.mark(1000);
		ref.setDefintition(kv.getValue());
		line = rd.readLine();
		
		while(line.startsWith(" ") && !line.trim().equals("REFERENCE")) {
			kv = getValue(rd, line);
			ref.put(kv.getKey(), kv.getValue());
			rd.mark(1000);
			line=rd.readLine();
		}
		rd.reset();
		return ref;
	}
	
	private Source getSource(BufferedReader rd , String line) throws Exception{
		Source src= new Source();
		
		KeyValue kv = getValue(rd, line);
		
		src.setSource(kv.getValue());
		
		rd.mark(1000);
		line= rd.readLine();
		
		kv = getValue(rd, line);
	
		src.setOrganism(kv.getValue());
				
		return src;
	}
	
	
	private GBKFeature initFeature(String line) {
		
		
		String type= line.substring(0, 21).trim();
		String loc = line.substring(21).trim();
		
		GBKFeature feat = new GBKFeature(type);
		String strand="+";
		String[] vals = loc.replace("..", "!").split("!");
		
		if(vals[0].contains("complement")) {
			strand="-";
			vals[0]= vals[0].replace("complement(", "");
			vals[1]= vals[1].replace(")", "");
		}
		
		if(vals[0].contains("<")) {
			feat.setExtends_left(true);
			vals[0]=vals[0].replace("<", "").trim();
		}
		
		if(vals[1].contains(">")) {
			feat.setExtends_right(true);
			vals[1]=vals[1].replace(">", "").trim();
		}
		
		feat.setStart(Integer.parseInt(vals[0]));
		feat.setEnd(Integer.parseInt(vals[1]));
		feat.setStrand(strand);
		
		
		return feat;
	}
	
	private KeyValue getValue(BufferedReader rd, String line) throws Exception{
		KeyValue value = new KeyValue();
		
		String[] values = new String[2]; 
		values[0]= line.substring(0,12).trim();
		values[1]= line.substring(12);
		String vl="";
		if(isCaps(values[0])) {
			value.setKey(values[0].trim());
			vl=StringUtils.stripStart(values[1], " ") ;
		}
		
		rd.mark(1000);
		line = rd.readLine();
		//System.out.println("_"+line.substring(0, 12)+"_");
		while(!isCaps(line.substring(0, 12).trim())) {
			vl+= line.substring(12);
			rd.mark(1000);
			line=rd.readLine();
		}
		rd.reset();
		
		value.setValue(vl);
		
		return value;
	}
	
	private KeyValue readFeatureProperty(BufferedReader rd, String line) throws Exception{
		KeyValue kv = new KeyValue();
		
		line = line.replace("/", "")+" ";
		try {
		String[] vals = line.split("=");
		//System.out.println(vals[0]);
		kv.setKey(vals[0].trim());
		
		if(vals[1].trim().endsWith("\"") || !vals[1].contains("\"")) {
			vals[1]=vals[1].replace("\"", "");
			kv.setValue(vals[1].trim());
			//rd.reset();
			return kv;
		}else {
			rd.mark(1000);
			line = rd.readLine();
			while(!line.endsWith("\"")) {
				vals[1]+=line;
				if(line.trim().startsWith("/")) {
					rd.reset();
					break;
				}
				rd.mark(1000);
				line=rd.readLine();
				
			}
			vals[1]+=line;
			kv.setValue(vals[1].trim().replace("                      ", "").replace("\"", ""));
			
		}
		
	}catch(Exception ex) {
		System.out.println("+"+line+"+" + rd.readLine());
		throw ex;
	}
		
		return kv;
	}
	
	public boolean isCaps(String value) {
		if(value.length()==0) return false;
		return value.equals(value.toUpperCase());
	}
	
}
