package adnseq;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controllerul interfeței grafice. Preia datele din UI,
 * pornește motorul genetic și afișează rezultatele.
 */
public class MainController {

    @FXML
    private TextField targetField;

    @FXML
    private TextField popSizeField;

    @FXML
    private TextField mutationField;

    @FXML
    private TextField maxGenerationsField;

    @FXML
    private Button startButton;

    @FXML
    private Label bestLabel;

    @FXML
    private Label fitnessLabel;

    @FXML
    private Label generationLabel;

    @FXML
    private TextArea logArea;

    @FXML
    private TextFlow bestSequenceFlow;

    @FXML
    private LineChart<Number, Number> fitnessChart;

    @FXML
    private TextArea infoArea;

    @FXML
    private Button openInfoWindowButton;

    @FXML
    public void initialize() {
        // grafic gol la început
        if (fitnessChart != null) {
            fitnessChart.setAnimated(false);
            fitnessChart.getData().clear();
        }

        // valori implicite
        targetField.setText("ATGCGA");
        popSizeField.setText("20");
        mutationField.setText("0.05");
        maxGenerationsField.setText("500");

        // umplem infoArea cu explicațiile rezumate
        infoArea.setText(getInfoText());

        // acțiuni pe butoane
        startButton.setOnAction(e -> pornesteSimularea());
        openInfoWindowButton.setOnAction(e -> deschideFereastraInfo());
    }

