/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class PerformanceSystem implements TaTeTiPlayer{
    
    double constant, w0, w1, w2, w3, w4, w5, /*w6, w7,*/ updateConstant;
    TaTeTi.CellValue cellValue;
    Board currentBoard;
    ExperimentGenerator eg;
    boolean print = false;
    
    public PerformanceSystem(Board currentBoard, TaTeTi.CellValue cellValue){
        this.cellValue = cellValue;
        this.currentBoard = currentBoard;
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
    
    public PerformanceSystem(Board currentBoard, TaTeTi.CellValue cellValue, double[] weigths, double initConstant){
        this.cellValue = cellValue;
        this.currentBoard = currentBoard;
        this.eg = new ExperimentGenerator();
        constant = initConstant;
        w0 = weigths[0];
        w1 = weigths[1];
        w2 = weigths[2];
        w3 = weigths[3];
        w4 = weigths[4];
        w5 = weigths[5];
        //w6 = .5;
        //w7 = .5;
        updateConstant = 0.0001;
    }
    
    public void printWeights(){        
        System.out.println("constant: " + constant);
        System.out.println("updateConstant: " + updateConstant);
        System.out.println("w0: " + w0);
        System.out.println("w1: " + w1);
        System.out.println("w2: " + w2);
        System.out.println("w3: " + w3);
        System.out.println("w4: " + w4);
        System.out.println("w5: " + w5);        
        System.out.println();
        System.out.println("=============================================");
        System.out.println();
    }
    
        
    public double[] getWeights(){
        double [] result = new double [6];
        result[0] = w0;
        result[1] = w1;
        result[2] = w2;
        result[3] = w3;
        result[4] = w4;
        result[5] = w5;                    
        return result;
    }
    
    public void setBoard(Board board){
        this.currentBoard = board;
    }
    
    public double vEstimate(Board board, boolean print) throws Exception{
        double[] feature = eg.getBoardFeatures(board, cellValue);
        if(print){
            for(int i=0; i<feature.length;i++)
                if(feature[i]!=0)
                    System.out.print("w" + i + ": " + feature[i] + ", ");
            System.out.println();
        }
        board.puntaje = constant + w0*feature[0] - w1*feature[1] + w2*feature[2] - w3*feature[3] + w4*feature[4] - w5*feature[5]; //+ w6*feature[6] + w7*feature[7];
        return board.puntaje;
    }
    
    
    public Board chooseMove() throws Exception{
        int ties = 1;
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;
        Board bestMove = null, currentMove;
        double[] results = new double[TaTeTi.MAX_CELLS*TaTeTi.MAX_CELLS];
        LinkedList<Board> availableMoves = eg.getAvailableMovesForPlayer(cellValue, currentBoard);
//        if(currentBoard.isEmpty()){
//            int random = new Random().nextInt(TaTeTi.MAX_CELLS * TaTeTi.MAX_CELLS);
//            return availableMoves.get(random);
//        }   
//        else{
//            }
            Iterator it = availableMoves.iterator();
            Random rand = new Random();
            while (it.hasNext()){
                currentMove = (Board)it.next();
                currentValue = vEstimate(currentMove, false);
                if(currentValue >= bestValue){
                    if(currentValue == bestValue){ // TODO: revisar
                        bestMove = (Math.random()>=(0.5/ties)) ? bestMove : currentMove;
                        ties++;
                    }
                    else{
                        bestMove = currentMove;
                    }
                    bestValue = currentValue;
                }
            }
            for(int i=0; i<availableMoves.size();i++)
                results[i] = vEstimate(availableMoves.get(i),false);
            if(TaTeTi.print){
                for(int i=0; i<availableMoves.size();i++){
                    if(results[i]==bestValue){
                        System.out.println("Movida con puntaje maximo: ");
                        vEstimate(availableMoves.get(i),true);
                        availableMoves.get(i).print();
                        System.out.println("puntaje: " + results[i]);
                    }
                }
                if(bestMove!=null){
                    System.out.println("Movida elegida: ");
                    bestMove.print();
                    System.out.println("value for best move: " + bestValue);
                    vEstimate(bestMove, true);
                }
            }
            return bestMove;
//        }
    }
    
    
    public void updateWeights(LinkedList<Board> gameHistory, double[] vTrainValues) throws Exception{
        double vTrain, vEst;
        Board board;
        for(int i = 0; i < gameHistory.size(); i++){
            vTrain = vTrainValues[i];
            board = gameHistory.get(i);
            vEst = board.puntaje;
            double[] feature = eg.getBoardFeatures(board, cellValue);
            if(TaTeTi.print)
                System.out.println("puntaje: " + vEst + ", vTrain: " + vTrain);
            constant = constant + updateConstant*(vTrain - vEst);
            w0 = w0 + updateConstant*(vTrain - vEst)*feature[0];
            w1 = w1 + updateConstant*(vTrain - vEst)*feature[1];
            w2 = w2 + updateConstant*(vTrain - vEst)*feature[2];
            w3 = w3 + updateConstant*(vTrain - vEst)*feature[3];
            w4 = w4 + updateConstant*(vTrain - vEst)*feature[4];
            w5 = w5 + updateConstant*(vTrain - vEst)*feature[5];
            //w6 = w6 + updateConstant*(vTrain - vEst)*feature[6];
            //w7 = w7 + updateConstant*(vTrain - vEst)*feature[7];
        }            
    }
    
    
    public void setUpdateConstant(double update){
        updateConstant= update;
    }
    
    
    public double getUpdateConstant(){
        return updateConstant;
    }
}
