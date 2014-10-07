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

public class ANN {

    // TAMAÑO ENTRADA
    private static int n = 1;
    // TAMAÑO SALIDA
    private static int s = 1;
    // UNIDADES EN LA CAPA OCULTA
    private static int m = 2;
    // CONJUNTO DE ENTRENAMIENTO
    private static int d = 20;
    // CTE DE APRENDIZAJE
    private static double constant = 0.1;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // GENERAR ENTRADA
        int mode = 0;
        int iteraciones = 1000;
        double error = 0.0;
        double [][] input = new double [d][n+1];
        double ini = -1.0;
        double fin = 1.0;
        double range = fin - ini;
        for(int i=0; i< d; i++){
            for(int j=0; j<n; j++){
                input[i][j] = ini;                
            }
            input[i][n] = 1.0;                
            ini += (range/d);
        }
                
        // INICIALIZAR PESOS           
        double [][] wij = new double [m][n+1];        
        for(int i=0; i< m; i++)
            for(int j=0; j<n+1; j++)                
                wij[i][j] = -1.0 + (Math.random() * (1 - (-1)));

        
        double [][] outw = new double [s][m+1];        
        for(int i=0; i<s; i++)
            for(int j=0; j< m+1; j++)
                outw[i][j] = -1.0 + (Math.random() * (1 - (-1)));
        
        NeuronLayer capaOculta = new NeuronLayer(wij);
        NeuronLayer capaSalida = new NeuronLayer(outw);
                
        
        double [] ni = new double[m];
        double [] outn = new double[s];
        double [] zi = new double[m];
        double [] outz = new double[s];
        double [] delta = new double[m];
        double [] outdelta = new double[s];
        
        
        for (int l= 0; l < iteraciones; l++){            
            error = 0.0;
            
            // COMIENZA EL ALGORITMO
            for(int k=0; k<d;k++){            

                // ADELANTE - CAPA OCULTA  
                capaOculta.setNeuronInput(input[k]);          
                zi = capaOculta.getResult();

                // ADELANTE - CAPA SALIDA
                capaSalida.setNeuronInput(zi);
                outz = capaSalida.getResult();

                // ATRAS
                for(int i=0;i<s;i++){
                    outdelta[i] = outz[i] * (1 - outz[i]) * (expectedResult(input[k], mode) - outz[i]);
                    for(int j=0;j<m;j++)
                        delta[j] = zi[j] * (1 - zi[j]) *  (expectedResult(input[k], mode) - outz[i]);
                }

                error += ((expectedResult(input[k], mode) - outz[s-1]) * (expectedResult(input[k], mode) - outz[s-1]))/2;
                

                // ACTUALIZAR PESOS
                for(int i=0; i< m; i++)
                    for(int j=0; j<n+1; j++)
                        wij[i][j] += (constant * delta[i] * input[k][j]);

                capaOculta.setNeuronWeights(wij);

                for(int i=0; i<s; i++){
                    for(int j=0; j< m; j++)
                        outw[i][j] += (constant * outdelta[i] * zi[j]);                   
                    outw[i][m] += (constant * outdelta[i]);
                }
                capaSalida.setNeuronWeights(outw);
                
                //if(k == 0)
                    //System.out.printf("%.2f\n", outz[s-1]);                    

            }
            
            System.out.printf("%.2f\n", error);                    
            //System.out.println(error);
            
        }
        
    }

    /*private static void print(int k, double[] zi, double[][] wij, double[] outdelta, double[][] outw, double [] outz, double[] input) {
        // PRINT
        System.out.println("******* K: " + k + " ********");
        for(int i=0; i< m; i++){
            System.out.println("z: " + zi[i]);
            for(int j=0; j<n; j++)
                System.out.println(i + "-" + j +": " + wij[i][j]);
            
        }
        
        for(int i=0; i<s; i++){
            System.out.println("delta: " + outdelta[i]);
            System.out.println("entrada: " + input[i]);
            System.out.println("salida: " + outz[i]);
            for(int j=0; j< m; j++)
                System.out.println(i + "-" + j +": " + outw[i][j]);
        }
    }*/
    
    static double expectedResult(double[] sample, int mode){
        if (mode == 0)
            return sample[n-1];
        else if(mode == 1)
            return ((sample[n-1]) * (sample[n-1]) * (sample[n-1])* (sample[n-1]));
        else
            return Math.sin((3.0/2.0) * Math.PI * sample[n-1]);
    }
}