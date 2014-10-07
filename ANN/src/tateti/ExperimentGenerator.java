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
public class ExperimentGenerator {
   
    boolean inc = true;
    
    public boolean isCellFree(int x, int y, Board board) throws Exception{
        return board.getCell(x, y).equals(TaTeTi.CellValue._);        
    }
    
    public boolean isCellTakenByPlayer(int x, int y, Board board, TaTeTi.CellValue player) throws Exception{
        return board.getCell(x, y).equals(player);        
    }
        
    public LinkedList<Board> getAvailableMovesForPlayer(TaTeTi.CellValue player, Board board) throws Exception{
        LinkedList<Board> result = new LinkedList<Board>();
        Board newBoard;
        for(int i=0; i<TaTeTi.MAX_CELLS; i++) {
            for(int j=0; j<TaTeTi.MAX_CELLS; j++){
                if(isCellFree(i, j, board)){
                    newBoard = new Board(board);
                    newBoard.setCell(player, i, j);
                    result.add(newBoard);
                }
            }
        }
        return result;
    }
    
    public double[] getBoardFeatures(Board board, TaTeTi.CellValue player) throws Exception{
        double[] result = new double[7];
        for(int k=0; k<7; k++)
            result[k]=0;
        TaTeTi.CellValue oponent = player.equals(TaTeTi.CellValue.X) ? TaTeTi.CellValue.O : TaTeTi.CellValue.X;
               
        //Checkeo de posibilidades features para filas
        int i = 0, j = 0, playerCount = 0, oponentCount = 0, emptyCells = 0;
        while(i<TaTeTi.MAX_CELLS){
            while(j<TaTeTi.MAX_CELLS){
                if(isCellTakenByPlayer(i, j, board, player))
                    playerCount++;
                else
                    if(isCellFree(i, j, board))
                        emptyCells++;
                    else
                        oponentCount++;
                        
                j++;
            }
            updateResults(playerCount, oponentCount, emptyCells, result );
            i++;
            j=0;
            playerCount = 0; emptyCells =0; oponentCount=0;
        }

        //Checkeo de posibilidades de ganar en alguna columna
        i = 0; j = 0; playerCount = 0; oponentCount = 0; emptyCells = 0;
        while(j<TaTeTi.MAX_CELLS){
            while(i<TaTeTi.MAX_CELLS){
                if(isCellTakenByPlayer(i, j, board, player))
                    playerCount++;
                else
                    if(isCellFree(i, j, board))
                        emptyCells++;
                    else
                        oponentCount++;
                        
                i++;
            }
            updateResults(playerCount, oponentCount, emptyCells, result );
            i=0;
            j++;
            playerCount = 0; emptyCells =0; oponentCount=0;
        }
        
        //Checkeo de posibilidades de ganar en diagonal, desde arriba a la izquierda
        i = 0; playerCount = 0; oponentCount = 0; emptyCells = 0;
        while(i<TaTeTi.MAX_CELLS){
            if(isCellTakenByPlayer(i, i, board, player))
                    playerCount++;
            else
                if(isCellFree(i, i, board))
                    emptyCells++;
                else
                    oponentCount++;                        
            i++;
        }
        updateResults(playerCount, oponentCount, emptyCells, result );
        playerCount = 0; emptyCells =0; oponentCount=0;
        //Checkeo de posibilidades de ganar en diagonal, desde arriba a la derecha
        i = 0; j = TaTeTi.MAX_CELLS - 1;playerCount = 0; oponentCount = 0; emptyCells = 0;
        while(j>=0){
            if(isCellTakenByPlayer(i, j, board, player))
                    playerCount++;
            else
                if(isCellFree(i, j, board))
                    emptyCells++;
                else
                    oponentCount++;                        
            i++;
            j--;
        }
        updateResults(playerCount, oponentCount, emptyCells, result );  
        
        //Normalizamos el result en el rango [0-1]
        for(int k=0; k<7; k++)
            result[k]=(( (result[k]-(-5)) / ((5)-(-5)) ));
        
        
        return result;
    }
        
        private void updateResults(int playerCount, int oponentCount, int emptyCells, double[] result){             //Jugada ganadora!
        if((playerCount == TaTeTi.MAX_CELLS))
            result[0] = playerCount;
        //Jugada en que se pierde!
        else if ((oponentCount == TaTeTi.MAX_CELLS))
            result[1] = oponentCount;
        //Jugada casi ganadora!
        if((playerCount == TaTeTi.MAX_CELLS - 1) && (emptyCells == 1))
            result[2] = playerCount;
        //Jugada en la que casi se pierde!
        else if ((oponentCount == TaTeTi.MAX_CELLS - 1) && (emptyCells == 1))
            result[3] = oponentCount;            
        //Fila donde solo hay de nuestras fichas
        else if(playerCount > 0 && ((playerCount + emptyCells) == TaTeTi.MAX_CELLS)){
            if(playerCount > result[4])
                result[4]= playerCount;
        }
        //Fila donde solo hay de las fichas del otro fichas
        else if(oponentCount > 0 && ((oponentCount + emptyCells) == TaTeTi.MAX_CELLS))
            if(oponentCount > result[5])
                result[5]= oponentCount;
        
        }
}

