/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ann;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Admin
 */
public class NeuronLayer {
    LinkedList<Neuron> neuronList;
    
    public NeuronLayer(){
        neuronList = new LinkedList<>();
    }
    
    public NeuronLayer(double [][] weights){
        neuronList = new LinkedList<>();
        for(int i = 0; i < weights.length; i++){
            neuronList.add(new Neuron(weights[i]));
        }
    }
    
    public void addNeuron(Neuron neuron){
        neuronList.add(neuron);
    }
    
    public double[] getResult(){
        double [] output = new double[neuronList.size()];
        Iterator it = neuronList.iterator();
        for(int i =0; i<neuronList.size(); i++)
            output[i] = neuronList.get(i).getResult();
        return output;
    }
    
    public void setNeuronInput(double [] input){
        Iterator it = neuronList.iterator();
        while(it.hasNext()){
            ((Neuron)it.next()).setInput(input);
        }
    }
    
    public void setNeuronWeights(double [][] weights){
        Iterator it = neuronList.iterator();
        int i = 0;
        while(it.hasNext()){
            ((Neuron)it.next()).setInput(weights[i]);
            i++;
        }
    }
}
