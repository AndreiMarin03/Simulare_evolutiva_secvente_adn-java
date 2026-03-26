package adnseq;

public class APITEST{
    public static void main(String[] args) {
        System.out.println("--- Testare API Genetic ---");

        // Apelăm API-ul exact ca în Controller, dar aici suntem în consolă
        GeneticResponse res = GeneticService.solve("ATGCGA", 50, 0.05, 100);

        System.out.println("Rezultat primit de la API:");
        System.out.println("Secventa: " + res.bestSequence);
        System.out.println("Fitness final: " + res.fitness);
        System.out.println("Generatii rulate: " + res.generationsRun);
    }
}