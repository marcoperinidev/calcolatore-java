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

    // gestisce numeri da 0 a 9
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

    // gestisce +, -, *, /
    @FXML
    private void onOperatorClick(ActionEvent event) {

        // se esite già un operatore, ignora il click per evitare premendo 2 volte un operatore ovviamente "5+" non può diventare un double e va in NumberFormatException
        if (operator != null) {
            return;
        }

        Button button = (Button) event.getSource();

        firstNumber = Double.parseDouble(display.getText());
        operator = button.getText();

        display.appendText(operator);
    }

    // gestisce il punto decimale (per entrambi gli operandi che possono essere entrambi con decimale)
    // inoltre se il currentNumber è vuoto e si preme l'operatore decimale, mostro "0."
    @FXML
    private void onDecimalClick() {

        String currentText = display.getText();
        String currentNumber;

        if (operator == null) {
            currentNumber = currentText;
        } else {
            int operatorPosition = currentText.lastIndexOf(operator);
            currentNumber = currentText.substring(operatorPosition + 1);
        }

        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                display.appendText("0.");
            } else {
                display.appendText(".");
            }
        }
    }

    // cancella operazione corrente
    @FXML
    private void onClearClick() {
        display.setText("0");
        firstNumber = 0;
        operator = null;
    }

    // esegue il calcolo
    @FXML
    private void onEqualsClick() {

        // nessun operatore selezionato
        if (operator == null) {
            return;
        }

        // prende la posizione ed estrae quello che viene dopo l'operatore per poterlo appendere
        int operatorPosition =
                display.getText().lastIndexOf(operator);

        String secondNumberText =
                display.getText().substring(operatorPosition + 1);

        // se non ho ancora inserito il secondo numero
        if (secondNumberText.isEmpty()) {
            return;
        }

        double secondNumber = Double.parseDouble(secondNumberText);

        // switch "moderno" contratto
        double result = switch (operator) {
            case "+" -> calculator.add(firstNumber, secondNumber);
            case "-" -> calculator.subtract(firstNumber, secondNumber);
            case "×" -> calculator.multiply(firstNumber, secondNumber);
            case "÷" -> calculator.divide(firstNumber, secondNumber);
            default -> 0;
        };

        display.setText(formatResult(result));
    }

    private String formatResult(double result) {
        // se modulo 1, la divisione per 1 è "0" non esistono decimali significativi da mostrare quindi mostriamo l'intero
        if (result % 1 == 0) {
            return String.valueOf((long) result);
        }

        return String.valueOf(result);
    }
}
