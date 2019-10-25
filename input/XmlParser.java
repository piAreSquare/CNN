package input;
import input.Input;
import input.Layer;
import input.Conv;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlParser
{
    int layerCount;
    String [] layerNames;   
    private Document doc;
    public HashMap<String, Layer> layerHash;
	
	
    public String[] getlayerNames()
	{
		return layerNames;
	}

    public HashMap<String, Layer> getHashMap(){
        
        return layerHash;

    }

    public XmlParser(String path){


        this.parseFile(path);
        this.setValues();
    }

    void parseFile(String fileName){

        try 
		{
			File file = new File(fileName);
			DocumentBuilderFactory DBFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DBuilder = DBFactory.newDocumentBuilder();
			doc = DBuilder.parse(file);
			doc.getDocumentElement().normalize();
						
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
            System.err.println("ERROR READING XML FILE");
            System.exit(1);
		}
        
    }

    void setValues(){
 
    NodeList layers = doc.getElementsByTagName("layers");
    String count = layers.item(0).getTextContent();
    System.out.println(count);
    layerCount = Integer.parseInt(count);
    layerHash = new HashMap<String, Layer>(layerCount);


    layerNames = new String[layerCount];

    NodeList layerList = doc.getElementsByTagName("layer");

    for(int i= 0 ; i < layerList.getLength(); i++){

        Element layer = (Element)layerList.item(i);	
        layerNames[i] = layer.getAttribute("name");
        String type = layer.getElementsByTagName("type").item(0).getTextContent();
        String csv_path = layer.getElementsByTagName("csv_file").item(0).getTextContent();
        System.out.println(csv_path);
        float[][] wb = Input.loadCSV(csv_path, true, true);
        System.out.println("hello");

        
        if(type.equals("conv")){
            System.out.println("1hello");

            int in_c = Integer.parseInt(layer.getElementsByTagName("in_c").item(0).getTextContent());
            System.out.println(in_c);
            int out_c = Integer.parseInt(layer.getElementsByTagName("out_c").item(0).getTextContent());
            int k_h = Integer.parseInt(layer.getElementsByTagName("k_h").item(0).getTextContent());
            int k_w = Integer.parseInt(layer.getElementsByTagName("k_w").item(0).getTextContent());

            Layer conv = new Layer();
            conv.setType(type);
            
            float[][][][] weight = Input.reshape(Input.getWeight(wb, in_c, out_c), k_h, k_w, in_c,out_c);
            float[] bias = Input.getBias(wb, out_c);
            conv = conv.getObject(k_h, k_w, in_c, out_c, weight, bias);
            weight = ((Conv)conv).getWeight();
            System.out.println("sdjhfkdsfs" + weight[0][0][0][0]);
            layerHash.put(layerNames[i], conv);

        }

        else if(type == "fc"){

            int inputs = Integer.parseInt(layer.getElementsByTagName("inputs").item(0).getTextContent());
            int outputs = Integer.parseInt(layer.getElementsByTagName("outputs").item(0).getTextContent());


            Layer fc = new Layer();
            fc.setType(type);
            
            float[][] weight = Input.reshape(Input.getWeight(wb, inputs, outputs) ,inputs,outputs);
            float[] bias = Input.getBias(wb, outputs);
            fc = fc.getObject(inputs,outputs,weight, bias);
            layerHash.put(layerNames[i], fc);

        }

    }

    }

}
