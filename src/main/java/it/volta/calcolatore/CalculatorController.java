package it.volta.calcolatore;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/* controlla comportamento interfaccia
* e.g. "utente clicca su tasto 7" */
/* prende i numeri, "chiama" Calculator e riceve il risultato e aggiorna il display */
/* conterrà i metodi dei click */
public class CalculatorController {

    @FXML
    private TextField display;
    /* in sostanza dice "crea una variabile calculator, di tipo Calculator, e mettici dentro un nuovo oggetto Calculator (costruttore)
    /* il "private" è perché quest'oggetto serve solo al Controller
    /* il "final" è perché voglio che la variabile punti sempre allo stesso oggetto Calculator */

    private final Calculator calculator = new Calculator();

    /* variabili come campi del controller per "salvare" i tasti premuti, disponibili tra un click e l'altro finché il controller esiste */
    private double firstNumber;
    private String operator;

    @FXML
    private void onNumberClick(ActionEvent event) {

        Button button = (Button) event.getSource();
        String number = button.getText();

        if (display.getText().equals("0")) {
            display.setText(number);
        } else {
            display.appendText(number);
        }
    }

    @FXML
    private void onOperatorClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        firstNumber = Double.parseDouble(display.getText());
        operator = button.getText();

        display.setText("0");
    }
}
