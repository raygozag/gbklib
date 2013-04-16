package org.raygoza.bx.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class GbkModel {

	private HashMap<String, String> properties;
	private HashMap<String,GBKFeature> features;
	private HashMap<String, Vector<GBKFeature>> featuresByType;
	private Vector<GBKFeature> featuresV;
	private String sequence="";
	private Vector<Reference> references;
	private Source source;
	private Vector<String> features_hash= new Vector<String>();
	private Vector<String> keys= new Vector<String>();
	
	public GbkModel() {
		properties = new HashMap<String, String>();
		features = new HashMap<String, GBKFeature>();
		featuresV = new Vector<GBKFeature>();
		references = new Vector<Reference>();
		featuresByType = new HashMap<String, Vector<GBKFeature>>();
		source = null;
	}
	
	public void addKey(String key) {
		keys.add(key);
	}
	
	public Vector<String> getKeys(){
		return keys;
	}
	
	
	public void addFeature(GBKFeature feat) {
		
		
		
		featuresV.add(feat);
		
		if(featuresByType.containsKey(feat.getType())) {
			Vector<GBKFeature> Vfeatures = featuresByType.get(feat.getType());
			Vfeatures.add(feat);
		}else {
			Vector<GBKFeature> Vfeatures = new Vector<GBKFeature>();
			Vfeatures.add(feat);
			featuresByType.put(feat.getType(), Vfeatures);
		}
		
		
	}
	
	public void fixHash() {
		
		for(GBKFeature feat: featuresV) {
			
			if(feat.getType().equals("gene") || feat.getType().equals("tRNA") || feat.getType().equals("rRNA")) {
				features.put(feat.get("locus_tag")+"_"+feat.getType(), feat);
			}else if(feat.getType().equals("CDS")) {
				features.put(feat.get("protein_id")+"_CDS", feat);
			}
			
		}
		
	}
	
	public void put(String key, String value) {
		properties.put(key, value);
	}
	
	public HashMap<String, String> getProperties(){
		return properties;
	}
	
	public Vector<GBKFeature> getFeatures(){
		return featuresV;
	}


	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public void addReference(Reference ref) {
		references.add(ref);
	}
	
	public GBKFeature getFeature(String key,String type) {
		return features.get(key+"_"+type);
	}
	
	public Vector<GBKFeature> getFeaturesByType(String type){
		return featuresByType.get(type);
	}

	public void setSource(Source src) {
		source=src;
	}
	
	public void sortFeatures() {
		Collections.sort(featuresV);
	}
	
	public Vector<Reference> getReferences(){
		return references;
	}
	
}
