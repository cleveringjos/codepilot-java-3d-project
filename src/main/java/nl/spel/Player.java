package nl.spel;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

/** Blokpoppetje. Kijkrichting bij hoek 0 is +Z. y is negatief naar boven. */
public class Player extends Group {
    private static final double JUMP_SPEED = 6.0; // eenheden/s
    private static final double GRAVITY = 18.0;   // eenheden/s^2

    private final Rotate yaw = new Rotate(0, Rotate.Y_AXIS);
    private final Group leftArm, rightArm, leftLeg, rightLeg;
    private double angle = 0; // graden
    private double x = 0, z = 0;
    private double walkPhase = 0;
    private double height = 0; // boven de grond (>= 0)
    private double velY = 0;

    public Player() {
        PhongMaterial skin = new PhongMaterial(Color.BISQUE);
        PhongMaterial shirt = new PhongMaterial(Color.CORNFLOWERBLUE);
        PhongMaterial pants = new PhongMaterial(Color.DARKBLUE);

        Box head = part(0.5, 0.5, 0.5, skin, 0, -1.75, 0);
        Box body = part(0.6, 0.8, 0.3, shirt, 0, -1.1, 0);
        leftArm = limb(0.2, 0.7, 0.2, skin, -0.4, -1.45);
        rightArm = limb(0.2, 0.7, 0.2, skin, 0.4, -1.45);
        leftLeg = limb(0.25, 0.7, 0.25, pants, -0.15, -0.7);
        rightLeg = limb(0.25, 0.7, 0.25, pants, 0.15, -0.7);

        getChildren().addAll(head, body, leftArm, rightArm, leftLeg, rightLeg);
        getTransforms().add(yaw);
    }

    private Box part(double w, double h, double d, PhongMaterial m, double px, double py, double pz) {
        Box b = new Box(w, h, d);
        b.setMaterial(m);
        b.setTranslateX(px);
        b.setTranslateY(py);
        b.setTranslateZ(pz);
        return b;
    }

    /** Ledemaat met draaipunt bovenaan (schouder/heup). */
    private Group limb(double w, double h, double d, PhongMaterial m, double px, double pivotY) {
        Box b = part(w, h, d, m, 0, h / 2, 0);
        Group g = new Group(b);
        g.setTranslateX(px);
        g.setTranslateY(pivotY);
        return g;
    }

    public void turn(double degrees) {
        angle += degrees;
        yaw.setAngle(angle);
    }

    /** Beweeg langs de kijkrichting (negatief = achteruit). */
    public void move(double distance) {
        double rad = Math.toRadians(angle);
        x += Math.sin(rad) * distance;
        z += Math.cos(rad) * distance;
    }

    public void clamp(double limit) {
        x = Math.max(-limit, Math.min(limit, x));
        z = Math.max(-limit, Math.min(limit, z));
    }

    /** Spring, alleen mogelijk vanaf de grond. */
    public void jump() {
        if (height == 0) velY = JUMP_SPEED;
    }

    /** Zet het poppetje terug op de startpositie. */
    public void reset() {
        x = 0;
        z = 0;
        angle = 0;
        height = 0;
        velY = 0;
        walkPhase = 0;
        yaw.setAngle(0);
        update(false, 0);
    }

    /** Werk de scenepositie en loopanimatie bij. */
    public void update(boolean walking, double dt) {
        setTranslateX(x);
        setTranslateZ(z);
        if (height > 0 || velY > 0) {
            velY -= GRAVITY * dt;
            height += velY * dt;
            if (height <= 0) {
                height = 0;
                velY = 0;
            }
        }
        setTranslateY(-height); // +Y wijst omlaag
        double swing = 0;
        if (walking && height == 0) {
            walkPhase += dt * 10;
            swing = Math.sin(walkPhase) * 40;
        } else {
            walkPhase = 0;
        }
        leftArm.getTransforms().setAll(new Rotate(swing, Rotate.X_AXIS));
        rightArm.getTransforms().setAll(new Rotate(-swing, Rotate.X_AXIS));
        leftLeg.getTransforms().setAll(new Rotate(-swing, Rotate.X_AXIS));
        rightLeg.getTransforms().setAll(new Rotate(swing, Rotate.X_AXIS));
    }

    public Point3D position() {
        return new Point3D(x, 0, z);
    }

    public double angleDegrees() {
        return angle;
    }
}
