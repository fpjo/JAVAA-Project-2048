package project.javaa.project2048;

public record Location(int x, int y) {

    public Location offset(Direction direction) {
        return new Location(x + direction.getX(), y + direction.getY());
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }

    public double getLayoutY(int CELL_SIZE) {
        if (y == 0) {
            return CELL_SIZE / 2;
        }
        return (y * CELL_SIZE) + CELL_SIZE / 2;
    }

    public double getLayoutX(int CELL_SIZE) {
        if (x == 0) {
            return CELL_SIZE / 2;
        }
        return (x * CELL_SIZE) + CELL_SIZE / 2;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isValidFor(int gridSize) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }

}
