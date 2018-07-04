import java.io.File;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Square {

    public static final int SIZE = 206;

    private int x, y;
    private Color color;
    private boolean hover, cpuPlaying;
    private MediaPlayer player;

    public Square(int xPos, int yPos, Color col, String noteStr) {
        x = xPos; y = yPos;
        color = col;
        hover = false;
        cpuPlaying = false;
        player = makePlayer(noteStr);
    }

    // https://stackoverflow.com/questions/23202272/how-to-play-sounds-with-javafx
    private MediaPlayer makePlayer(String noteStr) {
        String musicFile = "data/notes/" + noteStr + ".mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        return new MediaPlayer(sound);
    }

    public void show(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x, y, SIZE, SIZE);
        if (hover || cpuPlaying) gc.setFill(color.brighter());
        else gc.setFill(color);
        gc.fillRect(x+10, y+10, SIZE-20, SIZE-20);
    }

    public void playSound() {
        cpuPlaying = true;
        player.play();
        player.seek(player.getStartTime());
    }

    // Receives coords of mouse
    public void checkHover(double mx, double my) {
        if (x < mx && (x+SIZE) > mx && y < my && (y+SIZE) > my) {
            hover = true;
        } else {
            hover = false;
        }
    }

    // Receives coords of mouse click
    public void checkClick(double mx, double my) {
        if (x < mx && (x+SIZE) > mx && y < my && (y+SIZE) > my) {
            playSound();
        }
    }

    public void checkPlayback() {
        if (cpuPlaying) {
            if (player.getCurrentTime().equals(player.getStopTime())) {
                cpuPlaying = false;
            }
        }
    }

}
