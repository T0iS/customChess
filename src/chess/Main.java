package chess;

import chess.Board.Tile;
import chess.Pieces.Archer;
import chess.Pieces.Mage;
import chess.Pieces.MoveFiles.MoveInfo;
import chess.Pieces.MoveFiles.MoveList;
import chess.Pieces.Piece;
import chess.Pieces.Swordman;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

public class Main extends Application {


    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    public static int P1count = 0;
    public static int P2count = 0;

    public Tile activeTile = null;
    public boolean ActivePlayer = true;
    public String log = "Initialized board\n" ;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Parent createContent() throws FileNotFoundException {
        Pane root = new Pane();
        root.setPrefSize(WIDTH*TILE_SIZE,HEIGHT*TILE_SIZE);
        root.getChildren().addAll(tileGroup,pieceGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                final int F_x = x;
                final int F_y = y;



                board[x][y].setOnMouseClicked(e -> {
                    try {
                        onTileClick(F_x, F_y);
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                });

            }
        }

        createPieces();
        log += "Pieces created\n";
        log += "Starting a match, Player1: " + String.valueOf(P1count) + " pieces, Player2: " + String.valueOf(P2count) + " pieces. \n";
        return root;
    }

    private void createPieces() throws FileNotFoundException {


        board[2][6].setPiece(new Swordman(true, 2, 6));
        board[3][6].setPiece(new Swordman(true, 3, 6));
        board[4][6].setPiece(new Swordman(true, 4, 6));
        board[5][6].setPiece(new Swordman(true, 5, 6));

        board[4][7].setPiece(new Mage(true, 4, 7));
        board[3][7].setPiece(new Mage(true, 3, 7));

        board[5][7].setPiece(new Archer(true, 5, 7));
        board[2][7].setPiece(new Archer(true, 2, 7));






        board[2][1].setPiece(new Swordman(false, 2, 1));
        board[3][1].setPiece(new Swordman(false, 3, 1));
        board[4][1].setPiece(new Swordman(false, 4, 1));
        board[5][1].setPiece(new Swordman(false, 5, 1));

        board[4][0].setPiece(new Mage(false, 4, 0));
        board[3][0].setPiece(new Mage(false, 3, 0));

        board[5][0].setPiece(new Archer(false, 5, 0));
        board[2][0].setPiece(new Archer(false, 2, 0));



        for (int i = 0;i<WIDTH;i++){
            for (int j = 0;j<HEIGHT;j++){
                if (board[i][j].hasPiece()) {
                    if (board[i][j].getPiece().getPlayer()){
                        P1count++;
                    }
                    if (!board[i][j].getPiece().getPlayer()){
                        P2count++;
                    }
                    pieceGroup.getChildren().addAll((Node) board[i][j].getPiece());
                }
            }
        }



    }

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }



    public void onTileClick(int x, int y) throws FileNotFoundException, NullPointerException, UnsupportedEncodingException {
        Tile clickedSpace = board[x][y];
        //clickedSpace.getPiece().move(x+30, y+30);

        if (activeTile != null &&
                activeTile.getPiece() != null
        )
        {
          /*

            this.activeTile.releasePiece();
*/
            MoveInfo p;
            p = new MoveInfo((int)activeTile.getX(), (int)activeTile.getY(), x, y, ActivePlayer);
            System.out.println(activeTile.getX());
            System.out.println(activeTile.getY());
            this.processMove(p);

            this.setActiveTile(null);



            if (P1count == 0 ){
                log+="Game ends. WINNER: Player 2\n";

                PrintWriter w = new PrintWriter("log.txt", "UTF-8");
                w.println(log);
                w.close();
                System.out.print(log);
                //System.exit(0);
            }
            if (P2count == 0 ){
                log+="Game ends. WINNER: Player 1\n";

                PrintWriter w = new PrintWriter("log.txt", "UTF-8");
                w.println(log);

                w.close();
                System.out.print(log);
                //System.exit(0);
            }

        }
        else{

            if (board[x][y].getPiece() != null)
            {
                //make active square clicked square
                this.setActiveTile(board[x][y]);
                System.out.println("MOUSE CLICKED, setting active");
                System.out.println(x);
                System.out.println(y);

            }
        }

    }

    protected boolean processMove(MoveInfo p) throws FileNotFoundException, NullPointerException {
        if (moveIsValid(p))
        {
            Tile oldSpace = board[p.getOldX()][p.getOldY()];

            if(oldSpace.getPiece().getPlayer() == ActivePlayer) {
                Tile newSpace = board[p.getNewX()][p.getNewY()];
                oldSpace.getPiece().move(p.getNewX(), p.getNewY());



                if(newSpace.hasPiece() && newSpace.getPiece().getPlayer()){

                    Piece pc2 = newSpace.releasePiece();
                    pieceGroup.getChildren().remove(pc2);
                    log+=oldSpace.getPiece().getName()+" from Player2 killed "+pc2.getName()+ "from Player 1\n";
                    log += "Current score, Player1: " + String.valueOf(P1count) + " pieces, Player2: " + String.valueOf(P2count) + " pieces. \n";

                    P1count--;

                }
                if(newSpace.hasPiece() && !newSpace.getPiece().getPlayer()){
                    Piece pc1 = newSpace.releasePiece();
                    pieceGroup.getChildren().remove(pc1);
                    log+=oldSpace.getPiece().getName()+" from Player1 killed "+pc1.getName()+ "from Player 2\n";
                    log += "Current score, Player1: " + String.valueOf(P1count) + " pieces, Player2: " + String.valueOf(P2count) + " pieces. \n";

                    P2count--;
                }
                if(ActivePlayer){
                    log+= "Player1 moved with "+ oldSpace.getPiece().getName() + " from ["+ p.getOldX()+","+p.getOldY()+"] to ["+ p.getNewX()+","+p.getNewY()+"]\n";
                }
                if(!ActivePlayer){
                    log+= "Player2 moved with "+ oldSpace.getPiece().getName() + " from ["+ p.getOldX()+","+p.getOldY()+"] to ["+ p.getNewX()+","+p.getNewY()+"]\n";
                }
                newSpace.setPiece(oldSpace.getPiece());
                oldSpace.releasePiece();

                System.out.println("MOUSE CLICKED");
                System.out.println(p.getNewX());
                System.out.println(p.getNewY());
                ActivePlayer = !ActivePlayer;
                System.out.println(ActivePlayer);
                System.out.println("\n");
                System.out.println(P1count);
                System.out.println(P2count);






            }

            return true;
        }
        else // invalid move
        {
            log+="Chosen move is invalid\n";
            System.out.println("Invalid move");
            return false;
        }
    }


    public void setActiveTile(Tile s)
    {
        // Remove style from old active space
        if (this.activeTile != null)
            this.activeTile.getStyleClass().removeAll("chess-space-active");

        this.activeTile = s;

        // Add style to new active space
        if (this.activeTile != null)
            this.activeTile.getStyleClass().add("chess-space-active");
    }


    public boolean moveIsValid(MoveInfo p)
    {
        Tile oldSpace;
        Tile newSpace;
        Piece piece;
        MoveList[] moves;




        // Check for null move
        if (p == null) { return false; }

        // Note: Ideally we would check the space coordinates
        //       beforehand, but the try-catch blocks below were
        //       easier to implement.

        // Check if oldSpace in range
        try { oldSpace = board[p.getOldX()][p.getOldY()]; }
        catch (NullPointerException e) { return false; }

        // Check if newSpace in range
        try { newSpace = board[p.getNewX()][p.getNewY()]; }
        catch (NullPointerException e) { return false; }

        // Check if oldSpace is empty; (no movable piece)
        if (!oldSpace.hasPiece()) { return false; }

        // Check piece's move list
        piece = oldSpace.getPiece();
        moves = piece.getPieceMoves();
        boolean matchesPieceMoves = false;


        //for Pieces that move more than 1 base move (Bishop, Rook, Queen)
        int multiMoveCount;
        int stretchedMoveX;
        int stretchedMoveY;

        //labels this loop to break out later
        MoveLoop:
        for (MoveList m : moves)
        {//iterates through multiple times if has multiple possible moves
            multiMoveCount = 1;
            if(piece.usesSingleMove() == false) {multiMoveCount = 8;}

            boolean hasCollided = false;

            for(int c = 1; c <= multiMoveCount; c++)
            {
                //if the prior run hit a piece of opponent's color, done with this move
                if (hasCollided){break;}

                //stretches a base move out to see if it matches the move made
                stretchedMoveX = m.getX() * c;
                stretchedMoveY = m.getY() * c;

                Tile tempSpace;

                //If OOB, go to next move of the piece -- ensures space exists later
                try
                {
                    tempSpace = board[p.getOldX() + stretchedMoveX]
                            [p.getOldY() + stretchedMoveY];
                }
                catch (Exception e) { break; }

                //handles piece collision and capturing
                if(tempSpace.hasPiece())
                {
                    hasCollided = true;
                    boolean piecesSameColor = tempSpace.getPiece().getPlayer() == oldSpace.getPiece().getPlayer();
                    //stops checking this move if pieces are the same color
                    if (piecesSameColor){ break; }
                }

                //if stretched move matches made move
                if ( p.getGapX() == stretchedMoveX && p.getGapY() == stretchedMoveY)
                {
                    matchesPieceMoves = true;


                    piece.setHasMoved(true);
                    //breaks out of MoveLoop (both loops)
                    break MoveLoop;
                }
            }
        }
        if (!matchesPieceMoves) { return false; }

        return true;
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scn = new Scene(createContent());
        primaryStage.setTitle("Chess_final");
        primaryStage.setScene(scn);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }




}
