package input;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.imageio.ImageIO;
import java.awt.image.Raster;


public class Input
{

/*********************************************************************/   

    //read  image and return 3D array of pixel values 
    // [image_channels, image_height, image_width ]
    public static float[][][] getImageArray(String path)
    {

        File file = new File(path);
        BufferedImage img = null;

        try 
        {
            //read image file
            img = ImageIO.read(file);

        }

        catch (IOException e) 
        {

            System.err.println("ERROR READING IMAGE FILE");
            e.printStackTrace();

        }
        // get width and height of input image
        int width = img.getWidth();
        int height = img.getHeight();
        Raster ras = img.getRaster();
        // get no. of channels in image i.e 3 for RGB and 1 for grayscale
        int channel  = ras.getNumDataElements(); 
        
        float [][][] input  = new float[channel][height][width];
        Raster raster = img.getData();
        System.out.println("height: " + height + " width:" + width + " channels: " + channel);
        
    
        for (int i = 0; i < height; i++) 
        {
        
            for (int j = 0; j < width; j++) 
            {

                for(int c = 0; c < channel; c++ )
                {

                input[c][i][j] =  (float) (255 - raster.getSample(j, i, c));

                }

            }
            
        }
            return input;
    }



/**************************************************************************/


     // read weight and bias from CSV file and returns a 2D array of float type
     public static float[][] loadCSV(String path, Boolean header, Boolean serial) {
        // header  = true  if csv contains header
        // serial will be true if csv file has row numbering
        //Vector<Vector<Float>> s = null;
        float [][] input_array  = null;
        BufferedReader br = null;

        try 
        {

            br = new BufferedReader(new FileReader(path));

        } 

        catch (FileNotFoundException e)
        {

            e.printStackTrace();

        }

        String line;
        List<float[]> list = new ArrayList<float[]>();

        try 
        {
            int len = 0;
            if(header== true)
            {

            line = br.readLine();
            //split using COMMA_DELIMITER
            String[] values = line.split(",");

            } 
            int index = 0;
            if(serial == true){
                index = 1;
            }
            while ((line = br.readLine()) != null) {
              
               String[] values = line.split(",");
              len = values.length;

                float [] val = new float[len-1];
                for(int i = index; i < values.length; i++)
                {   
                    
                    //add element to row
                     val[i-1] = Float.parseFloat(values[i]);

                }
                list.add(val); //add row to list
                
            } 
            //close CSV file
            br.close();  

            input_array = new float[list.size()][len];  

            //convert list of float array to array of array 
            input_array = list.toArray(input_array); 
         
        }

        catch (IOException e) 
        {

            e.printStackTrace();

        }
            
            return input_array;
    }



      // extracts bias from loaded CSV and returns 2D array
    // shape of bias = [output channel in_channel]
    public static float[] getBias(float [][] data, int c_out)
    {
        float [] bias = new float[c_out];
        for (int i = 0; i < c_out; i++) 
        {
           

              bias[i] = data[i][data[i].length - 1];
       
            
        }
        return bias;
    }



    // extracts weights from loaded CSV and returns 2D array
    // shape of weight = [output_channel kernel_height * kernel_width * input_channel]
    public static float [][] getWeight(float[][] data, int c_in, int c_out) {
         
        float [][] weight = new float[c_out][data[0].length -1];
        for (int i = 0; i < c_out; i++) {

            for (int j = 0; j < data[i].length - 1; j++) 
            {
                
                weight[i][j] = data[i][j];

            }
        }
        return weight;
    }



    // covert weight from [o_c, filter_height * filter_width * filter_channel]
    // to [filter_height, filter_width, input_channel, output_channel]
    public static float [][][][] reshape(float [][] weights, int k_h,int k_w,int in_c,int o_c)
    {

        float [][][][] input = new float [k_h][k_w][in_c][o_c];
        for(int l=0;l<o_c;l++)
        {   
            for(int k = 0;k<in_c;k++)
            {
                for(int i = 0;i<k_h;i++)
                {

                    for(int j = 0;j<k_w;j++)
                    {

                        input[i][j][k][l] = weights[l][(k*(k_w*k_h)) + i*k_w + j ];

                    }
                }
            }
        }

        return input;

    }
    
    //overloaded method for reshaping weights of FC laayer
    // return matrix of shape [inputs outputs]
    public static float [][] reshape(float [][] weights,int inputs,int outputs)
    {

        float [][] input = new float[inputs][outputs];
    
                for(int i = 0;i<inputs;i++)
                {
                    for(int j = 0;j<outputs;j++)
                    {
                        input[i][j] = weights[j][i];
                    }
                }
            

        return input;

    }
 
}
