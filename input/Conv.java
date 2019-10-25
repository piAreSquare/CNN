package input;

public class Conv extends Layer
{

    int k_h;
    int k_w;
    int in_c;
    int out_c;
    float [][][][] weights;
    float [] bias;

    Conv(int k_h, int k_w, int in_c, int out_c,float[][][][] weights, float[] bias){
        
        super.type = "conv";
        this.k_h = k_h;
        this.k_w = k_w;
        this.in_c = in_c;
        this.out_c = out_c;
        this.weights = weights;
        this.bias = bias;
        
    }

    public float[][][][] getWeight(){


            return weights;

    }

    public float[] getBias(){

            return bias;

    }
 

}
