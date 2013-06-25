package org.raygoza.bx;

import java.io.FileWriter;
import java.util.Vector;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;

public class Gbk2JSON {

public static void main(String[] args)  throws Exception{
		
		GBKReader reader = new GBKReader();
		
		GbkModel model = reader.readGbk(args[0]);
		
	
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		
		Vector<GBKFeature> feats = model.getFeatures();
		
		for(GBKFeature feat: feats) {
			
			JSONObject ft = new JSONObject();
			ft.put("type", feat.getType());
			
			JSONObject json2 = new JSONObject();
			
			Vector<String> keys = feat.getKeys();
			
			for(String j: keys) {
				Vector<String> vals = feat.get(j);
				if(vals.size()==1) {
					json2.put(j, vals.get(0));
				}else {
					JSONArray arr = new JSONArray();
					for(String vl: vals) {
						arr.add(vl);
					}
					json2.put(j, arr);
				}
			}
			
			ft.put("properties", json2);
			array.add(ft);
		}
		
		json.put("features", array);
		
		FileWriter wr = new FileWriter("test.json");
		wr.write(json.toString(4));
		wr.close();
		//GBKWriter wr = new GBKWriter();
		System.out.println("writing");
		//wr.writeGbk(model, args[1]);
		
		
		
	}
	
	
	
}
