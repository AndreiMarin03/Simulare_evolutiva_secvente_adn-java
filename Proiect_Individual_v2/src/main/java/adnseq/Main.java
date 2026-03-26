package adnseq;

/**
 * Punctul de intrare în aplicație.
 * Creează un motor genetic, pornește simularea și afișează cel mai bun individ găsit.
 */
public class Main {
    public static void main(String[] args) {
        String target = "ATGCGA";        // secvența ADN țintă
        int dimensiunePopulatie = 20;   // număr de indivizi
        double rataMutatie = 0.05;      // rata de mutație
        int maxGeneratii = 500;         // număr maxim de generații

        GeneticEngine engine = new GeneticEngine(
                target,
                dimensiunePopulatie,
                rataMutatie,
                maxGeneratii
        );

        SecventaADN best = engine.ruleaza();

        System.out.println("Cel mai bun individ final: " +
                best.getSecventa_ADN() +
                " | Fitness: " + best.getFitness());
    }
}
