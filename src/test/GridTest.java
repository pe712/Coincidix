package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import main.Coordinate;
import main.Direction;
import main.Grid;
import main.Piece;

public class GridTest {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Grid partie3;
        Piece piece1, piece8;
        boolean placed;

        System.out.println("Test grille 3");
        partie3 = new Grid("data/grille/grille3.txt");
        System.out.println(partie3);
        piece8 = new Piece("data/pieces/piece8.txt", partie3.n);
        piece1 = new Piece("data/pieces/piece1.txt", partie3.n);

        System.out.println("Test placer piece1 en 0,2");
        System.out.println(piece1);
        placed = partie3.place(piece1, new Coordinate(0, 2, partie3.n));
        assert placed;
        System.out.println(partie3);

        System.out.println("Test placer piece8 non orientée en 4,4");
        System.out.println(piece8);
        placed = partie3.place(piece8, new Coordinate(4, 4, partie3.n));
        assert !placed;
        System.out.println(partie3);
        System.out.println("échec\n\n");

        System.out.println("Test placer piece8 orientée en 4,4");
        piece8.orientation = Direction.LEFT;
        System.out.println(piece8);
        placed = partie3.place(piece8, new Coordinate(4, 4, partie3.n));
        System.out.println(partie3);

        System.out.println("Test initialisation des pièces");
        partie3 = new Grid("data/grille/grille3.txt");
        partie3.initializeAllPieces("data/pieces");
        System.out.println(partie3.pieces);

        // System.out.println("\nTest placabilité de 1 pièces");
        // assert partie3.solve(partie3.pieces.size());
        // System.out.println(partie3);

        System.out.println("\nTest placabilité de 4 pièces");
        assert partie3.solve(Math.max(1, partie3.pieces.size() - 3));
        System.out.println(partie3);

        System.out.println("Test solvabilité grille3");
        partie3 = new Grid("data/grille/grille3.txt");
        partie3.initializeAllPieces("data/pieces");
        assert partie3.solve();
        System.out.println(partie3);

        System.out.println("Test solvabilité grille8");
        Grid partie8 = new Grid("data/grille/grille8.txt");
        partie8.initializeAllPieces("data/pieces");
        assert partie8.solve();
        System.out.println(partie8);

        System.out.println("\nTest findIsland");
        Coordinate leftTopCorner = new Coordinate(0, 0, 6);
        Grid partie38 = new Grid("data/grille/grille38.txt");
        assert partie38.findIsland(leftTopCorner).size() == 36;
        assert !partie38.isIsolated(leftTopCorner);

        Piece piece5 = new Piece("data/pieces/piece5.txt", partie38.n);
        piece5.flipped = true;
        piece5.orientation = Direction.LEFT;
        partie38.place(piece5, new Coordinate(0, 3, 6));
        Piece piece6 = new Piece("data/pieces/piece6.txt", partie38.n);
        piece6.orientation = Direction.RIGHT;
        partie38.place(piece6, new Coordinate(2, 5, 6));
        Coordinate rightTopCorner = new Coordinate(0, 4, 6);
        HashSet<Coordinate> island = new HashSet<Coordinate>();
        island.add(rightTopCorner);
        island.add(new Coordinate(0, 5, 6));
        island.add(new Coordinate(1, 5, 6));
        assert partie38.findIsland(rightTopCorner).equals(island);
        assert partie38.isIsolated(rightTopCorner);
        assert !partie38.isIsolated(new Coordinate(0, 3, 6));
        System.out.println("[OK]\n");

        System.out.println("Test remove a piece");
        partie3 = new Grid("data/grille/grille3.txt");
        piece1 = new Piece("data/pieces/piece1.txt", partie3.n);
        assert partie3.place(piece1, new Coordinate(0, 4, 6));
        // partie3.remove(piece1);
        // assert partie3.filledCells.isEmpty();
        // remove() became a private method
        System.out.println("[OK]\n");

        System.out.println("Test placabilité pièce1 en 0,4 avec création d'île");
        partie3 = new Grid("data/grille/grille3.txt");
        piece1 = new Piece("data/pieces/piece1.txt", partie3.n);
        assert partie3.place(piece1, new Coordinate(0, 4, 6));
        partie3.place(new Piece("data/pieces/piece8.txt", 6), new Coordinate(4, 1, 6));
        System.out.println(partie3);
        System.out.println("solving...");
        partie3.initializeAllPieces("data/pieces");
        assert !partie3.solve(8);
        System.out.println(partie3);
    }
}
