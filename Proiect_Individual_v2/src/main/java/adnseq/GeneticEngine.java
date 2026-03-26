package adnseq;

import java.util.ArrayList;
import java.util.List;

/**
 * Motorul algoritmului genetic.
 * Gestionează:
 * - inițializarea populației
 * - rularea generațiilor
 * - criteriile de oprire
 * - colectarea statisticilor (best/average fitness pe generații)
 */
public class GeneticEngine {

    private Populatie populatie;
    private String target;
    private double rataMutatie;
    private int maxGeneratii;

    // Istoric pentru statistici (util ulterior pentru grafice)
    private List<Double> bestHistory = new ArrayList<>();
    private List<Double> averageHistory = new ArrayList<>();

    /**
     * Constructorul motorului genetic.
     *
     * @param target              secvența ADN țintă
     * @param dimensiunePopulatie numărul de indivizi din populație
     * @param rataMutatie         rata de mutație pe individ
     * @param maxGeneratii        numărul maxim de generații de rulat
     */
    public GeneticEngine(String target, int dimensiunePopulatie,
                         double rataMutatie, int maxGeneratii) {
        this.target = target;
        this.rataMutatie = rataMutatie;
        this.maxGeneratii = maxGeneratii;
        this.populatie = new Populatie(dimensiunePopulatie, target);
        this.populatie.calculeazaFitness();
    }

    /**
     * Rulează algoritmul genetic până când:
     * - se găsește o soluție perfectă (fitness = 1.0), sau
     * - nu mai există îmbunătățiri timp de un anumit număr de generații, sau
     * - se atinge numărul maxim de generații.
     *
     * @return cel mai bun individ găsit
     */
    public SecventaADN ruleaza() {
        double celMaiBunGlobal = 0.0;
        int generatiiFaraImbunatatire = 0;
        int pragFaraImbunatatire = 50; // număr de generații fără progres după care oprim

        for (int generatie = 0; generatie < maxGeneratii; generatie++) {
            SecventaADN best = populatie.selecteazaCelMaiBun();
            double bestFitness = best.getFitness();
            double avgFitness = populatie.getAverageFitness();

            // salvăm în istoric (pentru grafice/statistici)
            bestHistory.add(bestFitness);
            averageHistory.add(avgFitness);

            // log în consolă (se poate scoate sau muta în UI mai târziu)
            System.out.println("Gen: " + generatie + " | " +
                    best.getSecventa_ADN() + " | Best: " + bestFitness +
                    " | Avg: " + avgFitness);

            // 1. criteriu: soluție perfectă
            if (bestFitness == 1.0) {
                System.out.println("Am gasit solutia perfecta la generatia " + generatie);
                return best;
            }

            // 2. criteriu: verificăm dacă există îmbunătățire globală
            if (bestFitness > celMaiBunGlobal) {
                celMaiBunGlobal = bestFitness;
                generatiiFaraImbunatatire = 0;
            } else {
                generatiiFaraImbunatatire++;
            }

            // dacă nu se mai îmbunătățește după un anumit număr de generații, oprim
            if (generatiiFaraImbunatatire >= pragFaraImbunatatire) {
                System.out.println("Algoritmul s-a stabilizat (nu se mai imbunatateste) la generatia " + generatie);
                return best;
            }

            // 3. trecem la următoarea generație
            populatie.genereazaNouaGeneratie(rataMutatie);
        }

        // dacă am ieșit pentru că am atins maxGeneratii
        System.out.println("Am atins numarul maxim de generatii.");
        return populatie.selecteazaCelMaiBun();
    }

    /**
     * @return lista cu valorile fitness-ului maxim pe fiecare generație
     */
    public List<Double> getBestHistory() {
        return bestHistory;
    }

    /**
     * @return lista cu valorile fitness-ului mediu pe fiecare generație
     */
    public List<Double> getAverageHistory() {
        return averageHistory;
    }

    /**
     * @return populația curentă (util dacă vrem să afișăm indivizii în UI)
     */
    public Populatie getPopulatie() {
        return populatie;
    }
}
