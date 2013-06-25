package org.raygoza.bx;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.io.GBKWriter;
import org.raygoza.bx.model.GbkModel;

public class GBKTest {

	
	public static void main(String[] args)  throws Exception{
		
		GBKReader reader = new GBKReader();
		
		GbkModel model = reader.readGbk(args[0]);
		
		
		
		//GBKWriter wr = new GBKWriter();
		System.out.println("writing");
		//wr.writeGbk(model, args[1]);
		
		
		
	}
	
	
}
