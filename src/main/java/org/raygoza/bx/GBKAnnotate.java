package org.raygoza.bx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.io.GBKWriter;
import org.raygoza.bx.model.GBKFeature;
import org.raygoza.bx.model.GbkModel;
import org.raygoza.bx.ncbi.Efetch;

public class GBKAnnotate {

	public static void main(String[] args)  throws Exception {
		
		HashMap<String,String> map = new HashMap<String, String>();
		
		double threshold = Double.parseDouble(args[2]);
		
		
		BufferedReader rd = new BufferedReader(new FileReader(args[1]));
		String line ="";
		
		while(true) {
			line = rd.readLine();
			if(line==null) break;
			
			String[] elements = line.split("\t");
			
			Properties props = new Properties();
			props.load(IOUtils.toInputStream(elements[8].replace(";", "\n")));
			
			double score = Double.parseDouble(props.getProperty("score"));
			
			if(score>threshold) {
				map.put(elements[0], props.getProperty("ID").replace(".1",""));
			}
			
			
			
		}
		
		GBKReader gbkr= new GBKReader();
		GbkModel model = gbkr.readGbk(args[0]);
		
		Vector<GBKFeature> feats = model.getFeatures();
		Efetch fetch = new Efetch();
		int i=0;
		for(GBKFeature feature: feats) {
			
			
			if(feature.getType().equals("gene") && map.containsKey(feature.get("locus_tag"))) {
				
				String name =fetch.getGeneName(map.get(feature.get("locus_tag")));
				feature.put("gene_desc", name.replace("[Mycobacterium tuberculosis H37Rv]", ""));
				
			}
			
			if(feature.getType().equals("CDS") && map.containsKey(feature.get("protein_id"))) {
				//String name =fetch.getGeneName(map.get(feature.get("protein_id")));
				//feature.put("product", name.replace("[Mycobacterium tuberculosis H37Rv]", ""));
			}
			
			
			
		}
		
		GBKWriter wr = new GBKWriter();
		wr.writeGbk(model, args[3]);
		System.out.println(i);
		
	}
	
	
	
}
