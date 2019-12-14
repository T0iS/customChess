package chess.Board;

import chess.Main;
import chess.Pieces.Piece;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Piece piece;

    public boolean hasPiece(){
        return piece != null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }


    public Tile(){

    }
    public Tile(boolean dark, int x, int y){
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        setX(x);
        setY(y);

        relocate(x *Main.TILE_SIZE, y * Main.TILE_SIZE);
        setFill(dark? Color.GRAY : Color.BEIGE);

    }

    public Piece releasePiece()
    {
        Piece tmpPiece = this.piece;
        this.piece.removeImg();
        setPiece(null);
        return tmpPiece;
    }


}
