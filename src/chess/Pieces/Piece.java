package chess.Pieces;


import chess.Pieces.MoveFiles.MoveList;
import javafx.scene.image.ImageView;

public interface Piece {



    MoveList[] getPieceMoves();

    double getOldY();

    double getOldX();

    void move(int x, int y);

    void abortMove();

    boolean getPlayer();

    void removeImg();

    boolean usesSingleMove();

    String getName();

    void setHasMoved(boolean shouldBeTrue);

    public ImageView getImg();
}
