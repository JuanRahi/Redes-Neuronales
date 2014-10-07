/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import java.util.LinkedList;

/**
 *
 * @author Admin
 */
public interface TaTeTiPlayer {
    public Board chooseMove() throws Exception;
    public void setBoard(Board board);
    public double getUpdateConstant();    
    public void setUpdateConstant(double update);
    public void updateWeights(LinkedList<Board> gameHistory, double[] vTrainValues) throws Exception;
    public double[] getWeights();
}
