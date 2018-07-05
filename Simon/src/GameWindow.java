import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow {

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static final int NUM_SQUARES = 4;
    private static final int CV = 180; // COLOR_VAL
    private static final int STARTING_LENGTH = 3;
    private static final int WAIT_TIME = 60;
    private static final int SQUARE_WAIT = 40;

    private GameState state;
    private int simonLength, waitTimeCount, squareWaitCount, simonIndex;
    private ArrayList<Integer> sequence, playerPushes;
    private Square[] squares;

    public GameWindow(Stage myStage) {

        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnMouseMoved(e -> {
            for (Square square : squares) {
                square.checkHover(e.getX(), e.getY());
            }
        });
        scene.setOnMousePressed(e -> {
            for (int i = 0; i < NUM_SQUARES; i++) {
                boolean clicked = squares[i].checkClick(e.getX(), e.getY());
                if (clicked && state == GameState.PLAYER_PLAYING) {
                    playerPushes.add(i);
                }
            }
        });

        state = GameState.WAITING;
        simonLength = STARTING_LENGTH;
        waitTimeCount = WAIT_TIME;
        squareWaitCount = 1;
        simonIndex = 0;
        sequence = new ArrayList<Integer>();
        makeNewSequence();
        playerPushes = new ArrayList<Integer>();
        squares = new Square[NUM_SQUARES];
        squares[0] = new Square(40, 40, Color.rgb(CV, 0, 0), "c4");
        squares[1] = new Square(266, 40, Color.rgb(0, CV, 0), "e4");
        squares[2] = new Square(40, 266, Color.rgb(0, 0, CV), "g4");
        squares[3] = new Square(266, 266, Color.rgb(CV, CV, 0), "c5");

        // Game loop - runs at (about) 60fps
        new AnimationTimer() {
            public void handle(long nano) {
                showBackground(gc);
                showSquares(gc);
                checkSquarePlayback();
                doGame();
            }
        }.start();

        myStage.setTitle("Simon");
        myStage.setScene(scene);
        myStage.show();

    }

    private void showBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(80, 80, 80));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.rgb(40, 40, 40));
        gc.fillRect(20, 20, WIDTH-40, HEIGHT-40);
    }

    private void showSquares(GraphicsContext gc) {
        for (Square square : squares) {
            square.show(gc);
        }
    }

    private void checkSquarePlayback() {
        for (Square square : squares) {
            square.checkPlayback();
        }
    }

    private void doGame() {

        switch (state) {

            case WAITING:
                waitTimeCount--;
                if (waitTimeCount == 0) {
                    waitTimeCount = WAIT_TIME;
                    state = GameState.CPU_PLAYING;
                }
                break;

            case CPU_PLAYING:
                squareWaitCount--;
                if (squareWaitCount == 0) {
                    if (simonIndex != simonLength) {
                        int next = sequence.get(simonIndex);
                        squares[next].playSound();
                        simonIndex++;
                    } else {
                        simonIndex = 0;
                        state = GameState.PLAYER_PLAYING;
                    }
                    squareWaitCount = SQUARE_WAIT;
                }
                break;

            case PLAYER_PLAYING:
                if (playerPushes.size() == simonLength) {
                    boolean allMatch = true;
                    for (int i = 0; i < simonLength; i++) {
                        if (sequence.get(i) != playerPushes.get(i)) {
                            allMatch = false;
                            i = simonLength;
                        }
                    }
                    if (allMatch) {
                        simonLength++;
                        playerPushes.clear();
                        makeNewSequence();
                        state = GameState.WAITING;
                    } else {
                        System.out.println("Game Over");
                        System.exit(0);
                    }
                }
                break;

        }

    }

    private void makeNewSequence() {
        sequence.clear();
        int firstSquare = (int) (Math.random() * NUM_SQUARES);
        sequence.add(firstSquare);
        for (int i = 1; i < simonLength; i++) {
            int prevSquare = sequence.get(i-1);
            int nextSquare = prevSquare;
            while (nextSquare == prevSquare) {
                nextSquare = (int) (Math.random() * NUM_SQUARES);
            }
            sequence.add(nextSquare);
        }
    }

}
