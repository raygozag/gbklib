package org.raygoza.bx;

import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.raygoza.bx.io.GBKReader;
import org.raygoza.bx.model.GbkModel;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;



public class GBK2Xml {
	
	public static void main(String[] args)  throws Exception{
		
		
		GBKReader rd = new GBKReader();
		
		GbkModel model = rd.readGbk(args[0]);
		
		
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        
        Element root = doc.createElement("Seq-entry");
        
        Element entry = doc.createElement("Seq-entry_set");
        
        Element set = doc.createElement("Bioseq-set");
        
        Element bclass = doc.createElement("Bioseq-set_class");
        bclass.setAttribute("value","nuc-prot");
        
        Element desc = doc.createElement("Bioseq-set_descr");
        Element sdesc= doc.createElement("Seq-descr");
        
        
        desc.appendChild(sdesc);
        set.appendChild(bclass);
        set.appendChild(desc);
        entry.appendChild(set);
        
        root.appendChild(entry);
        
        doc.appendChild(root);
        
        /////////////////
        //Output the XML

        //set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();

        //print xml
        FileWriter wr = new FileWriter(args[1]);
        wr.write(xmlString);
        wr.close();
		
		
		
		
		
	}
	
	

}
