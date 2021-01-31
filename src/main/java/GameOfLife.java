import java.util.Arrays;

class GameOfLife {
    private boolean[][] matrix;
    private boolean[][] nextMatrix;
    private int rows;
    private int cols;

    public GameOfLife(int cols, int rows){
        this.rows = rows;
        this.cols = cols;

        matrix = new boolean[rows][cols];
        nextMatrix = new boolean[rows][cols];
    }

    public void step(){
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                int count = getCountOfNeighbors(y, x);

                if(matrix[y][x]) {
                    if(count < 2 || count > 3) {
                        nextMatrix[y][x] = false;
                    } else {
                        nextMatrix[y][x] = matrix[y][x];
                    }
                } else {
                    nextMatrix[y][x] = count == 3;
                }
            }
        }

        copyMatrix(nextMatrix, matrix);
        clearMatrix(nextMatrix);
    }

    public void setMatrix(boolean[][] matrix){
        this.matrix = matrix;
    }

    public boolean[][] getMatrix(){
        return matrix;
    }

    private int getCountOfNeighbors(int y, int x) {
        return calcCountOfNeighbors(
                matrix[recalculatePos(y + 1, rows)][x],
                matrix[recalculatePos(y - 1, rows)][x],
                matrix[y][recalculatePos(x - 1, cols)],
                matrix[y][recalculatePos(x + 1, cols)],
                matrix[recalculatePos(y - 1, rows)][recalculatePos( x - 1, cols)],
                matrix[recalculatePos(y + 1, rows)][recalculatePos(x - 1, cols)],
                matrix[recalculatePos(y - 1, rows)][recalculatePos(x + 1, cols)],
                matrix[recalculatePos(y + 1, rows)][recalculatePos(x + 1, cols)]);
    }

    private int recalculatePos(int pos, int max) {
        return pos == max ? 0 : pos == -1 ? max - 1 : pos;
    }

    private int calcCountOfNeighbors(boolean ... elements) {
        int count = 0;

        for(boolean element : elements) {
            if (element) {
                count++;
            }
        }

        return count;
    }

    public static void clearMatrix(boolean[][] matrix) {
        for (boolean[] array : matrix) {
            Arrays.fill(array, false);
        }
    }

    private static void copyMatrix(boolean[][] fromMatrix, boolean[][] toMatrix) {
        for(int i = 0; i < fromMatrix.length; i++) {
            System.arraycopy(fromMatrix[i], 0, toMatrix[i], 0, fromMatrix[i].length);
        }
    }
}
