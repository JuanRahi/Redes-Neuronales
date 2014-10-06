/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.CellEditor;

/**
 *
 * @author Admin
 */
public class Critic {
    double constant, w0, w1, w2, w3, w4, w5, /*w6, w7*/ updateConstant;
    ExperimentGenerator eg;
    
    public Critic(){
        this.eg = new ExperimentGenerator();
        constant = 1;
        w0 = 1;
        w1 = 1;
        w2 = 1;
        w3 = 1;
        w4 = 1;
        w5 = 1;
        //w6 = .5;
        //w7 = .5;
        updateConstant = 0.0001;
    }
    
    public void updateWeights(double[] values, double newConstant)
    {
        constant = newConstant;
        w0 = values[0];
        w1 = values[1];
        w2 = values[2];
        w3 = values[3];
        w4 = values[4];
        w5 = values[5];            
    }
    
    public double evaluateBoard(Board board, TaTeTi.CellValue cellValue) throws Exception{
        double[] feature = eg.getBoardFeatures(board, cellValue);
        return constant + w0*feature[0] - w1*feature[1] + w2*feature[2] - w3*feature[3] + w4*feature[4] - w5*feature[5]; //+ w2*feature[6] + w3*feature[7];
    }
        
      public double[] getTrainingValues(LinkedList<Board> gameHistory, TaTeTi.CellValue player) throws Exception{
        double[] trainValues = new double[gameHistory.size()];
        Iterator it = gameHistory.iterator();
        Board board;
        TaTeTi.CellValue oponent = player.equals(TaTeTi.CellValue.X) ? TaTeTi.CellValue.O : TaTeTi.CellValue.X;
        for(int i=0; i<gameHistory.size(); i++){
            board = (Board)it.next();
            if(board.isFinished()){                
                if(board.hasCellValueWon(player))
                    trainValues[i] = 100;
                else if(board.hasCellValueWon(oponent))
                    trainValues[i] = -100;
                else if (board.isFull())
                    trainValues[i] = 0;
            }
            else{
                    if(i+2 > gameHistory.size()-1){
                       if(gameHistory.get(gameHistory.size()-1).hasCellValueWon(oponent))
                           trainValues[i] = -100;
                       else if (gameHistory.get(gameHistory.size()-1).hasCellValueWon(player))
                           trainValues[i] = 100;
                       else
                           trainValues[i] = 0;
                    }                       
                    else
                        trainValues[i] = evaluateBoard(gameHistory.get(i+2), player);                   
            }
        }
            
        return trainValues;
    }
     
   
}
