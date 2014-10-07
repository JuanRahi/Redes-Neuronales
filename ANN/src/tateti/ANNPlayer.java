/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import ann.BackPropagation;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author juan
 */
public class ANNPlayer implements TaTeTiPlayer {
        
    TaTeTi.CellValue cellValue;
    Board currentBoard;
    ExperimentGenerator eg;
    boolean print = false;
    BackPropagation backPropagation;
    double updateConstant;
    
    public ANNPlayer(Board currentBoard, TaTeTi.CellValue cellValue){
        this.cellValue = cellValue;
        this.currentBoard = currentBoard;
        this.eg = new ExperimentGenerator();
        backPropagation = new BackPropagation(6, 2, 1);
    }
    
    public Board chooseMove() throws Exception{
        int ties = 1;
        double bestValue = Double.NEGATIVE_INFINITY, currentValue;
        Board bestMove = null, currentMove;
        double[] results = new double[TaTeTi.MAX_CELLS*TaTeTi.MAX_CELLS];
        LinkedList<Board> availableMoves = eg.getAvailableMovesForPlayer(cellValue, currentBoard);
        Iterator it = availableMoves.iterator();
        Random rand = new Random();        
        while (it.hasNext()){
            currentMove = (Board)it.next();
            currentValue = vEstimate(currentMove, false);
            if(currentValue >= bestValue){
                if(currentValue == bestValue){ 
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
    }
    
    public void setBoard(Board board){
        this.currentBoard = board;
    }
    
    public double vEstimate(Board board, boolean print) throws Exception{
        double[] feature = eg.getBoardFeatures(board, cellValue);        
        board.puntaje = backPropagation.run(feature);        
        return board.puntaje;
    }

    
    public void updateWeights(LinkedList<Board> gameHistory, double[] vTrainValues) throws Exception{
        double vTrain, vEst;
        Board board;
        for(int i = 0; i < gameHistory.size(); i++){
            vTrain = vTrainValues[i];
            board = gameHistory.get(i);
            vEst = board.puntaje;            
            if(TaTeTi.print)
                System.out.println("puntaje: " + vEst + ", vTrain: " + vTrain);
            
            backPropagation.updateWeigths(vTrain);
        }            
    }
    
    
    public void setUpdateConstant(double update){
        updateConstant= update;
    }
    
    
    public double getUpdateConstant(){
        return updateConstant;
    }
}
