*************************************************************************
   By - Ravi Praksh Mishra (B.Tech 3rd Year, NIT Sikkim)
*************************************************************************



########################## HOW TO USE ####################################

Usage: python3 npy2csv.py -i <input-file.npy > -o <output_directory>

##########################################################################




--> Use it to convert weights stored in .npy file to csv files


--> in .npy file weights should be stored as dictionary of layers.
	>  key: should be name of layer
	>  value: numpy array of shape [weights bias] of that layer


--> generates <input-file>.xml file along with csv files of all entries in dictionary

--> xml contains all the details of layers in graph

--> each layer contains location tag which contains absolute path of csv file of weights and bias of layer
	-> convolution (conv) layer has following tags
	   >  k_h : kernel height
	   >  k_w : kernel width
	   >  in_c : input channels
	   >  out_c : output channels


	-> Fully Connected (FC) layer has following tags
	   >  here it is assumed that flatten operation has been performed before feeding input to FC layers
	   >  inputs : no. of input values
	   >  outputs : no. of output neurons
