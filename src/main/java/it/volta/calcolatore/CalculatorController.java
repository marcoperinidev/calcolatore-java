package it.volta.calcolatore;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
// per gestire la grandezza del font quando il numero diventa troppo lungo
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.Locale;

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

    // per gestire il fatto che appende cifre dopo il risultato dell'operazione precedente
    // True quando il display mostra il risultato dell'operazione precedente
    private boolean resultDisplayed = false;

    // gestisce numeri da 0 a 9
    @FXML
    private void onNumberClick(ActionEvent event) {

        Button button = (Button) event.getSource();
        String number = button.getText();
        /*
         * Se il display contiene il risultato precedente,
         * premendo un numero inizio una nuova operazione.
         *
         * Esempio:
         * 2 + 2 = 4
         * premo 3 -> display = 3, non 43
         */
        if (resultDisplayed) {
            display.setText(number);

            resultDisplayed = false;
            firstNumber = 0;
            operator = null;

            adjustDisplayFont();
            return;
        }

        String currentNumber;
        //recupera solo l'operando che sto scrivendo
        if (operator == null) {
            currentNumber = display.getText();
        } else {
            int operatorPosition =
                    display.getText().lastIndexOf(operator);

            currentNumber =
                    display.getText().substring(operatorPosition + 1);
        }

        // se c'è un "." permette al massimo 4 decimali
        if (currentNumber.contains(".")) {

            String decimals =
                    currentNumber.substring(currentNumber.indexOf(".") +1);

            if (decimals.length() >= 4) {
                return;
            }
        }

        // sostituisce lo 0 iniziale
        if (display.getText().equals("0")) {
            display.setText(number);
        } else {
            display.appendText(number);
        }

        adjustDisplayFont();
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

        // se mostrava il risultato, continua il calcolo dal risultato. esempio:
        // 2 + 2 = 4
        // poi + --> 4+
        resultDisplayed = false;

        adjustDisplayFont();
    }

    // gestisce il punto decimale (per entrambi gli operandi che possono essere entrambi con decimale)
    // inoltre se il currentNumber è vuoto e si preme l'operatore decimale, mostro "0."
    @FXML
    private void onDecimalClick() {

        // se premi "." dopo aver ottenuto il risultato inizia nuova operazione da 0
        if (resultDisplayed) {
            display.setText("0.");

            resultDisplayed = false;
            firstNumber = 0;
            operator = null;

            adjustDisplayFont();
            return;
        }

        String currentText = display.getText();
        String currentNumber;

        if (operator == null) {
            currentNumber = currentText;
        } else {
            int operatorPosition = currentText.lastIndexOf(operator);
            currentNumber = currentText.substring(operatorPosition + 1);
        }
        // operando può contenere un solo punto
        // se premi "." subito dopo un operatore (es) 5+ diventa 5+0.
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                display.appendText("0.");
            } else {
                display.appendText(".");
            }
        }

        adjustDisplayFont();
    }

    // cancella operazione corrente
    @FXML
    private void onClearClick() {
        display.setText("0");
        firstNumber = 0;
        operator = null;
        resultDisplayed = false;

        adjustDisplayFont();
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

        // operazione è terminata
        // operator torna null così "=" premuto di nuovo non continua a ricalcolare la vecchia operazione
        operator = null;
        resultDisplayed = true;
    }
    // formatta il risultato
    private String formatResult(double result) {
        // se modulo 1, la divisione per 1 è "0" non esistono decimali significativi da mostrare quindi mostriamo l'intero
        if (result % 1 == 0) {
            return String.valueOf((long) result);
        }
        /*
         * %.4f = massimo 4 cifre decimali.
         *
         * replaceAll("0+$", "")
         * elimina gli zeri finali.
         *
         * replaceAll("\\.$", "")
         * elimina un eventuale punto rimasto alla fine.
         */
        return String.format(Locale.US, "%.4f", result)
                .replaceAll("0*$", "")
                .replaceAll("\\.$", "");
    }

    // riduce automaticamente il font quando il testo non entra nel display
    private void adjustDisplayFont() {

        double fontSize = 38;
        double minFontSize = 24;

        // tolgo spazio per tener conto del padding display
        double availableWidth = display.getWidth() - 35;

        // creo oggetto text solo per misurare quanto spazio occupa il contenuto ma non viene mostrato in GUI
        Text text = new Text(display.getText());

        // finché il testo non entra riduco la grandezza 2px alla volta
        while (fontSize > minFontSize) {

            text.setFont(
                    Font.font(
                            display.getFont().getFamily(),
                            fontSize
                    )
            );

            double textWidth =
                    text.getLayoutBounds().getWidth();

            if (textWidth < availableWidth) {
                break;
            }
            fontSize -= 2;
        }

        // va applicata la dimensione trovata
        // il resto viene sempre dal CSS
        display.setStyle(
                "-fx-font-size: " + fontSize + "px;"
        );
    }
}
