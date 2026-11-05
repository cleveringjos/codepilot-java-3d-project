package nl.spel;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/** Vloer van tegels met een dambordpatroon, gecentreerd rond de oorsprong. */
public class Grid extends Group {
    private final int size;

    public Grid(int size) {
        this.size = size;
        PhongMaterial light = new PhongMaterial(Color.LIGHTGRAY);
        PhongMaterial dark = new PhongMaterial(Color.DARKSLATEGRAY);
        double offset = size / 2.0 - 0.5;
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Box tile = new Box(0.96, 0.1, 0.96);
                tile.setMaterial((x + z) % 2 == 0 ? light : dark);
                tile.setTranslateX(x - offset);
                tile.setTranslateZ(z - offset);
                tile.setTranslateY(0.05);
                getChildren().add(tile);
            }
        }
    }

    /** Halve breedte van het raster. */
    public double halfSize() {
        return size / 2.0;
    }
}
