/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ann;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Neuron {
    private double[] weights;
    private double[] input;

    public Neuron (double [] weights) {
        this.weights = weights;
    }

    public void setWeights (double[]weights) {
        this.weights = weights;
    }
    
    public void setInput (double[] input) {
        this.input = input;
    }

    public double[] getWeights () {
        return weights;
    }
    
    public double getResult(){
        double y= 0.0;
        //weigths.length tiene el expected result
        for(int j=0; j<weights.length -1; j++){
            y += (weights[j]) * input[j];
        }
        return (1.0 / (1 + Math.exp(-y)));             
    }
}
