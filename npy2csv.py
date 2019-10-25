from numpy import *
import xml.etree.ElementTree as ET
import xml.dom.minidom as minidom
import sys, getopt, os
import ntpath
from pathlib import Path
'''
.npy file should contain weights and bais of all layers in form of dictionary

'''


def addLayer(graph,key, layer_type, shape, out_path):
    #add information about layer in xml
    layer = ET.SubElement(graph, 'layer') 
    layer.set('name',key)  
    t = ET.SubElement(layer, 'type')
    location = ET.SubElement(layer, 'csv_file')
    location.text = str(Path(out_path).resolve())+"/"+key+".csv"
    t.text = layer_type

    #if layer type is conv it will have four parameters
    if layer_type == 'conv':
        #kernel height
        k_h = ET.SubElement(layer, 'k_h')
        k_h.text = shape[0]

        #kernelwidth
        k_w = ET.SubElement(layer, 'k_w')
        k_w.set('name','k_w' )
        k_w.text = shape[1]

        #inpput channels
        in_c = ET.SubElement(layer, 'in_c')
        in_c.text = shape[2]

        #output channels
        out_c = ET.SubElement(layer, 'out_c')
        out_c.text = shape[3]

    #no. of inputs and outputs for FC layer
    else:
        in_c = ET.SubElement(layer, 'inputs')
        in_c.text = shape[0]

        out_c = ET.SubElement(layer, 'outputs')
        out_c.text = shape[1] 
    
    
    return graph  



def load_npy(path, out_path):

    #load the npy file 
    net_data = load(open(path, "rb"), encoding="latin1").item()
    
    #crete xml 
    graph = ET.Element('graph')
    layers = ET.SubElement(graph, 'layers')
    
    #get keys of the dictionary
    keys  = list(net_data.keys())
    layers.text = str(len(keys))

    #read weights and bias from npy file and store in csv format for each layer 
    for key in keys:

        weight = net_data[key][0]
        shape = list(weight.shape)
        x = list( map(str, shape) )
       
        bias = net_data[key][1]
        s = list(bias.shape)
          
        layer_type = "conv"
        if len(shape) == 4:
            w = weight.reshape( shape[3], shape[1]*shape[2]*shape[0])      
            
        else:
            layer_type = "fc"
            w = weight.reshape(shape[1], shape[0])

        b = bias.reshape(s[0], 1)
    
        w = concatenate((w, b), axis=1)
        print("*********************************")
        print(str(key)+":\n")
        print("shape of weight: "+ str(shape) + "\n" + "shape of bias: "+ str(s) + "\n")
        print("dimensions of 2D matrix "+ str(key) + ": " + str(w.shape))
        print("*********************************")

        #add the info of layer in xml
        graph = addLayer(graph, key, layer_type, x,out_path) 
       
        try:

            #import pandas library
            import pandas as pd
            pd.DataFrame(w).to_csv(out_path +"/"+ key+".csv")

        
        except:
            #save using numpy library if pandas not found, not adding header
            savetxt(out_path +"/"+ key +".csv", w, delimiter=",")

    
    xml = minidom.parseString(ET.tostring(graph)).toprettyxml(indent="\t")
    f = open(out_path + "/"+path_leaf(path)[:-3]+"xml", "w+")
    f.write(xml)  



#returns file name from the path
def path_leaf(path):
    head, tail = ntpath.split(path)
    return tail or ntpath.basename(head)


#driver function
def main(argv):
    inputfile = ''
    outputdir = ''

    try:
        opts, args = getopt.getopt(argv,"hi:o:",["help","ifile=","o_dir="])
    except getopt.GetoptError:
        print('npy2csv.py -i <inputfile> -o <outputdir>')
        sys.exit(1)

    for opt, arg in opts:
        if opt in ('-h', "--help"):
            print('npy2csv.py -i <inputfile> -o <outputdir>')
            sys.exit(2)
        elif opt in ("-i", "--ifile"):
            inputfile = arg
        elif opt in ("-o", "--o_dir"):
            outputdir = arg

    print('Input file is ', inputfile)
    print( 'Output directory is ', outputdir)

    #check if output directory exists, create if not 
    if not os.path.exists(outputdir):
        os.makedirs(outputdir)

    load_npy(inputfile, outputdir)



if __name__ == "__main__":
    main(sys.argv[1:])
    
