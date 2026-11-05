package nl.spel;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.EnumSet;
import java.util.Set;

public class Main extends Application {
    private static final int GRID_SIZE = 20;
    private static final double WALK_SPEED = 3.0;   // eenheden/s
    private static final double TURN_SPEED = 120.0; // graden/s

    private final Set<KeyCode> keys = EnumSet.noneOf(KeyCode.class);
    private boolean paused = false;

    @Override
    public void start(Stage stage) {
        Grid grid = new Grid(GRID_SIZE);
        Player player = new Player();

        PointLight sun = new PointLight(Color.WHITE);
        sun.setTranslateY(-30);
        Group world = new Group(grid, player, new AmbientLight(Color.rgb(120, 120, 120)), sun);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(500);
        camera.setFieldOfView(60);
        Translate camPos = new Translate();
        Rotate camYaw = new Rotate(0, Rotate.Y_AXIS);
        Rotate camPitch = new Rotate(-25, Rotate.X_AXIS);
        camera.getTransforms().addAll(camPos, camYaw, camPitch, new Translate(0, 0, -6));

        StackPane root = new StackPane();
        SubScene sub = new SubScene(world, 900, 600, true, SceneAntialiasing.BALANCED);
        sub.setFill(Color.SKYBLUE);
        sub.setCamera(camera);
        sub.widthProperty().bind(root.widthProperty());
        sub.heightProperty().bind(root.heightProperty());
        PauseMenu menu = new PauseMenu(
                () -> setPaused(false, root),
                () -> {
                    player.reset();
                    setPaused(false, root);
                },
                Platform::exit);
        root.getChildren().addAll(sub, menu);

        Scene scene = new Scene(root, 900, 600);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                setPaused(!paused, root);
                e.consume();
                return;
            }
            if (paused) return; // laat knoppen bedienbaar met toetsenbord
            keys.add(e.getCode());
            e.consume();
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (paused) return;
            keys.remove(e.getCode());
            e.consume();
        });

        new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = Math.min((now - last) / 1e9, 0.1);
                last = now;
                if (paused) return;

                if (keys.contains(KeyCode.LEFT)) player.turn(-TURN_SPEED * dt);
                if (keys.contains(KeyCode.RIGHT)) player.turn(TURN_SPEED * dt);

                double dir = 0;
                if (keys.contains(KeyCode.UP)) dir += 1;
                if (keys.contains(KeyCode.DOWN)) dir -= 1;
                if (keys.contains(KeyCode.SPACE)) player.jump();
                player.move(dir * WALK_SPEED * dt);
                player.clamp(grid.halfSize() - 0.5);
                player.update(dir != 0, dt);

                // Volgcamera: op het poppetje, zelfde draaihoek, achter en boven.
                var p = player.position();
                camPos.setX(p.getX());
                camPos.setY(-1.5);
                camPos.setZ(p.getZ());
                camYaw.setAngle(player.angleDegrees());
            }
        }.start();

        stage.setTitle("3D Spel - pijltjes: lopen, spatie: springen, ESC: menu");
        stage.setScene(scene);
        stage.show();
    }

    private void setPaused(boolean value, StackPane root) {
        paused = value;
        keys.clear();
        root.getChildren().get(1).setVisible(value);
        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
