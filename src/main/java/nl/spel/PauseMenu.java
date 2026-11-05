package nl.spel;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

/** Pauzemenu-overlay met Hervatten, Opnieuw beginnen en Afsluiten. */
public class PauseMenu extends VBox {
    public PauseMenu(Runnable onResume, Runnable onRestart, Runnable onQuit) {
        super(12);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.6), null, null)));

        Label title = new Label("Pauze");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 32px;");

        getChildren().addAll(title,
                button("Hervatten", onResume),
                button("Opnieuw beginnen", onRestart),
                button("Afsluiten", onQuit));
        setVisible(false);
    }

    private Button button(String text, Runnable action) {
        Button b = new Button(text);
        b.setPrefWidth(200);
        b.setOnAction(e -> action.run());
        return b;
    }
}
