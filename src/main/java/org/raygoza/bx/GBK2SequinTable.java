package org.raygoza.bx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;

public class GBK2SequinTable {

	public static void main(String[] args)  throws Exception{
		
		GBKReader rd = new GBKReader();
		
		GbkModel model= rd.readGbk(args[0]);
		Vector<String> types = new Vector<String>();
		types.add("gene");
		types.add("CDS");
		types.add("tRNA");
		Vector<String> keys = new Vector<String>();
		keys.add("protein_id");
		keys.add("locus_tag");
		keys.add("note");
		keys.add("translation");
		keys.add("gene_desc");
		keys.add("product");
		BufferedWriter wr = new BufferedWriter(new FileWriter(args[1]));
		
		Vector<GBKFeature> feats = model.getFeatures();
		
		for(GBKFeature feat:feats) {
			if(types.contains(feat.getType())) {
				
				if(feat.getStrand().equals("+")) {
					wr.write(feat.getStart()+"\t"+feat.getEnd()+"\t"+feat.getType()+"\n");
				}else {
					wr.write(feat.getEnd()+"\t"+feat.getStart()+"\t"+feat.getType()+"\n");
				}
				
				Vector<String> kkeys = feat.getKeys();
				
				for(String key: kkeys) {
					
					if(!keys.contains(key)) continue;
					wr.write("\t\t\t"+key+"\t"+feat.get(key)+"\n");
				}
				if(feat.getType().equals("CDS")) {
					//wr.write("\t\t\ttransl_table\t11\n");
				}
				
				
			}
			
		}
		
		
	wr.close();	
	}
	
	
	
}
