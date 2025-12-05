package at.technikumwien.qds.core;

import at.technikumwien.qds.model.Bomb;
import at.technikumwien.qds.model.Photon;
import at.technikumwien.qds.model.QuantumObject;


public class BombTesterInterferometer implements Interferometer {

    private Detector detectorC;  // "safe" detector
    private Detector detectorD;  // "bomb path" detector

    // Statistiken
    private int bombsExploded = 0;
    private int bombsIdentifiedSafely = 0;  // Interaction-free!
    private int inconclusiveResults = 0;
    private int dudsDetected = 0;
    private int totalTests = 0;

    public BombTesterInterferometer() {
        this.detectorC = new Detector("Detector C (Safe)", Detector.PathType.PathA);
        this.detectorD = new Detector("Detector D (Bomb Path)", Detector.PathType.PathB);
    }

    @Override
    public void runExperiment(QuantumObject input) {
        if (!(input instanceof Bomb bomb)) {
            System.out.println("Error: Bomb Tester requires a Bomb object!");
            return;
        }

        System.out.println("\n--- Testing " + bomb.getId() + " ---");

        // erstellt Photon für den Test
        Photon photon = new Photon("Test-Photon-" + totalTests);

        // erster Beam Splitter teilt das Photon
        photon.BeamSplitter();

        // check ob Photon den Bomb-Pfad nimmt
        boolean photonInBombPath = photon.detectInPathB();

        if (photonInBombPath) {
            // Photon ist in Path B, dort wo die Bombe ist
            boolean bombInteracted = bomb.checkPhotonInteraction();

            if (bombInteracted) {
                // LIVE Bombe: explodiert
                bombsExploded++;
                System.out.println("Result: BOMB EXPLODED");
            } else {
                // DUD: Photon passiert ungestört
                // Geht zum zweiten Beam Splitter
                System.out.println("Result: No explosion - bomb is DUD");
                dudsDetected++;
            }
        } else {
            // Photon ist in Path A, der sichere Path

            if (bomb.isLive()) {
                // LIVE Bombe: Interferenz wird zerstört
                // Photon verhält sich wie normales teilchen
                // 50/50 Chance für Detector C oder D

                if (Math.random() < 0.5) {
                    detectorC.detect(photon);
                    System.out.println("Result: Detector C fired - INCONCLUSIVE (could be live or dud)");
                    inconclusiveResults++;
                } else {
                    // Detector D feuert -> Bbombe ist live!
                    // ohne dass sie explodiert ist!
                    System.out.println("Result: Detector D fired - BOMB IS LIVE (interaction-free measurement!)");
                    bombsIdentifiedSafely++;
                }

            } else {
                // DUD: Perfekte Interferenz
                // Nur Detector C feuert (destruktive Interferenz bei D)
                detectorC.detect(photon);
                System.out.println("Result: Detector C fired due to interference - bomb is DUD");
                dudsDetected++;
            }
        }

        totalTests++;
    }

    public void printStats() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Elitzur-Vaidman bomb tester statistic");
        System.out.println("=".repeat(60));
        System.out.println("Total tests: " + totalTests);
        System.out.println("\nLive bombs:");
        System.out.println("  - Exploded: " + bombsExploded);
        System.out.println("  - Identified safely (interaction-free!): " + bombsIdentifiedSafely);
        System.out.println("  - Inconclusive: " + inconclusiveResults);
        System.out.println("\nDuds identified: " + dudsDetected);

        if (totalTests > 0) {
            double successRate = (double) bombsIdentifiedSafely / totalTests * 100;
            System.out.println("\nInteraction-free success rate: " + String.format("%.1f%%", successRate));
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("QUANTUM PARADOX:");
        System.out.println("We identified " + bombsIdentifiedSafely + " live bombs");
        System.out.println("WITHOUT triggering them!");
        System.out.println("=".repeat(60));

        detectorC.printStats();
        detectorD.printStats();
    }

    public void resetStats() {
        bombsExploded = 0;
        bombsIdentifiedSafely = 0;
        inconclusiveResults = 0;
        dudsDetected = 0;
        totalTests = 0;
        detectorC.reset();
        detectorD.reset();
    }

    public int getBombsIdentifiedSafely() {
        return bombsIdentifiedSafely;
    }

    public int getBombsExploded() {
        return bombsExploded;
    }
}