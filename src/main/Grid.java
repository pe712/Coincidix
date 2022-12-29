package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

public class Grid {
    public final HashSet<Coordinate> smileys = new HashSet<Coordinate>();
    public final int n;
    public ArrayList<Piece> pieces = new ArrayList<Piece>();

    /**
     * When scanning the grid, the currentIsland is the island of non pieces
     * surrounded by piecePart
     */
    private HashSet<Coordinate> currentIsland = new HashSet<Coordinate>();
    /**
     * Should be used only when it's necessary to have every emptyCell and not to
     * check wether a cell is empty or not. For this, prefer filledCells
     */
    private HashSet<Coordinate> emptyCells = new HashSet<Coordinate>();
    public HashMap<Coordinate, Integer> filledCells = new HashMap<Coordinate, Integer>();

    public Grid(int n) {
        this.n = n;
        fillEmptyCells();
    }

    public Grid(String filename) throws FileNotFoundException {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file);) {
            this.n = Integer.valueOf(scanner.nextLine());
            while (scanner.hasNext()) {
                String strCoord = scanner.nextLine() + "," + n;
                this.smileys.add(new Coordinate(strCoord));
            }
        }
        fillEmptyCells();
    }

    private void fillEmptyCells() {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.emptyCells.add(new Coordinate(i, j, this.n));
            }
        }
    }

    public void initializeAllPieces(String folder) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folder))) {
            for (Path file : stream) {
                Piece piece = new Piece(file.toString(), this.n);
                this.pieces.add(piece);
            }
        }
    }

    public boolean solve() {
        return solve(1);
    }

    public boolean solve(int pieceId) {
        if (pieceId > this.pieces.size())
            return true;
        Piece piece = this.pieces.get(pieceId - 1);
        ArrayList<Coordinate> currentEmptyCells = new ArrayList<Coordinate>();
        currentEmptyCells.addAll(this.emptyCells);
        for (Coordinate tryCoord : currentEmptyCells) {
            piece.coordReference = tryCoord;
            for (Direction dir : Direction.values()) {
                piece.orientation = dir;
                piece.flipped = false;
                if (validConfig(piece))
                    return true;
                piece.flipped = true;
                if (validConfig(piece))
                    return true;
            }
        }
        piece.coordReference = null;
        return false;

    }

    private boolean validConfig(Piece piece) {
        if (this.place(piece, piece.coordReference)) {
            // On vérifie qu'il n'y a pas d'île isolée avant de tester les autres pièces
            if (!configWithoutIsolatedIsland()) {
                this.remove(piece);
                return false;
            }
            if (solve(piece.id + 1))
                return true;
            else
                this.remove(piece);
        }
        return false;
    }

    public boolean place(Piece piece, Coordinate refCoordinate) {
        return place(piece, refCoordinate, false);
    }

    public boolean place(Piece piece, Coordinate refCoordinate, Boolean representationPiece) {
        if (!placeAux(piece, piece.reference, refCoordinate, representationPiece)) {
            remove(piece);
            return false;
        } else {
            piece.coordReference = refCoordinate;
            return true;
        }
    }

    private boolean placeAux(Piece piece, PiecePart currentPiecePart, Coordinate currentCoord,
            Boolean representationPiece) {
        Integer cellContent = filledCells.get(currentCoord);
        if (cellContent != null) {
            if (cellContent == piece.id) {
                // On est en train de revenir en arrière
                return true;
            } else
                // Il y a déjà une autre pièce
                return false;
        }

        if (representationPiece) {
            add(piece, currentCoord);
            if (currentPiecePart.filling.equals(Filling.HOLE))
                smileys.add(currentCoord);
        } else {
            if (currentPiecePart.filling.equals(Filling.HOLE) == !this.smileys.contains(currentCoord))
                return false;
            else
                add(piece, currentCoord);
        }

        for (Direction dir : Direction.values()) {
            if (currentPiecePart.neighbours.containsKey(dir)) {
                Direction nextDir = dir.turn(piece.orientation);
                if (piece.flipped)
                    nextDir = nextDir.flip();
                Coordinate nextCoord;
                try {
                    nextCoord = currentCoord.moveTo(nextDir);
                } catch (AssertionError e) { // La pièce sort de la table
                    return false;
                }
                if (!placeAux(piece, currentPiecePart.neighbours.get(dir), nextCoord, representationPiece)) {
                    return false;
                }
            }

        }
        return true;
    }

    private void remove(Piece piece) {
        ArrayList<Coordinate> toRemove = new ArrayList<Coordinate>();
        for (Entry<Coordinate, Integer> entry : filledCells.entrySet()) {
            if (entry.getValue() == piece.id)
                toRemove.add(entry.getKey());
        }
        for (Coordinate coord : toRemove) {
            filledCells.remove(coord);
            emptyCells.add(coord);
        }
    }

    private void add(Piece piece, Coordinate coord) {
        filledCells.put(coord, piece.id);
        emptyCells.remove(coord);
    }

    @Override
    public String toString() {
        String retString = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Coordinate coord = new Coordinate(i, j, n);
                Integer cellContent = filledCells.get(coord);
                if (cellContent == null)
                    retString += ".";
                else
                    retString += cellContent;
                if (smileys.contains(coord))
                    retString += "s ";
                else
                    retString += "  ";
            }
            retString += "\n";
        }
        return retString;
    }

    public boolean configWithoutIsolatedIsland() {
        HashSet<Coordinate> todo = new HashSet<Coordinate>();
        todo.addAll(this.emptyCells);
        while (!todo.isEmpty()) {
            Coordinate coord = todo.iterator().next();
            if (isIsolated(coord)) {
                return false;
            }
            todo.removeAll(this.currentIsland);
        }
        return true;
    }

    /**
     * @param coord
     * @return true if coord is an empty cell in a less than 3 empty cell island
     *         (surrounded by PiecePart)
     */
    public boolean isIsolated(Coordinate coord) {
        int islandSize = findIsland(coord).size();
        return islandSize <= 3 && islandSize > 0;
    }

    public HashSet<Coordinate> findIsland(Coordinate coord) {
        this.currentIsland.clear();
        this.findIslandAux(coord);
        return this.currentIsland;
    }

    public void findIslandAux(Coordinate coord) {
        if (!this.currentIsland.contains(coord) && filledCells.get(coord) == null) {
            this.currentIsland.add(coord);
            for (Coordinate adjacentCoord : coord.neighbours()) {
                this.findIslandAux(adjacentCoord);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scannner = new Scanner(System.in);
        System.out.println("Enter the number of the level you want to solve");
        int grilleId;
        try {
            grilleId = Integer.valueOf(scannner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("The number entered is not valid");
            scannner.close();
            return;
        }
        scannner.close();
        Grid partie;
        try {
            partie = new Grid("data/grille/grille" + grilleId + ".txt");
        } catch (FileNotFoundException e) {
            System.out.println(
                    "The level has not been set up. Please go in data/grille and describe the position of your smileys");
            return;
        }
        try {
            partie.initializeAllPieces("data/pieces");
        } catch (IOException e) {
            System.out.println("data/pieces is not a folder");
            return;
        }
        System.out.println("Solving...");
        final long startTime = System.currentTimeMillis();
        
        partie.solve();
        final long endTime = System.currentTimeMillis();
        System.out.println(partie);
        System.out.println("Done in "+(endTime - startTime)+" ms\n");
        System.out.println(partie.pieces);
    }
}
