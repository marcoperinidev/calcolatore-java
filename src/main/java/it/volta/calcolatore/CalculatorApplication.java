package it.volta.calcolatore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/* classe che avvia JavaFX e mostre le finestre */

public class CalculatorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(CalculatorApplication.class.getResource("calculator-view.fxml"));
        Font.loadFont(
                Objects.requireNonNull(
                        CalculatorApplication.class.getResource("fonts/PressStart2P-Regular.ttf")
                ).toExternalForm(),
                16
        );

        Scene scene = new Scene(fxmlLoader.load(), 320, 450);

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        CalculatorApplication.class.getResource("calculator.css")
                ).toExternalForm()
        );

        stage.setTitle("Calcolatore");
        stage.setScene(scene);
        stage.show();
    }
}