    private void pornesteSimularea() {
        try {
            String target = targetField.getText().trim().toUpperCase();
            String popSizeText = popSizeField.getText().trim();
            String mutationText = mutationField.getText().trim();
            String maxGenText = maxGenerationsField.getText().trim();

            // Validări
            if (target.isEmpty()) {
                afiseazaEroare("Secvența țintă nu poate fi goală.");
                return;
            }

            if (!esteSecventaADNValida(target)) {
                afiseazaEroare("Secvența țintă trebuie să conțină doar literele A, C, G, T.");
                return;
            }

            int popSize = Integer.parseInt(popSizeText);
            double mutationRate = Double.parseDouble(mutationText);
            int maxGeneratii = Integer.parseInt(maxGenText);

            if (popSize <= 0) {
                afiseazaEroare("Dimensiunea populației trebuie să fie > 0.");
                return;
            }
            if (mutationRate < 0 || mutationRate > 1) {
                afiseazaEroare("Rata de mutație trebuie să fie între 0 și 1 (ex: 0.05).");
                return;
            }
            if (maxGeneratii <= 0) {
                afiseazaEroare("Numărul maxim de generații trebuie să fie > 0.");
                return;
            }

            // reset UI
            logArea.clear();
            bestSequenceFlow.getChildren().clear();
            bestLabel.setText("Cel mai bun individ: -");
            fitnessLabel.setText("Fitness: -");
            generationLabel.setText("Generații rulate: -");
            if (fitnessChart != null) {
                fitnessChart.getData().clear();
            }

            // --- MODIFICARE AICI: Apelăm API-ul în loc de Engine direct ---
            GeneticResponse response = GeneticService.solve(
                    target,
                    popSize,
                    mutationRate,
                    maxGeneratii
            );

            // Actualizăm UI cu rezultatul final extras din Response
            bestLabel.setText("Cel mai bun individ: " + response.bestSequence);
            fitnessLabel.setText("Fitness: " + response.fitness);
            generationLabel.setText("Generații rulate: " + response.generationsRun);

            // colorează secvența folosind datele din API
            afiseazaSecventaColorata(response.bestSequence, target);

            // LOG text folosind istoricul din Response
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < response.bestHistory.size(); i++) {
                sb.append("Gen ")
                        .append(i)
                        .append(" | Best: ")
                        .append(response.bestHistory.get(i))
                        .append(" | Avg: ")
                        .append(response.avgHistory.get(i))
                        .append("\n");
            }
            logArea.setText(sb.toString());

            // POPULĂM GRAFICUL
            if (fitnessChart != null) {
                XYChart.Series<Number, Number> bestSeries = new XYChart.Series<>();
                bestSeries.setName("Best fitness");

                XYChart.Series<Number, Number> avgSeries = new XYChart.Series<>();
                avgSeries.setName("Average fitness");

                for (int i = 0; i < response.bestHistory.size(); i++) {
                    bestSeries.getData().add(new XYChart.Data<>(i, response.bestHistory.get(i)));
                    avgSeries.getData().add(new XYChart.Data<>(i, response.avgHistory.get(i)));
                }

                fitnessChart.getData().add(bestSeries);
                fitnessChart.getData().add(avgSeries);
            }
            // --- SFÂRȘIT MODIFICARE ---
        } catch (NumberFormatException ex) {
            afiseazaEroare("Dimensiunea populației, rata de mutație și numărul maxim de generații trebuie să fie numere.");
        } catch (Exception ex) {
            afiseazaEroare("Eroare neașteptată: " + ex.getMessage());
        }
    }

    /**
     * Afișează secvența cea mai bună în TextFlow, colorând:
     * - verde literele corecte (identice cu ținta pe aceeași poziție)
     * - roșu literele greșite.
     */
    private void afiseazaSecventaColorata(String secventa, String target) {
        bestSequenceFlow.getChildren().clear();

        int len = Math.min(secventa.length(), target.length());

        for (int i = 0; i < len; i++) {
            char c = secventa.charAt(i);
            Text t = new Text(String.valueOf(c));

            if (c == target.charAt(i)) {
                t.setStyle("-fx-fill: green; -fx-font-weight: bold; -fx-font-size: 18;");
            } else {
                t.setStyle("-fx-fill: red; -fx-font-size: 18;");
            }

            bestSequenceFlow.getChildren().add(t);
        }
    }

    /**
     * Validează dacă secvența ADN conține doar A, C, G, T.
     */
    private boolean esteSecventaADNValida(String seq) {
        for (char c : seq.toCharArray()) {
            if (c != 'A' && c != 'C' && c != 'G' && c != 'T') {
                return false;
            }
        }
        return true;
    }

    /**
     * Setează un mesaj de eroare lizibil în UI.
     */
    private void afiseazaEroare(String mesaj) {
        bestLabel.setText("Eroare: " + mesaj);
        fitnessLabel.setText("");
        bestSequenceFlow.getChildren().clear();
        if (fitnessChart != null) {
            fitnessChart.getData().clear();
        }
    }

    /**
     * Textul explicativ (biologie + algoritm) folosit și în zona mică, și în fereastra mare.
     */
    private String getInfoText() {
        return ""
                + "CE REPREZINTĂ ACEASTĂ APLICAȚIE?\n"
                + "---------------------------------\n"
                + "Aplicația simulează evoluția unei populații de secvențe ADN\n"
                + "către o secvență țintă specificată de utilizator, folosind un algoritm genetic.\n"
                + "\n"
                + "NOȚIUNI DE BIOLOGIE (simplificat):\n"
                + " - ADN-ul este un lanț de nucleotide: A (adenină), C (citozină), G (guanină), T (timină).\n"
                + " - O secvență ADN (ex: ATGCGA) poate fi văzută ca o 'instrucțiune' genetică.\n"
                + " - În natură, populațiile de indivizi evoluează în timp prin mutații și selecție naturală.\n"
                + "\n"
                + "CORESPONDENȚA CU ALGORITMUL GENETIC:\n"
                + " - Fiecare individ = o secvență ADN (un șir de A/C/G/T).\n"
                + " - Populația = un set de indivizi (mai multe secvențe ADN).\n"
                + " - Fitness-ul = cât de apropiat este individul de secvența țintă\n"
                + "   (procent de litere identice pe aceleași poziții).\n"
                + " - Selecția = indivizii cu fitness mai bun au șanse mai mari să fie aleși ca părinți.\n"
                + " - Crossover (încrucișare) = combinăm două secvențe părinte pentru a obține un copil.\n"
                + " - Mutația = modificăm aleator unele poziții cu o anumită probabilitate.\n"
                + "\n"
                + "CUM FUNCȚIONEAZĂ SIMULAREA:\n"
                + " 1. Generăm o populație inițială aleatoare de secvențe ADN.\n"
                + " 2. Calculăm fitness-ul fiecărui individ față de ținta introdusă.\n"
                + " 3. Alegem părinți (mai buni) și producem o nouă generație prin crossover + mutație.\n"
                + " 4. Repetăm pașii 2-3 până când:\n"
                + "    - găsim un individ cu fitness = 1 (identic cu ținta), sau\n"
                + "    - se atinge numărul maxim de generații,\n"
                + "    - sau nu se mai îmbunătățește soluția după un anumit număr de generații.\n"
                + "\n"
                + "CUM INTERPRETEZI GRAFICUL:\n"
                + " - Axa X: numărul generației.\n"
                + " - Axa Y: valoarea fitness-ului (între 0 și 1).\n"
                + " - Linia 'Best fitness': cel mai bun individ din fiecare generație.\n"
                + " - Linia 'Average fitness': fitness-ul mediu al populației.\n"
                + "   În timp, liniile ar trebui să urce spre 1 dacă algoritmul convergă.\n"
                + "\n"
                + "UTILITATE EDUCAȚIONALĂ:\n"
                + " - Demonstrează cum putem modela concepte inspirate din biologie\n"
                + "   (evoluție, selecție, mutație) în informatică.\n"
                + " - Exemplu de algoritm euristic folosit în optimizare și inteligență artificială.\n";
    }

    /**
     * Deschide o fereastră nouă, mare, cu explicațiile teoretice.
     */
    private void deschideFereastraInfo() {
        Stage stage = new Stage();
        stage.setTitle("Explicații teoretice – ADN și algoritm genetic");

        TextArea textArea = new TextArea(getInfoText());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(30);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
