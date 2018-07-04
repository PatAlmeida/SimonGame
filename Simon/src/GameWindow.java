import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class GameWindow {

    private static int WIDTH = 512;
    private static int HEIGHT = 512;
    private static int CV = 180; // COLOR_VAL

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
        scene.setOnMousePressed( e-> {
            for (Square square : squares) {
                square.checkClick(e.getX(), e.getY());
            }
        });

        squares = new Square[4];
        squares[0] = new Square(40, 40, Color.rgb(CV, 0, 0), "c4");
        squares[1] = new Square(266, 40, Color.rgb(0, CV, 0), "e4");
        squares[2] = new Square(40, 266, Color.rgb(0, 0, CV), "g4");
        squares[3] = new Square(266, 266, Color.rgb(CV, CV, 0), "c5");

        /* // Testing music
        String musicFile = "data/notes/c4.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play(); */

        // Game loop - runs at (about) 60fps
        new AnimationTimer() {
            public void handle(long nano) {
                showBackground(gc);
                showSquares(gc);
                checkSquarePlayback();
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

}
