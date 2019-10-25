package input;


public class FC extends Layer
{

    int inputs;
    int outputs;

    float [][] weights;
    float [] bias;

    FC(int inputs, int outputs, float[][] weights, float[] bias){
        
        super.type = "fc";
        this.inputs = inputs;
        this.outputs = outputs;
        this.weights = weights;
        this.bias = bias;
        
    }

    public float[][] getWeight(){


            return weights;

    }

    public float[] getBias(){

            return bias;

    }
 





}
