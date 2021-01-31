import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    public Canvas canvas;
    private static final int cellSize = 10;
    public Button startStopButton;
    public Button stepButton;
    public Label generationNum;
    public ToggleButton editFieldToggleButton;
    public Button zeroGenButton;
    private int currentGen = 0;
    private boolean isRun = false;
    private boolean isEditable = false;
    private int rows = 50;
    private int cols = 100;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        canvas.setHeight(rows * cellSize);

        canvas.setWidth(cols * cellSize);
        var gc = canvas.getGraphicsContext2D();
        GameOfLife gameOfLife = new GameOfLife(rows, cols);
        drawField(gc, gameOfLife.getMatrix(), rows, cols);

        AnimationTimer runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(100)) {
                    gameOfLife.step();
                    drawField(gc, gameOfLife.getMatrix(), rows, cols);
                    incrGenCounter();
                    lastUpdate = now;
                }
            }
        };
        startStopButton.setOnAction((actionEvent ->
        {
            if (isRun) {
                isRun = false;
                runAnimation.stop();
                stepButton.setDisable(false);
                editFieldToggleButton.setDisable(false);
                zeroGenButton.setDisable(false);
            } else {
                isRun = true;
                runAnimation.start();
                stepButton.setDisable(true);
                editFieldToggleButton.setDisable(true);
                zeroGenButton.setDisable(true);
            }
        }));

        stepButton.setOnAction((actionEvent -> {
            gameOfLife.step();
            drawField(gc, gameOfLife.getMatrix(), rows, cols);
            incrGenCounter();
        }));

        zeroGenButton.setOnAction((actionEvent -> {
            currentGen  = 0;
            generationNum.setText(String.valueOf(currentGen));
        }));

        editFieldToggleButton.setOnAction((actionEvent -> {
            if(isEditable){
                isEditable = false;
                stepButton.setDisable(false);
                startStopButton.setDisable(false);
            }else {
                isEditable = true;
                stepButton.setDisable(true);
                startStopButton.setDisable(true);
            }
        }));

        canvas.setOnMouseClicked(mouseEvent -> {
            mouseHandlerFunc(mouseEvent, gameOfLife, gc);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                mouseEvent -> mouseHandlerFunc(mouseEvent, gameOfLife, gc));
    }


    void mouseHandlerFunc(MouseEvent mouseEvent , GameOfLife gameOfLife, GraphicsContext gc){
        if(isEditable){
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            int column = (int) (Math.ceil(x)/cellSize);
            int row = (int) (Math.ceil(y)/cellSize);
            if(column < cols && row < rows) {
                boolean[][] matrix = gameOfLife.getMatrix();
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    matrix[column][row] = true;
                } else {
                    matrix[column][row] = false;
                }
                gameOfLife.setMatrix(matrix);
                drawField(gc, gameOfLife.getMatrix(), rows, cols);
            }
        }
    }

    private void incrGenCounter(){
        currentGen++;
        generationNum.setText(String.valueOf(currentGen));
    }

    private void drawField(GraphicsContext gc, boolean[][] field, int rows, int cols) {
        gc.setFill(Color.LAVENDER);
        gc.fillRect(0, 0, cols * cellSize, rows * cellSize);

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j]) {
                    // first rect will end up becoming the border
                    gc.setFill(Color.gray(0.5, 0.5));
                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    gc.setFill(Color.RED);
                    gc.fillRect((i * cellSize) + 1, (j * cellSize) + 1, cellSize - 2, cellSize - 2);
                } else {
                    gc.setFill(Color.gray(0.5, 0.5));
                    gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    gc.setFill(Color.WHITE);
                    gc.fillRect((i * cellSize) + 1, (j * cellSize) + 1, cellSize - 2, cellSize - 2);
                }
            }
        }
    }
}

