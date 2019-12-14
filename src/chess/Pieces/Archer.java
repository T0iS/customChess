package chess.Pieces;


import chess.Main;
import chess.Pieces.MoveFiles.MoveList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Archer extends StackPane implements Piece   {

    private ImageView img;

    private MoveList[] validMoves;

    private double mouseX, mouseY;
    private double oldX, oldY;
    private boolean player;
    private boolean hasMoved;

    public Archer(boolean player, int x, int y) throws FileNotFoundException {
        super();
        this.player = player;

        if (player) {
            this.validMoves = new MoveList[]
                    {
                            MoveList.RIGHT,
                            MoveList.LEFT,
                            MoveList.DOWN,
                            MoveList.UP
                    };
        }
        if (!player){
            this.validMoves = new MoveList[]
                    {
                            MoveList.RIGHT,
                            MoveList.LEFT,
                            MoveList.DOWN,
                            MoveList.UP

                    };
        }
        move(x,y);
        Image im = null;
        if (player) {
            im = new Image(new FileInputStream("images/archer.png"));
        }
        if (!player) {
            im = new Image(new FileInputStream("images/archer2.png"));
        }
        this.img = new ImageView(im);
        this.img.setFitHeight(Main.TILE_SIZE*0.7);
        this.img.setFitWidth(Main.TILE_SIZE*0.7);
        this.img.setTranslateX((Main.TILE_SIZE - Main.TILE_SIZE*0.7) / 2);
        this.img.setTranslateY((Main.TILE_SIZE - Main.TILE_SIZE*0.7) / 2);
        getChildren().addAll(this.img);

        setOnMouseClicked(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
    }


    @Override
    public MoveList[] getPieceMoves() {
        return this.validMoves;
    }


    @Override
    public double getOldY() {
        return oldY;
    }

    @Override
    public double getOldX() {
        return oldX;
    }

    @Override
    public void move(int x, int y){
        oldX = x* Main.TILE_SIZE;
        oldY = y* Main.TILE_SIZE;
        relocate(oldX, oldY);
    }

    @Override
    public void abortMove(){
        relocate(oldX, oldY);
    }

    @Override
    public boolean getPlayer() {
        return this.player;
    }

    public void removeImg(){
        this.img = null;
    }

    public boolean usesSingleMove(){return false;}

    @Override
    public String getName() {
        return "Archer";
    }
    @Override
    public void setHasMoved(boolean shouldBeTrue)
    {
        this.hasMoved = shouldBeTrue;
    }

    @Override
    public ImageView getImg() {
        return img;
    }
}
