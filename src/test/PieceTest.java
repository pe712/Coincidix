package test;

import java.io.FileNotFoundException;

import main.Direction;
import main.Grid;
import main.Piece;

/**
 * PieceTest
 */
public class PieceTest {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("classic Piece");
        Grid grid = new Grid("data/grille/grille3.txt");
        int[] distinctFormPieceId = new int[]{1,5,8};
        for (int i:distinctFormPieceId) {
            String filename = "data/pieces/piece" + i + ".txt";
            Piece piece = new Piece(filename, grid.n);
            System.out.println(piece);
        }

        System.out.println("oriented Piece");
        Piece piece2 = new Piece("data/pieces/piece2.txt", grid.n);
        piece2.orientation = Direction.DOWN;
        System.out.println(piece2);
        piece2.orientation = Direction.LEFT;
        System.out.println(piece2);

        System.out.println("flipped oriented Piece");
        piece2.orientation = Direction.UP;
        piece2.flipped = true;
        System.out.println(piece2);
        piece2.orientation = Direction.RIGHT;
        System.out.println(piece2);
    }
}