package adnseq;

import java.util.Random;

/**
 * Reprezintă un individ din populație, modelat printr-o secvență ADN și un scor de fitness.
 * Secvența ADN este un șir de caractere format din {A, C, G, T}.
 */
public class SecventaADN {

    /** Secvența ADN a individului (cromozomul). */
    private String secventa_ADN;

    /** Scorul de fitness (între 0 și 1), cu cât mai aproape de 1, cu atât individul este mai bun. */
    private double fitness;

    /**
     * Constructor care generează o secvență ADN aleatoare de lungime dată.
     *
     * @param lungime lungimea secvenței ADN
     */
    public SecventaADN(int lungime) {
        char[] nucleotizi = {'A', 'C', 'G', 'T'}; // array de nucleotide posibile
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Construim secvența ADN caracter cu caracter, aleator
        for (int i = 0; i < lungime; i++) {
            char aleator = nucleotizi[random.nextInt(4)];
            sb.append(aleator);
        }

        secventa_ADN = sb.toString();
    }

    /**
     * Calculează fitness-ul individului în funcție de o secvență țintă.
     * Fitness = număr de caractere identice pe aceeași poziție / lungimea țintei.
     *
     * @param target secvența ADN țintă
     */
    public void calculamFitness(String target) {
        int scor = 0;
        for (int i = 0; i < secventa_ADN.length(); i++) {
            if (secventa_ADN.charAt(i) == target.charAt(i)) {
                scor++;
            }
        }
        this.fitness = (double) scor / target.length();
    }

    /**
     * @return secvența ADN a individului
     */
    public String getSecventa_ADN() {
        return secventa_ADN;
    }

    /**
     * @return scorul de fitness al individului
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Setează manual secvența ADN (folosită în reproducere pentru a construi copilul).
     *
     * @param secventa secvența ADN nouă
     */
    public void setSecventa_ADN(String secventa) {
        this.secventa_ADN = secventa;
    }

    /**
     * Aplică mutația asupra secvenței ADN.
     * Fiecare nucleotid are o probabilitate rataMutatie de a fi înlocuit cu un alt nucleotid aleator.
     *
     * @param rataMutatie probabilitatea de mutație pe fiecare poziție (ex: 0.01 = 1%)
     */
    public void mutatie(double rataMutatie) {
        char[] nucleotizi = {'A', 'C', 'G', 'T'};
        Random random = new Random();
        char[] sec = secventa_ADN.toCharArray();

        // Parcurgem fiecare poziție și mutăm cu probabilitatea rataMutatie
        for (int i = 0; i < sec.length; i++) {
            if (random.nextDouble() < rataMutatie) {
                sec[i] = nucleotizi[random.nextInt(4)];
            }
        }

        secventa_ADN = new String(sec);
    }
}
