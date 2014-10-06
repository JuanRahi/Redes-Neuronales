/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import java.util.Iterator;
import java.util.LinkedList;
import tateti.TaTeTi.CellValue;

/**
 *
 * @author Admin
 */
public class Game {
    private LinkedList<Board> gameHistory;
    CellValue turn, winner;            
    int moveCounter;
    TaTeTiPlayer player1, player2;
    
    
    public Game(TaTeTiPlayer player1, TaTeTiPlayer player2){
        gameHistory = new LinkedList<Board>();
        gameHistory.add(new Board());
        turn = CellValue.X;
        moveCounter = 0;
        winner = CellValue._;
        this.player1 = player1;
        this.player2 = player2;        
    }   
    
    public Game(TaTeTiPlayer player1){
        gameHistory = new LinkedList<Board>();
        gameHistory.add(new Board());
        turn = CellValue.X;
        moveCounter = 0;
        winner = CellValue._;
        this.player1 = player1;
        this.player2 = null;        
    }  
    
     public void move(Board newBoard) throws Exception{
        if(!newBoard.finished){
            gameHistory.add(newBoard);
            moveCounter++;
            if(TaTeTi.print)
                System.out.println("Jugada " + moveCounter + ":");
            newBoard.print();
            
            if(newBoard.hasCellValueWon(turn)){
                if(TaTeTi.print)
                    System.out.println("Jugador " + turn + " ganó!");
                newBoard.finished = true;
                winner = turn;
            }
            else if(newBoard.isFull()){
                if(TaTeTi.print)
                    System.out.println("Tablero lleno, partida empatada!");
                newBoard.finished = true;
            }
            player1.setBoard(newBoard);
            if(player2!=null)
                player2.setBoard(newBoard);
            
            turn = turn.equals(CellValue.X) ? CellValue.O : CellValue.X;
        }
        else if(newBoard.finished){
                if(TaTeTi.print)
                    System.out.println("La partida ya ha terminado!");                
                gameHistory.add(newBoard);                         
                if(newBoard.hasCellValueWon(turn)){                    
                    if(TaTeTi.print)
                        System.out.println("Jugador " + turn + " ganó!");
                    newBoard.finished = true;
                    winner = turn;
                }
                else if(newBoard.isFull()){
                    if(TaTeTi.print)
                        System.out.println("Tablero lleno, partida empatada!");
                    newBoard.finished = true;
                }
        }
    }   
    
    public void move(CellValue value, int x, int y, Board currentBoard) throws Exception{
        boolean isCellFree = currentBoard.isCellFree(x, y);
        if(!currentBoard.finished && turn.equals(value) && isCellFree){
            currentBoard.setCell(value, x, y);
            gameHistory.add(currentBoard);
            turn = value.equals(CellValue.X) ? CellValue.O : CellValue.X;
            moveCounter++;
            if(TaTeTi.print)
                System.out.println("Jugada " + moveCounter + ":");
            currentBoard.print();
            
            if(currentBoard.hasCellValueWon(value)){
                if(TaTeTi.print)
                    System.out.println("Jugador " + value + " ganó!");
                currentBoard.finished = true;
                winner = value;
            }
            else if(currentBoard.isFull()){
                if(TaTeTi.print)
                    System.out.println("Tablero lleno, partida empatada!");
                currentBoard.finished = true;
            }
            player1.setBoard(currentBoard);
            if(player2!=null)
                player2.setBoard(currentBoard);
        }
        else if(currentBoard.finished)
            System.out.println("La partida ya ha terminado!");
        else if(!turn.equals(value))
            System.out.println("Es el turno del jugador " + turn + ", no del jugador " + value);        
        else 
            System.out.println("La celda elegida no esta libre. Elija otra");
    }
    
    public void printGameHistory(){
        if(TaTeTi.print){
            System.out.println("Imprimiendo histórico de jugadas:");
            Iterator it = gameHistory.iterator();
            int i=0;
            while(it.hasNext()){
                System.out.println("Jugada " + i + ":");
                ((Board)it.next()).print();
                i++;
            }
            if(winner.equals(CellValue._))
                System.out.println("No hubo ganador, la partida fue empatada");
            else
                System.out.println("Jugador " + winner + " ganó la partida!");
        }
    }
    
    public LinkedList<Board> getGameHistory(){
        return gameHistory;
    }
}
