/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tateti;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class TaTeTi {


    static enum CellValue {X, O, _};
    static int MAX_CELLS = 5;
    static boolean print;
    public static int jugados;
    static int buenas =0;
    static int malas = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        print = false;
        int mode = 0; // 0 = vsRandom -- 1 = vsOldVersion -- 2 = vsHuman         
        int juegos = 100;
        PerformanceSystem oldVersionPlayer = null;
        
        try {
            while(true){
                switch (mode){
                    case 0: System.out.println("VS RANDOM PLAYER\n");
                            oldVersionPlayer = vsRandomPlayer(juegos, mode);                   
                            mode = 3;
                            break;
                    case 1: System.out.println("VS OLD VERSION\n");
                            oldVersionPlayer = vsOldVersion(juegos, mode, oldVersionPlayer);
                            mode = 2;
                            break;
                                                                                
                    case 2: System.out.println("VS HUMAN\n");
                            print = true;
                            vsHuman(null);
                            //vsHuman(oldVersionPlayer);
                            return;   
                    case 3: System.out.println("ANN\n");
                            print = false;
                            for(int j = 0; j< 100; j++)
                                ANN(juegos, mode, oldVersionPlayer);
                            System.out.println("==================================");
                            System.out.println("RESUMEN");
                            System.out.println("==================================");
                            System.out.println("buenas: " + buenas);
                            System.out.println("malas: " + malas);
                            return;       
                        
                }                                                
            }
            
        } catch (Exception ex) {            
            Logger.getLogger(TaTeTi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void play(int maxJuegos, Board board, TaTeTiPlayer player1, TaTeTiPlayer player2, int mode) throws Exception{                
        LinkedList<Integer> perdidas = new LinkedList<>();
        LinkedList<Integer> empatadas = new LinkedList<>();
        Game game = new Game(player1, player2);        
        Critic critic = new Critic();
        int x, y, winX =0, winO=0, ties=0, moveCounter = 0;
        for(jugados=0; jugados<maxJuegos; jugados++){
            board = new Board();
            game = new Game(player1, player2);
            player1.setBoard(board);
            player2.setBoard(board);
            if(TaTeTi.print)
                System.out.println("Nuevo juego!\n");
            while(!board.isFinished()){
                board = player1.chooseMove();
                game.move(board);
                if(!board.isFinished()){
                    if (mode == 0 || mode == 3){
                        int a = 2;
                        int b = 2;
                        while(!board.isCellFree(a, b))
                        {
                            a = new Random().nextInt(5);
                            b = new Random().nextInt(5);                            
                        }
                        game.move(CellValue.O,a,b,board);
                    }
                    else if (mode == 1){                        
                        board = player2.chooseMove();
                        game.move(board);
                    }                    
                }                    
            }            
            if(game.winner.equals(CellValue.O)){
                winO++;
                perdidas.add(jugados);                
            }
            else if(game.winner.equals(CellValue.X))
                winX++;
            else{
                ties++;
                empatadas.add(jugados);
            }
            moveCounter += game.moveCounter;
            
            //if(j%1000==0)
                //player1.setUpdateConstant(player1.getUpdateConstant()/2);
            
            player1.updateWeights(game.getGameHistory(), critic.getTrainingValues(game.getGameHistory(), CellValue.X));
        }
        
        if(winX<90){
            System.out.println("=============================================");
            System.out.println("=============================================");
            for(int i = 0; i < 6; i++){
                        System.out.println("w" + i + ": " + player1.getWeights()[i]);
                    }
            System.out.println("=============================================");
            System.out.println("wins: " + winX);
            System.out.println("lost: " + winO);
            System.out.println("tied: " + ties);
            System.out.println("avarage moves: " + moveCounter/maxJuegos);

            Iterator it = perdidas.iterator();
            System.out.print("perdidas: [");
            while(it.hasNext()){
                System.out.print(" " + it.next());
            }
            System.out.println(" ]");

            it = empatadas.iterator();
            System.out.print("empatadas: [");
            while(it.hasNext()){
                System.out.print(" " + it.next());
            }
            System.out.println(" ]");
            malas++;
        }
        else{
            buenas++;
        }       
        
        critic.updateWeights(player1.getWeights(), player1.getUpdateConstant());
        //player1.printWeights();        
    }
    
    private static void ANN(int juegos, int mode, PerformanceSystem oldVersion) throws Exception {
        Board board = new Board();
        TaTeTiPlayer player1 = new ANNPlayer(board, CellValue.X);
        //player1.setUpdateConstant(0.00001);
        TaTeTiPlayer player2 = new PerformanceSystem(board, CellValue.O, oldVersion.getWeights(), oldVersion.constant);
        play(juegos, board, player1, player2, mode);        
    }
    
    
    private static PerformanceSystem vsRandomPlayer(int juegos, int mode) throws Exception {
        Board board = new Board();
        PerformanceSystem player1 = new PerformanceSystem(board, CellValue.X);
        PerformanceSystem player2 = new PerformanceSystem(board, CellValue.O);
        play(juegos, board, player1, player2, mode);
        return player1;
    }
    
    public static PerformanceSystem vsOldVersion(int juegos, int mode, PerformanceSystem oldVersion) throws Exception{
        Board board = new Board();           
        PerformanceSystem player1 = new PerformanceSystem(board, CellValue.X);
        player1.setUpdateConstant(0.00001);
        PerformanceSystem player2 = new PerformanceSystem(board, CellValue.O, oldVersion.getWeights(), oldVersion.constant);
        play(juegos, board, player1, player2, mode);
        return player1;
    }
    
    public static void vsHuman(PerformanceSystem player1) throws Exception{
            Board board = new Board();    
            if (player1 == null)
                player1 = new PerformanceSystem(board, CellValue.X);
            PerformanceSystem player2 = new PerformanceSystem(board, CellValue.O);
            Game game = new Game(player1, player2);  
            Scanner s = new Scanner(System.in);
            Critic critic = new Critic();
            int x= 0, y=0, winX =0, winO=0, ties=0, moveCounter = 0, juegos = 0;
            boolean flag = true;
            while (flag){                
                board = new Board();
                game = new Game(player1, player2);
                player1.setBoard(board);
                player2.setBoard(board);
                if(TaTeTi.print)
                    System.out.println("Nuevo juego!\n");
                
                while(!board.isFinished()){
                    board = player1.chooseMove();
                    game.move(board);                    
                    if(!board.isFinished()){
                        do{
                            try{
                                System.out.println("Turno jugador O");
                                System.out.print("Ingrese coord x:");
                                x = s.nextInt();
                                System.out.print("Ingrese coord y:");
                                y = s.nextInt();
                            }
                            catch(Exception ex){}
                        } while (!board.isCellFree(x, y));
                        game.move(CellValue.O, x, y, board);                        
                    }            
                }
                juegos++;
                if(game.winner.equals(CellValue.O))
                    winO++;
                else if(game.winner.equals(CellValue.X))
                    winX++;
                else
                    ties++;

                moveCounter += game.moveCounter;

                if(print){
                    System.out.println("gandor partida " + (juegos) + ": " + (game.winner));
                    System.out.println("w0: " + player1.w0);
                    System.out.println("w1: " + player1.w1);
                    System.out.println("w2: " + player1.w2);
                    System.out.println("w3 " + player1.w3);
                    System.out.println("w4: " + player1.w4);
                    System.out.println("w5: " + player1.w5);                
                }

                player1.updateWeights(game.getGameHistory(), critic.getTrainingValues(game.getGameHistory(), CellValue.X));                                        
                                             
                System.out.print("Volver a jugar? y/n: ");
                String answer = s.next();
                if(!answer.equals("y")){
                    flag = false;
                    System.out.println("=============================================");
                    System.out.println("wins: " + winX);
                    System.out.println("lost: " + winO);
                    System.out.println("tied: " + ties);
                    System.out.println("avarage moves: " + moveCounter/juegos);
                    critic.updateWeights(player1.getWeights(), player1.constant);
                    player1.printWeights();   
                }
            }
    }      
}
    
    
    

