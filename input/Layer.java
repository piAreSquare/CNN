package input;
import input.*;

public class Layer
{

    String type;
    
    public String getType()
    {
        return type;

    }

    public void setType(String type){
        this.type = type;
    }

    public Layer getObject(int inputs, int outputs, float[][] weight, float[] bias){

       
            return   (new FC(inputs, outputs, weight, bias));


    }

    public Layer getObject(int k_h, int k_w, int in_c, int out_c,float[][][][] weight, float[] bias){

       
        return   (new Conv(k_h, k_w, in_c,out_c,weight,bias));


}


}




