import java.util.ArrayList;
import java.util.Random;

public class Main {
    int rows=4;
    int cols=4;

    protected int[][] matrix = new int[rows][cols];
    protected int goal = 2048;
    //此处的大小、目标考虑由前端设置，待开发

    public void main(String[] args) {
        init();
    }

    public void setCols(int cols) {
        this.cols = cols;
        matrix = new int[rows][cols];
        init();
    }
    //设置列数

    public void setRows(int rows) {
        this.rows = rows;
        matrix = new int[rows][cols];
        init();
    }
    //设置行数

    public void setGoal(int goal) {
        this.goal = goal;
    }
    //设定目标

    public int getGoal() {
        return goal;
    }
    //返回目标

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    private Random rand = new Random();

    public boolean merge() {
        if (availableSlots().isEmpty()) {
            return false;
        }
        //如果没有空余的格子则返回false

        int[] randPos;
        randPos = availableSlots().get(rand.nextInt(availableSlots().size()));

        matrix[randPos[0]][randPos[1]] = rand.nextInt(0,2)*2;

        return true;
        //成功生成则返回true
    }

    private ArrayList<int[]> availableSlots() {
        ArrayList<int[]> slots = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    slots.add(new int[] {i,j});
                }
            }
        }

        return slots;
    }
    //返回一个包含空闲格子信息的列表

    public boolean slide(Direction direction) {
        boolean ifSuccess = false;

        ifSuccess = move(direction);
        eliminate(direction);
        merge();

        return ifSuccess;
    }
    //滑动成功时返回true（移动并消除）

    private boolean eliminate(Direction direction) {
        boolean eliminated = false;

        if (direction.getX() == 1) {
            for (int i = 0; i < rows; i++) {
                for (int j = cols-1; j >0; j--) {
                    if (matrix[i][j] == 0) {
                        continue;
                    }

                    if (matrix[i][j] == matrix[i][j-1]) {
                        matrix[i][j] = 2*matrix[i][j];
                        matrix[i][j-1] = 0;

                        eliminated = true;
                    }
                }
            }
        }
        //向右移动

        else if (direction.getY() == 1) {
            for (int j = 0; j < cols; j++) {
                for (int i = rows-1; i >0; i--) {
                    if (matrix[i][j] == 0) {
                        continue;
                    }

                    if (matrix[i][j] == matrix[i-1][j]) {
                        matrix[i][j] = 2*matrix[i][j];
                        matrix[i-1][j] = 0;

                        eliminated = true;
                    }
                }
            }
        }

        else if (direction.getX() == -1) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (matrix[i][j] == 0) {
                        continue;
                    }

                    if (matrix[i][j] == matrix[i][j+1]) {
                        matrix[i][j] = 2*matrix[i][j];
                        matrix[i][j+1] = 0;

                        eliminated = true;
                    }
                }
            }
        }

        else if (direction.getY() == -1) {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i <rows; i++) {
                    if (matrix[i][j] == 0) {
                        continue;
                    }

                    if (matrix[i][j] == matrix[i+1][j]) {
                        matrix[i][j] = 2*matrix[i][j];
                        matrix[i+1][j] = 0;

                        eliminated = true;
                    }
                }
            }
        }

        move(direction);
        return eliminated;
    }
    //消除成功即返回true，使用该方法前需要进行一次move消除空格

    private boolean move(Direction direction) {
        boolean moved = false;

        if (direction.getX() == 1) {
            for (int i = 0; i < rows; i++) {
                for (int j = cols-1; j >0; j--) {
                    if (matrix[i][j] == 0) {
                        matrix[i][j] = matrix[i][j-1];
                        matrix[i][j-1] = 0;

                        moved = true;
                    }
                }
            }
        }
        //向右移动

        else if (direction.getY() == 1) {
            for (int j = 0; j < cols; j++) {
                for (int i = rows-1; i > 0; i--) {
                    if (matrix[i][j] == 0) {
                        matrix[i][j] = matrix[i-1][j];
                        matrix[i-1][j] = 0;

                        moved = true;
                    }
                }
            }
        }
        //向下移动

        else if (direction.getY() == -1) {
            for (int j = 0; j < cols; j++) {
                for (int i = 0; i < rows-1; i++) {
                    if (matrix[i][j] == 0) {
                        matrix[i][j] = matrix[i+1][j];
                        matrix[i+1][j] = 0;

                        moved = true;
                    }
                }
            }
        }
        //向上移动

        else if (direction.getX() == -1) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols-1; j++) {
                    if (matrix[i][j] == 0) {
                        matrix[i][j] = matrix[i][j+1];
                        matrix[i][j+1] = 0;

                        moved = true;
                    }
                }
            }
        }
        //向左移动

        return moved;
        //如果成功完成了任意方向的移动操作，则会返回true
    }
    //移动成功时返回true

    public void init() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j : matrix[i]) {
                matrix[i][j] = 0;
            }
        }
        //生成一个全为0的初始矩阵
    }
    //初始化

    private boolean ifSuccess() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] >= goal) {
                    return true;
                }
            }
        }

        return false;
    }
    //检测是否达到目标
}

