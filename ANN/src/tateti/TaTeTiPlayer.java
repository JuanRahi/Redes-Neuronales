/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

/**
 *
 * @author Admin
 */
public interface TaTeTiPlayer {
    public Board chooseMove() throws Exception;
    public void setBoard(Board board);
    
}
