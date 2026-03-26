package adnseq;

import java.util.Random;

/**
 * Reprezintă o populație de indivizi (secvențe ADN) și oferă
 * operațiile de bază pentru algoritmul genetic:
 * - calculul fitness-ului pentru toți indivizii
 * - selecția celui mai bun individ
 * - selecția părinților
 * - reproducere (crossover)
 * - generarea unei noi generații cu mutație și elitism
 */
public class Populatie {

    /** Lista de indivizi (secvențe ADN) din populație. */
    private SecventaADN[] indivizi;

    /** Secvența țintă față de care se calculează fitness-ul. */
    private String target;

    /**
     * Constructor: inițializează o populație de dimensiune dată cu indivizi aleatori.
     *
     * @param dimensiune dimensiunea populației (numărul de indivizi)
     * @param target     secvența ADN țintă
     */
    public Populatie(int dimensiune, String target) {
        this.target = target;
        indivizi = new SecventaADN[dimensiune];

        // Generăm fiecare individ cu o secvență ADN aleatoare
        for (int i = 0; i < dimensiune; i++) {
            indivizi[i] = new SecventaADN(target.length());
        }
    }

    /**
     * Recalculează fitness-ul tuturor indivizilor din populație.
     */
    public void calculeazaFitness() {
        for (SecventaADN s : indivizi) {
            s.calculamFitness(target);
        }
    }

    /**
     * Calculează fitness-ul mediu al populației.
     *
     * @return fitness-ul mediu
     */
    public double getAverageFitness() {
        double suma = 0.0;
        for (SecventaADN s : indivizi) {
            suma += s.getFitness();
        }
        return suma / indivizi.length;
    }

    /**
     * @return array-ul cu toți indivizii (util dacă vrei să îi parcurgi extern)
     */
    public SecventaADN[] getIndivizi() {
        return indivizi;
    }

    /**
     * Găsește și întoarce cel mai bun individ (cu fitness maxim) din populație.
     *
     * @return individul cu cel mai mare scor de fitness
     */
    public SecventaADN selecteazaCelMaiBun() {
        SecventaADN celMaiBun = indivizi[0];
        for (SecventaADN s : indivizi) {
            if (s.getFitness() > celMaiBun.getFitness()) {
                celMaiBun = s;
            }
        }
        return celMaiBun;
    }

    /**
     * @return scorul de fitness al celui mai bun individ
     */
    public double getBestFitness() {
        return selecteazaCelMaiBun().getFitness();
    }

    /**
     * Realizează reproducerea (crossover) între doi părinți.
     * Se alege un punct de tăiere aleator, iar copilul primește:
     * - partea stângă de la parinte1
     * - partea dreaptă de la parinte2.
     *
     * @param parinte1 primul părinte
     * @param parinte2 al doilea părinte
     * @return individul copil rezultat în urma crossover-ului
     */
    public SecventaADN reproducere(SecventaADN parinte1, SecventaADN parinte2) {
        int lungime = parinte1.getSecventa_ADN().length();
        Random random = new Random();
        int punctCrossover = random.nextInt(lungime);

        StringBuilder secventa_copil = new StringBuilder();
        // partea 1: de la parinte1 [0, punctCrossover)
        secventa_copil.append(parinte1.getSecventa_ADN(), 0, punctCrossover);
        // partea 2: de la parinte2 [punctCrossover, lungime)
        secventa_copil.append(parinte2.getSecventa_ADN().substring(punctCrossover));

        SecventaADN copil = new SecventaADN(lungime);
        copil.setSecventa_ADN(secventa_copil.toString());
        return copil;
    }

    /**
     * Selectează un părinte pentru reproducere folosind o selecție de tip „turneu”:
     * se aleg doi indivizi aleator și se întoarce cel cu fitness mai mare.
     *
     * @return un individ selectat ca părinte
     */
    private SecventaADN selecteazaParinte() {
        Random random = new Random();
        SecventaADN a = indivizi[random.nextInt(indivizi.length)];
        SecventaADN b = indivizi[random.nextInt(indivizi.length)];
        return (a.getFitness() > b.getFitness()) ? a : b;
    }

    /**
     * Generează o nouă generație de indivizi pe baza populației curente.
     * - se păstrează prin elitism cel mai bun individ (copiat direct)
     * - restul indivizilor sunt generați prin:
     *      selecție părinți -> reproducere (crossover) -> mutație.
     *
     * @param rataMutatie rata de mutație folosită pentru fiecare copil
     */
    public void genereazaNouaGeneratie(double rataMutatie) {
        SecventaADN[] nouaPopulatie = new SecventaADN[indivizi.length];

        // 1. ELITISM – păstrăm cel mai bun individ exact așa cum este
        SecventaADN celMaiBun = selecteazaCelMaiBun();
        nouaPopulatie[0] = celMaiBun;

        // 2. Generăm restul indivizilor prin selecție + crossover + mutație
        for (int i = 1; i < indivizi.length; i++) {
            SecventaADN p1 = selecteazaParinte();
            SecventaADN p2 = selecteazaParinte();

            SecventaADN copil = reproducere(p1, p2);
            copil.mutatie(rataMutatie);

            nouaPopulatie[i] = copil;
        }

        // Înlocuim vechea populație cu noua generație
        indivizi = nouaPopulatie;

        // Recalculăm fitness-ul pentru noua populație
        calculeazaFitness();
    }
}
