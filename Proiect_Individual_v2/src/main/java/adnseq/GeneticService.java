package adnseq;

import java.util.List;

/**
 * API-ul central al bibliotecii ADNSeq.
 * Izolează logica de calcul de interfața grafică.
 */
public class GeneticService {

    /**
     * Metoda principală a API-ului.
     */
    public static GeneticResponse solve(String target, int popSize, double mutation, int maxGen) {
        // Instanțiem motorul intern
        GeneticEngine engine = new GeneticEngine(target, popSize, mutation, maxGen);

        // Rulăm algoritmul
        SecventaADN best = engine.ruleaza();

        // Împachetăm rezultatele într-un obiect de răspuns (format standard API)
        return new GeneticResponse(
                best.getSecventa_ADN(),
                best.getFitness(),
                engine.getBestHistory().size(),
                engine.getBestHistory(),
                engine.getAverageHistory()
        );
    }
}

/**
 * Data Transfer Object (DTO) - formatul de date returnat de API.
 */
class GeneticResponse {
    public final String bestSequence;
    public final double fitness;
    public final int generationsRun;
    public final List<Double> bestHistory;
    public final List<Double> avgHistory;

    public GeneticResponse(String seq, double fit, int gen, List<Double> bestH, List<Double> avgH) {
        this.bestSequence = seq;
        this.fitness = fit;
        this.generationsRun = gen;
        this.bestHistory = bestH;
        this.avgHistory = avgH;
    }
}