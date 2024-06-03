package project.javaa.project2048;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

/**
 * @author Bruno Borges
 */
public class Tile extends Label implements Serializable {

    private Integer value;
    private Location location;
    private Boolean merged;

    private static final Random random = new Random();

    public static Tile newRandomTile(int tag) {
        int value = tag==0?(random.nextDouble() < 0.8 ? 2 : 4):tag;
        return new Tile(value);
    }

    public static Tile newTile(int value) {
        if (value % 2 != 0) {
            throw new IllegalArgumentException("Tile value must be multiple of 2");
        }

        return new Tile(value);
    }
    public Tile(Integer value,Location location) {
        this(value);
        this.location = location;
    }
    public Tile(Integer value) {
        final int squareSize = Board.CELL_SIZE - 13;
        setMinSize(squareSize, squareSize);
        setMaxSize(squareSize, squareSize);
        setPrefSize(squareSize, squareSize);
        setAlignment(Pos.CENTER);

        this.value = value;
        this.merged = false;
        setText(value.toString());
        getStyleClass().addAll("game-label", "game-tile-" + value);
    }

    public void merge(Tile another) {
        getStyleClass().remove("game-tile-" + value);
        this.value += another.getValue();
        setText(value.toString());
        merged = true;
        getStyleClass().add("game-tile-" + value);
    }

    public Integer getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Tile{" + "value=" + value + ", location=" + location + '}';
    }

    public boolean isMerged() {
        return merged;
    }

    public void clearMerge() {
        merged = false;
    }

    public boolean isMergeable(Optional<Tile> anotherTile) {
        return anotherTile.filter(t -> t.getValue().equals(getValue())).isPresent();
    }
}
