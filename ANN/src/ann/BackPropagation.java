/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ann;


/**
 *
 * @author juan
 */
public class BackPropagation {
    private int n;
    private int m;
    private int s;
    private final double constant = 0.1;    
    double [][] wij;
    double [][] outw;
    double [] input;
    double [] zi;
    double [] outz;
    NeuronLayer capaOculta, capaSalida;
    
    public BackPropagation(int inputUnits, int hiddenUnits, int outputUnits){        
        n = inputUnits;
        m = hiddenUnits;
        s = outputUnits;        
        
        // INICIALIZAR PESOS CON VALORES OBTENIDOS EN TAREA 1
        wij = new double [m][n+1];        
        for(int i=0; i< m; i++){
            wij[i][0] = 14.296025377586968;
            wij[i][1] = 0.8587015586144036;
            wij[i][2] = 12.879713603905838;
            wij[i][3] = 1.200823683287427;
            wij[i][4] = 4.7046462897541605;
            wij[i][5] = 2.7292736238399526;
            
        }
        outw = new double [s][m+1];        
        for(int i=0; i<s; i++)
            for(int j=0; j< m+1; j++)
                outw[i][j] = -1.0 + (Math.random()*(1 - (-1)));
        
        capaOculta = new NeuronLayer(wij);
        capaSalida = new NeuronLayer(outw);
    }
    
    public double run(double[] features){

        input = features;
        // ADELANTE - CAPA OCULTA  
        capaOculta.setNeuronInput(input);          
        zi = capaOculta.getResult();

        // ADELANTE - CAPA SALIDA
        capaSalida.setNeuronInput(zi);
        outz = capaSalida.getResult();
        
        return outz [s-1];
    }
    
    public double[] updateWeigths(double target){
        for(int i=0;i<s;i++){
            for(int j=0;j<m;j++){
                outw[i][j] += (constant * outz[i] * (1 - outz[i]) * (target - outz[i]) * zi[j]);            
                for(int a=0; a<n+1; a++)
                    wij[j][a] += (constant * zi[j] * (1 - zi[j]) *  (target - outz[i]) * input[a]);
            }                                    
            outw[i][m] += (constant  * outz[i] * (1 - outz[i]) * (target - outz[i]));
        }
        
        capaOculta.setNeuronWeights(wij);
        capaSalida.setNeuronWeights(outw);
        
        return wij[0];
    }
}