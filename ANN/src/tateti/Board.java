/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import tateti.TaTeTi.CellValue;

/**
 *
 * @author Admin
 */
public class Board {
    CellValue state[][];
    int availableCells;
    boolean finished;
    double puntaje;
    
    public Board(){
        this.state = new CellValue[TaTeTi.MAX_CELLS][TaTeTi.MAX_CELLS];
        availableCells = TaTeTi.MAX_CELLS * TaTeTi.MAX_CELLS;
        puntaje = 0.0;
        for(int i=0; i<TaTeTi.MAX_CELLS; i++) {
            for(int j=0; j<TaTeTi.MAX_CELLS; j++) {
                this.state[i][j] = CellValue._;
            }
        }
    }
    
    public Board (Board original){
        this.state = new CellValue[TaTeTi.MAX_CELLS][TaTeTi.MAX_CELLS];
        for(int i = 0; i < TaTeTi.MAX_CELLS; i++) {
            for(int j = 0; j < TaTeTi.MAX_CELLS; j++) {
                this.state[i][j] = original.state[i][j];
            }
        }
        this.availableCells = original.availableCells;
    }
    
    public boolean isEmpty(){
        return availableCells == (TaTeTi.MAX_CELLS * TaTeTi.MAX_CELLS);
    }
    
    public void setCell(CellValue value, int x, int y) throws Exception{
        if((x < 0) || (y < 0) || (x > TaTeTi.MAX_CELLS - 1) || (y > TaTeTi.MAX_CELLS - 1)) {
            throw new Exception("Posición elegida fuera de rango: x=" + x + ", y=" + y + ", MAX_CELLS=" + TaTeTi.MAX_CELLS);
        }
        this.state[x][y] = value;
        this.availableCells--;
        this.finished = (hasCellValueWon(value) || isFull());        
    }
    
    public CellValue getCell(int x, int y) throws Exception{
        if((x < 0) || (y < 0) || (x > TaTeTi.MAX_CELLS - 1) || (y > TaTeTi.MAX_CELLS - 1)) {
            throw new Exception("Posición elegida fuera de rango: x=" + x + ", y=" + y + ", MAX_CELLS=" + TaTeTi.MAX_CELLS);
        }
        return this.state[x][y];
    }
    
    public boolean isFull(){
        return this.availableCells == 0;
    }
    
    public boolean isCellFree(int x, int y) throws Exception{
        return getCell(x, y).equals(CellValue._);
    }
    
    public boolean hasCellValueWon(CellValue player) throws Exception{              
        //Checkeo horizontal
        int i=0, j=0;
        while((i < TaTeTi.MAX_CELLS) && (j < TaTeTi.MAX_CELLS)){
            if(getCell(i, j).equals(player))
                j++;         
            else{
                i++;
                j=0;
            }
        }
        if(j==TaTeTi.MAX_CELLS) {
            return true;
        }
        
        //Checkeo vertical
        i=0; j=0;
        while((i < TaTeTi.MAX_CELLS) && (j < TaTeTi.MAX_CELLS)){
            if(getCell(i, j).equals(player))
                i++;         
            else{
                i=0;
                j++;
            }
        }
        if(i==TaTeTi.MAX_CELLS)
            return true;
        
        //Checkeo diagonal, arrancando a la izquierda arriba
        i=0;
        while(i < TaTeTi.MAX_CELLS){
            if(getCell(i, i).equals(player))
                i++;
            else
                i=TaTeTi.MAX_CELLS + 1;
        }
        if(i==TaTeTi.MAX_CELLS)
            return true;
        
        //Checkeo diagonal, arrancando a la derecha arriba
        i=0; j=TaTeTi.MAX_CELLS - 1;
        while(i < TaTeTi.MAX_CELLS){
            if(getCell(i, j).equals(player)){
                i++;         
                j--;
            }
            else
                i=TaTeTi.MAX_CELLS + 1;
        }
        if(i==TaTeTi.MAX_CELLS)
            return true;
        
        return false;
    } 
    
    public boolean isFinished(){
        return finished;
    }
        
    public void print(){
        if(TaTeTi.print){
            for(int i = 0; i < TaTeTi.MAX_CELLS; i++) {
                System.out.print("| ");
                for(int j = 0; j < TaTeTi.MAX_CELLS; j++) {
                    System.out.print(this.state[i][j] + " ");
                }
                System.out.println("|");
            }
            System.out.println();
        }
    }
}
