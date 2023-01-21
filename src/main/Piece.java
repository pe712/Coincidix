package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Piece {
    public final int id;
    public Coordinate coordReference; // only when placed
    private final int n;
    public PiecePart reference;
    public Direction orientation = Direction.UP;
    public boolean flipped = false;

    public Piece(String filename, int n) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            // we are in the .jar (or the file do not exists)
            try {
                InputStream in = this.getClass().getResourceAsStream("/" + filename);
                scanner = new Scanner(in);
            } catch (NullPointerException f) {
                throw new FileNotFoundException("The file for this level does not exists");
            }
        }

        this.n = n;
        this.id = Integer.valueOf(filename.replaceAll("[^0-9]", ""));

        this.reference = new PiecePart(scanner.nextLine());
        PiecePart current = this.reference;
        while (scanner.hasNext()) {
            String[] mapping = scanner.nextLine().split(", ");
            if (current.neighbours.isEmpty()) {
                for (String map : mapping) {
                    String[] splitted = map.split(" ");
                    current.neighbours.put(Direction.valueOf(splitted[0]),
                            new PiecePart(Filling.valueOf(splitted[1])));
                }
            }
            Direction dir = Direction.valueOf(scanner.nextLine());
            current = current.moveTo(dir);
        }
        scanner.close();
    }

    @Override
    public String toString() {
        Grid emptyGrid = new Grid(this.n);
        Coordinate currentCoord;
        if (coordReference != null)
            currentCoord = coordReference;
        else {
            int center = emptyGrid.n / 2;
            currentCoord = new Coordinate(center, center, emptyGrid.n);
        }
        String retString = "Pièce " + this.id + " at coordinate " + currentCoord + " with orientation "
                + this.orientation + " and flipped=" + this.flipped + "\n";
        if (!emptyGrid.place(this, currentCoord, true))
            return retString + "échec";
        else
            return retString + emptyGrid.toString();
    }
}
