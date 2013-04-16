package org.raygoza.bx;

import java.util.Vector;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;

public class GBKGeneFiller {

	
	public static void main(String[] args)  throws Exception{
		
		GBKReader rd = new GBKReader();
		
		GbkModel MAP = rd.readGbk(args[0]);
		
		Vector<GBKFeature>  proteins = MAP.getFeaturesByType("CDS");
		
		for(GBKFeature feat: proteins) {
			
			String tr = feat.get("translation");
			
			if(tr==null || tr.trim().equals("")) {
				System.out.println(feat.get("protein_id"));
			}
			
		}
		
		
		
	}
	
	
	
}
