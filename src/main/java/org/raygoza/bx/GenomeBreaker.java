package org.raygoza.bx;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.model.GbkModel;

public class GenomeBreaker {

	public static void main(String[] args) throws Exception{
		
		GBKReader rd = new GBKReader();
		
		GbkModel model = rd.readGbk(args[0]);
		
		String seq = model.getSequence();
		
		String[] fragments = seq.split("(?<=\\G.{11})");
		
		System.out.println(fragments.length);
		
	}
	
	
}
