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
    private double constant;
    private final double descuento = 0.8;
    double [][] wij;
    double [][] outw;
    double [] input;
    double [] zi;
    double [] outz;
    NeuronLayer capaOculta, capaSalida;
    
    public BackPropagation(int inputUnits, int hiddenUnits, int outputUnits){        
        constant = 0.1;
        n = inputUnits;
        m = hiddenUnits;
        s = outputUnits;        
        double tmp;
        // INICIALIZAR PESOS
        wij = new double [m][n+1];        
        for(int i=0; i< m; i++){
            for(int j=0; j< n+1; j++){
                tmp = Math.random();
                if(tmp<0.3)
                    wij[i][j] = 0.3;
                else
                    wij[i][j] = tmp;
            }
        }
        outw = new double [s][m+1];        
        for(int i=0; i<s; i++)
            for(int j=0; j< m+1; j++)
                outw[i][j] = Math.random();
        
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
        double nuevoDescuento = (Math.pow(descuento,tateti.TaTeTi.jugados + 1));
        for(int i=0;i<s;i++){
            for(int j=0;j<m;j++){
                outw[i][j] += nuevoDescuento *(getConstant() * outz[i] * (1 - outz[i]) * (target - outz[i]) * zi[j]);            
                for(int a=0; a<n+1; a++)
                    wij[j][a] += nuevoDescuento *(getConstant() * zi[j] * (1 - zi[j]) *  (target - outz[i]) * input[a]);
            }                                    
            outw[i][m] += nuevoDescuento * (getConstant()  * outz[i] * (1 - outz[i]) * (target - outz[i]));
        }
        
        capaOculta.setNeuronWeights(wij);
        capaSalida.setNeuronWeights(outw);
        
        return wij[0];
    }

    /**
     * @return the constant
     */
    public double getConstant() {
        return constant;
    }

    /**
     * @param constant the constant to set
     */
    public void setConstant(double constant) {
        this.constant = constant;
    }
}